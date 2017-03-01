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

package com.aliece.alieee.client;

import com.aliece.alieee.aop.interceptor.InterceptorsChain;
import com.aliece.alieee.common.ContainerWrapper;
import com.aliece.alieee.container.ServletContextWrapper;
import com.aliece.alieee.container.builder.ContainerRegistryBuilder;
import com.aliece.alieee.container.finder.ContainerFinderImp;
import com.aliece.alieee.util.Debug;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Used in Web application.
 * 
 * Using WebAppUtil, framework's user can get his businesss service object that
 *
 * this is main and important client class for framework's user.
 * 
 * ForumService forumService = (ForumService)
 * WebAppUtil.getService("forumService", request);
 * 
 * forumService.getForums(start);
 * 
 *
 */
public class WebAppUtil {
	private final static String module = WebAppUtil.class.getName();

	private final static ContainerFinderImp scf = new ContainerFinderImp();

	/**
	 * get a component that registered in container. the component is not
	 * different from the service. the component instance is single instance Any
	 * intercepter will be disable
	 * 
	 */
	public static Object getComponentInstance(String name, HttpServletRequest request) {
		ServletContext sc = request.getSession().getServletContext();
		ContainerWrapper containerWrapper = scf.findContainer(new ServletContextWrapper(sc));
		if (!containerWrapper.isStart()) {
			Debug.logError("alieee not yet started, please try later ", module);
			return null;
		}
		return containerWrapper.lookup(name);
	}

	/**
	 * get a component that registered in container. the component is not
	 * different from the service. the component instance is single instance Any
	 * intercepter will be disable
	 * 
	 * @param sc
	 * @return
	 */
	public static Object getComponentInstance(String name, ServletContext sc) {
		ContainerWrapper containerWrapper = scf.findContainer(new ServletContextWrapper(sc));
		if (!containerWrapper.isStart()) {
			Debug.logError("alieee not yet started, please try later ", module);
			return null;
		}
		return containerWrapper.lookup(name);
	}



	/**
	 * get the key for the application container user can directly get his
	 * container from servletcontext by the key.
	 * 
	 * @return String
	 */
	public static String getContainerKey() {
		return ContainerRegistryBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME;
	}

	/**
	 * get the key for the interceptor, by the key, use can add his interceptor
	 * to the container
	 * 
	 * @return String
	 */
	public static String getInterceptorKey() {
		return InterceptorsChain.NAME;
	}

	/**
	 * get this Web application's container
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return ContainerWrapper
	 * @throws Exception
	 */
	public static ContainerWrapper getContainer(HttpServletRequest request) throws Exception {
		ContainerFinderImp scf = new ContainerFinderImp();
		ServletContext sc = request.getSession().getServletContext();
		return scf.findContainer(new ServletContextWrapper(sc));
	}

	public static ContainerWrapper getContainer(ServletContext sc) throws Exception {
		ContainerFinderImp scf = new ContainerFinderImp();
		return scf.findContainer(new ServletContextWrapper(sc));
	}

}
