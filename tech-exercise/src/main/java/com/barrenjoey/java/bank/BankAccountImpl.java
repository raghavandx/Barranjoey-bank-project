package com.barrenjoey.java.bank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;

public class BankAccountImpl implements BankAccount {

    private final int accountId;
    private double balance;
    private final List<AcccountEntry> entries;

    public BankAccountImpl(int accountId, double startingBalance, String action) {
        this.accountId = accountId;
        this.balance = startingBalance;
        this.entries = new ArrayList<>();
        AcccountEntry entry = new AcccountEntry(this.accountId, action, startingBalance);
        this.entries.add(entry);
    }

    private double apply(double deltaAmount) {
        balance = balance + deltaAmount;
        return balance;
    }

    @Override
    public double deposit(double depositAmount) {
        if(depositAmount < 0) throw new IllegalArgumentException("depositAmount must be positive");
        entries.add(new AcccountEntry(this.accountId, "Deposit", depositAmount));
        return apply(depositAmount);
    }

    @Override
    public double withdraw(double withdrawalAmount) {
        if(withdrawalAmount < 0) throw new IllegalArgumentException("withdrawalAmount must be positive");
        entries.add(new AcccountEntry(this.accountId, "Withdraw", withdrawalAmount));
        return apply(withdrawalAmount);
    }

    @Override
    public double getBalance() {
        return balance;
    }

    @Override
    public AcccountEntry[] getEntries() {
        return unmodifiableList(this.entries).toArray(new AcccountEntry[0]);
    }

    @Override
    public String toString() {
        return "BankAccountImpl{" +
                "accountId=" + accountId +
                ", balance=" + balance +
                '}';
    }
}
