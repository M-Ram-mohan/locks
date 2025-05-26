package org.concurrency.tests;

import org.concurrency.multiplexer.AtomicMultiplexer;
import org.concurrency.multiplexer.Multiplexer;
import org.concurrency.multiplexer.MuxBatchConsumerRunnable;
import org.concurrency.multiplexer.MuxBatchProducerRunnable;

import static org.concurrency.utils.Constants.*;

public class MultiplexerTest implements Test{

    public void performTests() {
        Multiplexer mux = new AtomicMultiplexer(MUX_PRODUCERS_CNT, MUX_QUEUE_SIZE);
        MuxBatchProducerRunnable[] producerRunnables = new MuxBatchProducerRunnable[MUX_PRODUCERS_CNT];
        for (int i = 0; i < MUX_PRODUCERS_CNT; i++) {
            producerRunnables[i] = new MuxBatchProducerRunnable(mux, MUX_BATCH_SIZE, i);
        }
        MuxBatchConsumerRunnable consumerRunnable = new MuxBatchConsumerRunnable(mux);
        testQueueLatency(producerRunnables, consumerRunnable);
    }
    public void testQueueLatency(MuxBatchProducerRunnable[] producerRunnables, MuxBatchConsumerRunnable consumerRunnable) {
        Thread consumerThread = new Thread(consumerRunnable);
        Thread[] producerThreads = new Thread[MUX_PRODUCERS_CNT];
        for (int i=0; i < MUX_PRODUCERS_CNT; i++) {
            producerThreads[i] = new Thread(producerRunnables[i]);
        }
        long startTime = System.currentTimeMillis();
        for(int i=0; i < MUX_PRODUCERS_CNT; i++) {
            producerThreads[i].start();
        }
        consumerThread.start();
        for(int i=0; i < MUX_PRODUCERS_CNT; i++) {
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
        System.out.println("Mux : " + (endTime - startTime) + " ms");
    }
}
