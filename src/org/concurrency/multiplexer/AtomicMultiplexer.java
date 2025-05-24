package org.concurrency.multiplexer;

import org.concurrency.queue.AtomicQueue;
import org.concurrency.queue.Queue;


import static org.concurrency.utils.Constants.QUEUE_SIZE;

public class AtomicMultiplexer extends Multiplexer {
    private int producerCount;
    private final Queue[] queues;
    public AtomicMultiplexer(int producerCount){
        this.producerCount = producerCount;
        this.queues = new AtomicQueue[producerCount];
        for(int i = 0; i < producerCount; i++){
            queues[i] = new AtomicQueue(QUEUE_SIZE);
        }
    }

    public StringBuilder push(int producerIndex) {
        if (producerIndex < 0 || producerIndex >= producerCount) {
            throw new IllegalArgumentException("Invalid producer index: " + producerIndex);
        }
        return queues[producerIndex].push();
    }
    public void flush(int producerIndex) {
        if (producerIndex < 0 || producerIndex >= producerCount) {
            throw new IllegalArgumentException("Invalid producer index: " + producerIndex);
        }
        queues[producerIndex].flush();
    }
    public int availableToPopCount() {
        int total = 0;
        for (int i = 0; i < producerCount; i++) {
            total += queues[i].availableToPopCount();
        }
        return total;
    }
    public StringBuilder pop() {
        for (int i = 0; i < producerCount; i++) {
            StringBuilder x = queues[i].pop();
            if(x != null){
                return x;
            }
        }
        return null;
    }
    public void doneFetching() {
        for (int i = 0; i < producerCount; i++) {
            queues[i].doneFetching();
        }
    }
    /*
    The AtomicMultiplexer supports multiple producer producing messages into the queue.
    Only one consumer would consume messages from the queue.
    Coral blocks used multiple AtomicQueue's to implement the AtomicMultiplexer.
    This way we can avoid coordination between the producers.

    Once the above approach is finalized, we can choose the implementation which would have
    better abstraction or improved simplicity.
    The "improved simplicity" hands over the responsibility of producer lifecycle to the user.
    This avoids great deal of headache managing producer's lifecycle

    int avail[] array is very important to know how many elements are available in each queue
    as per the current implementation of the pop() method in AtomicQueue. The current implementation
    always returns the next element without checking if the queue is empty or not.
    We can avoid avail[] array if the pop() method is modified to return null if the queue is empty.
     */
}
