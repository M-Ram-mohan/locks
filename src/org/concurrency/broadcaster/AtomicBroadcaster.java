package org.concurrency.broadcaster;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicBroadcaster implements Broadcaster{
    int consumerCount;
    int currProducerIndex;
    AtomicLong producerIndex;
    int capacity;
    int currEnd;
    int currFilled;
    int[] currConsumerIndices;
    AtomicLong[] consumerIndices;
    StringBuilder[] data;
    public AtomicBroadcaster(int consumerCount, int capacity) {
        this.consumerCount = consumerCount;
        this.capacity = capacity;
        this.currConsumerIndices = new int[consumerCount];
        this.currProducerIndex = 0;
        this.consumerIndices = new AtomicLong[consumerCount];
        this.data = new StringBuilder[capacity];
        this.currEnd = capacity;
        this.currFilled = 0;
        this.producerIndex = new AtomicLong(0);
        for (int i = 0; i < consumerCount; i++) {
            currConsumerIndices[i] = 0;
            consumerIndices[i] = new AtomicLong(0);
        }
        for (int i = 0; i < capacity; i++) {
            data[i] = new StringBuilder();
        }
    }
    public StringBuilder push(){
        if(currProducerIndex < currEnd){
            return data[getWrappedIndex(++currProducerIndex)];
        }
        currEnd = getSmallestOffset() + capacity;
        return null;
    }
    public void flush(){
        producerIndex.set(currProducerIndex);
    }
    public int availableToPopCount(int consumerIndex) {
        if(currConsumerIndices[consumerIndex] < currFilled) {
            return currFilled - currConsumerIndices[consumerIndex];
        }
        currFilled = (int) producerIndex.get();
        return 0;
    }
    public StringBuilder pop(int consumerIndex){
        if(currConsumerIndices[consumerIndex] < currFilled) {
            return data[getWrappedIndex(++currConsumerIndices[consumerIndex])];
        }
        return null;
    }
    public void doneFetching(int consumerIndex) {
        consumerIndices[consumerIndex].set(currConsumerIndices[consumerIndex]);
    }
    public int getWrappedIndex(int index) {
        return ((index-1) % capacity);
    }
    public int getSmallestOffset() {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < consumerCount; i++) {
            int consumerOffset = (int) consumerIndices[i].get();
            if (consumerOffset < min) {
                min = consumerOffset;
            }
        }
        return min;
    }
}
/*
How should consumer interact with the broadcaster?
When consumer calls availableToPopCount(), it should return the difference between
the atomic producer index and the current consumer index for that consumer.
When consumer calls pop(), it should return the data at the current consumer index
When consumer calls doneFetching(), it will just read the data and updates the
atomic consumer index to the minimum index of all consumers.
Dirty reads are possible. Will this effect the correctness of the system ?
The one to update last will ensure it points to the minimum

Simple way, use count on all the mutable objects. When producer publishes a message,
change it to zero. When consumer reads the message, it increments the count.
Producer can check if count is equal to number of consumers and then update the mutable
object. After updating it, it can reset the count to zero.
But this will require us to use capacity number of atomic variables, which means
that many times of memory push and pull operations.

Other way is to use the same number of atomic variables as the number of consumers.
This way, we can use the atomic variables to track the consumer indices.
Now, we can think of this as a single producer, single consumer problem.
Available to pop count will be the difference between the producer index and the current consumer index.
You can pop the objects till the producer index is greater than the current consumer index.
Once you have popped all the objects, you can call doneFetching() to update the consumer index.
Now, the actual problem is how the producer will know till what point the consumers
have consumed.
One way is to just blindly check the atomic consumer indices and then publish till
capacity + min(consumerIndices). This will probably work because it's like solving the
same AtomicQueue problem with one consumer, except that the one consumer is the slow consumer.
Whenever we get stuck, we just recheck if the consumer indices have updated or not.
If not, we just wait for the slowest consumer to consume the messages. Once it consumes the messages,
we can push the new messages. Do we need to have atomic variables for each consumer?
If we don't have atomic variables for each consumer, then it is hard to determine when the changes made
by the consumers will be visible to the producer.
We can try using a volatile for the consumer indices.
We can also use a volatile variable for the producer index to ensure it is visible to others.
A volatile variable can be used at times when there is only one writer and multiple readers.

 */
