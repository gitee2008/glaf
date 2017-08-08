<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信息发布</title>
<script type="text/javascript">
    var GLAF_PATH = "${serviceUrl}";
</script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/static/scripts/jquery.min.js"></script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/static/plugins/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/static/plugins/ueditor/ueditor.all.min.js"> </script>
<script type="text/javascript" charset="utf-8" src="${contextPath}/static/plugins/ueditor/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript">
	
    var ue = UE.getEditor('editor');

	function saveData(){
		    //var ue = UE.getEditor('editor');
			//document.getElementById("content").value=ue.getContent();
			var params = jQuery("#iForm").formSerialize();
			var nodeId = jQuery('#nodeId').val();
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/base/infoMgr/save',
				   data: params,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
					   window.opener.location.reload();
					   window.close();
				   }
		});
	}

	function publishXY(publishFlag){
		   var msg="";
           if(publishFlag==1){
			   msg="确定要发布该条信息吗？";
		   } else {
               msg="确定取消发布该条信息吗？";
		   }
           var nodeId = jQuery('#nodeId').val();
		   if(confirm(msg)){
               jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/base/infoMgr/publish?id=${publicInfo.id}&publishFlag='+publishFlag,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
					   window.opener.location.reload();
					   window.close();
				   }
			});
		}
	}

    function setContent() {
        ue.setContent('<h1>欢迎使用ueditor</h1>');
		<#if publicInfo.content?exists>
		ue.setContent('${publicInfo.content}');
		</#if>
        alert(ue.getContent());
    }

</script>
</head>
<body> 
 <div>
    <div style="width:920px;margin:0px 10px;">
	    <script id="editor" type="text/plain" style="width:805px;height:500px;"></script>
	</div>

    <div style="width:920px;margin:0px 10px;">
        <!-- <button onclick="setContentX()">写入内容</button> --> 
        <br><br>
    </div>
 </div>
 <script type="text/javascript">
     function xyz(){
		//alert("xyz");
        setContentX();
	 }
     function setContentX(){
		//alert("xxxxxxxxx"+UE.getEditor('editor'));
		//alert(UE.getEditor('editor').getContent());
		<#if publicInfo.content?exists>
		UE.getEditor('editor').setContent('${publicInfo.content}');
		</#if>
        //alert(UE.getEditor('editor').getContent());
	 }
	 window.setTimeout(xyz, 500);
	 //setContentX();

	 //jQuery(document).ready(function(){
     //   setTimout(xyz, 2000); 
     //}); 
 </script>
</body>
</html>