package org.concurrency.queue;

import static org.concurrency.utils.Constants.QUEUE_MSG_CNT;

public class BatchMessageConsumerRunnable implements Runnable{

    int cnt;
    Queue queue;
    public BatchMessageConsumerRunnable(Queue queue){
        this.queue = queue;
        cnt = 0;
    }
    public void run() {
        while (cnt < QUEUE_MSG_CNT) {
            int msgCnt = queue.availableToPopCount();
            while (msgCnt > 0) {
                StringBuilder x = queue.pop();
                if (x != null) {
                    cnt++;
                    msgCnt--;
                }
            }
            queue.doneFetching();
        }
    }
}
