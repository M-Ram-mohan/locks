package org.concurrency.queue;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicQueue extends AbstractQueue {
    int capacity;
    AtomicLong producerIndex;
    long currProducerIndex;
    long currEnd;
    long currFilled;
    AtomicLong consumerIndex;
    long currConsumerIndex;
    StringBuilder[] messages;
    public AtomicQueue(int capacity) {
        this.messages = new StringBuilder[capacity];
        for (int i=0; i<capacity; i++){
            messages[i] = new StringBuilder();
        }
        this.capacity = capacity;
        currProducerIndex = 0;
        currConsumerIndex = 0;
        currEnd = capacity;
        currFilled = 0;
        producerIndex = new AtomicLong(0);
        consumerIndex = new AtomicLong(0);
    }
    public int availableToPopCount() {
        if(currConsumerIndex<currFilled){
            return (int) (currFilled-currConsumerIndex);
        }
        currFilled = producerIndex.get();
        return 0;
    }
    public StringBuilder pop(){
        if(currConsumerIndex<currFilled){
            currConsumerIndex++;
            return messages[getWrappedIndex(currConsumerIndex)];
        }
        return null;
    }
    public StringBuilder push() {
        if(currProducerIndex<currEnd){
            currProducerIndex++;
            return messages[getWrappedIndex(currProducerIndex)];
        }
        currEnd = consumerIndex.get() + capacity;
        return null;
    }
    public void doneFetching() {
        consumerIndex.set(currConsumerIndex);
    }
    public void flush() {
        producerIndex.set(currProducerIndex);
    }
    public int getWrappedIndex(long index) {
        return (int) ((index-1) % capacity);
    }
    /**
     * pi = x : Producer has produced messages till the (x-1)th index
     * ci = y : Consumer has consumed messages till the (y-1)th index
     *
     * 1 2 3 4
     * pi = 0; push() will return 1 and updates pi to 1
     * pi = 3; push() will return 4 and updates pi to 4
     * pi = 4; push() will return null because pi < ci + capacity is not satisfied
     * ci = 0; pop() should return 1 and updates ci to 1
     *
     */
}
