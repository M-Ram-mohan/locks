package org.concurrency.multiplexer;

import static org.concurrency.utils.Constants.MUX_MSG_PER_PRODUCER;

public class MuxBatchProducerRunnable implements Runnable{
    Multiplexer queue;
    int batchSize;
    int id;
    int value = 0;
    public MuxBatchProducerRunnable(Multiplexer queue, int batchSize, int id){
        this.queue = queue;
        this.batchSize = batchSize;
        this.id = id;
    }
    public void run() {
        while (value < MUX_MSG_PER_PRODUCER) {
            int cnt=0;
            while(cnt < batchSize && value < MUX_MSG_PER_PRODUCER){
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
    }
}