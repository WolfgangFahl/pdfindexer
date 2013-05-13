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
 			<#list searchResult.getDocs() as doc>
 			  <#assign page=doc.get("pagenumber")>
 				<a href='${doc.get("SOURCE")}'#page=${page}'>${page}</a>
      </#list>	
    </#list>
 		</ul>
 	</body>
</html>