<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--
  Created with PDFIndexer
  see  https://github.com/WolfgangFahl/pdfindexer
-->
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>${title}</title>
  </head>
  <body>
    <h1>${title}</h1>
      <ul>
        <#list searchResults?keys as key>
          <#assign searchResult=searchResults[key]>
        <li>${key}:${searchResult.getTotalHits()}
          <#assign prevsource="">      
          <#list searchResult.getDocs() as doc>
            <#assign page=doc.get("pagenumber")>
            <#assign source=doc.get("SOURCE")>
            <#if prevsource!=source>
              <#if (prevsource!="")>
          <br/>
              </#if>
          <a href='${source}' title='${source}'>${source}</a><br/>
            </#if>  
          <a href='${source}#page=${page}' title='${source}#page=${page}'>${page}</a>
            <#assign prevsource=source>
          </#list>
        </li>   
       </#list>
    </ul>
  </body>
</html>
