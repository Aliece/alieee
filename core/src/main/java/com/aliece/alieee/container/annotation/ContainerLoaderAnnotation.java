package com.aliece.alieee.container.annotation;

import com.aliece.alieee.common.ContainerWrapper;
import com.aliece.alieee.container.AppContextWrapper;
import com.aliece.alieee.container.annotation.type.*;
import com.aliece.alieee.container.interceptor.IntroduceInfoHolder;
import com.aliece.alieee.container.pico.ConfigInfo;
import com.aliece.alieee.util.Debug;

/**
 * load all Annotation components
 * 
 * add all annotated components into AnnotationHolder then ContainerBuilder load
 * all components from AnnotationHolder and register into picocontainer.
 * 
 *
 * 
 */
public class ContainerLoaderAnnotation {
	public final static String module = ContainerLoaderAnnotation.class.getName();

	private AnnotationScaner annotationScaner;
	private ConfigInfo configInfo;
	private AnnotationHolder annotationHolder; // lazy

	public ContainerLoaderAnnotation() {
		configInfo = new ConfigInfo();
		annotationScaner = new AnnotationScaner();
	}

	public void startScan(final AppContextWrapper context) {
		annotationScaner.startScan(context);

	}

	public AnnotationHolder loadAnnotationHolder(AppContextWrapper context, ContainerWrapper containerWrapper) {
		if (annotationHolder != null)
			return annotationHolder;

		Debug.logVerbose("[JdonFramework] load all Annotation components ", module);
		annotationHolder = new AnnotationHolder();

		ConsumerLoader consumerLoader = new ConsumerLoader(annotationScaner);
		consumerLoader.loadAnnotationConsumers(annotationHolder, context, containerWrapper);

		ModelConsumerLoader modelConsumerLoader = new ModelConsumerLoader(annotationScaner);
		modelConsumerLoader.loadAnnotationModels(annotationHolder, context, containerWrapper);

		ServiceLoader serviceLoader = new ServiceLoader(annotationScaner, consumerLoader);
		serviceLoader.loadAnnotationServices(annotationHolder, context, containerWrapper);

		ComponentLoader componentLoader = new ComponentLoader(annotationScaner, consumerLoader);
		componentLoader.loadAnnotationComponents(annotationHolder, context, containerWrapper);

		InroduceLoader inroduceLoader = new InroduceLoader(annotationScaner, this.configInfo.getIntroduceInfoHolder());
		inroduceLoader.loadAnnotationIntroduceInfos(annotationHolder, context, containerWrapper);

		InterceptorLoader interceptorLoader = new InterceptorLoader(annotationScaner, configInfo.getIntroduceInfoHolder());
		interceptorLoader.loadAnnotationInterceptors(annotationHolder, context);

		containerWrapper.register(AnnotationHolder.NAME, annotationHolder);
		containerWrapper.register(IntroduceInfoHolder.NAME, configInfo.getIntroduceInfoHolder());
		return annotationHolder;
	}

	public ConfigInfo getConfigInfo() {
		return configInfo;
	}

	public void setConfigInfo(ConfigInfo configInfo) {
		this.configInfo = configInfo;
	}

}
