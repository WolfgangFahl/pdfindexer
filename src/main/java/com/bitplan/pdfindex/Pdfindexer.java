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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.pdfbox.Version;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import com.bitplan.rest.freemarker.FreeMarkerConfiguration;

/**
 * indexer for PDF files inspired by
 * http://kalanir.blogspot.de/2008/08/indexing-pdf-documents-with-lucene.html
 * 
 * @author wf
 * 
 */
public class Pdfindexer {
	/**
	 * current Version of the Pdfindexer tool
	 */
	public static final String VERSION = "0.0.3";

	@Option(name = "-d", aliases = { "--debug" }, usage = "debug\ncreate additional debug output if this switch is used")
	boolean debug = false;

	@Option(name = "-e", aliases = { "--autoescape" }, usage = "autoescape blanks\nset to off if you'd like to use lucene query syntax")
	private boolean autoescape=true;

	@Option(name = "-f", aliases = { "--src" }, usage = "source url, directory/or file")
	private String source;

	@Option(name = "-h", aliases = { "--help" }, usage = "help\nshow this usage")
	boolean showHelp = false;

	@Option(name = "-i", aliases = { "--idxfile" }, usage = "index file", required = true)
	private String indexFile;

	@Option(name = "-k", aliases = { "--keyWords" }, usage = "search\ncomma separated list of keywords to search")
	private String search;

	@Option(name = "-l", aliases = { "--sourceFileList" }, usage = "path to ascii-file with source urls,directories or file names\none url/file/directory may be specified by line")
	private String sourceFileList;
	
	@Option(name = "-m", aliases = { "--maxHits" }, usage = "maximum number of hits per keyword")
	private int maxHits=1000;

	@Option(name = "-o", aliases = { "--outputfile" }, usage = "(html) output file\nthe output file will contain the search result with links to the pages in the pdf files that haven been searched")
	private String outputFile;
	
	@Option(name = "-p", aliases = { "--templatePath" }, usage = "path to Freemarker template file(s) to be used to format the output")
	private String templatePath;

	@Option(name = "-r", aliases = { "--root" }, usage = "root\nif a  root is specified the paths in the sourceFileList and in the output will be considered relative to this root path")
	private String root;

	@Option(name = "-s", aliases = { "--silent" }, usage = "stay silent\ndo not create any output on System.out if this switch is used")
	boolean silent = false;
	
	@Option(name = "-t", aliases = { "--templateName" }, usage = "name of Freemarker template to be used")
	private String templateName="defaultindex.ftl";
	
	@Option(name = "--title",  usage = "title to be used in html result")
	private String title="PDF Index";

	@Option(name = "-v", aliases = { "--version" }, usage = "showVersion\nshow current version if this switch is used")
	boolean showVersion = false;

	@Option(name = "-w", aliases = { "--searchKeyWordList" }, usage = "file with search words")
	private String searchWordList;

	private static Pdfindexer indexer;
	private CmdLineParser parser;
	static int exitCode;
	public static boolean testMode = false;
	public List<Document> documents = new ArrayList<Document>();
	protected IndexWriter writer;
	protected IndexSearcher searcher = null;

	/**
	 * @return the templateName
	 */
	public String getTemplateName() {
		return templateName;
	}

	/**
	 * @param templateName the templateName to set
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @return the templatePath
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * @param templatePath the templatePath to set
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	/**
	 * @return the root
	 */
	public String getRoot() {
		return root;
	}

	/**
	 * @param root
	 *          the root to set
	 */
	public void setRoot(String root) {
		this.root = root;
	}

	/**
	 * @return the search
	 */
	public String getSearch() {
		return search;
	}

	/**
	 * @param search
	 *          the search to set
	 */
	public void setSearch(String search) {
		this.search = search;
	}

	/**
	 * @return the outputFile
	 */
	public String getOutputFile() {
		return outputFile;
	}

