<!--
  Created with PDFIndexer
  see  https://github.com/WolfgangFahl/pdfindexer
-->
<html>
	<head>
		<title>${title}</title>
	</head>
	<body>
 		<h1>${title}</h1>
 		<ul>
 		<#list searchResults?keys as key>
 			<#assign searchResult=searchResults[key]>
 			<li>${key}:${searchResult.getTotalHits()}</li>
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
    </#list>
 		</ul>
 	</body>
</html>