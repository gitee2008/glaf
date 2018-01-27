<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>基础数据配置表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tableDataItem/saveTableDataItem',
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
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tableDataItem/saveTableDataItem',
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

	function switchTable(){
	   var tableName = document.getElementById("tableName").value;
	   if(tableName != ""){
		   var link = "${contextPath}/sys/tableMgr/columnsJsonArray?tableName="+tableName;
		   jQuery.getJSON(link, function(data){
			  var nameColumn = document.getElementById("nameColumn");
			  var valueColumn = document.getElementById("valueColumn");
			  var sortColumn = document.getElementById("sortColumn");
			  nameColumn.options.length=0;
			  valueColumn.options.length=0;
			  sortColumn.options.length=0;
			  jQuery.each(data, function(i, item){
				 nameColumn.options.add(new Option(item.title+"["+item.columnName+"]", item.columnName));
				 valueColumn.options.add(new Option(item.title+"["+item.columnName+"]", item.columnName));
				 sortColumn.options.add(new Option(item.title+"["+item.columnName+"]", item.columnName));
			  });
			});
	   }
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:42px"  class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp; 
	<span class="x_content_title">编辑基础数据配置表</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${tableDataItem.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">标题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox  x-text" style="width:420px;" 
				   value="${tableDataItem.title}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">描述</td>
		<td align="left">
            <input id="description" name="description" type="text" 
			       class="easyui-validatebox  x-text"  style="width:420px;"
				   value="${tableDataItem.description}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">表名称</td>
		<td align="left">
			<select id="tableName" name="tableName" onchange="javascript:switchTable();">
				<#list tables as table>
				<option value="${table.tableName}">${table.title}</option>
				</#list>
		    </select>
            <script type="text/javascript">
				document.getElementById("tableName").value="${tableDataItem.tableName}";   
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">标题列名</td>
		<td align="left">
		    <select id="nameColumn" name="nameColumn">
				<#list columns as column>
				<option value="${column.columnName}">${column.title}</option>
				</#list>
		    </select>
            <script type="text/javascript">
				document.getElementById("nameColumn").value="${tableDataItem.nameColumn}";   
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">值列名</td>
		<td align="left">
		    <select id="valueColumn" name="valueColumn">
				<#list columns as column>
				<option value="${column.columnName}">${column.title}</option>
				</#list>
		    </select>
            <script type="text/javascript">
				document.getElementById("valueColumn").value="${tableDataItem.valueColumn}";   
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">排序列名</td>
		<td align="left">
		    <select id="sortColumn" name="sortColumn">
				<#list columns as column>
				<option value="${column.columnName}">${column.title}</option>
				</#list>
		    </select>
            <script type="text/javascript">
				document.getElementById("sortColumn").value="${tableDataItem.sortColumn}";   
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">过滤标识</td>
		<td align="left">
           <select id="filterFlag" name="filterFlag">
				<option value="G">全局</option>
				<option value="O">机构相关</option>
				<option value="T">租户相关</option>
		    </select>
            <script type="text/javascript">
				document.getElementById("filterFlag").value="${tableDataItem.filterFlag}";   
			</script>
		</td>
	</tr>

	<tr>
		<td width="20%" align="left">是否启用</td>
		<td align="left">
		  <input type="radio" name="locked" value="0" <#if tableDataItem.locked == 0>checked</#if>>是&nbsp;&nbsp;
	      <input type="radio" name="locked" value="1" <#if tableDataItem.locked == 1>checked</#if>>否&nbsp;&nbsp;
		</td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>