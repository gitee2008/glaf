<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>角色编辑</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
 function saveData(){
		var res = 'saveAdd';
		var params = jQuery("#editForm").formSerialize();
		var id = jQuery("#id").val();
		if(id != ''){
			res = 'saveModify';
		}
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/role/'+res,
				   data: params,
				   dataType:  'json',
				   error: function(data){
					   //alert('服务器处理错误！');
					   jQuery.messager.alert('Info', '服务器处理错误！', 'info');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   //alert(data.message);
						   jQuery.messager.alert('Info', data.message, 'info');
					   } else {
						   //alert('操作成功完成！');
						   jQuery.messager.alert('Info', '操作成功完成！', 'info');
					   }
					   jQuery('#mydatagrid').datagrid('reload');
					   jQuery('#edit_dlg').dialog('close');
				   }
			 });
	}
</script>
</head>  
<body>
<div style="width:580px; padding:10px 20px" >
    <form id="editForm" name="editForm" method="post">
	     <input type="hidden" id="id" name="id" value="${id}">
         <table class="easyui-form" width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
		  <tr>
			<td width="20%" align="left" class="input-box">角色名称*</td>
			<td>
			  <input id="name" name="name" type="text" size="35" class="easyui-validatebox x-text"  value="${role.name}"
			         style="width:350px;"  datatype="string" nullable="no" maxsize="20" 
			         chname="角色名称" data-options="required:true">
			</td>
		  </tr>
		  <tr>
			<td width="20%" align="left" class="input-box2" valign="top">代码*</td>
			<td>
			  <input id="code" name="code" type="text" size="35" class="easyui-validatebox x-text" 
			         datatype="string" value="${role.code}" style="width:350px;" nullable="no"
			         maxsize="20" chname="角色代码" data-options="required:true"></td>
		  </tr>
		  <tr>
			<td width="20%" align="left" class="input-box2" valign="top">开放给机构设置</td>
			<td>
			  <select id="isUseOrganization" name="isUseOrganization" class="select">
				<option value="N">不开放</option>
				<option value="Y">开放</option>
			  </select>
			  <script type="text/javascript">
			      document.getElementById("isUseOrganization").value="${role.isUseOrganization}";
			  </script>
            </td>
		  </tr>
		  <tr>
			<td width="20%" align="left" class="input-box2" valign="top">描　述</td>
			<td>
			  <textarea id="content" name="content" cols="68" rows="5" class="x-textarea"
			            style="width:350px;height:90px" datatype="string" nullable="yes"
			            maxsize="1000" chname="角色描述">${role.content}</textarea>        
			</td>
		  </tr>
		  <tr>
			<td width="20%" align="left" class="input-box2" valign="top">首页链接</td>
			<td>
			  <textarea id="indexUrl" name="indexUrl" cols="68" rows="5" class="x-textarea"
			            style="width:350px;height:90px" datatype="string" nullable="yes"
			            maxsize="500" chname="首页链接">${role.indexUrl}</textarea>        
			</td>
		  </tr>
		  <tr>
			<td colspan="2" align="center" valign="bottom" height="30">&nbsp;
				<button type="button" class="btnGray" onclick="javascript:saveData()">保存</button>
			</td>
		  </tr>
		</table>
    </form>
</div>
</body>
</html>
