<#assign contextPath="${request.getContextPath()}" />
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件上传</title>
<link rel="stylesheet" href="${contextPath}/static/plugins/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" href="${contextPath}/static/plugins/upload/css/style.css">
<link rel="stylesheet" href="${contextPath}/static/plugins/upload/css/jquery.fileupload.css">
<script type="text/javascript" src="${contextPath}/static/scripts/jquery.min.js"></script>
<script type="text/javascript" src="${contextPath}/static/scripts/jquery.form.js"></script>
<script type="text/javascript" src="${contextPath}/static/scripts/jquery.base64.js"></script>
<script type="text/javascript" src="${contextPath}/static/plugins/upload/js/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="${contextPath}/static/plugins/upload/js/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="${contextPath}/static/plugins/upload/js/jquery.fileupload.js"></script>
<script type="text/javascript" src="${contextPath}/static/plugins/upload/js/jquery.fileupload-process.js"></script>
<!-- <script type="text/javascript" src="${contextPath}/static/plugins/upload/js/jquery.fileupload-image.js"></script> -->
<script type="text/javascript" src="${contextPath}/static/plugins/upload/js/jquery.fileupload-audio.js"></script>
<script type="text/javascript" src="${contextPath}/static/plugins/upload/js/jquery.fileupload-video.js"></script>
<script type="text/javascript" src="${contextPath}/static/plugins/upload/js/jquery.fileupload-validate.js"></script>
<script type="text/javascript" src="${contextPath}/static/plugins/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript">

    function deleteById(fileId){
		if(confirm("文件删除后不能恢复，确定删除吗？")){
          jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/matrix/image/deleteById?fileId='+fileId,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
					   if(data.statusCode == 200){
                          jQuery("#div_"+fileId).hide();
					   }  
				   }
			 });
		}
	}

	function removeById(filename){
		  //jQuery.base64.is_unicode = true;
		  //filename = jQuery.base64.encode(filename);
		  document.getElementById("filename").value=filename;
		  var params = jQuery("#iForm").formSerialize();
          jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/matrix/image/deleteByName?serviceKey=${serviceKey}&businessKey_enc=${businessKey_enc}&status_enc=${status_enc}',
				   dataType: 'json',
				   data: params,
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
					   if(data.statusCode == 200){
                          jQuery("#div_"+jQuery.base64.encode(filename)).hide();
					   }  
				   }
			 });
	}

	function downById(fileId){
		window.open("${contextPath}/matrix/image/download?fileId="+fileId);
	}
</script>
</head>
<body>
<form id="iForm" name="iForm" method="post">
<input type="hidden" id="filename" name="filename">
<div class="container"> 
	
	<#if canUpload == true>
	<span class="btn btn-success fileinput-button">
	  <i class="glyphicon glyphicon-plus"></i>
	  <span>选择文件...</span>
	  <!-- The file input field used as target for the file upload widget -->
	  <input id="fileupload" type="file" name="files[]" multiple>
	</span>
	<br>
	<!-- The global progress bar -->
	<div id="progress" class="progress">
	  <div class="progress-bar progress-bar-success"></div>
	</div>
	</#if>
	
	<!-- The container for the uploaded files -->
	<div id="files" class="files">
	  <#list dataFiles as file>
	    <div id="div_${file.id}">${file.filename}&nbsp;
		<a href='javascript:downById("${file.id}");'>下载&nbsp;<img src="${contextPath}/static/images/download.png"></a>
		<#if canUpdate == true>
		<a href='javascript:deleteById("${file.id}");'>删除&nbsp;<img src="${contextPath}/static/images/remove.png"></a>
		</#if>
		</div>
	  </#list>
	</div>
	<br>
</div>		 
<script>
/*jslint unparam: true */
/*global window, $ */
jQuery(function () {
    'use strict';
    // Change this to the location of your server-side upload handler:
    var url = '${contextPath}/matrix/image/doUpload?serviceKey=${serviceKey}&businessKey_enc=${businessKey_enc}';
	<#if status_enc?if_exists>
	url = url + "&status_enc=${status_enc}";
	</#if>
    jQuery('#fileupload').fileupload({
        url: url,
        dataType: 'json',
		acceptFileTypes: /(\.|\/)(jpg|jpeg|png|gif|bmp)$/i,
		maxNumberOfFiles: 10,
		maxFileSize: 999000,
        done: function (e, data) {
            jQuery.each(data.files, function (index, file) {
				//alert(file.url + "->"+file.name+"  " + file.size);
                jQuery('<div id="div_'+jQuery.base64.encode(file.name)+'">').html(file.name+"&nbsp;<a href='javascript:removeById(\""+file.name+"\");'>&nbsp;删除&nbsp;<img src=\"${contextPath}/static/images/remove.png\"></a></div>").appendTo('#files');
            });
        },
        progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            jQuery('#progress .progress-bar').css(
                'width',
                progress + '%'
            );
        }
    }).prop('disabled', !$.support.fileInput)
        .parent().addClass($.support.fileInput ? undefined : 'disabled');
});
</script>
</form>
</body>
</html>