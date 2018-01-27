<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>字段列表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tableMgr/save',
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
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tableMgr/saveAs',
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

	function switchAttachmentFlag(){
        var attachmentFlag = document.getElementById("attachmentFlag").value;
		if(attachmentFlag == "1" || attachmentFlag == "2"){
			jQuery("#attachmentExtsDiv").show();
			jQuery("#attachmentSizeDiv").show();
		} else {
			jQuery("#attachmentExtsDiv").hide();
			jQuery("#attachmentSizeDiv").hide();
		}
	}


</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:42px"  class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑表</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
	<#if tableId?exists>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveAsData();" >另存</a> 
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="tableId" name="tableId" value="${tableId}"/>
  <table class="easyui-form" style="width:650px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">标题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox  x-text"  style="width:420px;"
				   value="${tableDefinition.title}" size="60"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">描述</td>
		<td align="left">
            <textarea id="description" name="description" type="text" 
			          class="easyui-validatebox  x-text"
					  style="width:420px;height:60px;">${tableDefinition.description}</textarea>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">报表编号</td>
		<td align="left">
            <input id="reportId" name="reportId" type="text" 
			       class="easyui-validatebox  x-text"  style="width:420px;"
				   value="${tableDefinition.reportId}" size="60" maxlength="80"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">表单打印编号</td>
		<td align="left">
            <input id="formReportId" name="formReportId" type="text" 
			       class="easyui-validatebox  x-text"  style="width:420px;"
				   value="${tableDefinition.formReportId}" size="60" maxlength="80"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">是否树表</td>
		<td align="left">
            <select id="treeFlag" name="treeFlag">
			    <option value="N">否</option>
				<option value="Y">是</option>
            </select>
			<script type="text/javascript">
			    document.getElementById("treeFlag").value="${tableDefinition.treeFlag}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">是否从表</td>
		<td align="left">
            <select id="isSubTable" name="isSubTable">
			    <option value="N">否</option>
				<option value="Y">是</option>
            </select>
			<script type="text/javascript">
			    document.getElementById("isSubTable").value="${tableDefinition.isSubTable}";
			</script>
		</td>
	</tr>
	<!-- <tr>
		<td width="20%" align="left">是否分区表</td>
		<td align="left">
            <select id="partitionFlag" name="partitionFlag">
			    <option value="N">否</option>
				<option value="Y">是</option>
            </select>
			<script type="text/javascript">
			    document.getElementById("partitionFlag").value="${tableDefinition.partitionFlag}";
			</script>
			&nbsp;（如果是分区表，默认使用的是256个分表）
		</td>
	</tr> -->
	<tr>
		<td width="20%" align="left">授权访问</td>
		<td align="left">
            <select id="privilegeFlag" name="privilegeFlag">
				<option value="Y">是</option>
				<option value="N">否</option>
            </select>
			<script type="text/javascript">
			    document.getElementById("privilegeFlag").value="${tableDefinition.privilegeFlag}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">上传附件</td>
		<td align="left">
            <select id="attachmentFlag" name="attachmentFlag" onchange="javascript:switchAttachmentFlag();">
				<option value="0">不允许</option>
				<option value="1">仅一个附件</option>
				<option value="2">允许多附件</option>
            </select>
			<script type="text/javascript">
			    document.getElementById("attachmentFlag").value="${tableDefinition.attachmentFlag}";
			</script>
		</td>
	</tr>
	<tr id="attachmentExtsDiv" style="display:none">
		<td width="20%" align="left">附件扩展名</td>
		<td align="left">
             <input id="attachmentExts" name="attachmentExts" type="text" 
			       class="easyui-validatebox  x-text"  style="width:420px;"
				   value="${tableDefinition.attachmentExts}" size="60"/>
			<br>&nbsp;（提示：多种类型附件文件类型以半角的|隔开。如图像类型：jpg|jpeg|png|gif）
		</td>
	</tr>
	<tr id="attachmentSizeDiv" style="display:none">
		<td width="20%" align="left">附件大小限制</td>
		<td align="left">
            <select id="attachmentSize" name="attachmentSize">
				<option value="1">1MB</option>
				<option value="2">2MB</option>
				<option value="3">3MB</option>
				<option value="4">4MB</option>
				<option value="5">5MB</option>
				<option value="10">10MB</option>
				<option value="20">20MB</option>
				<option value="50">50MB</option>
            </select>
			<script type="text/javascript">
			    document.getElementById("attachmentSize").value="${tableDefinition.attachmentSize}";
			</script>
			&nbsp;（提示：附件大小限制，单个附件的大小不能超过该限制）
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">审核标识</td>
		<td align="left">
            <select id="auditFlag" name="auditFlag">
				<option value="Y">是</option>
				<option value="N">否</option>
            </select>
			<script type="text/javascript">
			    document.getElementById("auditFlag").value="${tableDefinition.auditFlag}";
			</script>
			（提示：设置审核标记后，凡是通过审核的数据不允许修改及删除。）
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">修改标识</td>
		<td align="left">
            <select id="updateCascade" name="updateCascade">
			    <option value="2">不能修改</option>
				<option value="3">当天可以修改</option>
				<option value="4">两周内可以修改</option>
				<option value="5">当月内可修改</option>
            </select>
			<script type="text/javascript">
			    document.getElementById("updateCascade").value="${tableDefinition.updateCascade}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">删除标识</td>
		<td align="left">
            <select id="deleteCascade" name="deleteCascade">
			    <option value="0">可物理删除，记录删除后不能恢复</option>
				<option value="1">不做物理删除，只打删除标记</option>
				<option value="2">不能删除</option>
				<option value="3">当天可以删除</option>
				<option value="4">两周内可以删除</option>
				<option value="5">当月内可删除</option>
            </select>
			<script type="text/javascript">
			    document.getElementById("deleteCascade").value="${tableDefinition.deleteCascade}";
			</script>
		</td>
	</tr>
	<tr>
	  <td colspan="2"><br><br><br><br></td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>

<script type="text/javascript">
    switchAttachmentFlag();
</script>
</body>
</html>