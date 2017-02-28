/*
 * Copyright 2003-2006 the original author or authors.
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
package com.aliece.alieee.domain.model.cache;

import com.aliece.alieee.common.Startable;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * simple instance cache this class can be used cache some components.
 * 
 *
 * 
 */
public class InstanceCache implements Startable {
	public final static String NAME = "InstanceCache";

	private Map pool = new ConcurrentHashMap();

	public void start() {

	}

	public void stop() {
		pool.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aliece.alieee.controller.cache.Cache#get(java.lang.Object)
	 */
	public Object get(Object key) {
		return pool.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aliece.alieee.controller.cache.Cache#put(java.lang.Object,
	 * java.lang.Object)
	 */
	public void put(Object key, Object value) {
		pool.put(key, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aliece.alieee.controller.cache.Cache#remove(java.lang.Object)
	 */
	public void remove(Object key) {
		pool.remove(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aliece.alieee.controller.cache.Cache#size()
	 */
	public long size() {
		return pool.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aliece.alieee.controller.cache.Cache#clear()
	 */
	public void clear() {
		pool.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aliece.alieee.controller.cache.Cache#contain(java.lang.Object)
	 */
	public boolean contain(Object key) {
		return pool.containsKey(key);
	}

	public Set keys() {
		return pool.keySet();
	}

}
