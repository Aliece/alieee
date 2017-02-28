package com.aliece.alieee.common;


import com.aliece.alieee.container.finder.ContainerCallback;

public interface TargetObjectFactory {

	Object create(ContainerCallback containerCallback) throws Exception;

}