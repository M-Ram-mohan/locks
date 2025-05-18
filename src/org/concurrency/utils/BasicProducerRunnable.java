package org.concurrency.utils;

import org.concurrency.queue.Queue;

import java.util.Objects;

public class BasicProducerRunnable implements Runnable {
    int value;
    Queue queue;
    public BasicProducerRunnable(Queue queue){
        this.queue = queue;
        value = 0;
    }
    @Override
    public void run() {
        while (value < 1_000_000) {
            StringBuilder x = queue.push();
            if(Objects.nonNull(x)){
                x.setLength(0);
                x.append(value);
                value++;
                queue.flush();
            }
        }
    }
}
