<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑记录</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(refresh){
    <#list columns as col>
	<#if col.editableField == "Y" && col.requiredField == "1">
	  if(document.getElementById("${col.id}").value==""){
		  alert("${col.title}是必须的，不能为空。");
          document.getElementById("${col.id}").focus();
		  return;
	  }
	</#if>
	</#list>
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/tableDataSave/saveData',
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
						   if(refresh){
						       window.parent.location.reload();
					           window.close();
						   } else { 
							   //alert(window.parent);
							   //alert(window.parent.document.getElementById("nodeId").value);
							   window.parent.reloadGrid();
                               <#if uuid?exists>
                                 window.close();
							   </#if>
						   }
					   }  
				   }
			 });
	}

	function saveAsData(refresh){
    <#list columns as col>
	<#if col.editableField == "Y" && col.requiredField == "1">
	  if(document.getElementById("${col.id}").value==""){
		  alert("${col.title}是必须的，不能为空。");
          document.getElementById("${col.id}").focus();
		  return;
	  }
	</#if>
	</#list>
	    document.getElementById("uuid").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/tableDataSave/saveData',
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
						   if(refresh){
						       window.parent.location.reload();
					           window.close();
						   } else { 
							   //alert(window.parent);
							   //alert(window.parent.document.getElementById("nodeId").value);
							   window.parent.reloadGrid();
                               <#if uuid?exists>
                                 window.close();
							   </#if>
						   }
					   }  
				   }
			 });
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false, border:true" style="height:38px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		&nbsp;<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑记录</span>
		<#if canEdit == true>
		 <#if canUpdate == true>
		 <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
		   onclick="javascript:saveData(false);" >保存</a>
		 <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
		   onclick="javascript:saveData(true);" >保存并关闭</a>
		 </#if>
		</#if>
		<#if dataModel.businessStatus == 9>
		&nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已审核</span>&nbsp;
		<#else>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-saveas'" 
		   onclick="javascript:saveAsData(false);" >另存</a>
		</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false ">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="uuid" name="uuid" value="${uuid}"/>
  <input type="hidden" id="tableId" name="tableId" value="${tableId}" >
  <input type="hidden" id="topId" name="topId" value="${topId}" >
  <table class="easyui-form" style="width:880px; margin-left:5px; margin-top:8px;" align="left">
    <tbody>
	<#if treeNodes?exists>
		<tr>
			<td width="25%" align="left" height="30">上级节点</td>
			<td align="left">
				 <select id="parentId" name="parentId">
				   <option value="">----请选择----</option>
				   <#list treeNodes as tnode>
					<option value="${tnode.id}">${tnode.blank}${tnode.name} [${tnode.code}]</option>
				   </#list>
				 </select>
				 <script type="text/javascript">
		
				 </script>
			</td>
		</tr>		
	</#if>

    <#list columns as col>
	<#if col.editableField == "Y">
		<tr>
			<td width="25%" align="left" height="30">
			<b>${col.title}</b> 
			<#if col.columnName == "name_" || col.columnName == "code_" || col.requiredField == "1">
				<label for="${col.id}" class="required">&nbsp;*</label>
			</#if>
			</td>
			<td align="left">
				<#if col.javaType == "Integer">
				<input id="${col.id}" name="${col.id}" type="text" <#if col.comment?exists> title="${col.comment}" </#if>
					   class="easyui-numberbox easyui-validatebox x-text" style="width:60px; text-align:right;" precision="0"
					   <#if col.requiredField == "1"> required="true" data-options="required:true" </#if>
					   value="${col.value}" size="10"/>
				<#elseif col.javaType == "Long">
				<input id="${col.id}" name="${col.id}" type="text" <#if col.comment?exists> title="${col.comment}" </#if>
					   class="easyui-numberbox easyui-validatebox x-text" style="width:60px; text-align:right;" precision="0" 
					   <#if col.requiredField == "1"> required="true" data-options="required:true" </#if>
					   value="${col.value}" size="10"/>
				<#elseif col.javaType == "Double">
				<input id="${col.id}" name="${col.id}" type="text" <#if col.comment?exists> title="${col.comment}" </#if> 
					   class="easyui-numberbox easyui-validatebox x-text" style="width:60px; text-align:right;" precision="${col.scale}"
					   <#if col.requiredField == "1"> required="true" data-options="required:true" </#if>
					   value="${col.value}" size="10"/>
				<#elseif col.javaType == "Date">
				<input id="${col.id}" name="${col.id}" type="text" <#if col.comment?exists> title="${col.comment}" </#if>
					   class="easyui-datebox easyui-validatebox x-text" style="width:108px; text-align:center;"  
					   <#if col.requiredField == "1"> required="true" data-options="required:true" </#if>
					   <#if col.value?exists>value="${col.value?string('yyyy-MM-dd')}"</#if> size="30"/>
				<#else>
				
				 <#if col.items?exists >
					 <select id="${col.id}" name="${col.id}">
						<option value="">----请选择----</option>
						<#list  col.items as item>
						<option value="${item.value}">${item.name}</option>
						</#list>
					 </select>
					 <script type="text/javascript">
						 document.getElementById("${col.id}").value="${col.value}";
					 </script>
				 <#else>
				  <#if col.length gte 2000 >
				  <textarea id="${col.id}" name="${col.id}" type="text" <#if col.comment?exists> title="${col.comment}" </#if>
					   class="easyui-validatebox x-text" style="width:480px;height:240px"
					   <#if col.requiredField == "1"> required="true" data-options="required:true" </#if>
					   >${col.value}</textarea>
				  <#elseif col.length gte 500 >
				  <textarea id="${col.id}" name="${col.id}" type="text" <#if col.comment?exists> title="${col.comment}" </#if>
					   class="easyui-validatebox x-text" style="width:480px;height:120px"
					   <#if col.requiredField == "1"> required="true" data-options="required:true" </#if>
					   >${col.value}</textarea>
				  <#else>
				   <input id="${col.id}" name="${col.id}" type="text" 
					   class="easyui-validatebox x-text" <#if col.listWidth gte 180> style="width:${col.listWidth}px;" <#else> style="width:180px;"</#if>
					   <#if col.comment?exists> alt="${col.comment}" </#if>
					   <#if col.requiredField == "1"> required="true" data-options="required:true" </#if>
					   value="${col.value}" size="50"/>
				  </#if>
				 </#if>
				</#if>
				<#if col.comment?exists>
				  &nbsp;${col.comment}
				</#if>
			   <br>
			</td>
		</tr>
	</#if>
    </#list>

    <#if table.attachmentFlag == "1" || table.attachmentFlag == "2">
		<tr>
		 <td colspan="2">
			  <iframe id="newFrame" name="newFrame" width="100%" height="350" border="0" frameborder="0"
					  src="${contextPath}/dataFile/showUpload?tableId=${table.tableId}&serviceKey=${table.tableId}&businessKey_enc=${businessKey_enc}&status_enc=${status_enc}"></iframe>
		 </td>
		</tr>
	</#if>

		<tr>
		 <td colspan="2"><br><br><br><br></td>
		</tr>
	
    </tbody>
  </table>
 </form>
</div>
</div>

</body>
</html>