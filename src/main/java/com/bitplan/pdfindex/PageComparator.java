/**
 * Copyright (C) 2013-2014 BITPlan GmbH
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

import java.util.Comparator;

import org.apache.lucene.document.Document;

/**
 * compare pagenumbers of Lucene Documents
 * 
 * @author wf
 * 
 */
public class PageComparator implements Comparator<Document> {

	@Override
	public int compare(Document doc1, Document doc2) {
		String source1 = doc1.get("SOURCE");
		String source2 = doc2.get("SOURCE");
		int cmp = source1.compareTo(source2);
		if (cmp != 0)
			return cmp;
		String page1s = doc1.get("pagenumber");
		String page2s = doc2.get("pagenumber");
		int page1 = Integer.parseInt(page1s.trim());
		int page2 = Integer.parseInt(page2s.trim());
		if (page1 > page2)
			return 1;
		else if (page1 < page2)
			return -1;
		else
			return 0;
	}

}
