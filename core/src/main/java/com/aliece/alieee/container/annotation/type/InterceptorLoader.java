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

import com.aliece.alieee.annotation.Interceptor;
import com.aliece.alieee.common.POJOTargetMetaDef;
import com.aliece.alieee.container.AppContextWrapper;
import com.aliece.alieee.container.annotation.AnnotationHolder;
import com.aliece.alieee.container.interceptor.IntroduceInfoHolder;
import com.aliece.alieee.util.Debug;
import com.aliece.alieee.util.UtilValidate;

import java.util.Set;

public class InterceptorLoader {

	public final static String module = InterceptorLoader.class.getName();

	private AnnotationScaner annotationScaner;

	private IntroduceInfoHolder introduceInfoHolder;

	public InterceptorLoader(AnnotationScaner annotationScaner, IntroduceInfoHolder introduceInfoHolder) {
		super();
		this.annotationScaner = annotationScaner;
		this.introduceInfoHolder = introduceInfoHolder;
	}

	public void loadAnnotationInterceptors(AnnotationHolder annotationHolder, AppContextWrapper context) {
		Set<String> classes = annotationScaner.getScannedAnnotations(context).get(Interceptor.class.getName());
		if (classes == null)
			return;
		Debug.logVerbose("[alieee] found Annotation Interceptor size:" + classes.size(), module);
		for (Object className : classes) {
			createAnnotationInterceptor((String) className, annotationHolder);
		}
	}

	public void createAnnotationInterceptor(String className, AnnotationHolder annotationHolder) {
		try {
			Class cclass = Utils.createClass(className);
			Interceptor inter = (Interceptor) cclass.getAnnotation(Interceptor.class);

			String name = cclass.getName();
			if (!UtilValidate.isEmpty(inter.value())) {
				name = inter.value();
			} else if (!UtilValidate.isEmpty(inter.name())) {
				name = inter.name();
			}

			annotationHolder.addComponent(name, cclass);
			annotationHolder.getInterceptors().put(name, cclass);
			if (!UtilValidate.isEmpty(inter.pointcut())) {
				String[] targets = inter.pointcut().split(",");
				for (int i = 0; i < targets.length; i++) {
					Class targetClass = annotationHolder.getComponentClass(targets[i]);
					if (targetClass != null)
						introduceInfoHolder.addTargetClassNames(targetClass, targets[i]);
				}
			}
			POJOTargetMetaDef pojoMetaDef = new POJOTargetMetaDef(name, className);
			annotationHolder.getTargetMetaDefHolder().add(name, pojoMetaDef);
			Debug.logVerbose("[alieee] load Annotation Interceptor name:" + name + " target class:" + className, module);
		} catch (Exception e) {
			Debug.logError("[alieee] createAnnotationInterceptorClass error:" + e + className, module);
		}
	}

}
