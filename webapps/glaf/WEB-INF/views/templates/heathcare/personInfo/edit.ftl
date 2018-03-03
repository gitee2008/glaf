<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>就餐人数信息</title>
<#include "/inc/init_easyui_import.ftl"/> 
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personInfo/savePersonInfo',
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
				   url: '${contextPath}/heathcare/personInfo/savePersonInfo',
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
  <div data-options="region:'north',split:false,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<span class="x_content_title">&nbsp;<img src="${contextPath}/static/images/window.png">&nbsp;编辑就餐人数信息</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${personInfo.id}"/>
  <table class="easyui-form" style="width:650px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">年级</td>
		<td align="left">
             <select id="classType" name="classType">
			    <option value="">----请选择----</option>
				<#list dictoryList as d>
				<option value="${d.code}">${d.name}</option>
				</#list> 
             </select>
			 <script type="text/javascript">
			     document.getElementById("classType").value="${personInfo.classType}";
			 </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">年龄</td>
		<td align="left">
			 <select id="age" name="age">
				<!-- <option value="1">1岁</option>
				<option value="2">2岁</option> -->
				<option value="3">3岁</option>
				<option value="4">4岁</option>
				<option value="5">5岁</option>
				<option value="6">6岁</option>
			 </select>
			 <script type="text/javascript">
			     document.getElementById("age").value="${personInfo.age}";
			 </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">男生人数</td>
		<td align="left">
			<input id="male" name="male" type="text" 
			       class="easyui-numberbox x-text" 
				   increment="10" style="width:60px; text-align:right;" 
				   value="${personInfo.male}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">女生人数</td>
		<td align="left">
			<input id="female" name="female" type="text" 
			       class="easyui-numberbox x-text" 
				   increment="10" style="width:60px; text-align:right;" 
				   value="${personInfo.female}"/>
		</td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>