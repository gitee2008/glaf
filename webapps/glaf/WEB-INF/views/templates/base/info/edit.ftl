<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
        "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信息发布</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
<script type="text/javascript">
    var GLAF_PATH = "${serviceUrl}";
</script>
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
        alert(ue.getContent());
    }

</script>
</head>
<body> 
<div>
  <div class="x_content_title">
	<img src="${contextPath}/static/images/window.png" alt="编辑信息"> &nbsp;编辑信息
  </div>
  <form id="iForm" name="iForm" method="post">
	<input type="hidden" id="id" name="id" value="${publicInfo.id}"/>
	<input type="hidden" id="nodeId" name="nodeId" value="${nodeId}"/>
    <input type="hidden" id="serviceKey" name="serviceKey" value="${serviceKey}"/>
	<table style="width:920px;margin:0px 10px;" align="center">
		<tbody>
			<tr>
				 <td height="28" width="12%">主题</td>
				 <td height="28" colspan="3" >
                 <input id="subject" name="subject" class=" x-text" type="text" style="width:685px;"
	             value="${publicInfo.subject}" size="80"></input>
				 </td>
			</tr>

			<tr>
				 <td height="28" width="12%">关键字</td>
				 <td height="28" width="38%">
                 <input id="keywords" name="keywords" class=" x-text" type="text" style="width:225px;"
	             value="${publicInfo.keywords}" size="80"></input>
				 </td>
			 
				 <td height="28" width="12%">摘要</td>
				 <td height="28" width="38%">
                 <input id="summary" name="summary" class=" x-text" type="text" style="width:225px;"
	             value="${publicInfo.summary}" size="80"></input>
				 </td>
			</tr>

			<tr>
				 <td height="28">作者</td>
				 <td height="28" >
                 <input id="author" name="author" class=" x-text" type="text" style="width:225px;"
	             value="${publicInfo.author}" size="80"></input>
				 </td>
				 <td height="28">发布单位</td>
				 <td height="28" >
                 <input id="unitName" name="unitName" class=" x-text" type="text" style="width:225px;"
	             value="${publicInfo.unitName}" size="80"></input>
				 </td>
			</tr>
	    </tbody>
    </table>

	<div style="width:920px;margin:0px 10px;">
		<script id="editor" type="text/plain" style="width:805px;height:500px;"></script>
	</div>

    <div style="width:920px;margin:0px 10px;">
        <#if publicInfo?exists>
          <#if publicInfo.publishFlag == 0 ||  publicInfo.publishFlag == -1>
			<input type="button" name="save" value=" 保 存 " class="btnGray" onclick="javascript:saveData();" />
			<input type="button" name="save" value=" 发 布 " class="btnGray" onclick="javascript:publishXY(1);" />
		  <#elseif publicInfo.publishFlag == 1 >
            <input type="button" name="save" value=" 取消发布 " class="btnGray" onclick="javascript:publishXY(0);" />
		  </#if>
          <#else>
			<input type="button" name="save" value=" 保 存 " class="btnGray" onclick="javascript:saveData();" />
        </#if>
        <!-- <button onclick="setContentX()">写入内容</button> --> 
        <br><br>
    </div>
  </form>
  <script type="text/javascript">
   <#if publicInfo.content?exists>
	function setContentX(){
		UE.getEditor('editor').setContent('${publicInfo.content}');
	}
	window.setTimeout(setContentX, 500);
	//jQuery(document).ready(function(){
    //    setTimout(setContentX, 500); 
    //}); 
   </#if>
  </script>
</body>
</html>