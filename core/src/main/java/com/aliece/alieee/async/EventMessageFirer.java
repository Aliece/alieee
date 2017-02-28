package com.aliece.alieee.async;

import com.aliece.alieee.annotation.model.Owner;
import com.aliece.alieee.annotation.model.Receiver;
import com.aliece.alieee.async.disruptor.DisruptorFactory;
import com.aliece.alieee.async.disruptor.DisruptorForCommandFactory;
import com.aliece.alieee.async.future.FutureListener;
import com.aliece.alieee.domain.message.Command;
import com.aliece.alieee.domain.message.DomainMessage;
import com.aliece.alieee.domain.message.consumer.ModelConsumerMethodHolder;
import com.aliece.alieee.domain.model.injection.ModelProxyInjection;
import com.aliece.alieee.util.Debug;
import com.aliece.alieee.util.ModelUtil;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.aliece.alieee.annotation.model.Send;
import com.aliece.alieee.async.disruptor.EventDisruptor;
import com.aliece.alieee.async.future.EventResultFuture;
import com.aliece.alieee.async.future.FutureDirector;
import com.aliece.alieee.common.Startable;
import org.aopalliance.intercept.MethodInvocation;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuyuan on 17/2/24.
 */
public class EventMessageFirer implements Startable {

    public final static String module = EventMessageFirer.class.getName();

    private DisruptorFactory disruptorFactory;
    private DisruptorForCommandFactory disruptorForCommandFactory;
    private FutureDirector futureDirector;
    private ModelProxyInjection modelProxyInjection;

    public EventMessageFirer(DisruptorFactory disruptorFactory, DisruptorForCommandFactory disruptorForCommandFactory, FutureDirector futureDirector,
                             ModelProxyInjection modelProxyInjection) {
        super();
        this.disruptorFactory = disruptorFactory;
        this.disruptorForCommandFactory = disruptorForCommandFactory;
        this.futureDirector = futureDirector;
        this.modelProxyInjection = modelProxyInjection;
    }

    public void start() {

    }

    public void stop() {
        if (futureDirector != null) {
            futureDirector.stop();
            futureDirector = null;
        }
    }

    public void fire(DomainMessage domainMessage, Send send, FutureListener futureListener) {
        EventResultFuture eventMessageFuture = new EventResultFuture(send.value(), futureListener, domainMessage);
        eventMessageFuture.setAsyn(send.asyn());
        domainMessage.setEventResultHandler(eventMessageFuture);
        futureDirector.fire(domainMessage);

    }

    public void fire(DomainMessage domainMessage, Send send) {
        String topic = send.value();
        if (disruptorForCommandFactory.isContain(topic)) {
            return;
        }
        if (!disruptorFactory.isContain(topic)) {
            Debug.logError(" no found any consumer annonated with @Consumer or its methods with @OnEvent for topic=" + topic, module);
            return;
        }

        try {

            Disruptor disruptor = disruptorFactory.getDisruptor(topic);
            if (disruptor == null) {
                Debug.logError("not create disruptor for " + topic, module);
                return;
            }

            RingBuffer ringBuffer = disruptor.getRingBuffer();
            long sequence = ringBuffer.next();

            EventDisruptor eventDisruptor = (EventDisruptor) ringBuffer.get(sequence);
            if (eventDisruptor == null)
                return;
            eventDisruptor.setTopic(topic);
            eventDisruptor.setDomainMessage(domainMessage);
            ringBuffer.publish(sequence);

        } catch (Exception e) {
            Debug.logError("fire error: " + e.getMessage() + " for" + send.value() + " from:" + domainMessage.getEventSource() + " ", module);
        } finally {

        }
    }

    public void fireToModel(DomainMessage domainMessage, Send send, MethodInvocation invocation) {
        String topic = send.value();
        if (disruptorFactory.isContain(topic))
            return;
        ModelConsumerMethodHolder modelConsumerMethodHolder = disruptorForCommandFactory.getModelConsumerMethodHolder(topic);
        if (modelConsumerMethodHolder == null) {
            Debug.logError(" no found any consumer annonated with @OnCommand for topic=" + topic, module);
            return;
        }
        Object[] arguments = invocation.getArguments();
        if (arguments.length == 0) {
            Debug.logError("there is no a destination parameter(@Receiver) in this method:" + invocation.getMethod().getName() + topic, module);
            return;
        }

        Map params = fetchCommandReceiver(invocation.getMethod(), arguments);
        if (params.size() == 0 || !ModelUtil.isModel(params.get("Receiver"))) {
            Debug.logError(" there is no a destination parameter(@Receiver)  in this method:" + invocation.getMethod().getName()
                    + " or the destination class not annotated with @Model", module);
            return;
        }
        //
        modelProxyInjection.injectProperties(params.get("Receiver"));
        // target model is the owner of the disruptor, single thread to modify
        // aggregate root model's state.
        ((Command) domainMessage).setDestination(params.get("Receiver"));

        Object owner = "System";
        if (params.containsKey("Owner")) {
            owner = params.get("Owner");
        }

        Disruptor disruptor = disruptorForCommandFactory.getDisruptor(topic, owner);
        if (disruptor == null) {
            Debug.logWarning("not create command disruptor for " + topic, module);
            return;
        }

        try {

            RingBuffer ringBuffer = disruptor.getRingBuffer();
            long sequence = ringBuffer.next();

            EventDisruptor eventDisruptor = (EventDisruptor) ringBuffer.get(sequence);
            if (eventDisruptor == null)
                return;
            eventDisruptor.setTopic(topic);
            eventDisruptor.setDomainMessage(domainMessage);
            ringBuffer.publish(sequence);

        } catch (Exception e) {
            Debug.logError("fireToModel error: " + send.value() + " domainMessage:" + domainMessage.getEventSource() + " mode:"
                    + arguments[0].getClass().getName(), module);
        } finally {

        }
    }

    private Map fetchCommandReceiver(Method method, Object[] arguments) {
        Map result = new HashMap();
        int i = 0;
        Annotation[][] paramAnnotations = method.getParameterAnnotations();
        for (Annotation[] anns : paramAnnotations) {
            Object parameter = arguments[i++];
            for (Annotation annotation : anns) {
                if (annotation instanceof Receiver) {
                    result.put("Receiver", parameter);
                    return result;
                } else if (annotation instanceof Owner) {
                    result.put("Owner", parameter);
                }
            }
        }
        return result;
    }
}
