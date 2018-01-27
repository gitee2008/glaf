<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据库设置</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		if(jQuery("#port").val()*1 < 1 || jQuery("#port").val()*1 >65536){
             alert('端口不合法，必须大约1并且小于65536！');
			 document.getElementById("port").focus();
			 return;
		}
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/database/saveDB',
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
					   window.parent.location.reload();
					   window.close();
				   }
			 });
	}

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/database/saveDB',
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
					   window.parent.location.reload();
					   window.close();
				   }
			 });
	}

	function changePort(){
        var type = document.getElementById("type").value;

		if(type=="mysql"){
			document.getElementById("port").value="3306";
			document.getElementById("portLabel").innerHTML="3306";
		}
		if(type=="postgresql"){
			document.getElementById("port").value="5432";
			document.getElementById("portLabel").innerHTML="5432";
		}
		if(type=="sqlserver"){
			document.getElementById("port").value="1433";
			document.getElementById("portLabel").innerHTML="1433";
		}
		if(type=="mongodb"){
			document.getElementById("port").value="27017";
			document.getElementById("portLabel").innerHTML="27017";
		}
		if(type=="hbase"){
			document.getElementById("port").value="2181";
			document.getElementById("portLabel").innerHTML="2181";
		}
		if(type=="oracle"){
			document.getElementById("port").value="1521";
			document.getElementById("portLabel").innerHTML="1521";
		}
	}

	function verify(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/database/verify',
				   data: params,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert(data.message);
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
		<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑记录</span>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
		   onclick="javascript:saveData();" >保存</a>
		&nbsp;
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
		   onclick="javascript:saveAsData();" >另存</a>
		<#if database != null >
		&nbsp;
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-ok'" 
		   onclick="javascript:verify();" >验证</a>
		</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${database.id}"/>
  <input type="hidden" id="databaseId_enc" name="databaseId_enc" value="${databaseId_enc}"/>
  <input type="hidden" id="nodeId" name="nodeId" value="${nodeId}"/>
  <table class="easyui-form" style="width:800px;" align="left">
    <tbody>
	<tr>
		<td width="18%" align="left">主题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox  x-text"  
				   value="${database.title}" size="60"/>
		</td>
	</tr>
	<tr>
		<td width="18%" align="left">标段</td>
		<td align="left">
            <input id="section" name="section" type="text" 
			       class="easyui-validatebox  x-text"  
				   value="${database.section}" size="60" maxlength="50"/>
		</td>
	</tr>
	<tr>
		<td width="18%" align="left">别名</td>
		<td align="left">
            <input id="mapping" name="mapping" type="text" 
			       class="easyui-validatebox  x-text"  
				   value="${database.mapping}" size="60" maxlength="50"/>
		</td>
	</tr>
	<tr>
		<td width="18%" align="left">类别</td>
		<td align="left">
             <select id="discriminator" name="discriminator" >
			    <option value="">----请选择----</option>
				<option value="B">业务</option>
				<option value="S">存储</option>
				<option value="O">其他</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("discriminator").value="${database.discriminator}";
             </script>
		</td>
	</tr>
	<tr>
		<td width="18%" align="left">实例类别</td>
		<td align="left">
             <select id="runType" name="runType" >
			    <option value="">----请选择----</option>
			    <option value="TPL">模板库</option>
				<option value="INST">实例库</option>
				<option value="TPL&INST">模板库&实例库</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("runType").value="${database.runType}";
             </script>
		</td>
	</tr>
	<tr>
		<td width="18%" align="left">数据库类型</td>
		<td align="left">
             <select id="type" name="type" onchange="javascript:changePort();">
			    <option value="">----请选择----</option>
				<option value="hbase">HBase</option>
			    <option value="mysql">MySQL</option>
				<option value="sqlserver">Microsoft SQL Server</option>
				<option value="mongodb">MongoDB</option>
				<option value="oracle">Oracle</option>
				<option value="postgresql">PostgreSQL</option>
             </select>&nbsp;*
             <script type="text/javascript">
                 document.getElementById("type").value="${database.type}";
             </script>
		</td>
	</tr>

	<tr>
		<td width="18%" align="left">使用目的</td>
		<td align="left">
             <select id="useType" name="useType" >
			    <option value="">----请选择----</option>
				<option value="GENERAL">通用库</option>
				<option value="FILE">文件存储库</option>
				<option value="LOG">日志库</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("useType").value="${database.useType}";
             </script>
		</td>
	</tr>

	<tr>
		<td width="18%" align="left">数据库级别</td>
		<td align="left">
             <select id="level" name="level">
			    <option value="1">主库</option>
				<option value="2">从库</option>
				<option value="3">日志</option>
				<option value="7">其他</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("level").value="${database.level}";
             </script>
		</td>
	</tr>

	<tr>
		<td width="18%" align="left">主机</td>
		<td align="left">
            <input id="host" name="host" type="text" 
			       class="easyui-validatebox  x-text"  
				   value="${database.host}"  size="60"/>&nbsp;*
		</td>
	</tr>
	<tr>
		<td width="18%" align="left">端口</td>
		<td align="left">
			<input id="port" name="port" type="text" 
			       class="easyui-validatebox easyui-numberbox x-text" 
				   increment="1"  
				   value="${database.port}" size="5" maxlength="5"/>&nbsp;*
		    <label id="portLabel"></label>
		</td>
	</tr>
	<tr>
		<td width="18%" align="left">库名</td>
		<td align="left">
            <input id="dbname" name="dbname" type="text" 
			       class="easyui-validatebox  x-text"  
				   value="${database.dbname}"  size="60"/>&nbsp;*
		   <br>（提示：初始化后不能修改库名！）
		</td>
	</tr>

	<tr>
		<td width="18%" align="left">用户名</td>
		<td align="left">
            <input id="user" name="user" type="text" 
			       class="easyui-validatebox  x-text"  
				   value="${database.user}" size="60"/>&nbsp;*
		</td>
	</tr>
	<tr>
		<td width="18%" align="left">密码</td>
		<td align="left">
            <input id="password" name="password" type="password" 
			       class="easyui-validatebox  x-text"  size="60"
				   <#if database.password> value="88888888" </#if>  />
		</td>
	</tr>

	<tr>
		<td width="18%" align="left">短类别码</td>
		<td align="left">
			<input id="intToken" name="intToken" type="text" 
			       class="easyui-validatebox easyui-numberbox x-text" 
				   increment="1"  
				   value="${database.intToken}" size="4" maxlength="4"/>&nbsp;*
		    <label id="intTokenLabel"></label>
			<span style="color:blue; margin-left:2px;">
			 （提示：大于1000，小于9999的整数且不与其他库重复，用于缓存、合成树等前缀识别。）
			</span>
		</td>
	</tr>

  <#if database.token>
	<tr>
		<td width="18%" align="left">令牌</td>
		<td align="left">
			<input id="token" name="token" type="text" 
			       class="easyui-validatebox x-text"
				   value="${database.token}" size="60" maxlength="80" readonly />&nbsp;
		    <label id="tokenLabel"></label>
			<br>（提示：修改短类别码时可以同时修改令牌，提供给直接对外服务的应用做验证。）
		</td>
	</tr>
  </#if>

	<tr height="30">
		<td width="18%" align="left">是否启用</td>
		<td align="left">
		<input type="radio" name="active" value="1" <#if database.active == '1' >checked</#if>>启用&nbsp;&nbsp;
	    <input type="radio" name="active" value="0" <#if database.active == '0' >checked</#if>>禁用
		</td>
	</tr>
  
    <tr>
		<td width="18%" align="left">&nbsp;</td>
		<td align="left">
		  <br><br> 
		</td>
	</tr>
    
    <tr>
		<td width="18%" align="left">&nbsp;</td>
		<td align="left">
		  <br><br> 
		</td>
	</tr>
    </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>