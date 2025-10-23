/**
 * Copyright (C) 2013-2025 BITPlan GmbH
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.junit.Ignore;
import org.junit.Test;


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
	
	/**
	 * check the lines
	 * @param lines
	 * @param expectedSize
	 * @param lineToCheck
	 * @param expectedPart
	 */
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
		String docURI = "https://nottingham-repository.worktribe.com/OutputFile/1024717";
		    // "http://eprints.nottingham.ac.uk/249/1/cajun.pdf";
		String htmlOutputFileName = "test/cajun.html";
		this.testPdfIndexer(docURI, "test/indices/cajun",
				"Adobe,IBM,MS-DOS,Adobe Illustrator", htmlOutputFileName);

		List<String> lines = FileUtils.readLines(new File(htmlOutputFileName), StandardCharsets.UTF_8);

		// result has slightly changed as of 2016-04
		/**
		 * 24:        <li>Adobe Illustrator:2
     * 25:          <a href='http://eprints.nottingham.ac.uk/249/1/cajun.pdf' title='http://eprints.nottingham.ac.uk/249/1/cajun.pdf'>http://eprints.nottingham.ac.uk/249/1/cajun.pdf</a><br/>
     * 26:          <a href='http://eprints.nottingham.ac.uk/249/1/cajun.pdf#page=4' title='http://eprints.nottingham.ac.uk/249/1/cajun.pdf#page=4'>4</a>
     * 27:          <a href='http://eprints.nottingham.ac.uk/249/1/cajun.pdf#page=5' title='http://eprints.nottingham.ac.uk/249/1/cajun.pdf#page=5'>5</a>
     * 28:        </li>   
		 */
		checkLines(lines,41,26,".pdf#page=4");
	}

	@Test
	public void testSorting() throws IOException {
		String htmlOutputFileName = "test/cajunfiles.html";
		String[] args = { "--sourceFileList", "test/cajunfiles.lst", "--idxfile",
				"test/indices/cajunfiles", "--keyWords", "Adobe,IBM,MS-DOS",
				"--outputfile", htmlOutputFileName };
		this.testPdfIndexer(args);
		List<String> lines = FileUtils.readLines(new File(htmlOutputFileName));
		checkLines(lines,36, 16,".pdf#page=1");
	}
	
	/**
	 * check the given html text
	 * @param html
	 * @param expectedTagCount
	 */
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
		String bigPDF="https://manning-content.s3.amazonaws.com/download/0/ceadd4a-9f9c-4958-9892-06047c02651e/sample-ch01_Lowagie.pdf"; // 29 page pdf document
		String[] args = { "--idxfile",
				"test/indices/bigpdf", "--keyWords", "Acrobat,Adobe,base64,document,encrypted,file,Forms,FDF,ISO,PDF,version,XML,XMP,XDP",
				"--outputfile", htmlOutputFileName, bigPDF };
		this.testPdfIndexer(args);
		String html=FileUtils.readFileToString(new File(htmlOutputFileName));
		assertTrue(html.contains("</html>"));
		checkHtml(html,116);
	}
	
	/**
	 * test BITPlan internal usage of Pdfindexer for CPSA-F 
	 * visit http://www.bitplan.com if you are interested in the CPSA-F Software architecture course
	 */
	// Ignore this test on the OpenSource version
	@Ignore
	public void testBITPlan() throws IOException {
		
		String base="/Volumes/bitplan/Projekte/2009/CPSAMaterial2009/";
		String paths[]={"Folien/pdf","english/Folien/pdf"};
		String langs[]={"Deutsch", "English"};
		int index=0;
		for (String path:paths) {
			String lang=langs[index++];
			String htmlOutputFileName = "/tmp/cpsa-f_"+lang+".html";
			String[] args = { 
					"--sourceFileList", base+path+"/pdffiles.lst",
				  "--outputfile",htmlOutputFileName,
				  "--searchKeyWordList", base+path+"/searchwords.txt",
				  "--root",base+path+"/",
				  "--idxfile", base+path+"/index",
				  "--title", "CPSA-F "+lang};
			this.testPdfIndexer(args);
			List<String> lines = FileUtils.readLines(new File(htmlOutputFileName));
			showLines(lines);
		}
		
	}
} // TestPdfIndexer
