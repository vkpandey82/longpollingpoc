package org.poc.longpolling.rest;

import org.poc.longpolling.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AcountCreationController {

    Logger logger = LoggerFactory.getLogger(AcountCreationController.class);

    private final AccountService accountService;

    AcountCreationController(@Autowired AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/v1/create-account")
    public ResponseEntity<?> createAccount() {
        String accountNumber = UUID.randomUUID().toString();
        var account = accountService.createAccount(accountNumber);
        logger.info("Created new account with account number %s".formatted(accountNumber));
        return ResponseEntity.ok(account);
    }
}
