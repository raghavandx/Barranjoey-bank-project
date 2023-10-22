package bank;

import com.barrenjoey.java.bank.AcccountEntry;
import com.barrenjoey.java.bank.AccountActivityProducer;
import com.barrenjoey.java.bank.BankBalanceAggregator;
import com.barrenjoey.java.bank.LegacyReportingServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BankBalanceAggregatorTest {
    @Test
    public void testDataProducerQueueSize() throws InterruptedException {
        BlockingQueue sharedQueue = new LinkedBlockingQueue();
        AccountActivityProducer producer = new AccountActivityProducer(sharedQueue, "transaction-log.1.csv");
        Thread p = new Thread((Runnable) producer);
        p.start();
        p.join();
        Assertions.assertEquals(30001, sharedQueue.size());
    }

    @Test
    public void testDataConsumerProcessing() throws InterruptedException {
        BlockingQueue sharedQueue = new LinkedBlockingQueue();
        AccountActivityProducer producer = new AccountActivityProducer(sharedQueue, "transaction-log.1.csv");
        BankBalanceAggregator agg = new BankBalanceAggregator(sharedQueue, new LegacyReportingServer());
        Thread p = new Thread((Runnable) producer);
        Thread c = new Thread((Runnable) agg);
        c.start();
        p.start();
        Thread.sleep(1000);

        Assertions.assertEquals(11500.00, agg.getAccountById(11111).getBalance());
        testBalanceAgainstEntries(11500.00, Arrays.stream(agg.getAccountById(11111).getEntries()).toList());
        sharedQueue.put("kill");
        Thread.sleep(250);
        Assertions.assertEquals(Thread.State.TERMINATED, c.getState());
        Assertions.assertEquals(0, sharedQueue.size());
        c.join();

    }

    private void testBalanceAgainstEntries(double balance, List<AcccountEntry> entries){
        double runningBalance = 0;
        for(AcccountEntry entry : entries) {
            if(entry.getAction().equals("Withdraw")) {
                runningBalance = runningBalance - entry.getAmount();
            }
            else if(entry.getAction().equals("Deposit")) {
                runningBalance = runningBalance + entry.getAmount();
            }
        }
        Assertions.assertEquals(balance, runningBalance);
    }

}
