package com.aliece.alieee.container.annotation;


import com.aliece.alieee.common.ContainerWrapper;
import com.aliece.alieee.container.AppContextWrapper;
import com.aliece.alieee.container.builder.ContainerRegistry;
import com.aliece.alieee.container.builder.StartablecomponentsRegistry;
import com.aliece.alieee.container.finder.ComponentKeys;
import com.aliece.alieee.util.Debug;

public class AnnotationContainerRegistry extends ContainerRegistry {
	public final static String module = AnnotationContainerRegistry.class.getName();

	private ContainerLoaderAnnotation containerLoaderAnnotation;
	private AppContextWrapper context;

	public AnnotationContainerRegistry(AppContextWrapper context, ContainerWrapper containerWrapper,
			ContainerLoaderAnnotation containerLoaderAnnotation) {
		super(containerWrapper);
		this.containerLoaderAnnotation = containerLoaderAnnotation;
		this.context = context;
	}

	public void registerAnnotationComponents() throws Exception {
		Debug.logVerbose("[JdonFramework] <------ register all annotation components(@component('xxx')/@Interceptor)  ------> ", module);
		try {
			AnnotationHolder annotationHolder = containerLoaderAnnotation.loadAnnotationHolder(context, containerWrapper);
			for (String name : annotationHolder.getComponentNames()) {
				Class classz = annotationHolder.getComponentClass(name);
				containerWrapper.register(name, classz);
				StartablecomponentsRegistry scr = (StartablecomponentsRegistry) containerWrapper.lookup(StartablecomponentsRegistry.NAME);
				scr.add(classz, name);
			}
		} catch (Exception e) {
			Debug.logError("[JdonFramework] registerAnnotationComponents error:" + e, module);
			throw new Exception(e);
		}
	}

	public void copyTargetMetaDefHolder() {
		TargetMetaDefHolder targetMetaDefHoader = (TargetMetaDefHolder) containerWrapper.lookup(ComponentKeys.SERVICE_METAHOLDER_NAME);
		AnnotationHolder annotationHolder = containerLoaderAnnotation.loadAnnotationHolder(context, containerWrapper);
		targetMetaDefHoader.add(annotationHolder.getTargetMetaDefHolder().loadMetaDefs());
	}

	public ContainerLoaderAnnotation getContainerLoaderAnnotation() {
		return containerLoaderAnnotation;
	}

}
