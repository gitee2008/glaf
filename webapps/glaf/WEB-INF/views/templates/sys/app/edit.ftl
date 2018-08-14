<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>应用设置</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
    var contextPath="${contextPath}";

	function saveData(refresh){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/application/save',
				   data: params,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   if(data.statusCode == 200){
						       alert('操作成功完成！');
							   if(refresh){
							      window.parent.location.reload();
					              window.close();
							   }
						   }
					   }
				   }
			 });
	}

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/application/save',
				   data: params,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   if(data.statusCode == 200){
							   alert('操作成功完成！');
						       window.parent.location.reload();
					           window.close();
					       }
					   }    
				   }
			 });
	}

</script>
</head>
<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:45px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑记录</span>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
		   onclick="javascript:saveData(false);" >保存</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
		   onclick="javascript:saveData(true);" >保存并关闭</a>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${app.id}"/>
  <table class="easyui-form" style="width:600px;" align="left">
    <tbody>
    <tr>
        <td width="20%" class="input-box">上级节点</td>
        <td>
		  <select id="parentId" name="parentId" style="width:450px;">
		  <#list  apps as app>
			<#if app.locked == 0>
              <option value="${app.id}">${app.blank}${app.name}</option>
			</#if>
		  </#list>
          </select>
		  <script language="javascript">
		      document.all.parentId.value="${parentId}";	
	      </script>
		</td>
    </tr>
	<tr>
		<td width="20%" align="left">名称</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox  x-text"  
				   value="${app.name}" size="80" style="width:450px;"/>
		   <br>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">代码</td>
		<td align="left">
            <input id="code" name="code" type="text" 
			       class="easyui-validatebox  x-text"  
				   value="${app.code}" size="80" style="width:450px;"/>
		   <br>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">链接地址</td>
		<td align="left">
             <textarea id="url" name="url" rows="6" cols="50" class="x-textarea"   style="width:450px;height:120px;">${app.url}</textarea>
		</td>
	</tr>

	<tr>
		<td width="20%" align="left">是否弹窗</td>
		<td align="left">
		  <input type="radio" name="showMenu" value="2" <#if app.showMenu == 2>checked</#if>>是&nbsp;&nbsp;
		  <input type="radio" name="showMenu" value="1" <#if app.showMenu == 1>checked</#if>>否&nbsp;&nbsp;
		</td>
	</tr>
   
	<tr>
		<td width="20%" align="left">是否启用</td>
		<td align="left">
		  <input type="radio" name="locked" value="0" <#if app.locked == 0>checked</#if>>是&nbsp;&nbsp;
	      <input type="radio" name="locked" value="1" <#if app.locked == 1>checked</#if>>否&nbsp;&nbsp;
		</td>
	</tr>

	<tr>
	 <td colspan="2"><br><br><br><br><br><br><br><br></td>
	</tr>
	
    </tbody>
  </table>
 </form>
</div>
</div>

</body>
</html>