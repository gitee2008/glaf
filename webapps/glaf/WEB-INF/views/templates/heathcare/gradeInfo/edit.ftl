<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>班级信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

<#if gradeAdminPrivilege == true>

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/gradeInfo/saveGradeInfo',
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
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/gradeInfo/saveGradeInfo',
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

</#if>

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	 <img src="${contextPath}/static/images/window.png"> <span class="x_content_title">编辑班级信息</span>
     <#if gradeAdminPrivilege == true>
	 <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	    onclick="javascript:saveData();" >保存</a> 
     </#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${gradeInfo.id}"/>
  <table class="easyui-form" style="width:650px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">名称</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox  x-text" style="width:450px;"
				   value="${gradeInfo.name}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">代码</td>
		<td align="left">
            <input id="code" name="code" type="text" 
			       class="easyui-validatebox  x-text" style="width:450px;"
				   value="${gradeInfo.code}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">层级</td>
		<td align="left">
			 <select id="level" name="level">
				<#list dictoryList as d>
				<option value="${d.value}">${d.name}</option>
				</#list> 
			 </select>
			 <script type="text/javascript">
			     document.getElementById("level").value="${gradeInfo.level}";
			 </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">负责人</td>
		<td align="left">
            <input id="principal" name="principal" type="text" 
			       class="easyui-validatebox  x-text" style="width:450px;"
				   value="${gradeInfo.principal}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">电话</td>
		<td align="left">
            <input id="telephone" name="telephone" type="text" 
			       class="easyui-validatebox  x-text" style="width:450px;"
				   value="${gradeInfo.telephone}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">年份</td>
		<td align="left">
			<input id="year" name="year" type="text" 
			       class="easyui-numberbox x-text" style="width:60px;"
				   increment="10" maxlength="4"
				   value="${gradeInfo.year}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">备注</td>
		<td align="left">
            <textarea id="remark" name="remark" class="easyui-validatebox  x-text"  
				      rows="5" cols="50" style="height:150px; width:450px;">${gradeInfo.remark}</textarea>
		</td>
	</tr>

	<tr>
		<td width="20%" align="left">是否有效</td>
		<td align="left">
          <select id="locked" name="locked">
		    <option value="0">是</option>
		    <option value="1">否</option>
		  </select> 
		  <script type="text/javascript">
		       document.getElementById("locked").value="${gradeInfo.locked}";
		  </script>
		  &nbsp;<span style="color:red;">(提示：如果已经毕业，请设置为否即可，相关数据将继续保留。)</span>
		</td>
	</tr>

   <#if createGrade == true>
    <tr>
		<td width="20%" align="left">创建教师</td>
		<td align="left">
          <select id="createTeacherFlag" name="createTeacherFlag">
		    <option value="N">否</option>
			<option value="Y">是</option>
		  </select> 
		  &nbsp;<span style="color:red;">(提示：如果选择创建教师，系统自动为该班级创建一个教师账号。)</span>
		</td>
	</tr>
   </#if>

    </tbody>
  </table>
  </form>
  <br><br>
  <br><br>
</div>
</div>
</body>
</html>