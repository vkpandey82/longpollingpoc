package org.poc.longpolling.kafka;

import org.poc.longpolling.service.AccountStatus;

public record AccountStatusChangeEvent(String accountNumber, AccountStatus newAccountStatus) {

}
