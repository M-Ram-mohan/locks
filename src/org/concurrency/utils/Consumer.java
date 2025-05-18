package org.concurrency.utils;

import org.concurrency.queue.Queue;

import java.util.Objects;

public class Consumer implements Runnable {
    int cnt;
    Queue queue;

    public Consumer(Queue queue) {
        this.queue = queue;
        this.cnt = 0;
    }

    @Override
    public void run() {
        while (cnt < 1_000_000) {
            StringBuilder x = queue.pop();
            if (Objects.nonNull(x)) {
                cnt++;
                queue.doneFetching();
            }
        }
    }
}
