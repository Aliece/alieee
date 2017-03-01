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
package com.aliece.alieee.container.config.aspect;

import com.aliece.alieee.container.AppContextWrapper;
import com.aliece.alieee.container.config.ComponentMetaDef;
import com.aliece.alieee.container.config.XmlParser;
import com.aliece.alieee.util.Debug;
import org.jdom.Element;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * 
 */
public class AopInterceptorsXmlLoader extends XmlParser {

	public final static String module = AopInterceptorsXmlLoader.class.getName();

	private AppContextWrapper context;

	public AopInterceptorsXmlLoader(AppContextWrapper context) {
		this.context = context;
	}

	public AopInterceptorsXmlLoader() {

	}

	protected InputStream getInputStream(String configFileName) throws Exception {

		InputStream xmlStream = fileLocator.getConfPathXmlStream(configFileName);
		if (xmlStream == null) {
			if (context != null)
				xmlStream = context.getResourceAsStream(configFileName);
		}
		return xmlStream;

	}

	public void parse(Element root, Map mps) throws Exception {
		List interceptors = root.getChildren("interceptor");
		Debug.logVerbose("[alieee] found interceptor size:" + interceptors.size(), module);
		Iterator iter = interceptors.iterator();

		ComponentMetaDef componentMetaDef;

		while (iter.hasNext()) {
			Element component = (Element) iter.next();
			String name = component.getAttributeValue("name");
			Debug.logVerbose("[alieee] found interceptor name:" + name, module);
			String className = component.getAttributeValue("class");
			String pointcut = component.getAttributeValue("pointcut");
			List mappings = component.getChildren("constructor");
			String[] constructors = null;
			if ((mappings != null) && (mappings.size() != 0)) {
				constructors = new String[mappings.size()];
				int j = 0;
				Iterator i = mappings.iterator();
				while (i.hasNext()) {
					Element constructor = (Element) i.next();
					String value = constructor.getAttributeValue("value");
					Debug.logVerbose("[alieee] interceptor " + name + "constructor=" + value, module);
					constructors[j] = value;
					j++;
				}
			}

			if (constructors != null)
				componentMetaDef = new AspectComponentsMetaDef(name, className, constructors, pointcut);
			else
				componentMetaDef = new AspectComponentsMetaDef(name, className, pointcut);

			mps.put(name, componentMetaDef);
		}
	}

}
