package org.concurrency.utils;

public class Constants {
    public static final int QUEUE_SIZE = 4096;
    public static final int BATCH_SIZE = 256;
    public static final int TOTAL_MSG_COUNT = 9_000_000;
    public static final int PRODUCERS_CNT = 3;
    public static final int MSG_PER_PRODUCER = TOTAL_MSG_COUNT / PRODUCERS_CNT;
    public static final int CONSUMERS_CNT = 1;
    public static final int MSG_PER_CONSUMER = TOTAL_MSG_COUNT / CONSUMERS_CNT;

}
