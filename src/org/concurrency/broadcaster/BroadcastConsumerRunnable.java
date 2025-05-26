package org.concurrency.broadcaster;

import static org.concurrency.utils.Constants.BROADCASTER_MSG_CNT;

public class BroadcastConsumerRunnable implements Runnable{
    int cnt;
    final int id;
    Broadcaster queue;

    public BroadcastConsumerRunnable(Broadcaster queue, int id) {
        this.queue = queue;
        this.id = id;
        cnt = 0;
    }

    public void run() {
        while (cnt < BROADCASTER_MSG_CNT) {
            int msgCnt = queue.availableToPopCount(id);
            while (msgCnt > 0 && cnt < BROADCASTER_MSG_CNT) {
                StringBuilder x = queue.pop(id);
                if(x != null) {
//                    System.out.println("Consumer " + id + " received: " + x);
                    cnt++;
                    msgCnt--;
                }
            }
            queue.doneFetching(id);
        }
    }
}
