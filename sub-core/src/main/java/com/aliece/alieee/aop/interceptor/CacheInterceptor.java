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
package com.aliece.alieee.aop.interceptor;

import com.aliece.alieee.common.Startable;
import com.aliece.alieee.domain.model.cache.ModelKey;
import com.aliece.alieee.domain.model.cache.ModelManager;
import com.aliece.alieee.util.Debug;
import com.aliece.alieee.util.ModelUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * Cache Interceptor all Interceptors are added in picoContainer
 * 
 * method match can be done by this class, CacheInterceptor only interceptor the
 * method getXXXXX.
 * 
 */
public class CacheInterceptor implements MethodInterceptor, Startable {
	private final static String module = CacheInterceptor.class.getName();

	private ModelManager modelManager;

	public String match_MethodName = "get";

	private Set isModelCache = new HashSet();

	public CacheInterceptor(ModelManager modelManager) {
		this.modelManager = modelManager;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();

		if (!methodMatchsModelGET(method)) {
			 Debug.logVerbose("[alieee] cacheInteceptor don't action, invoke next invocation.proceed()",
			 module);
			return invocation.proceed(); // 下一个interceptor
		}
		Debug.logVerbose("[alieee] invoke cacheInteceptor method:" + method.getName(), module);
		Class modelClass = method.getReturnType();
		String dataKey = getArguments(invocation);
		if (dataKey == null || modelClass == null)
			return invocation.proceed();
		try {
			ModelKey modelKey = new ModelKey(dataKey, modelClass);
			Object model = modelManager.getCache(modelKey);
			if (model == null) {
				model = invocation.proceed(); // 下一个interceptor
				if (model != null && modelClass.isAssignableFrom(model.getClass())) {
					Debug.logVerbose("[alieee] save to cache", module);
					model = modelManager.addCache(modelKey, model);
				}
			}
			return model;
		} catch (Exception e) {
			Debug.logError("[alieee]CacheInterceptor Exception error:" + e + " method:" + method.getName() + " return class:" + modelClass
					+ " dataKey:" + dataKey, module);
		}
		return invocation.proceed();
	}

	/**
	 * 1.check return type if is Model 2.check method name if include "get" 3.
	 * if found them, cache this method
	 * 
	 * 
	 * @param method
	 *            Method
	 * @return boolean
	 */
	private boolean methodMatchsModelGET(Method method) {
		boolean condition = false;
		try {
			if (isModelCache.contains(method)) {
				condition = true;
				return condition;
			}

			String mehtodName = method.getName();
			if (method.getReturnType() == null)
				return condition; // 无返回值，不做缓存
			Class returnClass = method.getReturnType();
			if (returnClass.getSuperclass() == null)
				return condition; // 无返回值，不做缓存

			Debug.logVerbose("[alieee]methodMatchsModelGET: returnClassName = " + returnClass.getName(), module);
			if (ModelUtil.isModel(returnClass)) {
				if (mehtodName.indexOf(match_MethodName) != -1) {
					condition = true;
					// method name include "getXXXX" and return Type is subClass
					// of Model
					isModelCache.add(method);
				}
			}
		} catch (Exception ex) {
			Debug.logError("[alieee]Exception error:" + ex, module);
		} catch (Throwable the) {
			Debug.logError("[alieee]Throwable error:" + the, module);
		}
		return condition;

	}

	/**
	 * 组合参数数值为一个字符串 这些参数必须实现toString();
	 * 
	 * @param invocation
	 *            MethodInvocation
	 * @return String
	 */
	public String getArguments(MethodInvocation invocation) {

		try {
			Object[] args = invocation.getArguments();
			if (args == null)
				return null;
			if (args.length != 1)
				return null;// 参数是一个主键

			if (args[0] == null)
				return null;
			return args[0].toString();
		} catch (Exception ex) {
			Debug.logError("[alieee] method:" + invocation.getMethod().getName() + "  " + ex, module);
			return null;
		}

	}

	public String getMatch_MethodName() {
		return match_MethodName;
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		this.isModelCache.clear();
		this.isModelCache = null;
		this.modelManager = null;

	}

}
