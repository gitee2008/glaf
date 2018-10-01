<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导入定义</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
    var contextPath="${contextPath}";
 
    function saveData(){
		 if(document.getElementById("mFile").value==""){
             alert("请选择要导入的格式合法的json定义文件！");
			 return;
		 }
         if(confirm("您准备导入到“${xmlExport.title}”节点下，确认吗？")){
             document.iForm.submit();
		 }
	}

</script>
</head>
<body>
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:40px"> 
    <div class="toolbar-backgroud"> 
	<span class="x_content_title"><img src="${contextPath}/static/images/window.png">&nbsp;编辑导入定义</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:saveData();" >确定</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" enctype="multipart/form-data" method="post" 
        action="${contextPath}/matrix/xmlExport/importDef?expId=${xmlExport.id}">
  <input type="hidden" id="id" name="id" value="${xmlExport.id}"/>
  <input type="hidden" id="expId" name="expId" value="${xmlExport.id}"/>
  <table class="easyui-form" style="width:98%;" align="center">
    <tbody>
	<tr>
		<td width="15%" align="left">&nbsp;&nbsp;导入节点</td>
		<td align="left">
			&lt;${xmlExport.xmlTag}&gt; ${xmlExport.title}
		</td>
	</tr> 
	<tr>
		<td width="15%" align="left">&nbsp;&nbsp;JSON文件</td>
		<td align="left">
		     <input type="file" id="mFile" name="mFile" size="50" class="input-file"> 
		</td>
	</tr> 
	<tr>
		<td width="15%" align="left">&nbsp;&nbsp;</td>
		<td align="left">
		     <br><br>
		     <input type="button" name="bt01" value="确定" class="btn btnGray"
	                onclick="javascript:saveData();" /> 
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