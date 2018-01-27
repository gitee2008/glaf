<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据同步</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
    var contextPath="${request.contextPath}";

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/sys/syncApp/save',
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
					   if(data.statusCode == 200) { 
					       parent.location.reload(); 
					   }
				   }
			 });
	}

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/sys/syncApp/save',
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
	<span class="x_content_title">&nbsp;<img src="${request.contextPath}/static/images/window.png">&nbsp;编辑数据同步</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${syncApp.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="15%" align="left">标题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox x-text" style="width:425px;" 
				   value="${syncApp.title}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">来源数据库编号</td>
		<td align="left">
			<select id="srcDatabaseId" name="srcDatabaseId">
			    <option value="">----请选择----</option>
				<#list  databases as database>
				<option value="${database.id}">${database.title}[${database.dbname}]</option>
				</#list>
             </select>
             <script type="text/javascript">
                 document.getElementById("srcDatabaseId").value="${syncApp.srcDatabaseId}";
             </script>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">目标数据库</td>
		<td align="left">
		    <select class="easyui-combobox" name="targetDatabaseIds" 
			        multiple="true" multiline="true"  
			        labelPosition="top" style="width:425px;height:30px;">
				<option value=""></option>
                <#list databases as database>
				<option value="${database.id}" ${database.selected}>${database.title}[${database.dbname}]</option>
				</#list>
            </select>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">同步标识</td>
		<td align="left">
		    <select id="syncFlag" name="syncFlag">
			    <option value="">----请选择----</option>
				<option value="INSERT_UPDATE">增量同步</option>
				<option value="INSERT_ONLY">数据表为空才能增加</option>
			    <option value="DELETE_INSERT">数据表不空时先删除再增加</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("syncFlag").value="${syncApp.syncFlag}";
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
                 document.getElementById("type").value="${syncApp.type}";
             </script>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">是否自动同步</td>
		<td align="left">
		    <select id="autoSyncFlag" name="autoSyncFlag">
			    <option value="">----请选择----</option>
				<option value="Y">是</option>
			    <option value="N">否</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("autoSyncFlag").value="${syncApp.autoSyncFlag}";
             </script>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">时间间隔</td>
		<td align="left">
		    <select id="interval" name="interval">
			    <option value="">----请选择----</option>
				<option value="1">1分钟</option>
				<option value="2">2分钟</option>
				<option value="5">5分钟</option>
				<option value="10">10分钟</option>
				<option value="15">15分钟</option>
				<option value="20">20分钟</option>
				<option value="30">30分钟</option>
			    <option value="60">60分钟</option>
				<option value="120">2小时</option>
				<option value="240">4小时</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("interval").value="${syncApp.interval}";
             </script>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">是否有效</td>
		<td align="left">
		    <select id="active" name="active">
				<option value="Y">是</option>
			    <option value="N">否</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("active").value="${syncApp.active}";
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