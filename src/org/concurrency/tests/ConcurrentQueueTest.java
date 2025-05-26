package org.concurrency.tests;

import java.util.concurrent.ConcurrentLinkedQueue;

import static org.concurrency.utils.Constants.QUEUE_MSG_CNT;

public class ConcurrentQueueTest implements Test {
    public void performTests() {
        ConcurrentLinkedQueue<StringBuilder> queue = new ConcurrentLinkedQueue<>();
        Thread producerThread = new Thread(() -> {
            for (int i = 0; i < QUEUE_MSG_CNT; i++) {
                StringBuilder message = new StringBuilder(i);
                queue.offer(message);
            }
        });
        Thread consumerThread = new Thread(() -> {
            int cnt = 0;
            while(cnt < QUEUE_MSG_CNT) {
                StringBuilder message = queue.poll();
                if (message != null) {
                    cnt++;
                }
            }
        });
        long startTime = System.currentTimeMillis();
        producerThread.start();
        consumerThread.start();
        try {
            producerThread.join();
            consumerThread.join();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("ConcurrentQueue : " + (endTime - startTime) + " ms");
    }

}
