package org.concurrency.mpmc;

import org.concurrency.queue.Queue;

public interface MpMcQueue {

    public StringBuilder push(int producerIndex);
    public void flush(int producerIndex);
    public StringBuilder pop(int consumerIndex);
    public int availableToPopCount(int consumerIndex);
    public void doneFetching(int consumerIndex);
    public StringBuilder push(int producerIndex, int consumerIndex);
    public abstract void flush(int producerIndex, int consumerIndex);
}
