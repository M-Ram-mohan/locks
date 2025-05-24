package org.concurrency.utils;

import org.concurrency.queue.Queue;

import static org.concurrency.utils.Constants.CONSUMERS_CNT;
import static org.concurrency.utils.Constants.MSG_PER_PRODUCER;

public class BatchMessageProducerRunnable implements Runnable{
    int value = 0;
    Queue queue;
    int id = -1;
    int batchSize;
    boolean signalEnd = false;
    public BatchMessageProducerRunnable(Queue queue, int batchSize){
        this.queue = queue;
        this.batchSize = batchSize;
    }
    public BatchMessageProducerRunnable(Queue queue, int batchSize, int id){
        this.queue = queue;
        this.id = id;
        this.batchSize = batchSize;
    }
    public BatchMessageProducerRunnable(Queue queue, int batchSize, boolean signalEnd){
        this.queue = queue;
        this.batchSize = batchSize;
        this.signalEnd = signalEnd;
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
        sendEndSignal();
    }
    public void runWithId(){
        while (value < MSG_PER_PRODUCER) {
            int cnt=0;
            while(cnt < batchSize && value < MSG_PER_PRODUCER){
                StringBuilder x = queue.push(id);
                if(x != null){
                    x.setLength(0);
                    x.append(value);
                    value++;
                    cnt++;
                }
            }
            queue.flush(id);
        }
        sendEndSignal();
    }
    public void sendEndSignal() {
        if(!signalEnd) {
            return;
        }
        int consumer = 0;
        while (consumer < CONSUMERS_CNT){
            StringBuilder x = queue.push(consumer);
            if(x != null){
                x.setLength(0);
                x.append(-1);
                queue.flush(consumer);
                consumer++;
            }
        }
    }
}
