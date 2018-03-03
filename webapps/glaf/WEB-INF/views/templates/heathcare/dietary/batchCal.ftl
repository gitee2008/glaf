<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function executeCount(){
		if(document.getElementById("year").value==""){
			alert("请选择年份。");
			document.getElementById("year").focus();
			return;
		}
		if(document.getElementById("week").value==""){
			alert("请选择周次。");
			document.getElementById("week").focus();
			return;
		}
		var year = document.getElementById("year").value;
		var week = document.getElementById("week").value;
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/executeCount',
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
					       window.parent.location.href="${contextPath}/heathcare/dietaryCount?year="+year+"&week="+week;
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
	<span class="x_content_title">执行食谱成分汇总</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:executeCount();" >执行</a>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="objectIds" name="objectIds" value="${objectIds}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">年份</td>
		<td align="left">
          <select id="year" name="year">
			<#list years as year>
			<option value="${year}">${year}</option>
			</#list>
		  </select>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">周次</td>
		<td align="left">
          <select id="week" name="week">
			<#list weeks as week>
			<option value="${week}">${week}</option>
			</#list>
		  </select>
		</td>
	</tr>
   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>