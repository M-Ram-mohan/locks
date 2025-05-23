package org.concurrency.utils;

import org.concurrency.queue.Queue;

import static org.concurrency.utils.Constants.MSG_PER_CONSUMER;

public class BatchMessageConsumerRunnable implements Runnable{

    int cnt;
    Queue queue;
    public BatchMessageConsumerRunnable(Queue queue){
        this.queue = queue;
        cnt = 0;
    }
    public void run() {
        while (cnt < MSG_PER_CONSUMER) {
            int msgCnt = queue.availableToPopCount();
            while(msgCnt > 0){
                StringBuilder x = queue.pop();
                if(x != null){
                    cnt++;
                    msgCnt--;
                }
            }
            queue.doneFetching();
        }
    }
}
