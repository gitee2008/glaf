<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>表关联关系</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tableCorrelation/saveTableCorrelation',
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
				   url: '${contextPath}/sys/tableCorrelation/saveTableCorrelation',
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
  <div data-options="region:'north',split:false,border:true" style="height:42px"  class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑表关联关系</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${tableCorrelation.id}"/>
  <table class="easyui-form" style="width:650px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">主表名称</td>
		<td align="left">
             <input type="hidden" id="masterTableId" name="masterTableId" value="${masterTable.tableId}">
			 ${masterTable.title} [${masterTable.tableName}]
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">从表名称</td>
		<td align="left">
            <select id="slaveTableId" name="slaveTableId">
		      <#list tables as t>
				<option value="${t.tableId}">${t.title} [${t.tableName}]</option>
			  </#list>
            </select>
			<script type="text/javascript">
			    document.getElementById("slaveTableId").value="${tableCorrelation.slaveTableId}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">级联新增</td>
		<td align="left">
            <select id="insertCascade" name="insertCascade">
				<option value="M">主表保存后新增</option>
				<option value="S">主表未保存也可新增</option>
				<option value=""></option>
            </select>
			<script type="text/javascript">
			    document.getElementById("insertCascade").value="${tableCorrelation.insertCascade}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">级联删除</td>
		<td align="left">
            <select id="deleteCascade" name="deleteCascade">
				<option value="S">与主表一同删除</option>
				<option value="M">先删除子表才能删除主表</option>
				<option value="N">不能删除</option>
            </select>
			<script type="text/javascript">
			    document.getElementById("deleteCascade").value="${tableCorrelation.deleteCascade}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">级联更新</td>
		<td align="left">
            <select id="updateCascade" name="updateCascade">
				<option value="O">单独更新</option>
				<option value="S">与主表一同更新</option>
				<option value="N">不能更新</option>
            </select>
			<script type="text/javascript">
			    document.getElementById("updateCascade").value="${tableCorrelation.updateCascade}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">关联类型</td>
		<td align="left">
            <select id="relationshipType" name="relationshipType">
				<option value="OneToOne">一对一</option>
				<option value="OneToMany">一对多</option>
            </select>
			<script type="text/javascript">
			    document.getElementById("relationshipType").value="${tableCorrelation.relationshipType}";
			</script>
		</td>
	</tr>

    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>