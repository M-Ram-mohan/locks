package org.concurrency.multiplexer;

import static org.concurrency.utils.Constants.MUX_MSG_CNT;

public class MuxBatchConsumerRunnable implements Runnable{
    int cnt;
    Multiplexer queue;
    public MuxBatchConsumerRunnable(Multiplexer queue){
        this.queue = queue;
        cnt = 0;
    }
    public void run() {
        while (cnt < MUX_MSG_CNT) {
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
