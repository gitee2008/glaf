<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>审核记录</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(approval){
		if(!approval){
            var comment = document.getElementById("comment").value;
            if(comment == ""){
				alert('请输入不通过的原因！');
                document.getElementById("comment").focus();
				return;
			}
		}
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/tableDataSave/audit?approval='+approval,
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
					        window.close();
					   }  
				   }
			 });
	}

</script>
</head>

<body> 
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:38px"> 
    <div class="toolbar-backgroud" > 
		<span class="x_content_title">&nbsp;审核记录</span>
		<#if dataModel.businessStatus == 9>
		&nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已审核</span>&nbsp;
		<#else>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-ok'" 
		   onclick="javascript:saveData(true);" >审核通过</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-no'" 
		   onclick="javascript:saveData(false);" >审核不通过</a>
		</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="uuid" name="uuid" value="${uuid}"/>
  <input type="hidden" id="tableId" name="tableId" value="${tableId}" >
  <input type="hidden" id="topId" name="topId" value="${topId}" >
  <table class="easyui-form" style="width:600px;margin-left:5px;" align="left">
    <tbody>
	
	<#if treeNodes?exists>
    <tr>
		<td width="20%" align="left">上级节点</td>
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
		<td width="20%" align="left">
		<b>${col.title}</b>
		</td>
		<td align="left">
		    <#if col.javaType == "Integer">
			      ${col.value} 
			<#elseif col.javaType == "Long">
			      ${col.value}
			<#elseif col.javaType == "Double">
			      ${col.value}
			<#elseif col.javaType == "Date">
			      ${col.value}
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
                ${col.value}
			 </#if>
			</#if>
		   <br>
		</td>
	</tr>
	</#if>
    </#list>

	<tr>
	 <td colspan="2">
	   <b>审核意见：</b>
	   <div style="margin-left:50px;"><textarea id="comment" name="comment" rows="6" cols="50" style="width:480px;height:185px;" class="x-textarea"></textarea></div>
     </td>
	</tr>

    <#if table.attachmentFlag == "1" || table.attachmentFlag == "2">
	<tr>
	 <td colspan="2">
        <iframe id="newFrame" name="newFrame" width="100%" height="350" border="0" frameborder="0"
		        src="${contextPath}/dataFile/showUpload?tableId=${table.tableId}&serviceKey=${table.tableId}&businessKey_enc=${businessKey_enc}&status_enc=${status_enc}&audit=true"></iframe>
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