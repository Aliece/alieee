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
package com.aliece.alieee.container.annotation.type;


public class Utils {

	public static Class createClass(String className) {
		Class classService = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			classService = classLoader.loadClass(className);
		} catch (Exception ex) {
			System.err.print("[JdonFramework] createClass error:" + ex);
		}
		return classService;
	}

}
