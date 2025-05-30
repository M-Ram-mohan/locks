package org.concurrency.utils;

public class Constants {
    // Queue configuration constants
    public static final int QUEUE_SIZE = 1024;
    public static final int QUEUE_BATCH_SIZE = 128;
    public static final int QUEUE_MSG_CNT = 9_000_000;
    // Mux configuration constants
    public static final int MUX_PRODUCERS_CNT = 3;
    public static final int MUX_MSG_CNT = 9_000_000;
    public static final int MUX_MSG_PER_PRODUCER = MUX_MSG_CNT / MUX_PRODUCERS_CNT;
    public static final int MUX_QUEUE_SIZE = 1024;
    public static final int MUX_BATCH_SIZE = 128;
    // Demux configuration constants
    public static final int DEMUX_CONSUMERS_CNT = 3;
    public static final int DEMUX_MSG_CNT = 9_000_000;
    public static final int DEMUX_QUEUE_SIZE = 1024;
    public static final int DEMUX_BATCH_SIZE = 128;
    // MpMc configuration constants
    public static final int MPMC_PRODUCERS_CNT = 2;
    public static final int MPMC_CONSUMERS_CNT = 2;
    public static final int MPMC_MSG_CNT = 9_000_000;
    public static final int MPMC_QUEUE_SIZE = 1024;
    public static final int MPMC_BATCH_SIZE = 128;
    public static final int MPMC_MSG_PER_PRODUCER = MPMC_MSG_CNT / MPMC_PRODUCERS_CNT;
    // Broadcaster configuration constants
    public static final int BROADCASTER_QUEUE_SIZE = 1024;
    public static final int BROADCASTER_BATCH_SIZE = 128;
    public static final int BROADCASTER_MSG_CNT = 9_000_000;
    public static final int BROADCASTER_CONSUMERS_CNT = 3;

}
