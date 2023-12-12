package org.poc.longpolling.rest;

import org.poc.longpolling.event.AccountStatusChangeEventListener;
import org.poc.longpolling.kafka.AccountStatusChangeEvent;
import org.poc.longpolling.service.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RestAccountStatusChangeEventListener
        implements AccountStatusChangeEventListener<DeferredResult<ResponseEntity<?>>> {

    private final Logger logger = LoggerFactory.getLogger(RestAccountStatusChangeEventListener.class);

    private final Map<String, DeferredResult<ResponseEntity<?>>> accountDeferredResultMap = new ConcurrentHashMap<>();

    @KafkaListener(topics = "${app.kafka.topic}")
    public void listenForKafkaMessages(AccountStatusChangeEvent event) {
        String accountNumber = event.accountNumber();
        logger.info("Received event for status transition for account {} to status {}", accountNumber, event.newAccountStatus());
        var deferredResult = accountDeferredResultMap.remove(accountNumber);
        if (deferredResult != null && !deferredResult.isSetOrExpired()) {
            deferredResult.setResult(ResponseEntity.ok(new Account(event.accountNumber(), event.newAccountStatus())));
        }
    }

    public void registerEventTarget (String accountNumber, DeferredResult<ResponseEntity<?>> result) {
        accountDeferredResultMap.put(accountNumber, result);
    }
}
