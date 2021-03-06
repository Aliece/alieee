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

package com.aliece.alieee.container;

import com.aliece.alieee.common.Startable;
import com.aliece.alieee.common.TargetMetaDef;
import com.aliece.alieee.container.annotation.TargetMetaDefHolder;
import com.aliece.alieee.container.config.XmlParser;
import com.aliece.alieee.container.config.XmlPojoServiceParser;
import com.aliece.alieee.util.Debug;

import java.util.Iterator;
import java.util.Map;

/**
 * Load target service meta definition from jdonframework.xml this class is
 * registered in container.xml
 * 
 *
 */
public class TargetMetaDefXmlLoader implements Startable {

	private final static String module = TargetMetaDefXmlLoader.class.getName();

	private final XmlParser xmlPojoServiceParser;

	private TargetMetaDefHolder targetMetaDefHolder;
	private AppConfigureCollection appConfigureFiles;

	/**
	 * AppConfigureCollection has been registered in
	 * com.aliece.alieee.container.builder.ContainerRegistry.registerAppConfigureFiles
	 * and add configure file names by
	 * com.aliece.alieee.container.builder.DefaultContainerBuilder.addAppConfigureFile
	 * 
	 * @param appConfigureFiles
	 */
	public TargetMetaDefXmlLoader(AppConfigureCollection appConfigureFiles, TargetMetaDefHolder targetMetaDefHolder) {
		this.appConfigureFiles = appConfigureFiles;
		this.targetMetaDefHolder = targetMetaDefHolder;
		this.xmlPojoServiceParser = new XmlPojoServiceParser();
	}

	public void loadXML() {
		Debug.logVerbose("[alieee]TargetMetaDefXmlLoader start ... found configures:" + appConfigureFiles.getConfigList().size(), module);
		Iterator iter = appConfigureFiles.getConfigList().iterator();
		while (iter.hasNext()) {
			String configFileName = (String) iter.next();
			Debug.logVerbose("[alieee] start to load configure: " + configFileName, module);
			Map<String, TargetMetaDef> pojoMps = xmlPojoServiceParser.load(configFileName);
			targetMetaDefHolder.add(pojoMps);
		}
	}

	public void start() {

	}

	public void stop() {
		targetMetaDefHolder = null;
		appConfigureFiles = null;
	}

}
