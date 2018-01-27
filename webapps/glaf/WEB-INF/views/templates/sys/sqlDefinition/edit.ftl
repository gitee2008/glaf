<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SQL语句定义</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/sql/definition/saveSqlDefinition',
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
				   url: '${contextPath}/sys/sql/definition/saveSqlDefinition',
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
  <div data-options="region:'north',split:true,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">&nbsp; 
	    <span class="x_content_title">&nbsp;编辑SQL语句定义</span>
	    <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	       onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${sqlDefinition.id}"/>
  <table class="easyui-form" style="width:800px;" align="center">
    <tbody>
	<tr>
		<td width="15%" align="left">标题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox  x-text" style="width:580px;"
				   value="${sqlDefinition.title}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">名称</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox  x-text" style="width:580px;"
				   value="${sqlDefinition.name}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">代码</td>
		<td align="left">
            <input id="code" name="code" type="text" 
			       class="easyui-validatebox  x-text" style="width:580px;"
				   value="${sqlDefinition.code}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">SQL语句</td>
		<td align="left">
		    <textarea id="sql" name="sql" rows="6" cols="48" class="x-text"
				      style="width:580px;height:180px;" >${sqlDefinition.sql}</textarea>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">汇总SQL语句</td>
		<td align="left">
		    <textarea id="countSql" name="countSql" rows="6" cols="48" class="x-text"
				      style="width:580px;height:180px;" >${sqlDefinition.countSql}</textarea>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">抓取目标表</td>
		<td align="left">
            <input id="targetTableName" name="targetTableName" type="text" 
			       class="easyui-validatebox  x-text" style="width:580px;"
				   value="${sqlDefinition.targetTableName}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">是否导出</td>
		<td align="left">
            <input type="radio" name="exportFlag" value="Y" <#if sqlDefinition.exportFlag == 'Y' >checked</#if>>是&nbsp;&nbsp;
	        <input type="radio" name="exportFlag" value="N" <#if sqlDefinition.exportFlag == 'N' >checked</#if>>否
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">导出表名</td>
		<td align="left">
            <input id="exportTableName" name="exportTableName" type="text" 
			       class="easyui-validatebox x-text" style="width:580px;"
				   value="${sqlDefinition.exportTableName}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">导出模板</td>
		<td align="left">
            <input id="exportTemplate" name="exportTemplate" type="text" 
			       class="easyui-validatebox x-text" style="width:580px;"
				   value="${sqlDefinition.exportTemplate}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">是否调度</td>
		<td align="left">
            <input type="radio" name="scheduleFlag" value="Y" <#if sqlDefinition.scheduleFlag == 'Y' >checked</#if>>是&nbsp;&nbsp;
	        <input type="radio" name="scheduleFlag" value="N" <#if sqlDefinition.scheduleFlag == 'N' >checked</#if>>否
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">是否缓存结果</td>
		<td align="left">
            <input type="radio" name="cacheFlag" value="Y" <#if sqlDefinition.cacheFlag == 'Y' >checked</#if>>是&nbsp;&nbsp;
	        <input type="radio" name="cacheFlag" value="N" <#if sqlDefinition.cacheFlag == 'N' >checked</#if>>否
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">是否抓取结果</td>
		<td align="left">
            <input type="radio" name="fetchFlag" value="Y" <#if sqlDefinition.fetchFlag == 'Y' >checked</#if>>是&nbsp;&nbsp;
	        <input type="radio" name="fetchFlag" value="N" <#if sqlDefinition.fetchFlag == 'N' >checked</#if>>否
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">是否基础数据项</td>
		<td align="left">
            <input type="radio" name="dataItemFlag" value="Y" <#if sqlDefinition.dataItemFlag == 'Y' >checked</#if>>是&nbsp;&nbsp;
	        <input type="radio" name="dataItemFlag" value="N" <#if sqlDefinition.dataItemFlag == 'N' >checked</#if>>否
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">每次抓取前删除</td>
		<td align="left">
            <input type="radio" name="deleteFetch" value="Y" <#if sqlDefinition.deleteFetch == 'Y' >checked</#if>>是&nbsp;&nbsp;
	        <input type="radio" name="deleteFetch" value="N" <#if sqlDefinition.deleteFetch == 'N' >checked</#if>>否
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">是否共享</td>
		<td align="left">
            <input type="radio" name="shareFlag" value="Y" <#if sqlDefinition.shareFlag == 'Y' >checked</#if>>是&nbsp;&nbsp;
	        <input type="radio" name="shareFlag" value="N" <#if sqlDefinition.shareFlag == 'N' >checked</#if>>否
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">是否公开</td>
		<td align="left">
		    <input type="radio" name="publicFlag" value="Y" <#if sqlDefinition.publicFlag == 'Y' >checked</#if>>是&nbsp;&nbsp;
	        <input type="radio" name="publicFlag" value="N" <#if sqlDefinition.publicFlag == 'N' >checked</#if>>否
		</td>
	</tr>
 
	<tr>
		<td width="15%" align="left">是否启用</td>
		<td align="left">
		  <input type="radio" name="locked" value="0" <#if sqlDefinition.locked == 0 >checked</#if>>是&nbsp;&nbsp;
	      <input type="radio" name="locked" value="1" <#if sqlDefinition.locked == 1 >checked</#if>>否
		</td>
	</tr>

	<tr>
		<td width="15%" align="left">&nbsp;</td>
		<td align="left">
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