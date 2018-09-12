<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${tableDefinition.title}</title>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
<# include "/inc/init_layui_import.ftl"/>
<script type="text/javascript" src="#F{contextPath}/static/scripts/framework.js"></script>
<script type="text/javascript">

	function saveData(){
		//var jsonObject = fromToJson(document.getElementById("iForm"));
		var jsonObject = jQuery('#iForm').serializeObject();
        document.getElementById("json").value=JSON.stringify(jsonObject);
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/save',
				   data: params,
				   dataType: 'json',
				   error: function(data){
					   layer.msg('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   layer.msg(data.message);
					   } else {
						   layer.msg('操作成功完成！');
					   }
					   if(data.statusCode == 200){
					       window.parent.location.reload();
					   } 
				   }
			 });
	}

	function saveAsData(){
		//var jsonObject = fromToJson(document.getElementById("iForm"));
		var jsonObject = jQuery('#iForm').serializeObject();
        document.getElementById("json").value=JSON.stringify(jsonObject);
		document.getElementById("${idField.name}").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/save',
				   data: params,
				   dataType: 'json',
				   error: function(data){
					   layer.msg('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   layer.msg(data.message);
					   } 
					   if(data.statusCode == 200){
						   layer.msg('操作成功完成！');
					       window.parent.location.reload();
					   }
				   }
			 });
	}

</script>
</head>
<body style="margin:1px;">
<div class="layui-container" style="width:100%;">  
  <div> 
    <div class="toolbar-backgroud" style="height:48px"> 
	<span class="x_content_title">&nbsp;<img src="#F{contextPath}/static/images/window.png">&nbsp;编辑${tableDefinition.title}</span>
	<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="javascript:saveData();" >保存</button> 
    </div> 
  </div>

  <div>
  <form class="layui-form" id="iForm" name="iForm" method="post">
  <input type="hidden" id="json" name="json">
  <input type="hidden" id="${idField.name}" name="${idField.name}" value="#F{${modelName}.${idField.name}}"/>
  <table style="line-height:45px; width:100%;" align="center">
    <tbody>
  <#if pojo_fields?exists>
    <#list pojo_fields as field>	
	<#if field.editableField == "1">
	<tr>
		<td width="20%" align="left">${field.title}</td>
		<td align="left">
		<#if field.type?exists && field.type== 'Date'>
			<input id="${field.name}" name="${field.name}" type="text" 
			       class="layui-input" style="text-align: center"
				   lay-verify="date" placeholder="yyyy-MM-dd" autocomplete="off"
			<#if field.nullable == false> required="true"  </#if>
			       <# if ${modelName}.${field.name}?exists>
				   value="#F{${modelName}.${field.name} ? string('yyyy-MM-dd')}"
				   </# if>
				   />
            <#elseif field.type?exists && field.type== 'Integer'>
			<input id="${field.name}" name="${field.name}" type="text" 
			       class="layui-input" style="text-align: right" lay-verify="number"
				   increment="10"  <#if field.nullable == false> required="true" </#if>
				   value="#F{${modelName}.${field.name}}"/>
			<#elseif field.type?exists && field.type== 'Long'>
			<input id="${field.name}" name="${field.name}" type="text"
			       class="layui-input" style="text-align: right" lay-verify="number"
				   increment="100"  <#if field.nullable == false> required="true" </#if>
				   value="#F{${modelName}.${field.name}}"/>
			<#elseif field.type?exists && field.type== 'Double'>
			<input id="${field.name}" name="${field.name}" type="text" lay-verify="number"
			       class="layui-input"  precision="2" style="text-align: right"
			<#if field.nullable == false> required="true" </#if>
				  value="#F{${modelName}.${field.name}}"/>
			<#else>
			 <#if field.dataCode?exists>
			    
			 <#else>
              <input id="${field.name}" name="${field.name}" type="text" 
			         class="layui-input"  
			  <#if field.nullable == false> lay-verify="required" </#if>
				     value="#F{${modelName}.${field.name}}"/>
			 </#if>
			</#if>
		</td>
	</tr>
  </#if>	 
 </#list>
</#if>
 
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>