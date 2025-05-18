package org.concurrency.queue;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicQueue implements Queue {
    int capacity;
    AtomicLong producerIndex;
    AtomicLong consumerIndex;
    StringBuilder[] messages;
    public AtomicQueue(int capacity) {
        this.messages = new StringBuilder[capacity];
        for (int i=0; i<capacity; i++){
            messages[i] = new StringBuilder();
        }
        this.capacity = capacity;
        producerIndex = new AtomicLong(0);
        consumerIndex = new AtomicLong(0);
    }
    public StringBuilder pop() {
        int currProducerIndex = (int) producerIndex.get();
        int consumerIndexValue = (int) consumerIndex.get();
        if(consumerIndexValue+1>currProducerIndex)
            return null;
        return messages[getWrappedIndex(consumerIndexValue)];
    }
    public StringBuilder push() {
        int currConsumerIndex = (int) consumerIndex.get();
        int producerIndexValue = (int) producerIndex.get();
        if(producerIndexValue < currConsumerIndex + capacity) {
            return messages[getWrappedIndex(producerIndexValue)];
        }
        return null;
    }
    public void doneFetching() {
        consumerIndex.incrementAndGet();
    }
    public void flush() {
        producerIndex.incrementAndGet();
    }
    public int getWrappedIndex(int index) {
        return (index) % capacity;
    }
}