	/**
	 * @param outputFile
	 *          the outputFile to set
	 */
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	/**
	 * @return the indexFile
	 */
	public String getIndexFile() {
		return indexFile;
	}

	/**
	 * @param indexFile
	 *          the indexFile to set
	 */
	public void setIndexFile(String indexFile) {
		this.indexFile = indexFile;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param sourceDirectory
	 *          the sourceDirectory to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the autoescape
	 */
	public boolean isAutoescape() {
		return autoescape;
	}

	/**
	 * @param autoescape the autoescape to set
	 */
	public void setAutoescape(boolean autoescape) {
		this.autoescape = autoescape;
	}

	/**
	 * @return the sourceFileList
	 */
	public String getSourceFileList() {
		return sourceFileList;
	}

	/**
	 * @return the maxHits
	 */
	public int getMaxHits() {
		return maxHits;
	}

	/**
	 * @param maxHits the maxHits to set
	 */
	public void setMaxHits(int maxHits) {
		this.maxHits = maxHits;
	}

	/**
	 * @param sourceFileList
	 *          the sourceFileList to set
	 */
	public void setSourceFileList(String sourceFileList) {
		this.sourceFileList = sourceFileList;
	}

	/**
	 * @return the searchWordList
	 */
	public String getSearchWordList() {
		return searchWordList;
	}

	/**
	 * @param searchWordList
	 *          the searchWordList to set
	 */
	public void setSearchWordList(String searchWordList) {
		this.searchWordList = searchWordList;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * get an Index Writer
	 * 
	 * @return
	 * @throws Exception
	 */
	public IndexWriter getIndexWriter() throws Exception {
		if (writer == null) {

			StandardAnalyzer analyzer = new StandardAnalyzer();
			if (debug)
				System.out.println("indexing with PDFBox-Lucene Version "
						+ Version.getVersion());
			writer = new IndexWriter(this.indexFile, analyzer, true,
					MaxFieldLength.UNLIMITED);
		}
		return writer;
	}

	/**
	 * allow different Document Sources
	 * 
	 * @author wf
	 * 
	 */
	public class DocumentSource {
		File file;
		URL uri;

		/**
		 * create DocumentSource from pFile
		 * 
		 * @param pFile
		 */
		public DocumentSource(File pFile) {
			file = pFile;
		}

		/**
		 * create a DocumentSource from an URL
		 * 
		 * @param pUrl
		 */
		public DocumentSource(URL pUrl) {
			uri = pUrl;
		}

		/**
		 * get the Document
		 * @return
		 * @throws IOException
		 */
		public PDDocument getDocument() throws IOException {
			PDDocument result = null;
			if (file != null) {
				if (!file.canRead())
					throw new IllegalArgumentException(
							"addToIndex called with unreadable source " + file.getPath());
				result = PDDocument.load(file);
			} else {
				result=PDDocument.load(uri);
			}
			return result;
		}
		
		/**
		 * remove root part from path or uri
		 * 
		 * @param root
		 * @return
		 */
		public String unRooted(String root) {
			String result = null;
			if (file!=null) {
				result=file.getPath();
			} else {
				result=uri.toString();
			}
			if (root != null)
				if (result.startsWith(root)) {
					result = result.substring(root.length());
				}
			return result;
		}
	}

	/**
	 * add the given file or URI to the index
	 * 
	 * @param file
	 * @throws Exception
	 */
	private void addToIndex(DocumentSource source) throws Exception {

		PDDocument pddDocument = source.getDocument();
		PDFTextStripper textStripper = new PDFTextStripper();
		for (int pageNo = 1; pageNo <= pddDocument.getNumberOfPages(); pageNo++) {
			textStripper.setStartPage(pageNo);
			textStripper.setEndPage(pageNo);
			String pageContent = textStripper.getText(pddDocument);
			// System.out.println(pageContent);
			Document doc = new Document();

			// Add the page number
			doc.add(new Field("pagenumber", Integer.toString(pageNo),
					Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("content", pageContent, Field.Store.NO,
					Field.Index.ANALYZED));
			doc.add(new Field("SOURCE", source.unRooted(this.root), Field.Store.YES,
					Field.Index.ANALYZED));
			documents.add(doc);
			getIndexWriter().addDocument(doc);
		}
		pddDocument.close();
	}


	private void close() throws Exception {
		getIndexWriter().optimize();
		getIndexWriter().close();
	}

	/**
	 * get the sources to index
	 * 
	 * @return
	 * @throws MalformedURLException 
	 */
	public List<DocumentSource> getFilesToIndex(String pSource) throws MalformedURLException {
		List<DocumentSource> result = new ArrayList<DocumentSource>();
		if (pSource != null) {
			UrlValidator urlValidator = new UrlValidator();
			if (urlValidator.isValid(pSource)) {
				result.add(new DocumentSource(new URL(pSource)));
			} else {
				File sourceFile = new File(pSource);
				if (sourceFile.isFile()) {
					result.add(new DocumentSource(sourceFile));
				} else if (sourceFile.isDirectory()) {
					for (final File file : sourceFile.listFiles()) {
						if (file.isDirectory()) {
							result.addAll(getFilesToIndex(file.getAbsolutePath()));
						}
						if (file.isFile()) {
							if (file.getAbsolutePath().toLowerCase().endsWith(".pdf")) {
								result.add(new DocumentSource(file));
							}
						}
					}
				} else {
					throw new IllegalArgumentException("getFilesToIndex failed for '"
							+ pSource + "' it is neither an URI, nor a file nor a directory");
				}
			}
		}
		return result;
	}

	/**
	 * get the sources to Index
	 * 
	 * @return
	 * @throws IOException
	 */
	public List<DocumentSource> getSourcesToIndex() throws IOException {
		List<DocumentSource> result = new ArrayList<DocumentSource>();
		result.addAll(getFilesToIndex(source));
		if (sourceFileList != null) {
			List<String> lines = FileUtils.readLines(new File(sourceFileList));
			for (String aFilepath : lines) {
				if (root != null) {
					aFilepath = root + aFilepath;
				}
				result.addAll(getFilesToIndex(aFilepath));
			}
		}
		return result;
	}

	/**
	 * create an index
	 * 
	 * @throws Exception
	 */
	private void createIndex() throws Exception {
		List<DocumentSource> sources = getSourcesToIndex();
		for (DocumentSource source : sources) {
			if (!silent)
				System.out.println("adding " + source.unRooted(null) + " to index");
			addToIndex(source);
		}
		close();
	}

	/**
	 * http://lucene.472066.n3.nabble.com/search-trough-single-pdf-document-return
	 * -page-number-td598698.html
	 * http://tarikguelzim.wordpress.com/2008/09/13/creating
	 * -a-search-engine-to-parse-index-and-search-pdf-file-content/ search the
	 * 
	 * @return hits for the query
	 * @param qString
	 * @param idxField
	 * @param limit
	 * @return
	 */
	protected TopDocs searchIndex(String qString, String idxField, int limit) {
		TopDocs topDocs = null;
		Directory fsDir = null;
		Query query = null;
		QueryParser qParser = null;

		try {
			// query index
			if (debug) {
				System.out.println("Searching for " + qString + " in " + indexFile
						+ " — field: " + idxField);
			}

			// create index searcher
			fsDir = FSDirectory.getDirectory(indexFile);
			searcher = new IndexSearcher(fsDir);
			// parse query
			qParser = new QueryParser(idxField, new StandardAnalyzer());
			if (this.autoescape)
				qString=qString.replace(" ", "\\ ");
			query = qParser.parse(qString);
			// query = QueryParser.parse(qString, idxField, new StandardAnalyzer());
			if (debug)
				System.out.println("query: '" + query.toString() + "'");
			// search
			topDocs = searcher.search(query, limit);

		} catch (IOException ex) {
			System.err.println("Index file" + indexFile + " doesn’t exist");
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
			System.exit(1);
		} catch (ParseException ex) {
			System.err.println("Error while parsing the index.");
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		}
		return topDocs;
	}

	/**
	 * get Output
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public PrintWriter getOutput() throws FileNotFoundException {
		PrintWriter output = new PrintWriter(System.out);
		if (this.outputFile != null) {
			if (!silent)
				System.out.println("creating output " + outputFile);
			output = new PrintWriter(new File(outputFile));
		}
		return output;
	}

	/**
	 * get the search words
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getSearchWords() throws Exception {
		List<String> result = new ArrayList<String>();
		if (this.search != null) {
			for (String word : search.split(",")) {
				result.add(word);
			}
		}
		if (this.searchWordList != null) {
			result.addAll(FileUtils.readLines(new File(this.searchWordList)));
		}
		return result;
	}

	/**
	 * get the Index as html
	 * @param searchResults
	 * @return
	 * @throws Exception
	 */
	public String getIndexAsHtml(SortedMap<String, SearchResult> searchResults) throws Exception {
		Map<String,Object> rootMap=new HashMap<String,Object>();
		rootMap.put("searchResults", searchResults);
		rootMap.put("title", this.getTitle());
		if (this.getTemplatePath()!=null)
			FreeMarkerConfiguration.addTemplatePath(this.getTemplatePath());
		FreeMarkerConfiguration.addTemplateClass(FreeMarkerConfiguration.class, "/templates");
		String html=FreeMarkerConfiguration.doProcessTemplate(this.getTemplateName(), rootMap);
		return html;
	}

	/**
	 * create and search the given Index
	 * 
	 * @throws Exception
	 */
	protected void work() throws Exception {
		if (this.showVersion || this.debug)
			showVersion();
		if (this.showHelp)
			showHelp();
		if ((this.getSource() != null) || (this.getSourceFileList() != null))
			createIndex();
		PrintWriter output = getOutput();
		List<String> words = getSearchWords();
		SortedMap<String,SearchResult> searchResults=new TreeMap<String,SearchResult>();
		for (String word : words) {
			TopDocs topDocs = searchIndex(word, "content", getMaxHits());
			searchResults.put(word, new SearchResult(searcher,topDocs));
		}
		String html=this.getIndexAsHtml(searchResults);
		output.print(html);
		output.flush();
	}

	/**
	 * handle the given Throwable
	 * 
	 * @param t
	 */
	public void handle(Throwable t) {
		System.out.flush();
		t.printStackTrace();
		usage(t.getMessage());
	}

	/**
	 * show the Version
	 */
	public void showVersion() {
		System.err.println("Pdfindexer Version: " + VERSION);
		System.err.println();
		System.err
				.println(" github: https://github.com/WolfgangFahl/pdfindexer.git");
		System.err.println("");
	}

	/**
	 * display usage
	 * 
	 * @param msg
	 *          - a message to be displayed (if any)
	 */
	public void usage(String msg) {
		System.err.println(msg);
		showVersion();
		System.err.println("  usage: java com.bitplan.pdfindexer.Pdfindexer");
		parser.printUsage(System.err);
		exitCode = 1;
	}

	/**
	 * show Help
	 */
	public void showHelp() {
		usage("Help");
	}

	/**
	 * main routine
	 * 
	 * @param args
	 */
	public int maininstance(String[] args) {
		parser = new CmdLineParser(this);
		try {
			parser.parseArgument(args);
			work();
			exitCode = 0;
		} catch (CmdLineException e) {
			// handling of wrong arguments
			usage(e.getMessage());
		} catch (Exception e) {
			handle(e);
			// System.exit(1);
			exitCode = 1;
		}
		return exitCode;
	}

	/**
	 * main routine
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		indexer = new Pdfindexer();
		int result = indexer.maininstance(args);
		if (!testMode)
			System.exit(result);
	}

}
