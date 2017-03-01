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
package com.aliece.alieee.container.builder;

import com.aliece.alieee.common.ContainerWrapper;
import com.aliece.alieee.common.POJOTargetMetaDef;
import com.aliece.alieee.container.config.ComponentMetaDef;
import com.aliece.alieee.container.config.aspect.AspectComponentsMetaDef;
import com.aliece.alieee.util.Debug;

/**
 * all register methods for the container by a class manage all register
 * implemention. ContainerBuilder is its client.
 * 
 *
 * 
 */
public class XmlContainerRegistry extends ContainerRegistry {
	public final static String module = XmlContainerRegistry.class.getName();

	public XmlContainerRegistry(ContainerWrapper containerWrapper) {
		super(containerWrapper);

	}

	public void registerComponentMetaDef(ComponentMetaDef componentMetaDef) {
		try {
			String name = componentMetaDef.getName();
			String[] construtors = componentMetaDef.getConstructors();
			String className = componentMetaDef.getClassName();
			Class cclass = createClass(className);
			containerWrapper.register(name, cclass, construtors);
			StartablecomponentsRegistry scr = (StartablecomponentsRegistry) containerWrapper.lookup(StartablecomponentsRegistry.NAME);
			scr.add(cclass, name);
		} catch (Exception ex) {
			Debug.logError("[alieee] registerComponentMetaDef error:" + ex, module);
		}

	}

	public void registerPOJOTargetMetaDef(POJOTargetMetaDef pOJOTargetMetaDef) {
		try {
			String name = pOJOTargetMetaDef.getName();
			String className = pOJOTargetMetaDef.getClassName();
			Class cclass = createClass(className);
			String[] construtors = pOJOTargetMetaDef.getConstructors();
			containerWrapper.register(name, cclass, construtors);
			StartablecomponentsRegistry scr = (StartablecomponentsRegistry) containerWrapper.lookup(StartablecomponentsRegistry.NAME);
			scr.add(cclass, name);

		} catch (Exception ex) {
			Debug.logError("[alieee] registerPOJOTargetMetaDef error:" + ex, module);
		}
	}

	public void registerAspectComponentMetaDef(AspectComponentsMetaDef componentMetaDef) {
		registerComponentMetaDef(componentMetaDef);
	}

	private Class createClass(String className) {
		Class classService = null;
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			classService = classLoader.loadClass(className);
		} catch (Exception ex) {
			Debug.logError("[alieee] createClass:" + ex, module);
		}
		return classService;

	}

}
