package com.barrenjoey.java.bank;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Incomplete AccountEntry class...
 */
public class AcccountEntry {
    private final Integer accountId;
    private final String action;
    private final Double amount;

    public AcccountEntry(Integer accountId, String action, Double amount) {
        this.accountId = accountId;
        this.action = action;
        this.amount = amount;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public String getAction() {
        return action;
    }

    public Double getAmount() {
        return amount;
    }
}
