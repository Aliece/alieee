package com.aliece.alieee.container;

import com.aliece.alieee.util.FileLocator;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Application implements AppContextWrapper {

	private Map attrs = new HashMap();

	public InputStream getResourceAsStream(String name) {
		FileLocator fl = new FileLocator();
		return fl.getConfPathXmlStream(name);
	}

	public String getInitParameter(String key) {
		return "";
	}

	public Object getAttribute(String key) {
		return attrs.get(key);

	}

	public void setAttribute(String key, Object o) {
		attrs.put(key, o);
	}

	public void clear() {
		attrs.clear();
	}

	@Override
	public void removeAttribute(String key) {
		attrs.remove(key);
	}

}
