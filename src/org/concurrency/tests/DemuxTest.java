package org.concurrency.tests;

import org.concurrency.demultiplexer.AtomicDemux;
import org.concurrency.demultiplexer.Demultiplexer;
import org.concurrency.demultiplexer.DemuxBatchConsumerRunnable;
import org.concurrency.demultiplexer.DemuxBatchProducerRunnable;

import static org.concurrency.utils.Constants.*;

public class DemuxTest implements Test{

    public void performTests() {
        Demultiplexer demux = new AtomicDemux(DEMUX_CONSUMERS_CNT, DEMUX_QUEUE_SIZE);
        DemuxBatchConsumerRunnable[] consumerRunnables = new DemuxBatchConsumerRunnable[DEMUX_CONSUMERS_CNT];
        for (int i = 0; i < DEMUX_CONSUMERS_CNT; i++) {
            consumerRunnables[i] = new DemuxBatchConsumerRunnable(demux, i);
        }
        DemuxBatchProducerRunnable producerRunnable = new DemuxBatchProducerRunnable(demux, DEMUX_BATCH_SIZE);
        testQueueLatency(producerRunnable, consumerRunnables);
    }
    private void testQueueLatency(DemuxBatchProducerRunnable producerRunnable, DemuxBatchConsumerRunnable[] consumerRunnables) {
        Thread producerThread = new Thread(producerRunnable);
        Thread[] consumerThreads = new Thread[DEMUX_CONSUMERS_CNT];
        for (int i = 0; i < DEMUX_CONSUMERS_CNT; i++) {
            consumerThreads[i] = new Thread(consumerRunnables[i]);
        }
        long startTime = System.currentTimeMillis();
        producerThread.start();
        for(int i = 0; i < DEMUX_CONSUMERS_CNT; i++) {
            consumerThreads[i].start();
        }
        try {
            producerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        for (int i = 0; i < DEMUX_CONSUMERS_CNT; i++) {
            try {
                consumerThreads[i].join();
            } catch (Exception ex) {
                throw new RuntimeException();
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Demux : " + (endTime - startTime) + " ms");
    }

}
