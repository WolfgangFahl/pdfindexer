/**
 * Copyright (C) 2012 BITPlan GmbH
 *
 * Pater-Delp-Str. 1
 * D-47877 Willich-Schiefbahn
 *
 * http://www.bitplan.com
 *
 */
package com.bitplan.pdfindex;

import static org.junit.Assert.*;
import java.io.File;
import org.junit.Test;
import com.bitplan.pdfindex.Pdfindexer;

/**
 * Test the PDF Indexer
 * @author wf
 *
 */
public class TestPdfindexer {
	
	/**
	 * test the pdf Indexer
	 * @param args
	 */
	public void testPdfIndexer(String args[]) {
		Pdfindexer.testMode=true;
		Pdfindexer.main(args);
		assertEquals(0,Pdfindexer.exitCode);
	}
	
	/**
	 * test the PDF Indexer
	 * @param srcDir
	 * @param indexFolderPath
	 * @param searchPhrase
	 * @param htmlFilePath
	 */
	public void testPdfIndexer(String srcDir,String indexFolderPath, String searchPhrase,String htmlFilePath) {
		String[] args={"--src",srcDir,"--idxfile",indexFolderPath,"--search",searchPhrase,"--outputfile",htmlFilePath};
		testPdfIndexer(args);
		File indexFolder=new File(indexFolderPath);
		assertTrue(indexFolder.isDirectory());			
	}

	@Test
	public void testIndexing() {
		this.testPdfIndexer("src/test/data/pdfsource1","src/test/data/index1","Lorem ipsum","/tmp/index.html");
	}

	/**
	 * test the PDF Indexer
	 */
	@Test
	public void testIndexingWithFiles() {
		String[] args={"--sourceFileList","/tmp/pdffiles.lst","--idxfile","/tmp/pdfindex1","--searchWordList","/tmp/searchwords.txt","--outputfile","/tmp/pdfindex.html"};
		this.testPdfIndexer(args);
	}
	
}
