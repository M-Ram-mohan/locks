package org.concurrency.utils;

import org.concurrency.queue.Multiplexer;

import static org.concurrency.utils.Constants.MSG_PER_PRODUCER;

public class MultiplexerProducer implements Runnable{
    Multiplexer queue;
    int cnt;
    int maxCnt;
    public MultiplexerProducer(Multiplexer queue){
        this.queue = queue;
        this.maxCnt = MSG_PER_PRODUCER;
        this.cnt = 0;
    }
    public void run(){
        queue.registerProducer();
        while(cnt<maxCnt){
            StringBuilder x = queue.push();
            if(x != null){
                x.setLength(0);
                x.append(cnt);
                cnt++;
                queue.flush(x);
            }
        }
    }
}
