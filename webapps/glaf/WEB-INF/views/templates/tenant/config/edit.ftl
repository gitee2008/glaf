<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>参数设置</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/tenant/config/saveTenantConfig',
				   data: params,
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
				   }
			 });
	}
 
</script>
</head>

<body>
<div style="margin-top:5px;"></div>  
 <div class="x_content_title"><img
	src="${contextPath}/static/images/window.png"
	alt="参数设置">&nbsp;参数设置
 </div>
 <br>
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${tenantConfig.id}"/>
  <table class="easyui-form" style="width:880px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">系统名称</td>
		<td align="left">
            <input id="sysName" name="sysName" type="text" 
			       class="easyui-validatebox  x-text" style="width:485px;" maxlength="60"
				   value="${tenantConfig.sysName}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">食谱分享</td>
		<td align="left">
          <select id="shareFlag" name="shareFlag">
			<option value="">----请选择----</option>
			<option value="Y">是</option>
			<option value="N">否</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("shareFlag").value="${tenantConfig.shareFlag}";
		  </script>
		  （提示：如果设置为分享别人可以看到，也可以查看其他人分享的数据。）
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">餐点</td>
		<td align="left">
          <select id="typeId" name="typeId">
			<option value="">----请选择----</option>
			<#list dicts as dict>
			<option value="${dict.id}">${dict.name}</option>
			</#list> 
		  </select>
		  <script type="text/javascript">
			   document.getElementById("typeId").value="${tenantConfig.typeId}";
		  </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">早餐时间</td>
		<td align="left">
            <input id="breakfastTime" name="breakfastTime" type="text" 
			       class="easyui-timespinner easyui-validatebox  x-text" style=" width:85px;"
				   value="${tenantConfig.breakfastTime}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">早点时间</td>
		<td align="left">
            <input id="breakfastMidTime" name="breakfastMidTime" type="text" 
			       class="easyui-timespinner easyui-validatebox  x-text" style=" width:85px;"
				   value="${tenantConfig.breakfastMidTime}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">午餐时间</td>
		<td align="left">
            <input id="lunchTime" name="lunchTime" type="text" 
			       class="easyui-timespinner easyui-validatebox  x-text" style=" width:85px;"
				   value="${tenantConfig.lunchTime}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">午点时间</td>
		<td align="left">
            <input id="snackTime" name="snackTime" type="text" 
			       class="easyui-timespinner easyui-validatebox  x-text" style=" width:85px;"
				   value="${tenantConfig.snackTime}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">晚餐时间</td>
		<td align="left">
            <input id="dinnerTime" name="dinnerTime" type="text" 
			       class="easyui-timespinner easyui-validatebox  x-text" style=" width:85px;"			
				   value="${tenantConfig.dinnerTime}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left"> </td>
		<td align="left">
		    <br>
	        <input type="button"  name="save" value=" 保存 " class="btn btn-primary btnGray" onclick="javascript:saveData();"/>
		</td>
	</tr>
 	
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>