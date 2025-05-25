package org.concurrency.mpmc;

import static org.concurrency.utils.Constants.MPMC_PRODUCERS_CNT;

public class MpMcBatchConsumerRunnable implements Runnable{
    MpMcQueue queue;
    int id;
    int cnt;
    int stopRequests;
    public MpMcBatchConsumerRunnable(MpMcQueue queue, int id) {
        this.queue = queue;
        this.id = id;
        this.stopRequests = 0;
        cnt = 0;
    }
    public void run() {
        while (stopRequests  < MPMC_PRODUCERS_CNT) {
            int msgCnt = queue.availableToPopCount(id);
            while (msgCnt > 0 && stopRequests  < MPMC_PRODUCERS_CNT) {
                StringBuilder x = queue.pop(id);
                if (x != null) {
                    if (x.toString().equals("-1")) {
                        stopRequests++;
                    }
                    cnt++;
                    msgCnt--;
                }
            }
            queue.doneFetching(id);
        }
    }
}
