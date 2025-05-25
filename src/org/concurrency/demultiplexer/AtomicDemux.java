package org.concurrency.demultiplexer;

import org.concurrency.queue.AtomicQueue;
import org.concurrency.queue.Queue;

public class AtomicDemux implements Demultiplexer{
    private final Queue[] queue;
    private final int consumerCount;
    public AtomicDemux(int consumerCount, int capacity) {
        this.queue = new AtomicQueue[consumerCount];
        this.consumerCount = consumerCount;
        for(int i = 0; i < consumerCount; i++){
            queue[i] = new AtomicQueue(capacity);
        }
    }
    public StringBuilder push(){
        for(int i = 0; i < consumerCount; i++){
            StringBuilder x = queue[i].push();
            if(x != null){
                return x;
            }
        }
        return null;
    }
    public void flush(){
        for(int i = 0; i < consumerCount; i++){
            queue[i].flush();
        }
    }
    public StringBuilder push(int consumerIndex){
        return queue[consumerIndex].push();
    }
    public void flush(int consumerIndex){
        queue[consumerIndex].flush();
    }
    public int availableToPopCount(int consumerIndex){
        return queue[consumerIndex].availableToPopCount();
    }
    public StringBuilder pop(int consumerIndex){
        return queue[consumerIndex].pop();
    }
    public void doneFetching(int consumerIndex){
        queue[consumerIndex].doneFetching();
    }
}
