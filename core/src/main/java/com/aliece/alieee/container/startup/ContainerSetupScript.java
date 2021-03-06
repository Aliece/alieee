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

package com.aliece.alieee.container.startup;

import com.aliece.alieee.container.AppContextWrapper;
import com.aliece.alieee.container.builder.ContainerDirector;
import com.aliece.alieee.container.builder.ContainerRegistryBuilder;
import com.aliece.alieee.container.factory.ContainerBuilderFactory;
import com.aliece.alieee.util.Debug;

/**
 * setup container
 * 
 */
public class ContainerSetupScript {
	public final static String module = ContainerSetupScript.class.getName();

	private ContainerBuilderFactory containerBuilderContext;

	public ContainerSetupScript() {
		containerBuilderContext = new ContainerBuilderFactory();
	}

	/**
	 * Initialize application container
	 * 
	 * @param context
	 *            ServletContext
	 */
	public void initialized(AppContextWrapper context) {
		ContainerRegistryBuilder cb = (ContainerRegistryBuilder) context.getAttribute(ContainerRegistryBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME);
		if (cb != null)
			return;
		try {
			synchronized (context) {
				cb = containerBuilderContext.createContainerBuilder(context);
				context.setAttribute(ContainerRegistryBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME, cb);
				Debug.logVerbose("[alieee] Initialize the container OK ..");
			}
		} catch (Exception e) {
			Debug.logError("[alieee] initialized error: " + e, module);
		}
	}

	/**
	 * prepare to the applicaition xml Configure for container;
	 * 
	 *            Collection
	 * @param context
	 *            ServletContext
	 */
	public synchronized void prepare(String configureFileName, AppContextWrapper context) {
		ContainerRegistryBuilder cb;
		try {
			cb = (ContainerRegistryBuilder) context.getAttribute(ContainerRegistryBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME);
			if (cb == null) {
				initialized(context);
				cb = (ContainerRegistryBuilder) context.getAttribute(ContainerRegistryBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME);
			}
			ContainerDirector cd = new ContainerDirector(cb);
			cd.prepareAppRoot(configureFileName);
		} catch (Exception ex) {
			Debug.logError(ex, module);
		}
	}

	/**
	 * startup Application container
	 * 
	 *            Collection
	 * @param context
	 *            ServletContext
	 */

	public synchronized void startup(AppContextWrapper context) {
		ContainerRegistryBuilder cb;
		try {
			cb = (ContainerRegistryBuilder) context.getAttribute(ContainerRegistryBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME);
			if (cb == null) {
				Debug.logError("[alieee] at first call prepare method");
				return;
			}
			if (cb.isKernelStartup())
				return;
			ContainerDirector cd = new ContainerDirector(cb);
			cd.startup();
		} catch (Exception ex) {
			Debug.logError(ex, module);
		}
	}

	/**
	 * desroy Application container
	 * 
	 * @param context
	 *            ServletContext
	 */
	public synchronized void destroyed(AppContextWrapper context) {
		try {
			ContainerRegistryBuilder cb = (ContainerRegistryBuilder) context
					.getAttribute(ContainerRegistryBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME);
			if (cb != null) {
				ContainerDirector cd = new ContainerDirector(cb);
				cd.shutdown();
				context.removeAttribute(ContainerRegistryBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME);
				containerBuilderContext = null;
				// context.removeAttribute(ContainerBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME);
				Debug.logVerbose("[alieee] stop the container ..", module);
			}
		} catch (Exception e) {
			Debug.logError("[alieee] destroyed error: " + e, module);
		}

	}

}
