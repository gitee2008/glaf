<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据表存储</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
    var contextPath="${request.contextPath}";

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/sys/storageApp/save',
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
					   parent.location.reload(); 
				   }
			 });
	}

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/sys/storageApp/save',
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
				   }
			 });
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp; 
	<span class="x_content_title">编辑数据表存储</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${storageApp.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="15%" align="left">标题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox x-text" style="width:425px;" 
				   value="${storageApp.title}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">数据库编号</td>
		<td align="left">
			<select id="databaseId" name="databaseId">
			    <option value="">----请选择----</option>
				<#list  databases as database>
				<option value="${database.id}">${database.title}[${database.dbname}]</option>
				</#list>
             </select>
             <script type="text/javascript">
                 document.getElementById("databaseId").value="${storageApp.databaseId}";
             </script>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">恢复标识</td>
		<td align="left">
		    <select id="restoreFlag" name="restoreFlag">
			    <option value="">----请选择----</option>
				<option value="INSERT_ONLY">数据表为空才能恢复</option>
			    <option value="DELETE_INSERT">数据表不空时先删除再恢复</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("restoreFlag").value="${storageApp.restoreFlag}";
             </script>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">类型</td>
		<td align="left">
             <select id="type" name="type">
			    <option value="">----请选择----</option>
			    <option value="SYS">系统表</option>
				<option value="BASE">基础数据</option>
				<option value="BPM">工作流</option>
				<option value="USER">业务数据</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("type").value="${storageApp.type}";
             </script>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">表名</td>
		<td align="left">
		   <textarea id="tableNames" name="tableNames" rows="6" cols="46" class="x-textarea" style="width:425px;height:240px;" >${storageApp.tableNames}</textarea>
		   <br>（提示：多个表之间用半角的逗号","隔开。）
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">自动存储</td>
		<td align="left">
		    <select id="autoSaveFlag" name="autoSaveFlag">
			    <option value="">----请选择----</option>
				<option value="Y">是</option>
			    <option value="N">否</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("autoSaveFlag").value="${storageApp.autoSaveFlag}";
             </script>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">时间间隔</td>
		<td align="left">
		    <select id="interval" name="interval">
			    <option value="">----请选择----</option>
				<option value="10">10分钟</option>
				<option value="15">15分钟</option>
				<option value="20">20分钟</option>
				<option value="30">30分钟</option>
			    <option value="60">60分钟</option>
				<option value="120">2小时</option>
				<option value="240">4小时</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("interval").value="${storageApp.interval}";
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