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
            StringBuilder x = queue.pop();
            if (Objects.nonNull(x)) {
                cnt++;
                queue.doneFetching();
            }
        }
    }
}
