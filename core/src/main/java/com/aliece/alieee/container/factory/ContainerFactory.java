package com.aliece.alieee.container.factory;

import com.aliece.alieee.common.ContainerWrapper;
import com.aliece.alieee.container.pico.ConfigInfo;
import com.aliece.alieee.container.pico.PicoContainerWrapper;

public class ContainerFactory {

	public synchronized ContainerWrapper create(ConfigInfo configInfo) {
		PicoContainerWrapper pico = new PicoContainerWrapper(configInfo);
		configInfo.setContainerWrapper(pico);
		pico.registerContainerCallback();
		return pico;
	}
}
