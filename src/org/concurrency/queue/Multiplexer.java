package org.concurrency.queue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Multiplexer{
    int capacity;
    long currConsumerIndex;
    long currEnd;
    AtomicLong consumerIndex;
    StringBuilder[] arr;
    Map<String, Integer> threadToOffset;
    Map<StringBuilder, Boolean> isProducedMap;
    int producerCount;
    int currProducerCount;
    public Multiplexer(int capacity, int producerCount){
        arr = new StringBuilder[capacity];
        isProducedMap = new HashMap<>(4*capacity);
        for(int i=0;i<capacity;i++){
            arr[i] = new StringBuilder();
            isProducedMap.put(arr[i],false);
        }
        this.producerCount = producerCount;
        this.currProducerCount = 0;
        this.capacity = capacity;
        this.consumerIndex = new AtomicLong(0);
        this.currConsumerIndex = 0;
        this.currEnd = capacity;
        this.threadToOffset = new HashMap<>(4*producerCount);
    }
    public synchronized void registerProducer(){
        threadToOffset.put(Thread.currentThread().getName(), currProducerCount);
        currProducerCount++;
    }
    public Integer getProducerOffset(){
        return threadToOffset.get(Thread.currentThread().getName());
    }
    public void setNextOffset(int currOffset){
        threadToOffset.put(Thread.currentThread().getName(), currOffset+producerCount);
    }
    public StringBuilder push(){
        int currThreadProducerIndex = getProducerOffset();
        if(currThreadProducerIndex<currEnd){
            setNextOffset(currThreadProducerIndex);
            currThreadProducerIndex++;
            return arr[getWrappedIndex(currThreadProducerIndex)];
        }
        currEnd = consumerIndex.get() + capacity;
        if(currThreadProducerIndex < currEnd) {
            setNextOffset(currThreadProducerIndex);
            currThreadProducerIndex++;
            return arr[getWrappedIndex(currThreadProducerIndex)];
        }
        return null;
    }
    public void flush(StringBuilder obj){
        isProducedMap.put(obj,true);
    }
    public StringBuilder pop(){
        currConsumerIndex++;
        int consumerIndexValue = getWrappedIndex(currConsumerIndex);
        if(isProducedMap.get(arr[consumerIndexValue])){
            return arr[consumerIndexValue];
        }
        currConsumerIndex--;
        return null;
    }
    public void processed(StringBuilder obj){
        isProducedMap.put(obj, false);
    }
    public void doneFetching() {
        consumerIndex.set(currConsumerIndex);
    }
    public int getWrappedIndex(long index){
        return (int) ((index-1) % capacity);
    }
}
/*
Multiplexer allows multiple producers to push the messages to the queue. Only one consumer is consuming form the queue
- Consumer is single threaded. So you just need one variable to track how many messages are read.
- This can be used by all the producer threads to decide whether new messages have to be pushed or not.

We have all the free will we want to think about how we can make things work faster.
When it comes to producers, we need to decide of a way to produce messages on to queue.
- One way is to split the queue into multiple partitions so that they can each produce messages independently.
  This is not a bad idea as such. But i', little skeptical if i'm missing something.
  Instead of partitions, we can prolly fix the number of producers and based on that we can assign a block of array in which each producer will be publishing one message
  This way, we can make the producer threads act on independent elements of the queue.
  If we do batches, then it's basically only one thread would be active for a certain block of array when the consumer starts consuming the full queue
  If we are using an atomic integer to coordinate and increment the next producer offset, then we are just doing single threaded producer with added contention and memory push/pull
  Though, doing this can be called lock free.


Consumer index doesn't have to change. How do we represent the producer index? How will consumer know till what point producer has produced? Should we have a variable marking for every message saying that it has been produced?

Implementation :
push : As per the logic, the threads must be coordinated and assigned some offset. Who controls this logic ?
We can construct the producer threads with some offset. Now the producer will request for a message by giving the offset.
Or else, each thread has to manage their own offset and the next index. Now, i have to store it somehow. I can make the threads call a different method with a counter. I can make it synchronized for simplicity.

They chose to not complicate things. They just used an AtomicQueue per producer. 


 */
