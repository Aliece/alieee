/*
 * Copyright 2003-2009 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.aliece.alieee.aop.interceptor;

import com.aliece.alieee.annotation.model.Send;
import com.aliece.alieee.async.EventMessageFirer;
import com.aliece.alieee.async.future.FutureListener;
import com.aliece.alieee.common.Startable;
import com.aliece.alieee.container.finder.ContainerCallback;
import com.aliece.alieee.domain.message.DomainMessage;
import com.aliece.alieee.domain.message.MessageInterceptor;
import com.aliece.alieee.util.Debug;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 *
 */
public class ComponentMessageInterceptor implements MethodInterceptor, Startable {
	public final static String module = MessageInterceptor.class.getName();

	private ContainerCallback containerCallback;
	protected EventMessageFirer eventMessageFirer;

	public ComponentMessageInterceptor(ContainerCallback containerCallback, EventMessageFirer eventMessageFirer) {
		super();
		this.containerCallback = containerCallback;
		this.eventMessageFirer = eventMessageFirer;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		if (!invocation.getMethod().isAnnotationPresent(Send.class))
			return invocation.proceed();

		Send send = invocation.getMethod().getAnnotation(Send.class);
		String channel = send.value();
		Object result = null;
		try {

			result = invocation.proceed();

			DomainMessage message = null;
			if (DomainMessage.class.isAssignableFrom(result.getClass())) {
				message = (DomainMessage) result;
			} else {
				message = new DomainMessage(result);
			}
			eventMessageFirer.fire(message, send);

			// older queue @Send(myChannl) ==> @Component(myChannl)
			Object listener = containerCallback.getContainerWrapper().lookup(channel);
			if (listener != null && listener instanceof FutureListener)
				eventMessageFirer.fire(message, send, (FutureListener) listener);

			eventMessageFirer.fireToModel(message, send, invocation);
		} catch (Exception e) {
			Debug.logError("invoke error: " + e, module);
		}
		return result;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		this.containerCallback = null;
		this.eventMessageFirer = null;

	}
}
