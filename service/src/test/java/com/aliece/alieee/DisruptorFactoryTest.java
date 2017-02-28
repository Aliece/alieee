package com.aliece.alieee;

import com.aliece.alieee.async.disruptor.DisruptorFactory;
import com.aliece.alieee.async.disruptor.EventResultDisruptor;
import com.aliece.alieee.async.disruptor.EventResultFactory;
import com.aliece.alieee.async.disruptor.ValueEventProcessor;
import com.lmax.disruptor.*;
import junit.framework.TestCase;

import java.util.concurrent.TimeUnit;

/**
 * Created by wuyuan on 17/2/27.
 */
public class DisruptorFactoryTest extends TestCase {

    DisruptorFactory disruptorFactory;

    protected void setUp() throws Exception {
        super.setUp();
        disruptorFactory = new DisruptorFactory();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testValueEventProcessor() throws AlertException, InterruptedException, TimeoutException {
        RingBuffer ringBuffer = RingBuffer.createSingleProducer(new EventResultFactory(), 4, new TimeoutBlockingWaitStrategy(10000,
                TimeUnit.MILLISECONDS));
        ValueEventProcessor valueEventProcessor = new ValueEventProcessor(ringBuffer);

        int numMessages = ringBuffer.getBufferSize();
        int offset = 0;
        for (int i = 0; i < numMessages + offset; i++) {
            valueEventProcessor.send(i);
            System.out.print("\n push=" + i);
        }

        long expectedSequence = numMessages + offset - 1;
        SequenceBarrier barrier = ringBuffer.newBarrier();
        long available = barrier.waitFor(expectedSequence);
        assertEquals(expectedSequence, available);
        System.out.print("\n expectedSequence=" + expectedSequence);

        for (int i = 0; i < numMessages + offset; i++) {
            System.out.print("\n i=" + ((EventResultDisruptor) ringBuffer.get(i)).getValue() + " " + i);
        }
    }
}
