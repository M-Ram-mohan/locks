package org.concurrency;

import org.concurrency.tests.*;
import org.concurrency.tests.Test;

public class Main {
    public static void main(String[] args) {
//        Test testAtomicQueue = new QueueTest();
//        testAtomicQueue.performTests();
        Test multiplexerTest = new MultiplexerTest();
        multiplexerTest.performTests();
    }

}

 //TODO : https://github.com/coralblocks/CoralME?tab=readme-ov-file - MatchingEngine CoralSequencer