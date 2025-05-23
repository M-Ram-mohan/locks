package org.concurrency.tests;

import org.concurrency.multiplexer.AtomicMultiplexer;
import org.concurrency.multiplexer.Multiplexer;
import org.concurrency.utils.BatchMessageConsumerRunnable;
import org.concurrency.utils.BatchMessageProducerRunnable;

import static org.concurrency.utils.Constants.BATCH_SIZE;
import static org.concurrency.utils.Constants.PRODUCERS_CNT;

public class MultiplexerTest implements Test{
    public MultiplexerTest(){

    }

    public void performTests() {
        Multiplexer mux = new AtomicMultiplexer(PRODUCERS_CNT);
        BatchMessageProducerRunnable[] producerRunnables = new BatchMessageProducerRunnable[PRODUCERS_CNT];
        for (int i = 0; i < PRODUCERS_CNT; i++) {
            producerRunnables[i] = new BatchMessageProducerRunnable(mux, BATCH_SIZE, i);
        }
        BatchMessageConsumerRunnable consumerRunnable = new BatchMessageConsumerRunnable(mux);
        testQueueLatency(producerRunnables, consumerRunnable);
    }
    public void testQueueLatency(BatchMessageProducerRunnable[] producerRunnables, BatchMessageConsumerRunnable consumerRunnable) {
        long startTime = System.currentTimeMillis();
        Thread[] producerThreads = new Thread[PRODUCERS_CNT];
        for (int i=0; i < PRODUCERS_CNT; i++) {
            producerThreads[i] = new Thread(producerRunnables[i]);
            producerThreads[i].start();
            System.out.println("Producer " + i + " started");
        }
        Thread consumerThread = new Thread(consumerRunnable);
        consumerThread.start();
        for(int i=0; i < PRODUCERS_CNT; i++) {
            try {
                producerThreads[i].join();
            } catch (InterruptedException e) {
                throw  new RuntimeException();
            }
        }
        try {
            consumerThread.join();
        } catch (Exception ex){
            throw new RuntimeException();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Total time taken: " + (endTime - startTime) + " ms");
    }
}
