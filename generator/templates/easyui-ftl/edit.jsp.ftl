<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${tableDefinition.title}</title>
<# include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/save${entityName}',
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
		document.getElementById("${idField.name}").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '#F{contextPath}/${tableDefinition.moduleName}/${modelName}/save${entityName}',
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
  <div data-options="region:'north',split:true,border:true" style="height:40px"> 
    <div class="toolbar-backgroud"> 
	<span class="x_content_title">编辑${tableDefinition.title}</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="${idField.name}" name="${idField.name}" value="#F{${modelName}.${idField.name}}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
  <#if pojo_fields?exists>
    <#list  pojo_fields as field>	
	<#if field.editableField == "1">
	<tr>
		<td width="20%" align="left">${field.title}</td>
		<td align="left">
		<#if field.type?exists && field.type== 'Date'>
			<input id="${field.name}" name="${field.name}" type="text" 
			       class="easyui-datebox x-text"
			<#if field.nullable == false> required="true" data-options="required:true" </#if>
			       <# if ${modelName}.${field.name}?exists>
				   value="#F{${modelName}.${field.name} ? string('yyyy-MM-dd')}"
				   </# if>
				   />
            <#elseif field.type?exists && field.type== 'Integer'>
			<input id="${field.name}" name="${field.name}" type="text" 
			       class="easyui-numberbox x-text" 
				   increment="10"  <#if field.nullable == false> required="true" data-options="required:true" </#if>
				   value="#F{${modelName}.${field.name}}"/>
			<#elseif field.type?exists && field.type== 'Long'>
			<input id="${field.name}" name="${field.name}" type="text"
			       class="easyui-numberbox x-text"
				   increment="100"  <#if field.nullable == false> required="true" data-options="required:true" </#if>
				   value="#F{${modelName}.${field.name}}"/>
			<#elseif field.type?exists && field.type== 'Double'>
			<input id="${field.name}" name="${field.name}" type="text"
			       class="easyui-numberbox  x-text"  precision="2" 
			<#if field.nullable == false> required="true" data-options="required:true" </#if>
				  value="#F{${modelName}.${field.name}}"/>
			<#else>
			 <#if field.dataCode?exists>
			   <input class="easyui-combobox" id="${field.name}" name="${field.name}" style="width:185px;" 
			          value="#F{${modelName}.${field.name}}"
                      data-options="
                        url: '#F{contextPath}/dictory/jsonArray?nodeCode=${field.dataCode}',
                        method: 'get',
                        valueField: 'value',
                        textField: 'value'">
			 <#else>
              <input id="${field.name}" name="${field.name}" type="text" 
			         class="easyui-validatebox x-text"  
			  <#if field.nullable == false> required="true" data-options="required:true" </#if>
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