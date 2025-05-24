package org.concurrency.queue;

public interface Queue {
    public StringBuilder push();
    public void flush();
    public StringBuilder push(int index);
    public void flush(int index);
    public StringBuilder pop();
    public void doneFetching();
    public int availableToPopCount();
    public int availableToPopCount(int index);
    public StringBuilder pop(int index);
    public void doneFetching(int index);
}
