package com.aliece.alieee.common;


import com.aliece.alieee.container.finder.ContainerCallback;
import com.aliece.alieee.util.Debug;

public class SingletonPOJOObjectFactory extends POJOObjectFactory {
	private final static String module = SingletonPOJOObjectFactory.class.getName();

	public SingletonPOJOObjectFactory(POJOTargetMetaDef pOJOTargetMetaDef) {
		super(pOJOTargetMetaDef);
	}

	public Object create(ContainerCallback containerCallback) throws Exception {
		Object o = null;
		try {
			Debug.logVerbose("[alieee] create singleton pojo Object for " + pOJOTargetMetaDef.getName(), module);
			ContainerWrapper containerWrapper = containerCallback.getContainerWrapper();
			o = containerWrapper.lookup(pOJOTargetMetaDef.getName());
			Debug.logVerbose("[alieee] create singleton pojo Object id " + o.hashCode(), module);
		} catch (Exception ex) {
			Debug.logError("[alieee]create Singleton error: " + ex + " for class=" + pOJOTargetMetaDef.getClassName(), module);
			throw new Exception(ex);
		} catch (Throwable tw) {
			Debug.logError("[alieee]create Singleton error: " + tw + " for class=" + pOJOTargetMetaDef.getClassName(), module);
			throw new Exception(tw);
		}
		return o;
	}

}
