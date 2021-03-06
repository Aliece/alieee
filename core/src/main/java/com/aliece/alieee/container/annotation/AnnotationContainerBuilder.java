package com.aliece.alieee.container.annotation;

import com.aliece.alieee.annotation.Interceptor;
import com.aliece.alieee.aop.interceptor.InterceptorsChain;
import com.aliece.alieee.aop.joinpoint.Pointcut;
import com.aliece.alieee.common.ContainerWrapper;
import com.aliece.alieee.container.AppContextWrapper;
import com.aliece.alieee.container.builder.DefaultContainerBuilder;
import com.aliece.alieee.container.config.ContainerComponents;
import com.aliece.alieee.container.finder.ComponentKeys;
import com.aliece.alieee.container.interceptor.IntroduceInfoHolder;
import com.aliece.alieee.util.Debug;
import com.aliece.alieee.util.UtilValidate;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.List;
import java.util.Map;

public class AnnotationContainerBuilder extends DefaultContainerBuilder {
	public final static String module = AnnotationContainerBuilder.class.getName();
	private AnnotationContainerRegistry annotationContainerRegistry;
	private AppContextWrapper context;

	public AnnotationContainerBuilder(AppContextWrapper context, ContainerWrapper containerWrapper, ContainerComponents basicComponents,
									  ContainerComponents aspectConfigComponents, ContainerLoaderAnnotation containerLoaderAnnotation) {
		super(containerWrapper, basicComponents, aspectConfigComponents);
		this.context = context;
		this.annotationContainerRegistry = new AnnotationContainerRegistry(context, containerWrapper, containerLoaderAnnotation);
	}

	public void registerAspectComponents() throws Exception {
		super.registerAspectComponents();
		registerAnnotationAspectComponents();

		InterceptorsChain existedInterceptorsChain = (InterceptorsChain) containerWrapper.lookup(ComponentKeys.INTERCEPTOR_CHAIN);
		// existedInterceptorsChain.getAdvisors(pointcut)
		IntroduceInfoHolder ih = annotationContainerRegistry.getContainerLoaderAnnotation().getConfigInfo().getIntroduceInfoHolder();
		for (String adviceName : ih.getIntroduceNames()) {
			// there is a class(target) use annotation @Introduce("advicename")
			List<String> targetNames = ih.getIntroducerNameByIntroducedName(adviceName);
			for (String targetName : targetNames) {
				// if the advicename has been register by its pointcut is all
				// pojoServices, change its pointcut to this target class
				if (existedInterceptorsChain.findInterceptorFromChainByName(Pointcut.DOMAIN, adviceName)) {
					existedInterceptorsChain.addInterceptor(targetName, adviceName);
				}
			}
		}
	}

	public void registerAnnotationAspectComponents() throws Exception {
		try {
			InterceptorsChain existedInterceptorsChain = (InterceptorsChain) containerWrapper.lookup(ComponentKeys.INTERCEPTOR_CHAIN);
			AnnotationHolder annotationHolder = annotationContainerRegistry.getContainerLoaderAnnotation().loadAnnotationHolder(context,
					containerWrapper);
			Map<String, Class> inters = annotationHolder.getInterceptors();
			for (String name : inters.keySet()) {
				Class classz = inters.get(name);
				if (!(MethodInterceptor.class.isAssignableFrom(classz))) {
					Debug.logError(" your @Interceptor class not implements MethodInteceptor! class=" + classz.getName(), module);
					continue;
				}
				Interceptor inter = (Interceptor) classz.getAnnotation(Interceptor.class);
				if (!UtilValidate.isEmpty(inter.pointcut())) {
					String[] targets = inter.pointcut().split(",");
					for (int i = 0; i < targets.length; i++) {
						// add interceptor instance into InterceptorsChain
						// object
						existedInterceptorsChain.addInterceptor(targets[i], name);
					}
				} else {
					IntroduceInfoHolder ih = annotationContainerRegistry.getContainerLoaderAnnotation().getConfigInfo().getIntroduceInfoHolder();
					List<String> targetNames = ih.getIntroducerNameByIntroducedName(name);
					for (String targetName : targetNames) {
						existedInterceptorsChain.addInterceptor(targetName, name);

					}
					if (targetNames.size() == 0)
						existedInterceptorsChain.addInterceptor(Pointcut.POJO_TARGET_PROPS_SERVICES, name);
				}
			}
		} catch (Exception e) {
			Debug.logError("[alieee] registerAspectComponents" + e, module);
		}
	}

	public void registerUserService() throws Exception {
		super.registerUserService();
		annotationContainerRegistry.registerAnnotationComponents();
	}

	public void doAfterStarted() throws Exception {
		annotationContainerRegistry.copyTargetMetaDefHolder();
	}

}
