/**
 * Copyright (C) 2013 BITPlan GmbH
 *
 * Pater-Delp-Str. 1
 * D-47877 Willich-Schiefbahn
 *
 * http://www.bitplan.com
 *
 */

package com.bitplan.pdfindex;

import java.util.Comparator;

import org.apache.lucene.document.Document;

/**
 * compare pagenumbers of Lucene Documents
 * @author wf
 *
 */
public class PageComparator implements Comparator<Document> {

	@Override
	public int compare(Document doc1, Document doc2) {
		String source1=doc1.get("SOURCE");
		String source2=doc2.get("SOURCE");
		int cmp=source1.compareTo(source2);
		if (cmp!=0)
			return cmp;
		String page1s=doc1.get("pagenumber");
		String page2s=doc2.get("pagenumber");
		int page1=Integer.parseInt(page1s.trim());
		int page2=Integer.parseInt(page2s.trim());
		if (page1>page2)
			return 1;
		else if (page1<page2)
			return -1;
		else
			return 0;
	}

}
