package org.concurrency.utils;

public class Constants {
    // Queue configuration constants
    public static final int QUEUE_SIZE = 1024;
    public static final int QUEUE_BATCH_SIZE = 128;
    public static final int QUEUE_MSG_CNT = 1_000_000;
    // Mux configuration constants
    public static final int MUX_PRODUCERS_CNT = 1;
    public static final int MUX_MSG_CNT = 1_000_000;
    public static final int MUX_MSG_PER_PRODUCER = MUX_MSG_CNT / MUX_PRODUCERS_CNT;
    public static final int MUX_QUEUE_SIZE = 1024;
    public static final int MUX_BATCH_SIZE = 128;
    // Demux configuration constants
    public static final int DEMUX_CONSUMERS_CNT = 1;
    public static final int DEMUX_MSG_CNT = 1_000_000;
    public static final int DEMUX_QUEUE_SIZE = 1024;
    public static final int DEMUX_BATCH_SIZE = 128;

}
