package com.aliece.alieee.client;

import com.aliece.alieee.aop.interceptor.InterceptorsChain;
import com.aliece.alieee.common.ContainerWrapper;
import com.aliece.alieee.common.MethodMetaArgs;
import com.aliece.alieee.container.Application;
import com.aliece.alieee.container.builder.ContainerRegistryBuilder;
import com.aliece.alieee.container.finder.ContainerFinderImp;
import com.aliece.alieee.container.startup.ContainerSetupScript;
import com.aliece.alieee.util.Debug;

/**
 * Java Application call this class to use alieee.
 * 
 * 
 * AppUtil appUtil = new AppUtil();
 * 
 * BInterface b = (BInterface) appUtil.getService("b");
 * 
 *
 * 
 */
public class AppUtil extends Application {
	private final static String module = WebAppUtil.class.getName();

	private final ContainerSetupScript css = new ContainerSetupScript();
	private final static ContainerFinderImp scf = new ContainerFinderImp();

	public AppUtil(String fileName) {
		css.prepare(fileName, this);
	}

	public AppUtil() {
		css.prepare("", this);
	}

	public void stop() {
		css.destroyed(this);
	}

	public Object getComponentInstance(String name) {
		ContainerWrapper containerWrapper = scf.findContainer(this);
		return containerWrapper.lookup(name);
	}


	public String getContainerKey() {
		return ContainerRegistryBuilder.APPLICATION_CONTEXT_ATTRIBUTE_NAME;
	}

	public String getInterceptorKey() {
		return InterceptorsChain.NAME;
	}

	public ContainerWrapper getContainer() throws Exception {
		ContainerFinderImp scf = new ContainerFinderImp();
		return scf.findContainer(this);
	}

	public static MethodMetaArgs createDirectMethod(String methodName, Object[] methodParams) {
		MethodMetaArgs methodMetaArgs = null;
		try {
			if (methodName == null)
				throw new Exception("no configure method value, but now you call it: ");

			Debug.logVerbose("[alieee] construct " + methodName, module);
			Class[] paramTypes = new Class[methodParams.length];
			Object[] p_args = new Object[methodParams.length];
			for (int i = 0; i < methodParams.length; i++) {
				paramTypes[i] = methodParams[i].getClass();
				p_args[i] = methodParams[i];
				Debug.logVerbose("[alieee], parameter type:" + paramTypes[i] + " and parameter value:" + p_args[i], module);
			}
			methodMetaArgs = new MethodMetaArgs(methodName, paramTypes, p_args);

		} catch (Exception ex) {
			Debug.logError("[alieee] createDirectMethod error: " + ex, module);
		}
		return methodMetaArgs;
	}

}
