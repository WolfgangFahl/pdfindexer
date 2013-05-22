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
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import com.bitplan.pdfindex.Pdfindexer;

/**
 * Test the PDF Indexer
 * 
 * @author wf
 * 
 */
public class TestPdfindexer {

	boolean debug = false;

	/**
	 * test the pdf Indexer
	 * 
	 * @param args
	 */
	public void testPdfIndexer(String args[]) {
		Pdfindexer.testMode = true;
		Pdfindexer.main(args);
		assertEquals(0, Pdfindexer.exitCode);
	}

	/**
	 * test the PDF Indexer
	 * 
	 * @param srcDir
	 * @param indexFolderPath
	 * @param searchPhrase
	 * @param htmlFilePath
	 */
	public void testPdfIndexer(String srcDir, String indexFolderPath,
			String searchPhrase, String htmlFilePath) {
		String[] args = { "--src", srcDir, "--idxfile", indexFolderPath,
				"--keyWords", searchPhrase, "--outputfile", htmlFilePath, "--root",
				"test/" };
		// --debug
		testPdfIndexer(args);
		File indexFolder = new File(indexFolderPath);
		assertTrue(indexFolder.isDirectory());
		File htmlFile = new File(htmlFilePath);
		assertTrue(htmlFile.isFile());
	}

	@Test
	public void testIndexing() {
		this.testPdfIndexer("test/pdfsource1", "test/indices/index1",
				"Lorem ipsum", "test/index.html");
	}

	/**
	 * test the PDF Indexer
	 */
	@Test
	public void testIndexingWithFiles() {
		String[] args = { "--sourceFileList", "test/pdffiles.lst", "--idxfile",
				"test/indices/pdffiles", "--searchKeyWordList", "test/searchwords.txt",
				"--outputfile", "test/pdfindex.html", "--root", "test/" };
		this.testPdfIndexer(args);
	}

	@Test
	/**
	 * test using an URI
	 */
	public void testURI() throws IOException {
		String docURI = "http://eprints.nottingham.ac.uk/249/1/cajun.pdf";
		String htmlOutputFileName = "test/cajun.html";
		this.testPdfIndexer(docURI, "test/indices/cajun",
				"Adobe,IBM,MS-DOS,Adobe Illustrator", htmlOutputFileName);
		List<String> lines = FileUtils.readLines(new File(htmlOutputFileName));
		if (debug) {
			int lineNo = 1;
			for (String line : lines) {
				System.out.println("" + lineNo + ":" + line);
				lineNo++;
			}
		}
		assertEquals(35, lines.size());
		String line = lines.get(23);
		assertTrue(line.contains("cajun.pdf#page=5"));
	}

	@Test
	public void testSorting() throws IOException {
		String htmlOutputFileName = "test/cajunfiles.html";
		String[] args = { "--sourceFileList", "test/cajunfiles.lst", "--idxfile",
				"test/indices/cajunfiles", "--keyWords", "Adobe,IBM,MS-DOS",
				"--outputfile", htmlOutputFileName };
		this.testPdfIndexer(args);
		List<String> lines = FileUtils.readLines(new File(htmlOutputFileName));
		assertEquals(44, lines.size());
		String line = lines.get(13);
		assertTrue(line.contains("cajun.pdf#page=1"));
	}

}
