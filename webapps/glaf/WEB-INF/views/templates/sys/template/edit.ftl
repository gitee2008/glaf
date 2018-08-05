<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>模板管理</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
    var contextPath="${request.contextPath}";

	function saveData(){
		if(document.getElementById("file").value==""){
            alert("请选择您要导入的模板文件！");
			return;
		}
		document.iForm.action="${request.contextPath}/sys/template/save";
        document.iForm.submit();
	}

	function down(templateId){
		window.open('${request.contextPath}/sys/template/download?templateId='+templateId);
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:40px"> 
    <div class="toolbar-backgroud"> 
	<span class="x_content_title">编辑模板</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post" enctype="multipart/form-data">
  <table class="easyui-form" style="width:780px;" align="center">
    <tbody>
	<tr>
		<td width="15%" align="left">标题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox x-text" style="width:425px;" 
				   value="${template.title}"/>
		</td>
	</tr>
	 
	<tr>
		<td width="15%" align="left">模板编号</td>
		<td align="left">
		    <input id="templateId" name="templateId" type="text" 
			       class="easyui-validatebox x-text" style="width:425px;" 
				   value="${template.templateId}"/>
		</td>
	</tr>
  
  	<tr>
		<td width="15%" align="left">模板名称</td>
		<td align="left">
		    <input id="name" name="name" type="text" 
			       class="easyui-validatebox x-text" style="width:425px;" 
				   value="${template.name}"/>
		</td>
	</tr>

	<tr>
		<td width="15%" align="left">模板文件</td>
		<td align="left">
		    <input type="file" id="file" name="file" size="50" class="input-file">
			<#if template.data?exists>
			 &nbsp;&nbsp;<img src="${request.contextPath}/static/images/download.png" style="cursor:pointer;"
			                     onclick="javascript:down('${template.templateId}');">
			</#if>
		</td>
	</tr>

	<tr>
		<td width="15%" align="left">是否有效</td>
		<td align="left">
		    <select id="locked" name="locked">
				<option value="0">是</option>
			    <option value="1">否</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("locked").value="${template.locked}";
             </script>
		</td>
	</tr>
	<tr><td><br><br><br><br></td></tr>
    </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>