package org.poc.longpolling.event;

public interface AccountStatusChangeEventListener<R> {

    void registerEventTarget(String accountNumber, R result);
}
