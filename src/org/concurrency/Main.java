package org.concurrency;

import org.concurrency.tests.*;
import org.concurrency.tests.Test;

public class Main {
    public static void main(String[] args) {
//        Test testAtomicQueue = new QueueTest();
//        testAtomicQueue.performTests();
//        Test testMux = new MultiplexerTest();
//        testMux.performTests();
        Test testDemux = new DemuxTest();
        testDemux.performTests();
    }
}

 //TODO : https://github.com/coralblocks/CoralME?tab=readme-ov-file - MatchingEngine CoralSequencer
 //TODO : Checkout ConcurrentQueue and ConcurrentHashMap Implementations
 //TODO : Checkout how to implement locks using wait() and notify()