package org.concurrency.multiplexer;

public interface Multiplexer {
    public StringBuilder push(int producerIndex);
    public void flush(int producerIndex);
    public int availableToPopCount();
    public StringBuilder pop();
    public void doneFetching();
}
