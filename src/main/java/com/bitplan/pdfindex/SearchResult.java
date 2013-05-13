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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 * Helper / wrapper class to allow FreeMarker to display Lucene TopDocs results
 * @author wf
 *
 */
public class SearchResult {

	TopDocs topDocs;
	private IndexSearcher searcher;
	private Exception problem;
	
	/**
	 * @return the problem
	 */
	public Exception getProblem() {
		return problem;
	}

	/**
	 * @param problem the problem to set
	 */
	public void setProblem(Exception problem) {
		this.problem = problem;
	}

	/**
	 * @return the topDocs
	 */
	public TopDocs getTopDocs() {
		return topDocs;
	}

	/**
	 * get the totalHits
	 * @return totalHits in Freemarker accessible way
	 */
	public int getTotalHits() {
		return topDocs.totalHits;
	}
	
	/**
	 * get the Documents (sorted by pagenumber)
	 * @return
	 */
	public List<Document> getDocs() {
		List<Document> result=new ArrayList<Document>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			try {
				Document doc = searcher.doc(scoreDoc.doc);
				result.add(doc);
			} catch (Exception e) {
				problem=e;
			}
		}
		Collections.sort(result,new PageComparator());
		return result;
	}
	
	/**
	 * @param topDocs the topDocs to set
	 */
	public void setTopDocs(TopDocs topDocs) {
		this.topDocs = topDocs;
	}

	public SearchResult(IndexSearcher searcher, TopDocs topDocs) {
		this.topDocs=topDocs;
		this.searcher=searcher;
	}

}
