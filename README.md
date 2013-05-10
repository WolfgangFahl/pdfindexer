pdfindexer
==========

Index and search PDF files using Apache Lucene and PDFBox

# How to build
mvn install

## Usage
		java -jar target/com.bitplan.pdfindex-0.0.1.jar 
		
		Pdfindexer Version: 0.0.1
		 github: https://github.com/WolfgangFahl/pdfindexer.git
		
		  usage: java com.bitplan.pdfindexer.Pdfindexer
		 -d (--debug)                 : debug
		                                create additional debug output if this switch
		                                is used
		 -f (--src) VAL               : source directory/or file
		 -h (--help)                  : help
		                                show this usage
		 -i (--idxfile) VAL           : index file
		 -k (--keyWords) VAL          : search
		                                comma separated list of keywords to search
		 -l (--sourceFileList) VAL    : path to ascii-file with source file names
		                                one file/directory may be specified by line
		 -o (--outputfile) VAL        : (html) output file
		                                the output file will contain the search result
		                                with links to the pages in the pdf files that
		                                haven been searched
		 -r (--root) VAL              : root
		                                if a  root is specified the paths in the
		                                sourceFileList and in the output will be
		                                considered relative to this root path
		 -s (--silent)                : stay silent
		                                do not create any output on System.out if this
		                                switch is used
		 -v (--version)               : showVersion
		                                show current version if this switch is used
		 -w (--searchKeyWordList) VAL : file with search words

 
## Example
see test folder for example input and results

    java -jar target/com.bitplan.pdfindex-0.0.1.jar --sourceFileList test/pdffiles.lst --idxfile test/index2 --outputfile test/html/pdfindex.html --searchWordList test/searchwords.txt --root test/ 

resulting html file is in test/html/pdfindex.html

## Author
Author: Wolfgang Fahl / BITPlan GmbH
see http://www.bitplan.com

## License
License as per the parts used (see pom.xml)
