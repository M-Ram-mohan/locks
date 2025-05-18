package org.concurrency.utils;

import org.concurrency.queue.Queue;

public class BatchMessageConsumerRunnable implements Runnable{

    int cnt;
    Queue queue;
    public BatchMessageConsumerRunnable(Queue queue){
        this.queue = queue;
        cnt = 0;
    }
    public void run() {
        while (cnt < 1_000_000) {
            int msgCnt = queue.availableToPopCount();
            while(msgCnt > 0){
                StringBuilder x = queue.pop();
                cnt++;
                msgCnt--;
            }
            queue.doneFetching();
        }
    }
}
