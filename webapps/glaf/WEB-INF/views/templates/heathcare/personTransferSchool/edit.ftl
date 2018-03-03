<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>儿童转园信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personTransferSchool/savePersonTransferSchool',
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
				   url: '${contextPath}/heathcare/personTransferSchool/savePersonTransferSchool',
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
	<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;编辑儿童转园信息</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${personTransferSchool.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">姓名</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox  x-text"  
				   value="${personTransferSchool.name}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">体检时间</td>
		<td align="left">
			<input id="checkDate" name="checkDate" type="text" 
			       class="easyui-datebox x-text" style="width:120px"
			       <#if personTransferSchool.checkDate?exists>
				   value="${personTransferSchool.checkDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">转入前就读幼儿园</td>
		<td align="left">
            <input id="fromSchool" name="fromSchool" type="text" 
			       class="easyui-validatebox  x-text" style="width:350px"  
				   value="${personTransferSchool.fromSchool}"/>
			<br>（提示：如果是转入，请填写转入前就读幼儿园）
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">转到幼儿园</td>
		<td align="left">
            <input id="toSchool" name="toSchool" type="text" 
			       class="easyui-validatebox  x-text" style="width:350px"  
				   value="${personTransferSchool.toSchool}"/>
			<br>（提示：如果是转出，请填写转出幼儿园）
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">体检机构</td>
		<td align="left">
            <input id="checkOrganization" name="checkOrganization" type="text" 
			       class="easyui-validatebox  x-text" style="width:350px"  
				   value="${personTransferSchool.checkOrganization}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">健康状况</td>
		<td align="left">
            <input id="checkResult" name="checkResult" type="text" 
			       class="easyui-validatebox  x-text" style="width:350px"  
				   value="${personTransferSchool.checkResult}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">备注</td>
		<td align="left">
		    <textarea id="remark" name="remark" 
			          class="easyui-validatebox  x-text"  
			          style="width:350px;height:120px">${personTransferSchool.remark}</textarea>
			<br><br><br><br>
		</td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>