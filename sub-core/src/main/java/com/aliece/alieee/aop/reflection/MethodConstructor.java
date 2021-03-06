/**
 * Copyright 2003-2006 the original author or authors. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.aliece.alieee.aop.reflection;

import com.aliece.alieee.common.*;
import com.aliece.alieee.container.finder.ContainerCallback;
import com.aliece.alieee.util.Debug;

import java.lang.reflect.Method;

public class MethodConstructor {

    private final static String module = MethodConstructor.class.getName();

    private final MethodInvokerUtil methodInvokerUtil ;
    
    private final TargetMetaRequestsHolder targetMetaRequestsHolder;
    
    private final ContainerCallback containerCallback;
    
    public MethodConstructor(ContainerCallback containerCallback,
    		TargetMetaRequestsHolder targetMetaRequestsHolder) {
    	this.containerCallback = containerCallback;
		this.targetMetaRequestsHolder = targetMetaRequestsHolder;
		this.methodInvokerUtil = new MethodInvokerUtil(targetMetaRequestsHolder);
	}

	/**
     * @return Returns the methodInvokerUtil.
     */
    public MethodInvokerUtil getMethodInvokerUtil() {
        return methodInvokerUtil;
    }
    
    /**
     * ejb's method creating must at first get service's EJB Object;
     * pojo's method creating can only need service's class. 
     *  
     * @param targetServiceFactory
     * @param targetMetaRequest
     * @param methodMetaArgs
     * @return
     */
    public Method createMethod(TargetServiceFactory targetServiceFactory) {
        Method method = null;
        Debug.logVerbose("[alieee] enter create the Method " , module);
        try {
        	TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();
            if (targetMetaRequest.getTargetMetaDef().isEJB()) { 
                Object obj= methodInvokerUtil.createTargetObject(targetServiceFactory);
                method = createObjectMethod(obj, targetMetaRequest.getMethodMetaArgs());
            }else{
                method = createPojoMethod();
            }
        } catch (Exception ex) {
            Debug.logError("[alieee] createMethod error: " + ex, module);
        }
        
        return method;

    }
    
    
    /**
     * create a method object by its meta definition
     * @param targetMetaDef
     * @param cw
     * @param methodMetaArgs
     */
    public Method createPojoMethod() {
        Method method = null;
        TargetMetaRequest targetMetaRequest = targetMetaRequestsHolder.getTargetMetaRequest();
        TargetMetaDef targetMetaDef = targetMetaRequest.getTargetMetaDef();
        MethodMetaArgs methodMetaArgs = targetMetaRequest.getMethodMetaArgs();
        Debug.logVerbose("[alieee] createPOJO Method :" + methodMetaArgs.getMethodName() + " for target service: " + targetMetaDef.getName(), module);
        try {       
            Class thisCLass = containerCallback.getContainerWrapper().getComponentClass(targetMetaDef.getName());            
            if (thisCLass == null) return null;
            method = thisCLass.getMethod(methodMetaArgs.getMethodName(),
                    methodMetaArgs.getParamTypes());
        } catch (NoSuchMethodException ne) {
            Debug.logError("[alieee] method name:"
                    + methodMetaArgs.getMethodName() + " or method parameters type don't match with your service's method", module);
            Object types[] = methodMetaArgs.getParamTypes();
            for(int i = 0; i<types.length; i ++){
                Debug.logError("[alieee]service's method parameter type must be:" + types[i] + "; ", module);
            }
        } catch (Exception ex) {
            Debug.logError("[alieee] createPojoMethod error: " + ex, module);
        }
        
        return method;

    }
    
    /**
     * create a method object by target Object
     * @param ownerClass
     * @param methodMetaArgs
     * @return
     */
    public Method createObjectMethod(Object ownerClass, MethodMetaArgs methodMetaArgs) {
        Method m = null;        
        try {
            m = ownerClass.getClass().getMethod(methodMetaArgs.getMethodName(), 
                                                methodMetaArgs.getParamTypes());
        } catch (NoSuchMethodException nsme) {
            String errS = " NoSuchMethod:" + methodMetaArgs.getMethodName() + " in MethodMetaArgs of className:"
                    + ownerClass.getClass().getName();
            Debug.logError(errS, module);
        } catch (Exception ex) {
            Debug.logError("[alieee] createMethod error:" + ex, module);
        }
        return m;
    }
    
    /**
     * create a method object 
     * @param ownerClass
     * @param methodName
     * @param paramTypes
     * @return
     */
    public Method createObjectMethod(Object ownerClass, String methodName,
            Class[] paramTypes) {
        Method m = null;
        try {
            m = ownerClass.getClass().getMethod(methodName, paramTypes);
        } catch (NoSuchMethodException nsme) {
            String errS = " NoSuchMethod:" + methodName + " in className:"
                    + ownerClass.getClass().getName() + " or method's args type error";
            Debug.logError(errS, module);
        } catch (Exception ex) {
            Debug.logError("[alieee] createMethod error:" + ex, module);
        }
        return m;
    }

}
