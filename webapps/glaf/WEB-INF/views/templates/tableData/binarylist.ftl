<#assign contextPath="${request.getContextPath()}" />
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>附件列表</title>
    <script type="text/javascript" src="${contextPath}/static/scripts/jquery.min.js"></script>
    </head>
    <body>
        <div class="wrapper">
		    <#if upload == "true">
		    <p>
			    <iframe id="newFrame" name="newFrame" width="100%" height="350" border="0" frameborder="0"
		          src="${contextPath}/matrix/binary/showUpload?serviceKey=${serviceKey}&businessKey_enc=${businessKey_enc}&status_enc=${status_enc}"></iframe>
			</p>
			</#if>
			<#if dataFiles?exists>          
                <div class="filelist">
                    <ul>
					   <#list dataFiles as dataFile>
                        <li><a href="${contextPath}/matrix/binary/download?fileId=${dataFile.id}" >${dataFile.filename}</a></li>
                       </#list>
                    </ul>
                </div>
			</#if>
        </div>
    </body>
</html>
