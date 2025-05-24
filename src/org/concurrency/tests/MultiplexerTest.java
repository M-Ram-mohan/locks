package org.concurrency.tests;

import org.concurrency.multiplexer.AtomicMultiplexer;
import org.concurrency.multiplexer.Multiplexer;
import org.concurrency.utils.BatchMessageConsumerRunnable;
import org.concurrency.utils.BatchMessageProducerRunnable;

import static org.concurrency.utils.Constants.BATCH_SIZE;
import static org.concurrency.utils.Constants.PRODUCERS_CNT;
/*
 * Messages : 9_000_000 - Producers : 3
 *
 * Time taken - 900 ms
 *
 * Messages : 9_00_000 - Producers : 3
 *
 * Time taken - 120 ms
 *
 * Results seem a bit inconsistent. Achieved 400ms || 60ms at some point of time.
 * Need to figure out why.
 *
 */
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
        Thread consumerThread = new Thread(consumerRunnable);
        Thread[] producerThreads = new Thread[PRODUCERS_CNT];
        for (int i=0; i < PRODUCERS_CNT; i++) {
            producerThreads[i] = new Thread(producerRunnables[i]);
        }
        long startTime = System.currentTimeMillis();
        for(int i=0; i < PRODUCERS_CNT; i++) {
            producerThreads[i].start();
        }
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
