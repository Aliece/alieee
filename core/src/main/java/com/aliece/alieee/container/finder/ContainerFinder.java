package com.aliece.alieee.container.finder;


import com.aliece.alieee.common.ContainerWrapper;
import com.aliece.alieee.container.AppContextWrapper;

/**
 * difference with ContainerCallback:
 * ContainerCallback client is in the container.
 * this client is outside the container
 *
 *
 */
public interface ContainerFinder {

	public abstract ContainerWrapper findContainer(AppContextWrapper sc);

}