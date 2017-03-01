/**
 * Copyright 2003-2006 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aliece.alieee.container.config;

import com.aliece.alieee.common.TargetMetaDef;
import com.aliece.alieee.util.Debug;
import com.aliece.alieee.util.FileLocator;
import com.aliece.alieee.util.UtilValidate;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * by Jdom parse the jdonframework.xml dom4j is faster than jdom, but this
 * parser only be runned for one time ,so parsing speed is not important.
 * 
 * <p>
 *
 * </p>
 * <p>
 * @version alieee 2005 v1.0
 * </p>
 */
public abstract class XmlParser {
	private final static String module = XmlParser.class.getName();

	protected final FileLocator fileLocator = new FileLocator();

	public Map load(String configFileName) {
		Map<String, TargetMetaDef> mps = new LinkedHashMap<String, TargetMetaDef>();
		try {
			if (UtilValidate.isEmpty(configFileName))
				return mps;

			Document doc = buildDocument(configFileName);
			if (doc == null)
				return mps;

			Element root = doc.getRootElement();
			parse(root, mps);

			Debug.logVerbose("[alieee]<!--   config load finished -->", module);
		} catch (Exception ex) {
			Debug.logError("[alieee]configure FileName: " + configFileName + " parsed error: " + ex, module);
		}
		return mps;
	}

	protected Document buildDocument(String configFileName) {
		Debug.logVerbose("[alieee] locate configure file  :" + configFileName, module);
		Document doc = null;
		try {
			InputStream xmlStream = getInputStream(configFileName);
			if (xmlStream == null) {
				Debug.logVerbose("[alieee]can't locate file:" + configFileName, module);
				return null;
			} else {
				Debug.logVerbose("[alieee] configure file found :" + xmlStream, module);
			}

			SAXBuilder builder = new SAXBuilder();
//			builder.setEntityResolver(new DTDEntityResolver());
			doc = builder.build(xmlStream);
			Debug.logVerbose("[alieee] got mapping file ", module);
		} catch (Exception e) {
			Debug.logError("[alieee] JDOMException error: " + e, module);
		}
		return doc;
	}

	protected InputStream getInputStream(String configFileName) throws Exception {
		return fileLocator.getConfPathXmlStream(configFileName);
	}

	public abstract void parse(Element root, Map<String, TargetMetaDef> results) throws Exception;

}
