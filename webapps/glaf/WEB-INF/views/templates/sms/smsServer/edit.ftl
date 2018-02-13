<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>短信服务网关配置</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/smsServer/saveSmsServer',
				   data: params,
				   dataType: 'json',
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
					       window.parent.location.reload();
					   } 
				   }
			 });
	}

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/smsServer/saveSmsServer',
				   data: params,
				   dataType: 'json',
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
					       window.parent.location.reload();
					   }
				   }
			 });
	}

</script>
</head>
<body>
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;" >  
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑短信服务网关配置</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${smsServer.id}"/>
  <table class="easyui-form" style="width:800px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">主题</td>
		<td align="left">
            <input id="subject" name="subject" type="text" 
			       class="easyui-validatebox  x-text" style=" width:385px;"
				   value="${smsServer.subject}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">SMS短信服务器地址</td>
		<td align="left">
            <input id="serverIP" name="serverIP" type="text" 
			       class="easyui-validatebox  x-text" style=" width:385px;"
				   value="${smsServer.serverIP}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">端口</td>
		<td align="left">
			<input id="port" name="port" type="text" 
			       class="easyui-numberbox x-text" 
				   increment="10"  style=" text-align:right; width:65px;"
				   value="${smsServer.port}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">路径</td>
		<td align="left">
            <input id="path" name="path" type="text" 
			       class="easyui-validatebox  x-text" style=" width:385px;"
				   value="${smsServer.path}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">请求消息体模板</td>
		<td align="left">
		    <textarea  id="requestBody" name="requestBody" rows="6" cols="46" class="x-text" style="height:150px;width:385px;" >${smsServer.requestBody}</textarea>
			<br>（提示：可以使用以下几个内置变量）
            <br> <script>document.write("#")</script>{mobile}代表手机号码，<script>document.write("#")</script>{code}代表验证码，<script>document.write("#")</script>{uuid}代表消息业务编号。
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">响应结果模板</td>
		<td align="left">
		    <input id="responseResult" name="responseResult" type="text" 
			       class="easyui-validatebox  x-text" style=" width:385px;"
				   value="${smsServer.responseResult}"/>
			<br>（提示：JSON格式的结果true或false，以/隔开层级，比如response/result）
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">发送频率</td>
		<td align="left">
			<input id="frequence" name="frequence" type="text" 
			       class="easyui-numberbox x-text" 
				   increment="10" style=" text-align:right; width:65px;" 
				   value="${smsServer.frequence}"/>
			<br>（提示：间隔时间，单位为秒）
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">重试次数</td>
		<td align="left">
			<input id="retryTimes" name="retryTimes" type="text" 
			       class="easyui-numberbox x-text" 
				   increment="10" style=" text-align:right; width:65px;" 
				   value="${smsServer.retryTimes}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">是否启用</td>
		<td align="left">
		  <input type="radio" name="locked" value="0" <#if smsServer.locked == 0>checked</#if>>是&nbsp;&nbsp;
	      <input type="radio" name="locked" value="1" <#if smsServer.locked == 1>checked</#if>>否&nbsp;&nbsp;
		</td>
	</tr>
    </tbody>
  </table>
  <br><br><br><br>
  </form>
</div>
</div>
</body>
</html>