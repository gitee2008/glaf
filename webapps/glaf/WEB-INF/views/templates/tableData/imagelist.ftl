<#assign contextPath="${request.getContextPath()}" />
<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>图片列表</title>
    <link rel="stylesheet" type="text/css" href="${contextPath}/static/plugins/jcarousel/jcarousel.basic.css">
    <script type="text/javascript" src="${contextPath}/static/scripts/jquery.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/plugins/jcarousel/jquery.jcarousel.min.js"></script>
    <script type="text/javascript" src="${contextPath}/static/plugins/jcarousel/jcarousel.basic.js"></script>
    </head>
    <body>
        <div class="wrapper">
		    <#if upload == "true">
		    <p>
			    <iframe id="newFrame" name="newFrame" width="680" height="350" border="0" frameborder="0"
		          src="${contextPath}/matrix/image/showUpload?serviceKey=${serviceKey}&businessKey_enc=${businessKey_enc}&status_enc=${status_enc}"></iframe>
			</p>
			</#if>
			<#if dataFiles?exists>
            <div class="jcarousel-wrapper">
                <div class="jcarousel">
                    <ul>
					   <#list dataFiles as dataFile>
                        <li><img src="${contextPath}/matrix/image/download?fileId=${dataFile.id}" width="600" height="400" ></li>
                       </#list>
                    </ul>
                </div>
                <p class="photo-credits">
                    
                </p>

                <a href="#" class="jcarousel-control-prev">&lsaquo;</a>
                <a href="#" class="jcarousel-control-next">&rsaquo;</a>
                
                <p class="jcarousel-pagination">
                    
                </p>
            </div>
			</#if>
        </div>
    </body>
</html>
