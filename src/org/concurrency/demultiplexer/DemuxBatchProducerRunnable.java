package org.concurrency.demultiplexer;

import static org.concurrency.utils.Constants.*;

public class DemuxBatchProducerRunnable implements Runnable{
    int value = 0;
    Demultiplexer queue;
    int batchSize;
    public DemuxBatchProducerRunnable(Demultiplexer queue, int batchSize) {
        this.queue = queue;
        this.batchSize = batchSize;
    }
    public void run(){
        while (value < DEMUX_MSG_CNT) {
            int cnt=0;
            while(cnt < batchSize && value < DEMUX_MSG_CNT){
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
    public void sendEndSignal() {
        int consumer = 0;
        while (consumer < DEMUX_CONSUMERS_CNT){
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