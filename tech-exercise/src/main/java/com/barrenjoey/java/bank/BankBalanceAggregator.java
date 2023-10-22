package com.barrenjoey.java.bank;

import java.time.Instant;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class BankBalanceAggregator implements Runnable, Bank{
    private final BlockingQueue sharedQueue;
    private final ConcurrentHashMap<Integer, BankAccount> accountBalances;
    private final ReportingServer reportingServer;
    public BankBalanceAggregator(BlockingQueue sharedQueue, ReportingServer reportingServer) {
        this.sharedQueue = sharedQueue;
        this.reportingServer = reportingServer;
        accountBalances = new ConcurrentHashMap<>();
    }
    @Override
    public void run() {
        System.out.println("Start listening to Account entry events...");
        while(true){
            try {
                String activity = sharedQueue.take().toString();
                if(activity.equals("kill")) {
                    System.out.println("Exiting consumer");
                    break;
                }
                String[] items = activity.split(",");
                if(items.length > 0) {
                    Integer accountId = Integer.parseInt(items[0]);
                    String action = items[1];
                    Double amount = Double.parseDouble(items[2]);
                    if(accountBalances.containsKey(accountId)) {
                        BankAccount bankAcc = accountBalances.get(accountId);
                        if("Withdraw".equals(action)) {
                            bankAcc.withdraw(amount);
                        }
                        else if("Deposit".equals(action)) {
                            bankAcc.deposit(amount);
                        }
                        CompletableFuture.runAsync(() -> {
                            //System.out.println("Reporting activity at "+ Instant.now());
                            reportingServer.reportActivity(accountId, Instant.now(), amount, bankAcc.getBalance());
                        });
                    }
                    else {
                        accountBalances.put(accountId, new BankAccountImpl(accountId, amount, action));
                    }
                }
                System.out.println("Consumed: "+ activity);
            } catch (InterruptedException ex) {
                System.out.println(BankBalanceAggregator.class.getName() + " error : " + ex.getMessage());
            }
        }
    }

    @Override
    public BankAccount getAccountById(int accountId) {
        return accountBalances.get(accountId);
    }
}
