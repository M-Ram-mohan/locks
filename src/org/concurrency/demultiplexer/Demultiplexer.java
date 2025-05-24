package org.concurrency.demultiplexer;

import org.concurrency.queue.Queue;

public abstract class Demultiplexer implements Queue {

    public abstract StringBuilder push();
    public abstract void flush();
    public abstract int availableToPopCount(int consumerIndex);
    public abstract StringBuilder pop(int consumerIndex);
    public abstract void doneFetching(int consumerIndex);
    public abstract StringBuilder push(int consumerIndex);
    public abstract void flush(int consumerIndex);
    public StringBuilder pop(){
        throw new RuntimeException();
    }
    public void doneFetching(){
        throw new RuntimeException();
    };
    public int availableToPopCount(){
        throw new RuntimeException();
    };
}
