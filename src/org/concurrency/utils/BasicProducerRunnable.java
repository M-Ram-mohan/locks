package org.concurrency.utils;

import org.concurrency.queue.Queue;

import java.util.Objects;

import static org.concurrency.utils.Constants.TOTAL_MSG_COUNT;

public class BasicProducerRunnable implements Runnable {
    int value;
    Queue queue;
    int maxValue = TOTAL_MSG_COUNT;
    public BasicProducerRunnable(Queue queue){
        this.queue = queue;
        value = 0;
    }
    @Override
    public void run() {
        while (value < maxValue) {
            StringBuilder x = queue.push();
            if(x != null){
                x.setLength(0);
                x.append(value);
                value++;
                queue.flush();
            }
        }
    }
}
