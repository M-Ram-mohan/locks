package org.concurrency.utils;

import org.concurrency.queue.Queue;

import static org.concurrency.utils.Constants.MSG_PER_PRODUCER;

public class BatchMessageProducerRunnable implements Runnable{
    int value;
    Queue queue;
    int id;
    int batchSize;
    public BatchMessageProducerRunnable(Queue queue, int batchSize){
        this.queue = queue;
        value = 0;
        id = -1;
        this.batchSize = batchSize;
    }
    public BatchMessageProducerRunnable(Queue queue, int batchSize, int id){
        this.queue = queue;
        this.id = id;
        value = 0;
        this.batchSize = batchSize;
    }
    public void run() {
       if(id==-1) {
           runWithoutId();
       } else {
              runWithId();
       }
    }
    public void runWithoutId(){
        while (value < MSG_PER_PRODUCER) {
            int cnt=0;
            while(cnt < batchSize && value < MSG_PER_PRODUCER){
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
    public void runWithId(){
        while (value < MSG_PER_PRODUCER) {
            int cnt=0;
            while(cnt < batchSize && value < MSG_PER_PRODUCER){
                StringBuilder x = queue.push(id);
                if(x != null){
                    x.setLength(0);
                    x.append("hello testing");
                    value++;
                    cnt++;
                }
            }
            queue.flush(id);
        }
    }
}
