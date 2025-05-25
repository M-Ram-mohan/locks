package org.concurrency.tests;

import org.concurrency.mpmc.AtomicMpMcQueue;
import org.concurrency.mpmc.MpMcBatchConsumerRunnable;
import org.concurrency.mpmc.MpMcBatchProducerRunnable;
import org.concurrency.mpmc.MpMcQueue;

import static org.concurrency.utils.Constants.*;

public class MpMcQueueTest implements Test{

    public void performTests(){
        MpMcQueue queue  = new AtomicMpMcQueue(MPMC_PRODUCERS_CNT, MPMC_CONSUMERS_CNT, MPMC_QUEUE_SIZE);
        Runnable[] producerRunnables = new Runnable[MPMC_PRODUCERS_CNT];
        Runnable[] conumerRunnables = new Runnable[MPMC_CONSUMERS_CNT];
        for(int i=0; i<MPMC_PRODUCERS_CNT; i++){
            producerRunnables[i] = new MpMcBatchProducerRunnable(queue, MPMC_BATCH_SIZE, i);
        }
        for(int i=0; i<MPMC_CONSUMERS_CNT; i++){
            conumerRunnables[i] = new MpMcBatchConsumerRunnable(queue, i);
        }
        testQueueLatency(producerRunnables, conumerRunnables);
    }

    public void testQueueLatency(Runnable[] producerRunnables, Runnable[] consumerRunnables){
        Thread[] producerThreads = new Thread[MPMC_PRODUCERS_CNT];
        Thread[] consumerThreads = new Thread[MPMC_CONSUMERS_CNT];

        long startTime = System.currentTimeMillis();
        for(int i=0; i<MPMC_PRODUCERS_CNT; i++){
            producerThreads[i] = new Thread(producerRunnables[i]);
            producerThreads[i].start();
        }

        for(int i=0; i<MPMC_CONSUMERS_CNT; i++){
            consumerThreads[i] = new Thread(consumerRunnables[i]);
            consumerThreads[i].start();
        }

        try{
            for(Thread t : producerThreads){
                t.join();
            }
            for(Thread t : consumerThreads){
                t.join();
            }
        } catch (Exception ex){
            throw new RuntimeException();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("MpMcQueue : " + (endTime - startTime) + " ms");
    }
}
