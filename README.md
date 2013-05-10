pdfindexer
==========

Index and search PDF files using Apache Lucene and PDFBox

# How to build
mvn install

# Usage
java -jar target/com.bitplan.pdfindex-0.0.1.jar 

Pdfindexer Version: 0.0.1
 github: https://github.com/WolfgangFahl/pdfindexer.git

  usage: java com.bitplan.pdfindexer.Pdfindexer
 -f (--src) VAL            : source directory/or file
 -i (--idxfile) VAL        : index file
 -l (--sourceFileList) VAL : file with source file names
 -o (--outputfile) VAL     : output file
 -r (--root) VAL           : root
 -s (--search) VAL         : search
 -w (--searchWordList) VAL : file with search words

 
# Example
see test
java -jar target/com.bitplan.pdfindex-0.0.1.jar --sourceFileList test/pdffiles.lst --idxfile test/index2 --outputfile test/html/pdfindex.html --searchWordList test/searchwords.txt 

resulting html file is in test/html/pdfindex.html

# Author
Author: Wolfgang Fahl / BITPlan GmbH
see http://www.bitplan.com

# License
License as per the parts used (see pom.xml)
