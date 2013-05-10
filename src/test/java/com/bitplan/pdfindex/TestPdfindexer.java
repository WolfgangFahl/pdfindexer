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
		String[] args={"--src",srcDir,"--idxfile",indexFolderPath,"--keyWords",searchPhrase,"--outputfile",htmlFilePath,"--root","test/"};
		testPdfIndexer(args);
		File indexFolder=new File(indexFolderPath);
		assertTrue(indexFolder.isDirectory());			
	}

	@Test
	public void testIndexing() {
		this.testPdfIndexer("test/pdfsource1","test/index1","Lorem ipsum","test/index.html");
	}

	/**
	 * test the PDF Indexer
	 */
	@Test
	public void testIndexingWithFiles() {
		String[] args={"--sourceFileList","test/pdffiles.lst","--idxfile","test/index2","--searchKeyWordList","test/searchwords.txt","--outputfile","test/pdfindex.html","--root","test/"};
		this.testPdfIndexer(args);
	}
	
	@Test
	public void testURI() {
		String docURI="http://eprints.nottingham.ac.uk/249/1/cajun.pdf";
		String htmlResult="test/cajun.html";
		this.testPdfIndexer(docURI,"test/indices/cajun","Adobe IBM MS-DOS",htmlResult);
	}
	
}
