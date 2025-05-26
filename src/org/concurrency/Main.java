package org.concurrency;

import org.concurrency.tests.*;
import org.concurrency.tests.Test;

public class Main {
    public static void main(String[] args) {
        Test testAtomicQueue = new QueueTest();
        testAtomicQueue.performTests();
        Test testMux = new MultiplexerTest();
        testMux.performTests();
        Test testDemux = new DemuxTest();
        testDemux.performTests();
        Test testMpMcQueue = new MpMcQueueTest();
        testMpMcQueue.performTests();
        Test testBroadcaster = new BroadcasterTest();
        testBroadcaster.performTests();
        Test testConcurrentQueue = new ConcurrentQueueTest();
        testConcurrentQueue.performTests();
    }
}

/*
The tests are done on a 4 core machine

 Messages : 1_000_000 - Single Producer and Single Consumer
 - Atomic Queue : 100ms - 150ms
 - Mux : 100ms - 150ms
 - Demux : 100ms - 150ms
 - MpMc Queue : 100ms- 150ms
 - Broadcaster : 100ms - 150ms
 - Concurrent Queue : 58000ms - 60000ms

 Messages : 10_000_000
 - Atomic Queue - 1P - 1C : ~900ms
 - Mux - 3P - 1C : ~1200ms
 - Demux - 1P - 3C : ~1000ms
 - MpMc Queue - 2P - 2C : ~500ms
 - Broadcaster - 1P - 3C : ~1000ms

 */

 //TODO : https://github.com/coralblocks/CoralME?tab=readme-ov-file - MatchingEngine CoralSequencer
 //TODO : Checkout ConcurrentQueue and ConcurrentHashMap Implementations
 //TODO : Checkout how to implement locks using wait() and notify()
 //TODO : LCK - Need to update the stats correctly. Prev stats are derived from the wrong code