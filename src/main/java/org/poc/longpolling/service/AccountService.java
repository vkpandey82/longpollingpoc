package org.poc.longpolling.service;

import org.poc.longpolling.kafka.AccountStatusChangeEvent;
import org.poc.longpolling.kafka.KafkaMessageSender;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class AccountService {

    Logger logger = LoggerFactory.getLogger(AccountService.class);

    private final Map<String, AccountStatus> accountStatusMap;

    private KafkaMessageSender kafkaMessageSender;

    AccountService(
            @Autowired KafkaMessageSender kafkaMessageSender,
            @Autowired RedissonClient redissonClient
            ) {
        this.kafkaMessageSender = kafkaMessageSender;
        this.accountStatusMap = redissonClient.getMap("account-status-map");
    }

    @Scheduled(cron = "${account-transition-cron-expr:-}")
    void mockAccountStatusTransition() {
        var accountStatusValues = AccountStatus.values();
        accountStatusMap.forEach((key, value) -> {
            if (!value.isProcessingComplete()) {
                var newAccountStatus = accountStatusValues[value.ordinal() + 1];
                accountStatusMap.put(key, newAccountStatus);
                var accountStatusChangeEvent = new AccountStatusChangeEvent(key, newAccountStatus);
                kafkaMessageSender.publishKafkaMessage(accountStatusChangeEvent);
                logger.info("Transitioned account {} to {}", key, newAccountStatus);
            }
        });
    }

    public Account createAccount(String accountNumber) {
        if (!accountStatusMap.containsKey(accountNumber)) {
            accountStatusMap.put(accountNumber, AccountStatus.REQUESTED);
            return new Account(accountNumber, AccountStatus.REQUESTED);
        } else {
            return new Account(accountNumber, accountStatusMap.get(accountNumber));
        }
    }

    public Optional<Account> getAccount(String accountNumber) {
        var accountStatus = accountStatusMap.get(accountNumber);
        if (accountStatus != null) {
            return Optional.of(new Account(accountNumber, accountStatus));
        } else {
            return Optional.empty();
        }
    }
}
