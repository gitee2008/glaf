<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>体格检查主题</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		if(document.getElementById("type").value==""){
			alert("类型不能为空。");
			document.getElementById("type").focus();
			return;
		}
		if(document.getElementById("title").value==""){
			alert("主题不能为空。");
			document.getElementById("title").focus();
			return;
		}
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/medicalExaminationDef/saveMedicalExaminationDef',
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
		if(document.getElementById("type").value==""){
			alert("类型不能为空。");
			document.getElementById("type").focus();
			return;
		}
		if(document.getElementById("title").value==""){
			alert("主题不能为空。");
			document.getElementById("title").focus();
			return;
		}
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/medicalExaminationDef/saveMedicalExaminationDef',
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
    <div data-options="region:'north',split:false,border:true" style="height:42px"  class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑体格检查主题</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${medicalExaminationDef.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">类型</td>
		<td align="left">
             <select id="type" name="type">
				<option value="">----请选择----</option> 
				<option value="3">开学体检</option>
				<option value="5">定期体检</option>
				<option value="6">常规体检</option>
				<option value="7">专项体检</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("type").value="${medicalExaminationDef.type}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">主题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox x-text" style="width:320px;" 
				   value="${medicalExaminationDef.title}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">体检时间</td>
		<td align="left">
			<input id="checkDate" name="checkDate" type="text" 
			       class="easyui-datebox x-text" style="width:100px;"
			       <#if medicalExaminationDef.checkDate?exists>
				   value="${medicalExaminationDef.checkDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">备注</td>
		<td align="left">
		    <textarea id="remark" name="remark" rows="8" cols="56" class="x-textarea"
			          style="width:320px;height:120px">${medicalExaminationDef.remark}</textarea>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">启用中</td>
		<td align="left">
             <select id="enableFlag" name="enableFlag">
				<option value="">----请选择----</option> 
				<option value="Y">是</option>
				<option value="N">否</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("enableFlag").value="${medicalExaminationDef.enableFlag}";
			</script>
		</td>
	</tr>
	 
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>