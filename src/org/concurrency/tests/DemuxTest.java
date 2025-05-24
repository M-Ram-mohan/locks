package org.concurrency.tests;

import org.concurrency.demultiplexer.AtomicDemux;
import org.concurrency.demultiplexer.Demultiplexer;
import org.concurrency.utils.BatchMessageConsumerRunnable;
import org.concurrency.utils.BatchMessageProducerRunnable;

import static org.concurrency.utils.Constants.BATCH_SIZE;
import static org.concurrency.utils.Constants.CONSUMERS_CNT;

public class DemuxTest implements Test{
    public DemuxTest(){

    }

    public void performTests() {
        Demultiplexer demux = new AtomicDemux(CONSUMERS_CNT);
        BatchMessageConsumerRunnable[] consumerRunnables = new BatchMessageConsumerRunnable[CONSUMERS_CNT];
        for (int i = 0; i < CONSUMERS_CNT; i++) {
            consumerRunnables[i] = new BatchMessageConsumerRunnable(demux, i, true);
        }
        BatchMessageProducerRunnable producerRunnable = new BatchMessageProducerRunnable(demux, BATCH_SIZE, true);
        testQueueLatency(producerRunnable, consumerRunnables);
    }
    private void testQueueLatency(BatchMessageProducerRunnable producerRunnable, BatchMessageConsumerRunnable[] consumerRunnables) {
        Thread producerThread = new Thread(producerRunnable);
        Thread[] consumerThreads = new Thread[CONSUMERS_CNT];
        for (int i = 0; i < CONSUMERS_CNT; i++) {
            consumerThreads[i] = new Thread(consumerRunnables[i]);
        }
        long startTime = System.currentTimeMillis();
        producerThread.start();
        for(int i = 0; i < CONSUMERS_CNT; i++) {
            consumerThreads[i].start();
        }
        try {
            producerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        for (int i = 0; i < CONSUMERS_CNT; i++) {
            try {
                consumerThreads[i].join();
            } catch (Exception ex) {
                throw new RuntimeException();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Total time taken: " + (endTime - startTime) + " ms");
    }

}
