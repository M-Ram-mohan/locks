package org.concurrency.multiplexer;

import org.concurrency.queue.Queue;

public abstract class Multiplexer implements Queue {
    public abstract StringBuilder push(int producerIndex);
    public abstract void flush(int producerIndex);
    public abstract int availableToPopCount();
    public abstract StringBuilder pop();
    public abstract void doneFetching();
    public StringBuilder push(){
        throw new RuntimeException();
    }
    public void flush(){
        throw new RuntimeException();
    }
}
