package org.concurrency.tests;

import org.concurrency.queue.Multiplexer;
import org.concurrency.utils.MultiplexerConsumer;
import org.concurrency.utils.MultiplexerProducer;

import static org.concurrency.utils.Constants.PRODUCER_COUNT;
import static org.concurrency.utils.Constants.QUEUE_SIZE;
/*
 * Messages : 9_000_000 - Producers : 3
 *
 * Time taken - 1393 ms
 *
 * Messages : 9_00_000 - Producers : 3
 *
 * Time taken - 250 ms
 *
 */
public class MultiplexerTest implements Test{
    public MultiplexerTest(){

    }
    public void performTests(){
        Multiplexer queue = new Multiplexer(QUEUE_SIZE, PRODUCER_COUNT);
        Runnable[] producerRunnables = new Runnable[PRODUCER_COUNT];
        for(int i=0; i<PRODUCER_COUNT; i++){
            producerRunnables[i] = new MultiplexerProducer(queue);
        }
        Runnable consumerRunnable = new MultiplexerConsumer(queue);
        testQueueLatency(producerRunnables, consumerRunnable);
    }
    public void testQueueLatency(Runnable[] producerRunnables, Runnable consumerRunnable){
        Thread[] producers = new Thread[PRODUCER_COUNT];
        for(int i=0; i<PRODUCER_COUNT; i++){
            producers[i] = new Thread(producerRunnables[i]);
        }
        Thread consumer = new Thread(consumerRunnable);
        long start = System.currentTimeMillis();
        for(int i=0; i<PRODUCER_COUNT; i++){
            producers[i].start();
        }
        consumer.start();
        try {
            for(int i=0; i<PRODUCER_COUNT; i++){
                producers[i].join();
            }
            consumer.join();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        long end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + "ms");
    }
}
