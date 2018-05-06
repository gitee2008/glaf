<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据字典</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

    function saveData(refresh){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/dictory/saveAdd',
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
					   } else { 
						   window.parent.reloadGrid();
					   }
				   }
			 });
	}

</script>
</head>
<body style="margin:10px;">
<form id="iForm"  action="${contextPath}/sys/dictory/saveAdd" method="post" >
<input type="hidden" name="nodeId" value="${nodeId}">
<div class="easyui-panel" title="新增数据字典" style="width:550px;padding:10px"> 
<table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
      <tr>
        <td width="21%" class="input-box">名称&nbsp;<font color="red">*</font></td>
        <td width="79%">
		<input type="text" name="name"  datatype="string" nullable="no" maxsize="50" chname="名称"
		       class="easyui-validatebox x-text" data-options="required:true" size="50" style="width:320px;">
		</td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">代码</td>
        <td>
		<input type="text" name="code" datatype="string" nullable="yes" maxsize="50" chname="代码"
		       class="easyui-validatebox x-text" size="50" style="width:320px;">
		</td>
      </tr>
	  <tr>
        <td class="input-box2" valign="top">属性值</td>
        <td>
		<input type="text" name="value" datatype="string" nullable="yes" maxsize="200" chname="属性值"
		       class="easyui-validatebox x-text" size="50" style="width:320px;">
		</td>
      </tr>
	  <tr>
        <td class="input-box2" valign="top">描述</td>
        <td>
		<textarea type="textarea" id="desc" name="desc" rows="5" cols="50" class="x-textarea" 
		       style="width:320px;height:90px" datatype="string" nullable="yes" maxsize="2000" chname="描述"></textarea>
		</td>
      </tr>

	  <list dicts as dict>
	  <tr>
        <td class="input-box2" valign="top">
		${dict.title}&nbsp;<#if dict.required==1><font color="red">*</font></#if >
		</td>
        <td>
		     <#if  dict.type=='String' >
			    <input type="text" name="${dict.name}" datatype="string" nullable="${dict.nullable}" maxsize="${dict.length}" 
				       chname="${dict.title}" value="${dict.value}" size="50"
				       class="easyui-validatebox"
				<#if dict.required==1>data-options="required:true"</#if >>
			 <#elseif dict.type=='Date'>
			    <input type="text" name="${dict.name}" class="easyui-datebox"  datatype="datetime" nullable="${dict.nullable}" 
				       maxsize="${dict.length}" chname="${dict.title}" size="50"
				       value="${dict.value}"
				<#if dict.required==1>data-options="required:true"</#if >
				>&nbsp;
				 
			 <#elseif dict.type=='Long'>
			    <input type="text" name="${dict.name}" datatype="integer" nullable="${dict.nullable}" maxsize="12" chname="${dict.title}" 
				       value="${dict.value}" size="50"
				       class="easyui-validatebox"
				<#if dict.required==1>data-options="required:true"</#if >>

			 <#elseif dict.type=='Double'>
			    <input type="text" name="${dict.name}" datatype="double" nullable="${dict.nullable}" maxsize="20" chname="${dict.title}" 
				       value="${dict.value}" size="50"
				       class="easyui-validatebox"
				<#if dict.required==1>data-options="required:true"</#if >
				>
             </#if>	
		</td>
      </tr>
	  </list>
	   
      <tr>
        <td class="input-box2" valign="top">是否有效</td>
        <td>
		  <input type="radio" name="locked" value="0" checked>是
          <input type="radio" name="locked" value="1">否
        </td>
      </tr>

      <tr>
        <td colspan="2" align="center" valign="bottom" height="30">&nbsp;
           <input name="btn_save" type="button" value="保存" class="btnGray" onclick="javascript:saveData(false);" >
		   <input name="btn_save" type="button" value="保存并关闭" class="btnGray" onclick="javascript:saveData(true);" >
		</td>
      </tr>
  </table> 
 </div>
</form> 
</body>
</html>
