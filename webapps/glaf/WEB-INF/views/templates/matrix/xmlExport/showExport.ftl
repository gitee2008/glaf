<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据导出</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
    var contextPath="${request.contextPath}";

	function exportXml(){
		var databaseId = document.getElementById("databaseId").value;
		if(databaseId != ""){
            var link = '${request.contextPath}/matrix/xmlExport/exportXml?expId=${xmlExport.id}&databaseId='+databaseId;
		    window.open(link);
		} else {
			layer.msg("请选择要导出数据库。");
		}
	}

	function exportJson(){
		var databaseId = document.getElementById("databaseId").value;
		if(databaseId != ""){
            var link = '${request.contextPath}/matrix/xmlExport/exportJson?expId=${xmlExport.id}&databaseId='+databaseId;
		    window.open(link);
		} else {
			layer.msg("请选择要导出数据库。");
		}
	}

	function exportXls(){
		var databaseId = document.getElementById("databaseId").value;
		var templateId = document.getElementById("templateId").value;
		if(databaseId != ""){
            var link = '${request.contextPath}/matrix/xmlExport/exportXls?expId=${xmlExport.id}&databaseId='+databaseId+'&templateId='+templateId;
		    window.open(link);
		} else {
			layer.msg("请选择要导出数据库。");
		}
	}

	function exportVar(){
		var databaseId = document.getElementById("databaseId").value;
		if(databaseId != ""){
            var link = '${request.contextPath}/matrix/xmlExport/exportVar?expId=${xmlExport.id}&databaseId='+databaseId;
		    window.open(link);
		} else {
			layer.msg("请选择要导出数据库。");
		}
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:40px"> 
    <div class="toolbar-backgroud"> 
	<span class="x_content_title"><img src="${request.contextPath}/static/images/window.png">&nbsp;编辑导出节点定义</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-exp'" 
	   onclick="javascript:exportXml();" >导出XML</a> 
	&nbsp;
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-exp'" 
	   onclick="javascript:exportJson();" >导出JSON</a>
	&nbsp;
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-exp'" 
	   onclick="javascript:exportXls();" >导出Excel</a>
	&nbsp;
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-exp'" 
	   onclick="javascript:exportVar();" >查看变量输出</a>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${xmlExport.id}"/>
  <input type="hidden" id="expId" name="expId" value="${xmlExport.id}"/>
  
  <table class="easyui-form" style="width:98%;" align="center">
    <tbody>
	<tr>
		<td width="15%" align="left">&nbsp;&nbsp;导出数据库</td>
		<td align="left">
			<select id="databaseId" name="databaseId">
			    <option value="">----请选择----</option>
				<#list  databases as database>
				<option value="${database.id}">${database.title}[${database.dbname}]</option>
				</#list>
            </select> 
		</td>
	</tr> 
	<tr>
		<td width="15%" align="left">&nbsp;&nbsp;导出模板</td>
		<td align="left">
			<select id="templateId" name="templateId">
			    <option value="">----请选择----</option>
				<#list templates as template >
				<option value="${template.templateId}">${template.title}</option>
				</#list>
            </select> 
            <script type="text/javascript">
                document.getElementById("templateId").value="${xmlExport.templateId}";
            </script>
			<div style="margin-top:5px;">
		     （提示：需要导出Excel时才选择模板。）
	        </div>
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