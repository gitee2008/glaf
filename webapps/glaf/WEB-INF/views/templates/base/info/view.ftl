<!DOCTYPE html>
<html>
<title>${publicInfo.subject}</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link rel="stylesheet" type="text/css" href="${contextPath}/static/css/info.css" />
</head>
<body class="news_main" style="margin:10px;">  
<div class="news_main">
  <div class="news">
		<#if publicInfo.subject?exists>
		<div class="clearfix">
			<div class="subject">
			<h3>${publicInfo.subject}</h3>
			</div>
		</div>
		</#if>
	    <div id="content">&nbsp;&nbsp;&nbsp;&nbsp;${publicInfo.content}</div>
		<#if dataFiles?exists>
		<div id="x_files" style="font-size: 12px; padding-left: 60px;">
		<iframe id="newFrame" name="newFrame" width="0" height="0"></iframe> 
		<#list dataFiles as dataFile>
			<a  href='${contextPath}/matrix/binary/download?fileId=${dataFile.fileId}'
				target="newFrame"> ${dataFile.filename}&nbsp;<img
				src="${contextPath}/static/images/download.gif"
				border="0"> </a>
		</#list>
		</div>
		</#if>
	    <br/>
    </div>
	<div class="news_info" style="background-color: transparent;"><span
		class='date'>${publicInfo.createDate?string('yyyy-MM-dd')}</span> by ${publicInfo.createByName} <br/>
	 <span class='view'>有${publicInfo.viewCount}人浏览</span>  
	</div>
  </div>
 </div>
</body>
</html>
 