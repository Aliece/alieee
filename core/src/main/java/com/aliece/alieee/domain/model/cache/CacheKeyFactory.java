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

package com.aliece.alieee.domain.model.cache;

/**
 * different cached object, there is different cache key. CacheKeyFactory is for
 * creating cache key for different object
 * 
 * 
 *
 * @version 1.0
 */
public abstract class CacheKeyFactory {
	public final static String module = CacheKeyFactory.class.getName();

	public CacheKey createCacheKey(String dataKey, String typeName) {
		return createCacheKeyImp(dataKey, typeName);
	}

	public abstract CacheKey createCacheKeyImp(String dataKey, String typeName);

}
