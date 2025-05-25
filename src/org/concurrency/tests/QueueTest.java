package org.concurrency.tests;

import org.concurrency.queue.AtomicQueue;
import org.concurrency.queue.Queue;
import org.concurrency.queue.BatchMessageConsumerRunnable;
import org.concurrency.queue.BatchMessageProducerRunnable;

import static org.concurrency.utils.Constants.QUEUE_BATCH_SIZE;
import static org.concurrency.utils.Constants.QUEUE_SIZE;

/**
 * Tests the queues with single producer and single consumer
 */
/**
 * Messages : 1_000_000
 *
 * Synchronized Queue - Time taken (LCK-002): 150ms - 250ms [ Brute force ]
 *                    - Time taken (LCK-003): 50ms - 150ms [ Avoided mutex locking ]
 * Atomic Queue       - Time taken (LCK-002): 50ms - 150ms [ Lock free ]
 *                    - Time taken (LCK-005): 50ms - 100ms  [ Batching ]
 *
 * Messages : 10_000_000
 *
 * Synchronized Queue - Time taken (LCK-002): 3000ms - 4000ms [ Brute force ]
 *                    - Time taken (LCK-003): 1000ms - 1100ms [ Avoided mutex locking ]
 * Atomic Queue       - Time taken (LCK-002): 500ms - 600ms [ Lock free ]
 *                    - Time taken (LCK-005): 50ms - 100ms  [ Batching ]
 *
 * Enhancements :
 * Synchronized Queue : Message Batching can be done to improve the performance
 *
 */
public class QueueTest implements Test {

    public void performTests(){
        try{
            testSingleProducerSingleConsumerQueueWithBatching(new AtomicQueue(QUEUE_SIZE));
        } catch (Exception ex){
            throw new RuntimeException();
        }
    }

    public static void testSingleProducerSingleConsumerQueueWithBatching(Queue queue){
        BatchMessageProducerRunnable producerRunnable = new BatchMessageProducerRunnable(queue, QUEUE_BATCH_SIZE);
        BatchMessageConsumerRunnable consumerRunnable = new BatchMessageConsumerRunnable(queue);
        testQueueLatency(producerRunnable, consumerRunnable);
    }

    public static void testQueueLatency(Runnable producerRunnable, Runnable consumerRunnable){
        Thread producerThread = new Thread(producerRunnable);
        Thread consumerThread = new Thread(consumerRunnable);
        long start = System.currentTimeMillis();
        producerThread.start();
        consumerThread.start();
        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        long end = System.currentTimeMillis();
        System.out.println("Queue : " + (end - start) + "ms");
    }
}
/**
 * Comments :
 *
 * I need to implement a concurrent queue using atomic operations.
 * To do that, I need to create a class that will represent the queue.
 * When dealing with concurrent programming,
 * I need to be careful about the scope of the objects that are going to be created and
 * what threads are going to be using them.
 * To avoid this mistake, i can just initialise a bounded queue with a fixed size with message objects.
 * Now producer and consumer would have to borrow those messages from the queue and push/pull the
 * information to/from the queue. To do this,
 * - push method has to borrow a message object.
 * (You can probably generalize it by sending the type of message class you want to use to communicate)
 * - pop method will also have to borrow a message object to read the information from the queue.
 * As these are single threaded producer and consumer, Queue has to take advantage of that
 * Producer thread and consumer thread doesn't have to worry about.
 * They can just produce and consume irrespective of the multithreading logic.
 * Either the queue can block the producer/consumer thread or they can handle it on their own
 * As it is lock free queue, we should not block the producer/consumer thread.
 * If we do that, we are kind of blocking the producer/consumer threads.
 *
 * Finalized
 * - Queue class
 * - Producer/Consumer handling their own life cycle when they are not able to produce/consume
 * - Queue will expose the push() and pop() methods
 * - You should start with a synchronized queue with produce and consumer queue ready
 *
 *
 * Enhancements
 * SynchronizedQueue
 * LCK-003 : Avoided repetitive entering into synchronized blocks by storing the count of available message objects to produce/consume
 *
 *  *
 * AtomicQueue
 * - LCK-003 Logic didn't have much impact on the performance of the queue because you already avoided locking
 * - Batching of messages can be done on the producer side and the consumer can get all the available messages to avoid flushing
 *   multiple times.
 *
 * What example class of Message should we proceed with ?
 * - You can't take string, because it's immutable
 * - You can't take int, becaue they are primitive data types
 * - You can probably take Integer class.
 *
 */
