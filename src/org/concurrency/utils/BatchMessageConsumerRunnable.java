package org.concurrency.utils;

import org.concurrency.queue.Queue;

import static org.concurrency.utils.Constants.MSG_PER_CONSUMER;

public class BatchMessageConsumerRunnable implements Runnable{

    int cnt;
    final int id;
    boolean signalEnd = false;
    Queue queue;
    public BatchMessageConsumerRunnable(Queue queue){
        this.queue = queue;
        this.id = -1;
        cnt = 0;
    }
    public BatchMessageConsumerRunnable(Queue queue, int id){
        this.queue = queue;
        this.id = id;
        cnt = 0;
    }
    public BatchMessageConsumerRunnable(Queue queue, int id, boolean signalEnd){
        this.queue = queue;
        this.id = id;
        this.signalEnd = signalEnd;
    }
    public void run() {
        if(id == -1) {
            runWithoutId();
        } else {
            runWithId();
        }
    }
    private void runWithId(){
        while (cnt < MSG_PER_CONSUMER || signalEnd) {
            int msgCnt = queue.availableToPopCount(id);
            while(msgCnt > 0){
                StringBuilder x = queue.pop(id);
                if(x != null){
                    if(checkEndSignal(x)) {
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
    private void runWithoutId() {
        while (cnt < MSG_PER_CONSUMER || signalEnd) {
            int msgCnt = queue.availableToPopCount();
            while (msgCnt > 0) {
                StringBuilder x = queue.pop();
                if (x != null) {
                    if(checkEndSignal(x)) {
                        queue.doneFetching();
                        return;
                    }
                    cnt++;
                    msgCnt--;
                }
            }
            queue.doneFetching();
        }
    }
    public boolean checkEndSignal(StringBuilder x) {
        return signalEnd && x.toString().equals("-1");
    }
}
