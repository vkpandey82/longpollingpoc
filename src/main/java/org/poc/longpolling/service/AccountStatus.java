package org.poc.longpolling.service;

public enum AccountStatus {

    REQUESTED,APPROVED,CREATED,ACTIVE;

    public boolean isProcessingComplete() {
        return this == ACTIVE;
    }
}
