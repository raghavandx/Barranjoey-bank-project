package com.barrenjoey.java.bank;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public class AccountActivityProducer implements Runnable{
    private final BlockingQueue sharedQueue;
    private final String fileName;
    public AccountActivityProducer(BlockingQueue sharedQueue, String fileName) {
        this.sharedQueue = sharedQueue;
        this.fileName = fileName;
    }


    @Override
    public void run() {
        System.out.println("Start producing account entry events..");
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream in = classloader.getResourceAsStream(this.fileName);
            System.out.println("Processing file : " + this.fileName);
            try (InputStreamReader streamReader =
                         new InputStreamReader(in, StandardCharsets.UTF_8);
                 BufferedReader reader = new BufferedReader(streamReader)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if(!line.equals("Acc Id,Action,Amount")) {
                        System.out.println("Produced :" + line);
                        sharedQueue.put(line);
                    }
                }
            } catch (IOException e) {
                System.out.println("Produced: " + e.getMessage());
            }
        } catch (InterruptedException ex) {
            System.out.println("Produced: " + ex.getMessage());
        }
    }
}
