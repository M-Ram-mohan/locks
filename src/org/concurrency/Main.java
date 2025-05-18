package org.concurrency;

import org.concurrency.queue.AtomicQueue;
import org.concurrency.queue.Queue;
import org.concurrency.queue.SynchronizedQueue;
import org.concurrency.utils.Consumer;
import org.concurrency.utils.Producer;

public class Main {
    public static void main(String[] args) {
         testLockFreeQueue();
    }
    /**
     * Time taken: 150ms - 200ms || 3X faster than synchronized queue
     */
    public static void testLockFreeQueue(){
        Queue queue = new AtomicQueue(1024);
        testQueue(queue);
    }
    /**
     * Time taken: 450ms - 650ms
     */
    public static void testSynchronizedQueue(){
        Queue queue = new SynchronizedQueue(1024);
        testQueue(queue);
    }
    public static void testQueue(Queue queue){
        Producer producer = new Producer(queue);
        Thread producerThread = new Thread(producer);
        Consumer consumer = new Consumer(queue);
        Thread consumerThread = new Thread(consumer);
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
        System.out.println("Time taken: " + (end - start) + "ms");
    }
}
/**
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
 * - You can try to modify the consume operation by returning the number of messages that are yet to be consumed.
 *   Then, this can be used by the consumer to consume the messages freely without worrying about the other thread for every message.
 *
 * What example class of Message should we proceed with ?
 * - You can't take string, because it's immutable
 * - You can't take int, becaue they are primitive data types
 * - You can probably take Integer class.
 */