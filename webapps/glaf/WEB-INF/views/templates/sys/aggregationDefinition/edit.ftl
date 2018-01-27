<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇总定义</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/aggregationDefinition/saveAggregationDefinition',
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
				   url: '${contextPath}/sys/aggregationDefinition/saveAggregationDefinition',
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
	    <span class="x_content_title">&nbsp;编辑汇总定义</span>
	    <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	       onclick="javascript:saveData();" >保存</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	       onclick="javascript:saveAsData();" >另存</a>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${aggregationDefinition.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">分类</td>
		<td align="left">
             <select id="serviceKey" name="serviceKey">
				<option value="">----请选择----</option>
				<#list dictoryList as d>
				<option value="${d.code}">${d.name}</option>
				</#list> 
		    </select>
		    <script type="text/javascript">
			   document.getElementById("serviceKey").value="${aggregationDefinition.serviceKey}";
		    </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">主题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox  x-text" style="width:450px;" 
				   value="${aggregationDefinition.title}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">目标表名称</td>
		<td align="left">
            <input id="targetTableName" name="targetTableName" type="text" 
			       class="easyui-validatebox  x-text" style="width:450px;" 
				   value="${aggregationDefinition.targetTableName}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">统计项名称</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox  x-text" style="width:450px;"   
				   value="${aggregationDefinition.name}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">统计SQL</td>
		<td align="left">
		    <textarea id="sql" name="sql" rows="6" cols="50" class="x-textarea" 
			    style="width:450px;height:120px;">${aggregationDefinition.sql}</textarea>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">精度</td>
		<td align="left">
			<input id="precision" name="precision" type="text" 
			       class="easyui-numberbox x-text" style="width:60px;"  
				   increment="10"  
				   value="${aggregationDefinition.precision}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">顺序</td>
		<td align="left">
			<input id="sortNo" name="sortNo" type="text" 
			       class="easyui-numberbox x-text"  style="width:60px;" 
				   increment="10"  
				   value="${aggregationDefinition.sortNo}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">表分区标识</td>
		<td align="left">
		    <select id="partitionFlag" name="partitionFlag" class="select">
			    <option value="">----请选择----</option>
			    <option value="N">不分表</option>
			    <option value="Y">分表</option>
		    </select>
            <script type="text/javascript">
		        document.getElementById("partitionFlag").value="${aggregationDefinition.partitionFlag}";
		    </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">结果标识</td>
		<td align="left">
		    <select id="resultFlag" name="resultFlag" class="select">
			    <option value="">----请选择----</option>
			    <option value="S">单个值</option>
			    <option value="M">多个值</option>
		    </select>
			<script type="text/javascript">
		        document.getElementById("resultFlag").value="${aggregationDefinition.resultFlag}";
		    </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">类型</td>
		<td align="left">
		    <select id="type" name="type" class="select">
			    <option value="SQL">SQL</option>
		    </select>
            <script type="text/javascript">
		        document.getElementById("type").value="${aggregationDefinition.type}";
		    </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">是否启用</td>
		<td align="left">
		  <input type="radio" name="locked" value="0" <#if aggregationDefinition.locked == 0>checked</#if>>是&nbsp;&nbsp;
	      <input type="radio" name="locked" value="1" <#if aggregationDefinition.locked == 1>checked</#if>>否&nbsp;&nbsp;
		</td>
	</tr>
    </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>