package org.concurrency.queue;

public interface Queue {
    public StringBuilder push();
    public void flush();
    public StringBuilder pop();
    public void doneFetching();
    public int availableToPopCount();
}
