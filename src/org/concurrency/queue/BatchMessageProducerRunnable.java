package org.concurrency.queue;

import static org.concurrency.utils.Constants.QUEUE_MSG_CNT;

public class BatchMessageProducerRunnable implements Runnable{
    int value = 0;
    Queue queue;
    int batchSize;
    public BatchMessageProducerRunnable(Queue queue, int batchSize){
        this.queue = queue;
        this.batchSize = batchSize;
    }
    public void run() {
        while (value < QUEUE_MSG_CNT) {
            int cnt=0;
            while(cnt < batchSize && value < QUEUE_MSG_CNT){
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
