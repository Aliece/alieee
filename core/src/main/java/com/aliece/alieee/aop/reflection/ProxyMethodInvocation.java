/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aliece.alieee.aop.reflection;

import com.aliece.alieee.common.TargetMetaRequestsHolder;
import com.aliece.alieee.common.TargetServiceFactory;
import com.aliece.alieee.util.Debug;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * MethodInvocation Implemention by this class, Interceptor will action.
 * 
 *
 */
public class ProxyMethodInvocation implements MethodInvocation, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8210744876679222009L;

	private final static String module = ProxyMethodInvocation.class.getName();

	protected final TargetServiceFactory targetServiceFactory;

	private final Method method;

	private final Object[] args;

	private Object target;

	protected final List<MethodInterceptor> interceptors;

	protected final MethodInvokerUtil mUtil;

	protected int currentInterceptorInt = -1;

	public ProxyMethodInvocation(List<MethodInterceptor> interceptors, TargetMetaRequestsHolder targetMetaRequestsHolder,
			TargetServiceFactory targetServiceFactory, Method method, Object[] args) {
		Debug.logVerbose("[alieee] method.getName() :" + method.getName(), module);
		this.interceptors = interceptors;
		this.targetServiceFactory = targetServiceFactory;
		this.mUtil = new MethodInvokerUtil(targetMetaRequestsHolder);
		this.method = method;
		this.args = args;
	}

	/**
	 * Invokes next interceptor/proxy target. now there is no mixin
	 */
	public Object proceed() throws Throwable {
		// Debug.logVerbose("[alieee] <-----> enter ProxyMethodInvocation proceed() for "
		// + currentInterceptorInt, module);
		if (currentInterceptorInt == interceptors.size() - 1) {
			Debug.logVerbose("[alieee] finish call all inteceptors", module);
			return methodInvoke();
		}

		Object interceptor = interceptors.get(++currentInterceptorInt);
		if (interceptor != null) {
			MethodInterceptor methodInterceptor = (MethodInterceptor) interceptor;
			// Debug.logVerbose("[alieee] now call inteceptor : "
			// + methodInterceptor.getClass().getName(), module);
			return methodInterceptor.invoke(this);
		} else {
			Debug.logVerbose("[alieee] null finish call all inteceptors", module);
			return methodInvoke();
		}
	}

	private Object methodInvoke() throws Throwable {
		Debug.logVerbose("[alieee]enter method reflection ", module);
		Object result = null;
		try {
			if (target == null) {// interceptor not set target
				Debug.logVerbose("[alieee] all interceptors not set this target object, now create it", module);
				target = mUtil.createTargetObject(targetServiceFactory);
			}

			Debug.logVerbose("[alieee] target:" + target.getClass().getName() + " service's method:" + method.getName() + " running.. ",
					module);
			Debug.logVerbose("[alieee] it is pojo target service", module);
			result = mUtil.execute(method, target, args);
		} catch (Exception ex) {
			Debug.logError("[alieee]run error: " + ex, module);
			throw new Throwable(ex);
		} catch (Throwable tex) {
			throw new Throwable(tex);
		}
		return result;
	}

	public Object[] getArguments() {
		return this.args;
	}

	public Object getThis() {
		/**
		 * try { if (target == null) target =
		 * mUtil.createTargetObject(targetServiceFactory, targetMetaRequest); }
		 * catch (Exception e) {
		 * Debug.logError("[alieee]createTargetObject error :" + e,
		 * module); }
		 **/
		return target;
	}

	public void setThis(Object target) {
		this.target = target;
	}

	public AccessibleObject getStaticPart() {
		return null;
	}

	public Method getMethod() {
		return method;
	}

}
