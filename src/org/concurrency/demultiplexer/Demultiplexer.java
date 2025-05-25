package org.concurrency.demultiplexer;

public interface Demultiplexer {

    public StringBuilder push();
    public void flush();
    public int availableToPopCount(int consumerIndex);
    public StringBuilder pop(int consumerIndex);
    public void doneFetching(int consumerIndex);
    public StringBuilder push(int consumerIndex);
    public void flush(int consumerIndex);
}
