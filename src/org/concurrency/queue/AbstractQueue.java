package org.concurrency.queue;

public abstract class AbstractQueue implements Queue{
    public abstract int availableToPopCount();
    public abstract StringBuilder pop();
    public abstract void doneFetching();
    public abstract StringBuilder push();
    public abstract void flush();
    public StringBuilder push(int producerIndex){
        throw new RuntimeException();
    }
    public void flush(int producerIndex){
        throw new RuntimeException();
    }
    public int availableToPopCount(int consumerIndex){
        throw new RuntimeException();
    }
    public StringBuilder pop(int consumerIndex){
        throw new RuntimeException();
    }
    public void doneFetching(int consumerIndex){
        throw new RuntimeException();
    };
}
