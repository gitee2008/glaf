<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>伙食月结算统计</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function exportXls(){
		if(document.getElementById("year").value==""){
			alert("请选择年份。");
			document.getElementById("year").focus();
			return;
		}
		if(document.getElementById("month").value==""){
			alert("请选择月份。");
			document.getElementById("month").focus();
			return;
		}
		var year = document.getElementById("year").value;
		var month = document.getElementById("month").value;
		var link = '${contextPath}/heathcare/mealFeeCount/exportXls?year='+year+'&month='+month;
		window.open(link);		   
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:40px"> 
    <div class="toolbar-backgroud"> 
	  <span class="x_content_title">伙食月结算统计</span>
	  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
	     onclick="javascript:exportXls();" >导出Excel</a>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
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
		<td width="20%" align="left">月份</td>
		<td align="left">
          <select id="month" name="month">
			<#list months as month>
			<option value="${month}">${month}</option>
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