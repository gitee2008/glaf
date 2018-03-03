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
				   url: '${contextPath}/heathcare/triphopathia/saveTriphopathia',
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
				   url: '${contextPath}/heathcare/triphopathia/saveTriphopathia',
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
	&nbsp;<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑营养性疾病</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${triphopathia.id}"/>
  <input type="hidden" id="gradeId" name="gradeId" value="${gradeId}"/>
  <#if person?exists >
  <input type="hidden" id="personId" name="personId" value="${person.id}">
  </#if>
  <table class="easyui-form" style="width:100%;" align="center">
    <tbody>
    <#if person?exists >
	<tr>
		<td width="15%" align="left">姓名</td>
		<td width="75%" align="left">${person.name}</td>
	</tr>
	</#if>
	<#if persons?exists >
	<tr>
		<td width="15%" align="left">姓名</td>
		<td align="left" > 
            <select id="personId" name="personId">
			    <option value="">--请选择--</option>
				<#list persons as person>
			    <option value="${person.id}">${person.name}</option>
			    </#list> 
			</select>
		</td>
	</tr>
	</#if>
	<tr>
		<td width="15%" align="left">类别</td>
		<td align="left">
            <select id="type" name="type">
				<option value="">----请选择----</option> 
				<option value="1">营养性疾病</option>
				<option value="2">肥胖与消瘦</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("type").value="${triphopathia.type}";
			</script>
		</td>
	</tr>
    <tr>
		<td width="15%" align="left">疾病名称</td>
		<td align="left">
            <input id="diseaseName" name="diseaseName" type="text" 
			       class="easyui-validatebox  x-text" style="width:345px;"   
				   value="${triphopathia.diseaseName}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">发病时间</td>
		<td align="left">
			 <input id="discoverDate" name="discoverDate" type="text" 
			       class="easyui-datebox x-text"
			       style="width:120px"
			       <#if triphopathia.discoverDate?exists>
				   value="${triphopathia.discoverDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">诊断日期</td>
		<td align="left">
			 <input id="clinicDate" name="clinicDate" type="text" 
			       class="easyui-datebox x-text"
			       style="width:120px"
			       <#if triphopathia.clinicDate?exists>
				   value="${triphopathia.clinicDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
    <tr>
		<td width="15%" align="left">诊断机构</td>
		<td align="left">
            <input id="clinicOrg" name="clinicOrg" type="text" 
			       class="easyui-validatebox  x-text" style="width:345px;"   
				   value="${triphopathia.clinicOrg}"/>
		</td>
	</tr>
    <tr>
		<td width="15%" align="left">既往史</td>
		<td align="left">
            <input id="medicalHistory" name="medicalHistory" type="text" 
			       class="easyui-validatebox  x-text" style="width:345px;"  
				   value="${triphopathia.medicalHistory}"/>
		</td>
	</tr>
    <tr>
		<td width="15%" align="left">临床表现</td>
		<td align="left">
            <input id="clinicalSituation" name="clinicalSituation" type="text" 
			       class="easyui-validatebox  x-text" style="width:345px;" 
				   value="${triphopathia.clinicalSituation}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">建案时间</td>
		<td align="left">
			 <input id="archivingDate" name="archivingDate" type="text" 
			       class="easyui-datebox x-text"
			       style="width:120px"
			       <#if triphopathia.archivingDate?exists>
				   value="${triphopathia.archivingDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
		<tr>
		<td width="15%" align="left">结案时间</td>
		<td align="left">
			 <input id="closeDate" name="closeDate" type="text" 
			       class="easyui-datebox x-text"
			       style="width:120px"
			       <#if triphopathia.closeDate?exists>
				   value="${triphopathia.closeDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">建案时身高</td>
		<td align="left">
			<input id="height" name="height" type="text" style="width:40px; text-align:right;"
			       class="easyui-numberbox  x-text"  precision="0" maxlength="3"
				   value="${triphopathia.height}"/>&nbsp;(单位：厘米cm)
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">建案时体重</td>
		<td align="left">
			<input id="weight" name="weight" type="text" style="width:40px; text-align:right;"
			       class="easyui-numberbox  x-text"  precision="1" maxlength="5"
				   value="${triphopathia.weight}"/>&nbsp;(单位：千克kg)
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">主要症状</td>
		<td align="left">
		    <textarea id="symptom" name="symptom" type="text" 
			   class="x-textarea" style="width:345px;height:90px">${triphopathia.symptom}</textarea>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">建议及意见</td>
		<td align="left">
		    <textarea id="suggest" name="suggest" type="text" 
			   class="x-textarea" style="width:345px;height:90px">${triphopathia.suggest}</textarea>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">备注</td>
		<td align="left">
		    <textarea id="remark" name="remark" type="text" 
			   class="x-textarea" style="width:345px;height:90px">${triphopathia.remark}</textarea>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">&nbsp;</td>
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