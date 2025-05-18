package org.concurrency.utils;

import org.concurrency.queue.Queue;

public class BatchMessageProducerRunnable implements Runnable{
    int value;
    Queue queue;
    int batchSize;
    public BatchMessageProducerRunnable(Queue queue, int batchSize){
        this.queue = queue;
        value = 0;
        this.batchSize = batchSize;
    }
    public void run() {
        while (value < 1_000_000) {
            int cnt=0;
            while(cnt < batchSize && value < 1_000_000){
                StringBuilder x = queue.push();
                if(x != null){
                    x.setLength(0);
                    x.append(value);
                    value++;
                    cnt++;
                }
            }
            queue.flush();
        }
    }
}
