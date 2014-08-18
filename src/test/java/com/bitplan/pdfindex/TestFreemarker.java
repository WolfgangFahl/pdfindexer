/**
 * Copyright (C) 2012-2014 BITPlan GmbH
 *
 * Pater-Delp-Str. 1
 * D-47877 Willich-Schiefbahn
 *
 * http://www.bitplan.com
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.bitplan.pdfindex;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.bitplan.rest.freemarker.FreeMarkerConfiguration;

import freemarker.template.Template;

/**
 * test jqGridFilter
 * @author wf
 *
 */
public class TestFreemarker {

	@Test
	public void testFreemarker() throws Exception {
		// add a template path (test.ftl is to be loaded from this path)
		FreeMarkerConfiguration.addTemplatePath("test/template");
		// add a class path (test1.ftl is to be loaded from this path the source is
		// at src/main/resources/test1.ftl and it is included via
		// target/main/resources/test1.ftl 
		FreeMarkerConfiguration.addTemplateClass(FreeMarkerConfiguration.class, "/templates");
		// the list of test template names
		String templateNames[]={"test.ftl","test1.ftl"};
		Map<String,Object> rootMap=new HashMap<String,Object>();
		String testTitle="Freemarker rocks!";
		rootMap.put("title", testTitle);
		// loop over the templates
		for (String templateName:templateNames){
			// get the template by templateName no matter what the path is
			Template template = FreeMarkerConfiguration.getTemplate(templateName);
			// make sure the template is found
			assertNotNull("template "+templateName+" should not be null",template);
			String html=FreeMarkerConfiguration.doProcessTemplate(templateName, rootMap);
			assertTrue(html.contains(testTitle));
		}
	}

}
