package org.poc.longpolling.rest;

import org.poc.longpolling.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class LongPollingController {

    private final AccountService accountService;

    private final RestAccountStatusChangeEventListener restAccountStatusChangeEventListener;

    Logger logger = LoggerFactory.getLogger(LongPollingController.class);

    LongPollingController(
        @Autowired AccountService accountService,
        @Autowired RestAccountStatusChangeEventListener restAccountStatusChangeEventListener
    ) {
        this.accountService = accountService;
        this.restAccountStatusChangeEventListener = restAccountStatusChangeEventListener;
    }

    @GetMapping("/v1/long-poll")
    public DeferredResult<ResponseEntity<?>> accountStatusChange(
        @RequestParam(name = "account-number") String accountNumber,
        @RequestParam(required = false, name = "timeout", defaultValue = "5000") long timeout
    ) {
        DeferredResult<ResponseEntity<?>> output = new DeferredResult<>(
            timeout,
            () -> {
                logger.info("Response timed out. Returning current status of account {}", accountNumber);
                return ResponseEntity.status(200).body(accountService.getAccount(accountNumber));
            }
        );

        restAccountStatusChangeEventListener.registerEventTarget(accountNumber, output);
        return output;
    }
}
