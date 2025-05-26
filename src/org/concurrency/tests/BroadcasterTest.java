package org.concurrency.tests;

import org.concurrency.broadcaster.AtomicBroadcaster;
import org.concurrency.broadcaster.BroadcastConsumerRunnable;
import org.concurrency.broadcaster.BroadcastProducerRunnable;
import org.concurrency.broadcaster.Broadcaster;

import static org.concurrency.utils.Constants.BROADCASTER_CONSUMERS_CNT;
import static org.concurrency.utils.Constants.BROADCASTER_QUEUE_SIZE;

public class BroadcasterTest implements Test{
    public void performTests() {
        Broadcaster queue = new AtomicBroadcaster(BROADCASTER_CONSUMERS_CNT, BROADCASTER_QUEUE_SIZE);
        Runnable producerRunnable = new BroadcastProducerRunnable(queue);
        Runnable[] consumerRunnables = new Runnable[BROADCASTER_CONSUMERS_CNT];
        for (int i = 0; i < BROADCASTER_CONSUMERS_CNT; i++) {
            consumerRunnables[i] = new BroadcastConsumerRunnable(queue, i);
        }
        testQueueLatency(producerRunnable, consumerRunnables);
    }
    public void testQueueLatency(Runnable producerRunnable, Runnable[] consumerRunnables) {
        Thread producerThread = new Thread(producerRunnable);
        Thread[] consumerThreads = new Thread[BROADCASTER_CONSUMERS_CNT];

        long startTime = System.currentTimeMillis();
        producerThread.start();

        for (int i = 0; i < BROADCASTER_CONSUMERS_CNT; i++) {
            consumerThreads[i] = new Thread(consumerRunnables[i]);
            consumerThreads[i].start();
        }

        try {
            producerThread.join();
            for (Thread t : consumerThreads) {
                t.join();
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Broadcaster : " + (endTime - startTime) + " ms");
    }
}
