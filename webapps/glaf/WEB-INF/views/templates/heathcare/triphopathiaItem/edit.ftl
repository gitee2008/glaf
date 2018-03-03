<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>营养性疾病</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/triphopathiaItem/saveTriphopathiaItem',
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
					   } 
				   }
			 });
	}

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/triphopathiaItem/saveTriphopathiaItem',
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
					   }
				   }
			 });
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
    <div data-options="region:'north',split:false,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	&nbsp;<img src="${contextPath}/static/images/window.png"><span class="x_content_title">编辑营养性疾病</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${triphopathiaItem.id}"/>
  <input type="hidden" id="triphopathiaId" name="triphopathiaId" value="${triphopathiaId}"/>
  <input type="hidden" id="gradeId" name="gradeId" value="${gradeId}"/>
  <#if person?exists >
  <input type="hidden" id="personId" name="personId" value="${person.id}">
  </#if>
  <table class="easyui-form" style="width:100%;margin-top:5px;" align="center">
    <tbody>
    <#if person?exists >
	<tr>
		<td width="25%" align="left">姓名</td>
		<td width="75%" align="left">${person.name}</td>
	</tr>
	</#if>
	<tr>
		<td width="25%" align="left">检查日期</td>
		<td align="left">
			 <input id="checkDate" name="checkDate" type="text" 
			       class="easyui-datebox x-text"
			       style="width:100px"
			       <#if triphopathiaItem.checkDate?exists>
				   value="${triphopathiaItem.checkDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">身高</td>
		<td align="left">
			<input id="height" name="height" type="text" style="width:60px; text-align:right;" align="right"
			       class="easyui-numberbox  x-text"  precision="0" maxlength="3"
				   value="${triphopathiaItem.height}"/>&nbsp;(单位：厘米cm)
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">体重</td>
		<td align="left">
			<input id="weight" name="weight" type="text" style="width:60px; text-align:right;" align="right"
			       class="easyui-numberbox  x-text"  precision="1" maxlength="5"
				   value="${triphopathiaItem.weight}"/>&nbsp;(单位：千克kg)
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">主要症状</td>
		<td align="left">
		    <textarea id="symptom" name="symptom" type="text" 
			   class="x-textarea" style="width:345px;height:90px">${triphopathiaItem.symptom}</textarea>
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">建议及意见</td>
		<td align="left">
		    <textarea id="suggest" name="suggest" type="text" 
			   class="x-textarea" style="width:345px;height:90px">${triphopathiaItem.suggest}</textarea>
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">心理行为及发育评估结果</td>
		<td align="left">
            <input id="result" name="result" type="text" 
			       class="easyui-validatebox  x-text" style="width:345px;" 
				   value="${triphopathiaItem.result}"/>
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">心理行为及发育评估意见</td>
		<td align="left">
		    <textarea id="evaluate" name="evaluate" type="text" 
			   class="x-textarea" style="width:345px;height:90px">${triphopathiaItem.evaluate}</textarea>
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">备注</td>
		<td align="left">
		    <textarea id="remark" name="remark" type="text" 
			   class="x-textarea" style="width:345px;height:90px">${triphopathiaItem.remark}</textarea>
		</td>
	</tr>
	<tr>
		<td width="25%" align="left">&nbsp;</td>
		<td align="left">
		    <br><br><br><br>
		</td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>