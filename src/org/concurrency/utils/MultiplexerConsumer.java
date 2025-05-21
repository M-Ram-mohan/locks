package org.concurrency.utils;

import org.concurrency.queue.Multiplexer;

import static org.concurrency.utils.Constants.BATCH_SIZE;
import static org.concurrency.utils.Constants.TOTAL_MSG_COUNT;

public class MultiplexerConsumer implements Runnable{
    Multiplexer queue;
    int cnt;
    int maxCnt;
    public MultiplexerConsumer(Multiplexer queue){
        this.queue = queue;
        this.maxCnt = TOTAL_MSG_COUNT;
        this.cnt = 0;
    }
    public void run(){
        while(cnt<maxCnt){
            int msg = 0;
            while (msg < BATCH_SIZE && cnt < maxCnt) {
                StringBuilder x = queue.pop();
                if(x != null){
                    cnt++;
                    msg++;
                    queue.processed(x);
                }
            }
            queue.doneFetching();
        }
    }
}
