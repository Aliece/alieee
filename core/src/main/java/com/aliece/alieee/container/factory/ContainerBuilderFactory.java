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
package com.aliece.alieee.container.factory;

import com.aliece.alieee.common.ContainerWrapper;
import com.aliece.alieee.container.AppContextWrapper;
import com.aliece.alieee.container.Application;
import com.aliece.alieee.container.annotation.AnnotationContainerBuilder;
import com.aliece.alieee.container.annotation.ContainerLoaderAnnotation;
import com.aliece.alieee.container.builder.ContainerRegistryBuilder;
import com.aliece.alieee.container.config.ContainerComponents;

/**
 * fetch the all components configures, and create ContainerBuilder Instance.
 * 
 *
 * 
 */
public class ContainerBuilderFactory {
	public final static String module = ContainerBuilderFactory.class.getName();

	private ContainerLoaderXML containerLoaderXML;
	private ContainerLoaderAnnotation containerLoaderAnnotation;

	public ContainerBuilderFactory() {
		containerLoaderXML = new ContainerLoaderXML();
		containerLoaderAnnotation = new ContainerLoaderAnnotation();
	}

	/**
	 * the main method in this class, read all components include interceptors
	 * from Xml configure file.
	 * 
	 * create a micro container instance. and then returen a ContainerBuilder
	 * instance
	 * 
	 * @param context
	 * @return
	 */
	public synchronized ContainerRegistryBuilder createContainerBuilder(AppContextWrapper context) {
		containerLoaderAnnotation.startScan(context);

		ContainerFactory containerFactory = new ContainerFactory();
		ContainerWrapper cw = containerFactory.create(containerLoaderAnnotation.getConfigInfo());

		ContainerComponents configComponents = containerLoaderXML.loadAllContainerConfig(context);
		ContainerComponents aspectConfigComponents = containerLoaderXML.loadAllAspectConfig(context);

		return createContainerBuilder(context, cw, configComponents, aspectConfigComponents);
	}

	public synchronized ContainerRegistryBuilder createContainerBuilderForTest(String container_configFile, String aspect_configFile) {
		AppContextWrapper context = new Application();
		containerLoaderAnnotation.startScan(context);

		ContainerFactory containerFactory = new ContainerFactory();
		ContainerWrapper cw = containerFactory.create(containerLoaderAnnotation.getConfigInfo());

		ContainerComponents configComponents = containerLoaderXML.loadBasicComponents(container_configFile);
		ContainerComponents aspectConfigComponents = containerLoaderXML.loadAspectComponents(null, aspect_configFile);
		return createContainerBuilder(context, cw, configComponents, aspectConfigComponents);

	}

	public ContainerRegistryBuilder createContainerBuilder(AppContextWrapper context, ContainerWrapper cw, ContainerComponents configComponents,
			ContainerComponents aspectConfigComponents) {
		return new AnnotationContainerBuilder(context, cw, configComponents, aspectConfigComponents, containerLoaderAnnotation);
	}

}
