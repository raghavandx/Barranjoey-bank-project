package com.barrenjoey.java.bank;

public interface BankAccount {
    /**
     * Increase the balance of the account by depositAmount
     * @param depositAmount
     * @return the updated balance
     */
    double deposit(double depositAmount);

    /**
     * Decrease the balance of the account by withdrawalAmount
     * @param withdrawalAmount
     * @return the updated balance
     */
    double withdraw(double withdrawalAmount);

    /**
     * Get the current balance of the account
     * @return the balance
     */
    double getBalance();

    /**
     * Return the individual account entries in the order they were applied
     * @return the entries
     */
    AcccountEntry[] getEntries();
}
