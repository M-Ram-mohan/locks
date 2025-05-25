package org.concurrency.demultiplexer;

public class DemuxBatchConsumerRunnable implements Runnable {
    int cnt;
    final int id;
    Demultiplexer queue;
    public DemuxBatchConsumerRunnable(Demultiplexer queue, int id){
        this.queue = queue;
        this.id = id;
        cnt = 0;
    }
    public void run() {
        while (true) {
            int msgCnt = queue.availableToPopCount(id);
            while(msgCnt > 0){
                StringBuilder x = queue.pop(id);
                if(x != null){
                    if(x.toString().equals("-1")) {
                        queue.doneFetching(id);
                        return;
                    }
                    cnt++;
                    msgCnt--;
                }
            }
            queue.doneFetching(id);
        }
    }
}
