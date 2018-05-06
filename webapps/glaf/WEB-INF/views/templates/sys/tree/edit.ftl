<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分类设置</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
    var contextPath="${contextPath}";

	function saveData(refresh){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tree/save',
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
					   if(refresh){
					       window.parent.location.reload();
					       window.close();
					   }
				   }
			 });
	}

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tree/save',
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
					   if(data.statusCode == 200){
						   window.parent.location.reload();
					       window.close();
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
		<span class="x_content_title">编辑记录</span>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
		   onclick="javascript:saveData(false);" >保存</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
		   onclick="javascript:saveData(true);" >保存并关闭</a>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${tree.id}"/>
  <table class="easyui-form" style="width:600px;" align="left">
    <tbody>
      <tr>
        <td width="20%" class="input-box">上级节点</td>
        <td>
		  <select id="parentId" name="parentId" style="width:450px;">
		  <#list trees as tree>
			<#if tree.locked == 0>
              <option value="${tree.id}">${tree.blank}${tree.name}</option>
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
				   value="${tree.name}" size="80" style="width:450px;"/>
		   <br>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">代码</td>
		<td align="left">
            <input id="code" name="code" type="text" 
			       class="easyui-validatebox  x-text"  
				   value="${tree.code}" size="80" style="width:450px;"/>
		   <br>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">描述</td>
		<td align="left">
            <input id="desc" name="desc" type="text" 
			       class="easyui-validatebox  x-text"  
				   value="${tree.desc}" size="80" style="width:450px;"/>
		   <br>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">属性值</td>
		<td align="left">
            <input id="value" name="value" type="text" 
			       class="easyui-validatebox  x-text"  
				   value="${tree.value}" size="80" style="width:450px;"/>
		   <br>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">链接地址</td>
		<td align="left">
             <textarea id="url" name="url" rows="6" cols="50" style="width:450px;height:120px;"
			    class="x-textarea" >${tree.url}</textarea>
		</td>
	</tr>

	<tr>
		<td width="20%" align="left">是否启用</td>
		<td align="left">
		  <input type="radio" name="locked" value="0" <#if tree.locked == 0>checked</#if>>启用&nbsp;&nbsp;
	      <input type="radio" name="locked" value="1" <#if tree.locked == 1>checked</#if>>禁用&nbsp;&nbsp;
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