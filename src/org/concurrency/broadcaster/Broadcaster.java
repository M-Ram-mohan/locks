package org.concurrency.broadcaster;

public interface Broadcaster {
    public StringBuilder push();
    public void flush();
    public int availableToPopCount(int consumerIndex);
    public StringBuilder pop(int consumerIndex);
    public void doneFetching(int consumerIndex);
}
/*
 Broadcaster ensures it delivers a message to all the subscribers (consumers).
 If we start simple, we can have a single producer and multiple consumers.
 In case of single producer, we can have a push() and flush() methods.
 As there are multiple consumers, we need to have all the three methods with input param
 being the consumer index.
 The above methods should do the work expected from a broadcaster.
 Now internally, you need to ensure the message is delivered to all the consumers.
 You also need to track the producer index so that it doesn't override a message which
 is not yet consumed by all the consumers.
 You can do two things here.
 - You can send the message to all the queues of the consumers and then flush them.
 - You can just use the same queue and the store the information about the number of consumers
 that have consumed the message. You need to create a different array which tracks the
 number of consumers that have consumed the message. If the message is consumed by all the
 consumers, then you just mark it as free and allow the producer to push a new message.
 Producer can check everytime if the mutable object is free or not and then push the message
 accordingly. Then it marks the message as locked.
 Whenever a consumer pops the message, it processes it and then triggers the doneFetching method.
 This method should increase the counter of all the consumed objects.
 */
