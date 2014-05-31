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
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
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
	
	/**
	 * test extracting text
	 * @throws IOException 
	 */
	@Test
	public void testExtracting() throws IOException {
		File txtFile=new File("test/pdfsource1/LoremIpsum.txt");
		if (txtFile.exists())
			txtFile.delete();
		String[] args = { "--sourceFileList", "test/pdffiles.lst", "--idxfile",
				"test/indices/pdffiles", "--searchKeyWordList", "test/searchwords.txt",
				"--outputfile", "test/pdfindex.html", "--root", "test/", "-x" };
		this.testPdfIndexer(args);
		assertTrue(txtFile.exists());
		String txt=FileUtils.readFileToString(txtFile,"UTF-8");
		assertTrue(txt.contains("Lorem"));
	}
	
	/**
	 * show lines for debug
	 * @param lines
	 */
	public void showLines(List<String> lines ){
		if (debug) {
			int lineNo = 1;
			for (String line : lines) {
				System.out.println("" + lineNo + ":" + line);
				lineNo++;
			}
		}
	}
	
	public void checkLines(List<String> lines, int expectedSize, int lineToCheck,String expectedPart) {
		showLines(lines);
		assertEquals(expectedSize, lines.size());
		String line = lines.get(lineToCheck-1);
		assertTrue("line "+lineToCheck+" should contain "+expectedPart,line.contains(expectedPart));
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
		/**
		 * 24:        <li>Adobe Illustrator:2
     * 25:          <a href='http://eprints.nottingham.ac.uk/249/1/cajun.pdf' title='http://eprints.nottingham.ac.uk/249/1/cajun.pdf'>http://eprints.nottingham.ac.uk/249/1/cajun.pdf</a><br/>
     * 26:          <a href='http://eprints.nottingham.ac.uk/249/1/cajun.pdf#page=4' title='http://eprints.nottingham.ac.uk/249/1/cajun.pdf#page=4'>4</a>
     * 27:          <a href='http://eprints.nottingham.ac.uk/249/1/cajun.pdf#page=5' title='http://eprints.nottingham.ac.uk/249/1/cajun.pdf#page=5'>5</a>
     * 28:        </li>   
		 */
		checkLines(lines,41,27,"cajun.pdf#page=5");
	}

	@Test
	public void testSorting() throws IOException {
		String htmlOutputFileName = "test/cajunfiles.html";
		String[] args = { "--sourceFileList", "test/cajunfiles.lst", "--idxfile",
				"test/indices/cajunfiles", "--keyWords", "Adobe,IBM,MS-DOS",
				"--outputfile", htmlOutputFileName };
		this.testPdfIndexer(args);
		List<String> lines = FileUtils.readLines(new File(htmlOutputFileName));
		checkLines(lines,49, 16,"cajun.pdf#page=1");
	}
	
	public void checkHtml(String html, int expectedTagCount) {
		CleanerProperties props = new CleanerProperties();
		// set some properties to non-default values
		props.setTranslateSpecialEntities(true);
		props.setTransResCharsToNCR(true);
		props.setOmitComments(true);
		 
		// do parsing
		TagNode tagNode = new HtmlCleaner(props).clean(
		    html
		);
		TagNode[] tags = tagNode.getAllElements(true);
		if (debug) {
			System.out.println("found: "+tags.length+" tags");
		}
		assertEquals("html #of tags",expectedTagCount,tags.length);
	}

	@Test
	public void testBigPDF() throws IOException {
		String htmlOutputFileName = "test/bigpdf.html";
		String bigPDF="http://www.manning.com/lowagie/sample-ch03_Lowagie.pdf"; // 29 page pdf document
		String[] args = { "--idxfile",
				"test/indices/bigpdf", "--keyWords", "Acrobat,Adobe,base64,document,encrypted,file,Forms,FDF,ISO,PDF,version,XML,XMP,XDP",
				"--outputfile", htmlOutputFileName, bigPDF };
		this.testPdfIndexer(args);
		String html=FileUtils.readFileToString(new File(htmlOutputFileName));
		assertTrue(html.contains("</html>"));
		checkHtml(html,169);
	}
	
	/**
	 * test BITPlan internal usage of Pdfindexer for CPSA-F 
	 * visit http://www.bitplan.com if you are interested in the CPSA-F Software architecture course
	@Test
	public void testBITPlan() throws IOException {
		String htmlOutputFileName = "/tmp/cpsa-f.html";
		String path="/Volumes/bitplan/Projekte/2009/CPSAMaterial2009/english/Folien/pdf";
		String[] args = { 
				"--sourceFileList", path+"/pdffiles.lst",
			  "--outputfile",htmlOutputFileName,
			  "--searchKeyWordList", path+"/searchwords.txt",
			  "--root",path+"/",
			  "--idxfile", path+"/index",
			  "--title", "CPSA-F English"};
		this.testPdfIndexer(args);
		List<String> lines = FileUtils.readLines(new File(htmlOutputFileName));
		showLines(lines);
		
	}
	*/
}
