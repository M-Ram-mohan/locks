package org.concurrency.mpmc;

import static org.concurrency.utils.Constants.MPMC_CONSUMERS_CNT;
import static org.concurrency.utils.Constants.MPMC_MSG_PER_PRODUCER;

public class MpMcBatchProducerRunnable implements Runnable{
    MpMcQueue queue;
    int value;
    int batchSize;
    int id;
    public MpMcBatchProducerRunnable(MpMcQueue queue, int batchSize, int id) {
        this.queue = queue;
        value = 0;
        this.batchSize = batchSize;
        this.id = id;
    }
    public void run(){
        while (value < MPMC_MSG_PER_PRODUCER) {
            int msgCnt = 0;
            while (msgCnt < batchSize && value < MPMC_MSG_PER_PRODUCER) {
                StringBuilder x = queue.push(id);
                if (x != null) {
                    x.setLength(0);
                    x.append(value);
                    value++;
                    msgCnt++;
                }
            }
            queue.flush(id);
        }
        sendEndSignal();
    }
    public void sendEndSignal(){
        int consumer = 0;
        while (consumer < MPMC_CONSUMERS_CNT) {
            StringBuilder x = queue.push(id, consumer);
            if (x != null) {
                x.setLength(0);
                x.append(-1);
                queue.flush(id, consumer);
                consumer++;
            }
        }
    }

}
