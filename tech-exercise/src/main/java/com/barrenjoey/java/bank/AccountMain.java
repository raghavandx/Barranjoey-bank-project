package com.barrenjoey.java.bank;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AccountMain {


    public static void main(String args[]) throws InterruptedException {


        //Create an unbounded queue
        BlockingQueue sharedQueue = new LinkedBlockingQueue();
        ReportingServer reportingServer = new LegacyReportingServer();
        Bank bank = new BankBalanceAggregator(sharedQueue, reportingServer);
//This step can be further improved by scanning all files in the directory and ordering by file suffix (*.1.csv, *.2.csv) and sending
        //files in the order to different producer threads
        //for simplicity, i am hard coding 2 files in the right order
        //Here .1.csv and .2.csv are processed in 2 threads sequentially
        Thread prodThread = new Thread(new AccountActivityProducer(sharedQueue, "transaction-log.1.csv"));
        Thread prodThread2 = new Thread(new AccountActivityProducer(sharedQueue, "transaction-log.2.csv"));

        Thread aggThread = new Thread((Runnable) bank);
        aggThread.start();
        prodThread.start();
        prodThread.join();
        prodThread2.start();
        prodThread2.join();

        Scanner scanner = new Scanner(System.in);
        Thread.sleep(5000);

        String query = "Y";
        while (true) {
            System.out.println("Provide the account id to find the balance");
            int accountId = Integer.parseInt(scanner.nextLine());
            BankAccount account = bank.getAccountById(accountId);
            System.out.println("Account balance for " + accountId + " is : " + account.getBalance());
            AcccountEntry[] entries = account.getEntries();
            for(int i = 0 ; i< entries.length; i++) {
                System.out.println(""+ entries[i].getAction() + " - amount : " + entries[i].getAmount());
            }
            System.out.println("Please enter Y to continue and N to exit");
            String answer = scanner.nextLine();
            if(answer.equals("N")) {
                sharedQueue.put("kill");
                break;
            }
        }

    }
}
