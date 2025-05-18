package org.concurrency.queue;

public class SynchronizedQueue implements Queue{
    int capacity;
    int producerIndex;
    int consumerIndex;
    StringBuilder[] messages;
    public SynchronizedQueue(int capacity){
        messages = new StringBuilder[capacity];
        for (int i=0; i<capacity; i++){
            messages[i] = new StringBuilder();
        }
        this.capacity = capacity;
        producerIndex = 0;
        consumerIndex = 0;
    }
    public synchronized StringBuilder push(){
        if(producerIndex<consumerIndex+capacity)
            return messages[getWrappedIndex(producerIndex)];
        return null;
    }
    public void flush(){
        producerIndex++;
    }
    public synchronized StringBuilder pop(){
        if(consumerIndex+1>producerIndex)
            return null;
        return messages[getWrappedIndex(consumerIndex)];
    }
    public synchronized void doneFetching(){
        consumerIndex++;
    }
    public int getWrappedIndex(int index){
        return (index) % capacity;
    }
    /**
     * 1 2 3
     * pi = 0 : producerIndex represents the index of the next message to be produced i.e.; 0
     * ci = 0 : consumerIndex represents the index of the next message to be consumed i.e.; 0
     *
     * case 1 : pi=1, ci=0 consumer can consume 0th message
     * case 2 : pi=1, ci=1 consumer cannot consume 1st message yet because producer didn't produce it yet
     * case 3 : pi=3, ci=0 producer cannot produce any more messages because the queue is full
     */
}
