<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>服务器设置</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
    var contextPath="${contextPath}";

	function saveData(){
		if(jQuery("#port").val()*1 < 1 || jQuery("#port").val()*1 >65536){
             alert('端口不合法，必须大约1并且小于65536！');
			 document.getElementById("port").focus();
			 return;
		}
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/server/saveServer',
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
				   url: '${contextPath}/sys/server/saveServer',
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
        <#if  serverEntity?exists>
        var type = document.getElementById("type").value;
		//alert(type);
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
		if(type=="oracle"){
			document.getElementById("port").value="1521";
			document.getElementById("portLabel").innerHTML="1521";
		}
		if(type=="rabbitmq"){
			document.getElementById("port").value="5672";
			document.getElementById("portLabel").innerHTML="5672";
		}
		if(type=="redis"){
			document.getElementById("port").value="6379";
			document.getElementById("portLabel").innerHTML="6379";
		}
		if(type=="zookeeper"){
			document.getElementById("port").value="2181";
			document.getElementById("portLabel").innerHTML="2181";
		}
		if(type=="ftp"){
			document.getElementById("port").value="21";
			document.getElementById("portLabel").innerHTML="21";
		}
		if(type=="ssh"){
			document.getElementById("port").value="22";
			document.getElementById("portLabel").innerHTML="22";
		}
		</#if>
	}

	function verify(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/server/verify',
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
		<#if  serverEntity?exists>
		&nbsp;
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-ok'" 
		   onclick="javascript:verify();" >验证</a>
		</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${serverEntity.id}"/>
  <input type="hidden" id="serverEntityId_enc" name="serverEntityId_enc" value="${serverEntityId_enc}"/>
  <input type="hidden" id="nodeId" name="nodeId" value="${nodeId}"/>
  <table class="easyui-form" style="width:680px;" align="left">
    <tbody>
	<tr>
		<td width="25%" align="left">主题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox  x-text" style="width:425px;" 
				   value="${serverEntity.title}" size="50"/>
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">识别码</td>
		<td align="left">
            <input id="discriminator" name="discriminator" type="text" 
			       class="easyui-validatebox  x-text" style="width:425px;" 
				   value="${serverEntity.discriminator}" size="10" maxlength="10"/>
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">别名</td>
		<td align="left">
            <input id="mapping" name="mapping" type="text" 
			       class="easyui-validatebox  x-text" style="width:425px;" 
				   value="${serverEntity.mapping}" size="50" maxlength="50"/>
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">服务器类型</td>
		<td align="left">
             <select id="type" name="type" onchange="javascript:changePort();">
			    <option value="">----请选择----</option>
				<option value="mongodb">MongoDB</option>
			    <option value="rabbitmq">RabbitMQ</option>
				<option value="redis">Redis</option>
				<option value="http">HTTP</option>
				<option value="https">HTTPS</option>
				<option value="ftp">FTP</option>
				<option value="smtp">SMTP</option>
				<option value="smb">SMB</option>
				<option value="ssh">SSH</option>
				<option value="hdfs">HDFS</option>
				<option value="zookeeper">Zookeeper</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("type").value="${serverEntity.type}";
             </script>
		</td>
	</tr>

	<tr>
		<td width="25%" align="left">服务器级别</td>
		<td align="left">
             <select id="level" name="level">
			    <option value="0">----请选择----</option>
			    <option value="1">主</option>
				<option value="2">从</option>
				<option value="7">其他</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("level").value="${serverEntity.level}";
             </script>
		</td>
	</tr>

	<tr>
		<td width="25%" align="left">主机</td>
		<td align="left">
            <input id="host" name="host" type="text" 
			       class="easyui-validatebox  x-text" style="width:425px;" 
				   value="${serverEntity.host}"  size="50"/>
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">端口</td>
		<td align="left">
			<input id="port" name="port" type="text" 
			       class="easyui-validatebox easyui-numberbox x-text" 
				   increment="1" style="width: 60px;" 
				   value="${serverEntity.port}" size="5" maxlength="5"/>
		    <label id="portLabel"></label>
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">用户名</td>
		<td align="left">
            <input id="user" name="user" type="text" 
			       class="easyui-validatebox  x-text" style="width:425px;" 
				   value="${serverEntity.user}" size="50"/>
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">密码</td>
		<td align="left">
            <input id="password" name="password" type="password" 
			       class="easyui-validatebox  x-text" style="width:425px;" 
				   <#if serverEntity.password?if_exists> value="88888888" </#if>  size="50"/>
		</td>
	</tr>

	<tr>
		<td width="25%" align="left">库名</td>
		<td align="left">
            <input id="dbname" name="dbname" type="text" 
			       class="easyui-validatebox  x-text" style="width:425px;" 
				   value="${serverEntity.dbname}"  size="50"/>
		   <br> 
		</td>
	</tr>

	<tr>
		<td width="25%" align="left">根路径</td>
		<td align="left">
            <input id="path" name="path" type="text" 
			       class="easyui-validatebox  x-text" style="width:425px;" 
				   value="${serverEntity.path}"  size="50"/>
		   <br> 
		</td>
	</tr>

	<tr>
		<td width="25%" align="left">目录</td>
		<td align="left">
            <input id="catalog" name="catalog" type="text" 
			       class="easyui-validatebox  x-text"  style="width:425px;"
				   value="${serverEntity.catalog}"  size="50"/>
		   <br> 
		</td>
	</tr>

	<tr>
		<td width="25%" align="left">应用程序</td>
		<td align="left">
            <input id="program" name="program" type="text" 
			       class="easyui-validatebox  x-text" style="width:425px;" 
				   value="${serverEntity.program}"  size="50"/>
		   <br>（提示：应用程序名称，相当于操作系统完整的路径如/bin/bash。）
		</td>
	</tr>

    <tr>
		<td width="25%" align="left">加密算法</td>
		<td align="left">
             <select id="secretAlgorithm" name="secretAlgorithm">
			    <option value="">----请选择----</option>
			    <option value="DES">3DES</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("secretAlgorithm").value="${serverEntity.secretAlgorithm}";
             </script>
		</td>
	</tr>

	<tr>
		<td width="25%" align="left">密锁</td>
		<td align="left"> 
		    <textarea id="secretKey" name="secretKey" rows="6" cols="46" class="x-textarea" style="width:425px;" >${serverEntity.secretKey}</textarea>
			<div style="margin-top:5px;">
			<span style="color:red; margin-left:2px;">
			 （提示：默认生成2048位密锁串，加密端与解密端的密锁及向量必须匹配才能正常加解密。）
			</span>
	   </div>
		</td>
	</tr>

	<tr>
		<td width="25%" align="left">密锁向量</td>
		<td align="left">
            <input id="secretIv" name="secretIv" type="text" 
			       class="easyui-validatebox  x-text"  style="width:425px;"
				   value="${serverEntity.secretIv}"  size="50"/>
		   <br> 
		</td>
	</tr>

    <tr>
		<td width="25%" align="left">访问角色</td>
		<td align="left">
			<input type="hidden" id="perms" name="perms" value="${serverEntity.perms}">
            <textarea id="x_roles_name" name="x_roles_name" rows="6" cols="36" class="x-textarea" style="width:425px;"
			          readonly>${x_role_names}</textarea>
			&nbsp;
			<input type="button" name="button" value="添加" class="btnGray" 
			       onclick="javascript:selectRole('iForm', 'perms','x_roles_name');"> 
			&nbsp;
			<input type="button" name="button" value="清空" class="btnGray" 
			       onclick="javascript:clearSelected('perms','x_roles_name');">
		</td>
	</tr>

	<tr>
		<td width="25%" align="left">允许访问IP地址</td>
		<td align="left"> 
		    <textarea id="addressPerms" name="addressPerms" rows="6" cols="46" class="x-textarea" style="width:425px;" >${serverEntity.addressPerms}</textarea>
			<br><span style="color:red;">（需要独立提供对外服务时才设定。）</span>
			<br>允许使用*为通配符，多个地址之间用半角的逗号“,”隔开。
			<br>例如：192.168.*.*，那么192.168.1.100及192.168.142.100都可访问该服务。
            <br>192.168.142.*，那么192.168.1.100不能访问但192.168.142.100可访问该服务。
			<br>如果配置成192.168.1.*,192.168.142.*，那么192.168.1.100及192.168.142.100均可访问该服务。
		</td>
	</tr>

	<tr>
		<td width="25%" align="left">扩展属性</td>
		<td align="left"> 
		    <textarea id="attribute" name="attribute" rows="6" cols="46" class="x-textarea" style="width:425px;" >${serverEntity.attribute}</textarea>
		</td>
	</tr>

    <tr>
		<td width="25%" align="left">检测是否在线</td>
		<td align="left">
             <select id="detectionFlag" name="detectionFlag">
			    <option value="">----请选择----</option>
			    <option value="Y">是</option>
				<option value="N">否</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("detectionFlag").value="${serverEntity.detectionFlag}";
             </script>
		</td>
	</tr>

	<tr>
		<td width="25%" align="left">是否启用</td>
		<td align="left">
		<input type="radio" name="active" value="1" <#if serverEntity.active == '1'>checked</#if>>启用&nbsp;&nbsp;
	    <input type="radio" name="active" value="0" <#if serverEntity.active == '0'>checked</#if>>禁用
		</td>
	</tr>

	<tr>
		<td width="25%" align="left"><br><br></td>
		<td align="left"><br><br><br><br></td>
	</tr>
 
    </tbody>
  </table>
  </form>
</div>
</div>

</body>
</html>