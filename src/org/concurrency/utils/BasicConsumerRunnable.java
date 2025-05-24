package org.concurrency.utils;

import org.concurrency.queue.Queue;

import java.util.Objects;

import static org.concurrency.utils.Constants.TOTAL_MSG_COUNT;

public class BasicConsumerRunnable implements Runnable {
    int cnt;
    int maxCnt = TOTAL_MSG_COUNT;
    Queue queue;

    public BasicConsumerRunnable(Queue queue) {
        this.queue = queue;
        this.cnt = 0;
    }

    @Override
    public void run() {
        while (cnt < maxCnt) {
            int msgCnt = queue.availableToPopCount();
            while(msgCnt > 0) {
                StringBuilder x = queue.pop();
                if (x != null) {
                    cnt++;
                    msgCnt--;
                    queue.doneFetching();
                }
            }
        }
    }
}
