### pdfindexer
[Java Library and Tool to Index and search PDF files using Apache Lucene and PDF Box](http://www.bitplan.com/PdfIndexer)

[![Travis (.org)](https://img.shields.io/travis/WolfgangFahl/pdfindexer.svg)](https://travis-ci.org/WolfgangFahl/pdfindexer)
[![Maven Central](https://img.shields.io/maven-central/v/com.bitplan.pdfindex/com.bitplan.pdfindex.svg)](https://search.maven.org/artifact/com.bitplan.pdfindex/com.bitplan.pdfindex/0.0.11/jar)
[![codecov](https://codecov.io/gh/WolfgangFahl/pdfindexer/branch/master/graph/badge.svg)](https://codecov.io/gh/WolfgangFahl/pdfindexer)
[![GitHub issues](https://img.shields.io/github/issues/WolfgangFahl/pdfindexer.svg)](https://github.com/WolfgangFahl/pdfindexer/issues)
[![GitHub issues](https://img.shields.io/github/issues-closed/WolfgangFahl/pdfindexer.svg)](https://github.com/WolfgangFahl/pdfindexer/issues/?q=is%3Aissue+is%3Aclosed)
[![GitHub](https://img.shields.io/github/license/WolfgangFahl/pdfindexer.svg)](https://www.apache.org/licenses/LICENSE-2.0)
[![BITPlan](http://wiki.bitplan.com/images/wiki/thumb/3/38/BITPlanLogoFontLessTransparent.png/198px-BITPlanLogoFontLessTransparent.png)](http://www.bitplan.com)

### Documentation
* [Wiki](http://www.bitplan.com/PdfIndexer)
* [pdfindexer Project pages](https://WolfgangFahl.github.io/pdfindexer)
* [Javadoc](https://WolfgangFahl.github.io/pdfindexer/apidocs/index.html)
* [Test-Report](https://WolfgangFahl.github.io/pdfindexer/surefire-report.html)
### Maven dependency

Maven dependency
```xml
<!-- Java Library and Tool to Index and search PDF files using Apache Lucene and PDF Box http://www.bitplan.com/PdfIndexer -->
<dependency>
  <groupId>com.bitplan.pdfindex</groupId>
  <artifactId>com.bitplan.pdfindex</artifactId>
  <version>0.0.11</version>
</dependency>
```

[Current release at repo1.maven.org](http://repo1.maven.org/maven2/com/bitplan/pdfindex/com.bitplan.pdfindex/0.0.11/)

### How to build
```
git clone https://github.com/WolfgangFahl/pdfindexer
cd pdfindexer
mvn install
```

### Purpose
Index and search for keywords in PDF sources (files and URLs) using Apache Lucene and PDFBox
The result will be put in a HTML file - the layout can be modified using a Freemarker template

### Integration into Development enviroment
* The approach from http://stackoverflow.com/questions/14013644/hosting-a-maven-repository-on-github is used

# Examples
see [test folder](https://github.com/WolfgangFahl/pdfindexer/tree/master/test) for example input and results
see Usage below for how to run pdfindexer from command line

### Lorem Ipsum
* Source: [Lorem Ipsum PDF](https://github.com/WolfgangFahl/pdfindexer/blob/master/test/pdfsource1/LoremIpsum.pdf "Click to open PDF source")
* Keywords: https://github.com/WolfgangFahl/pdfindexer/blob/master/test/searchwords.txt
* Result:  [Lorem Ipsum PDF Index](https://github.com/WolfgangFahl/pdfindexer/blob/master/test/pdfindex.html "Click to open html source")

    java -jar pdfindex.jar --sourceFileList test/pdffiles.lst --idxfile test/index2 --outputfile test/html/pdfindex.html --searchKeyWordList test/searchwords.txt --root test/ 
     
resulting html file is in test/html/pdfindex.html

### Cajun project 
PDF text from the University of Notthingham about how to publish journals using the brand new Adobe technology (written 1993)
* Source: http://eprints.nottingham.ac.uk/249/1/cajun.pdf
* Keywords: Adobe IBM MS-DOS
* Result: [Cajun PDF Index](https://github.com/WolfgangFahl/pdfindexer/blob/master/test/cajun.html "Click to open HTML source") 

# Usage
## Directly from jar
  java -jar pdfindexer.jar [options]
  
see usage page below
  
## Usage page
		Pdfindexer Version: 0.0.9
		
		 github: https://github.com/WolfgangFahl/pdfindexer.git
		
		  usage: java com.bitplan.pdfindexer.Pdfindexer
		 --title VAL                  : title to be used in html result
		 -d (--debug)                 : debug
		                                create additional debug output if this switch
		                                is used
		 -e (--autoescape)            : autoescape blanks
			                              set to off if you'd like to use lucene query
			                              syntax		                                
		 -f (--src) VAL               : source url, directory/or file
		 -h (--help)                  : help
		                                show this usage
		 -i (--idxfile) VAL           : index file
		 -k (--keyWords) VAL          : search
		                                comma separated list of keywords to search
		 -l (--sourceFileList) VAL    : path to ascii-file with source urls,directories
		                                or file names
		                                one url/file/directory may be specified by line
		 -m (--maxHits) N             : maximum number of hits per keyword
		 -o (--outputfile) VAL        : (html) output file
		                                the output file will contain the search result
		                                with links to the pages in the pdf files that
		                                haven been searched
		 -p (--templatePath) VAL      : path to Freemarker template file(s) to be used
		                                to format the output
		 -r (--root) VAL              : root
		                                if a  root is specified the paths in the
		                                sourceFileList and in the output will be
		                                considered relative to this root path
		 -s (--silent)                : stay silent
		                                do not create any output on System.out if this
		                                switch is used
		 -t (--templateName) VAL      : name of Freemarker template to be used
		 -v (--version)               : showVersion
		                                show current version if this switch is used
		 -x (--extract)               : extract text
                                    extract text content to files	                                
		 -w (--searchKeyWordList) VAL : file with search words

## Modifying the template
		 src/main/resources/templates 
contains the default freemarker template "defaultindex.ftl". 
You  might want to modify it our create your own template and use the -t/--templateName option to use it.
## Version history
| Version | date  | changes
| ------- | ----- | -------
| 0.0.3 | 2013      | first published version
| 0.0.4 | 2013      | adds text extract feature
| 0.0.5 | 2014-05-31| fixes template - fixes this README  - allows positional command line arguments
| 0.0.6 | 2014-08-18| fixes bug - adds Apache License to README - adds github as maven repository
| 0.0.7 | 2015-02-13| upgrade to apache pdfbox 1.8.4 to avoid bug https://issues.apache.org/jira/browse/PDFBOX-1541
| 0.0.8 | 2015-02-14| switch to NonSeq parser and upgrade to apache pdfbox 1.8.8 to avoid bugs https://issues.apache.org/jira/browse/PDFBOX-1845 and https://issues.apache.org/jira/browse/PDFBOX-2523 a
| 0.0.9 | 2015-02-14| and for good measure apache pdfbox 1.8.9 to avoid https://issues.apache.org/jira/browse/PDFBOX-2579
| 0.0.10 | 2017-04-28| upgrades pdfbox to 1.8.13
| 0.0.11 | 2018-08-22| fixes #4, fixes #5, upgrades Java to Java8 uses com.bitplan.pom
