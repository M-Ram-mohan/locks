package org.concurrency.broadcaster;

import static org.concurrency.utils.Constants.BROADCASTER_BATCH_SIZE;
import static org.concurrency.utils.Constants.BROADCASTER_MSG_CNT;

public class BroadcastProducerRunnable implements Runnable {
    int value;
    final Broadcaster queue;

    public BroadcastProducerRunnable(Broadcaster queue) {
        this.queue = queue;
        this.value = 0;
    }

    public void run() {
        while (value < BROADCASTER_MSG_CNT) {
            int batchSize = 0;
            while (batchSize < BROADCASTER_BATCH_SIZE && value < BROADCASTER_MSG_CNT) {
                StringBuilder x = queue.push();
                if (x != null) {
//                    System.out.println("Produced : " + value);
                    x.setLength(0);
                    x.append(value);
                    value++;
                    batchSize++;
                }
            }
            queue.flush();
        }
    }
}
