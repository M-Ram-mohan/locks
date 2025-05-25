package org.concurrency.mpmc;

import org.concurrency.demultiplexer.AtomicDemux;
import org.concurrency.demultiplexer.Demultiplexer;

public class AtomicMpMcQueue implements MpMcQueue{
    final int producerCount;
    final int consumerCount;
    Demultiplexer[] demuxes;
    public AtomicMpMcQueue(int producerCount, int consumerCount, int capacity) {
        this.demuxes = new AtomicDemux[producerCount];
        this.producerCount = producerCount;
        this.consumerCount = consumerCount;
        for (int i = 0; i < producerCount; i++) {
            demuxes[i] = new AtomicDemux(consumerCount, capacity);
        }
    }
    public StringBuilder push(int producerIndex) {
        return demuxes[producerIndex].push();
    }
    public void flush(int producerIndex) {
        demuxes[producerIndex].flush();
    }
    public StringBuilder push(int producerIndex, int consumerIndex) {
        return demuxes[producerIndex].push(consumerIndex);
    }
    public void flush(int producerIndex, int consumerIndex) {
        demuxes[producerIndex].flush(consumerIndex);
    }
    public int availableToPopCount(int consumerIndex) {
        int total = 0;
        for (int i = 0; i < producerCount; i++) {
            total += demuxes[i].availableToPopCount(consumerIndex);
        }
        return total;
    }
    public StringBuilder pop(int consumerIndex) {
        for (int i = 0; i < producerCount; i++) {
            StringBuilder msg = demuxes[i].pop(consumerIndex);
            if (msg != null) {
                return msg;
            }
        }
        return null;
    }
    public void doneFetching(int consumerIndex) {
        for (int i = 0; i < producerCount; i++) {
            demuxes[i].doneFetching(consumerIndex);
        }
    }
}
/*
 producerCount = 3;
 consumerCount = 8;
 no-of-groups = p = (3)
 [0,3,6],[1,4,7],[2,5]

 producerCount = 2;
 consumerCount = 5;
 no-of-groups = p = (2)
 [0,2,4],[1,3]

 How to find the number of consumers per group?
 res = (consumerCount/producerCount) + 1(if consumerCount % producerCount > i);

 I've come up with the above design.
 The above approach tries to save space by allotting the same number of queues as the number of consumers.
 This is a good approach if there are going to be a lot of consumers and producers.

 The coral blocks approach is more simplified and uses P*C number of queues.
 This way we can have a single queue for each producer-consumer pair.
 At any point in time, we can be assured that the message will be delivered to any one of the consumers.
 But this approach still doesn't solve the stagnated messages problem.
 We need to have some kind of work stealing mechanism to ensure that the messages are not stagnated in the queues.
 */
