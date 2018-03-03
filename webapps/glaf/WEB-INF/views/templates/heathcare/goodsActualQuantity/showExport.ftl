<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计报表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">
   
    function verifyForm(){
		if(document.getElementById("date").value==""){
			alert("请选择日期。");
			document.getElementById("date").focus();
			return false;
		}
		return true;
	}

	function verifyForm2(){
		if(document.getElementById("date2").value==""){
			alert("请选择日期。");
			document.getElementById("date2").focus();
			return false;
		}
		return true;
	}

	function exportXls(reportId){
		if(verifyForm()){
		    var date = document.getElementById("date").value;
            var link = '${contextPath}/heathcare/reportMain/exportXls?date='+date+'&reportId='+reportId;
		    window.open(link);	
		}
	}

	function exportXls2(reportId){
		if(verifyForm()){
		    var date = document.getElementById("date").value;
            var link = '${contextPath}/heathcare/reportMain/exportXls?date='+date+'&reportId='+reportId;
		    window.open(link);	
		}
	}


	function doWeeklyExport12(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		var link="${contextPath}/heathcare/reportJxls2/exportXls?reportId=WeeklyFoodActualQuantityCount";
		if(startTime != ""){
			link = link + "&startDate=" + startTime;
		}
		if(endTime != ""){
			link = link  + "&endDate="+endTime;
		}
        window.open(link);
	}


	function doWeeklyExport1(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=WeekMultiSheetGoodsActualQuantity";
		if(startTime != ""){
			link = link + "&startDate=" + startTime ;
		}
		if(endTime != ""){
			link = link  + "&endDate="+endTime;
		}
        window.open(link);
	}

	function doWeeklyExport2(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=WeekMultiAreaGoodsActualQuantity";
		if(startTime != ""){
			link = link + "&startDate=" + startTime ;
		}
		if(endTime != ""){
			link = link  + "&endDate="+endTime;
		}
        window.open(link);
	}

	function doWeeklyExport3(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=WeekGoodsActualQuantity";
		if(startTime != ""){
			link = link + "&startDate=" + startTime ;
		}
		if(endTime != ""){
			link = link  + "&endDate="+endTime;
		}
        window.open(link);
	}

	function doWeeklyExport5(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=WeeklyMultiAreaFoodActualQuantity";
		if(startTime != ""){
			link = link + "&startDate=" + startTime ;
		}
		if(endTime != ""){
			link = link  + "&endDate="+endTime;
		}
        window.open(link);
	}

    function doWeeklyExport5x(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=WeeklyMultiSheetFoodActualQuantity";
		if(startTime != ""){
			link = link + "&startDate=" + startTime ;
		}
		if(endTime != ""){
			link = link  + "&endDate="+endTime;
		}
        window.open(link);
	}

	function doWeeklyExport6(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=WeeklyFoodActualQuantity";
		if(startTime != ""){
			link = link + "&startDate=" + startTime ;
		}
		if(endTime != ""){
			link = link  + "&endDate="+endTime;
		}
        window.open(link);
	}

	function doWeeklyExport8(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=WeeklyFoodNutritionCount";
		if(startTime != ""){
			link = link + "&startDate=" + startTime;
		}
		if(endTime != ""){
			link = link  + "&endDate="+endTime+" 23:59:59";
		}
        window.open(link);
	}

	function doWeeklyExport9(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=DietaryMultiAreaNutritionCount";
		if(startTime != ""){
			link = link + "&startDate=" + startTime;
		}
		if(endTime != ""){
			link = link  + "&endDate="+endTime;
		}
        window.open(link);
	}

	function doDietaryAnalyzeExport12(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=DietaryAnalyze";
		if(startTime != ""){
			link = link + "&startDate=" + startTime;
		}
		if(endTime != ""){
			link = link  + "&endDate="+endTime;
		}
        window.open(link);
	}

	function doExport15(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=GoodsInOutStockActualQuantity";
		if(startTime != ""){
			link = link + "&startTime=" + startTime;
		}
		if(endTime != ""){
			link = link  + "&endTime="+endTime;
		}
        window.open(link);
	}

	function showDayExport(){
        if(verifyForm()){
		    var date = document.getElementById("date").value;
            var link = '${contextPath}/heathcare/goodsActualQuantity/showDayExport?date='+date;
		    window.open(link);	
		}
	}

	function showSectionExport(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
        var link = '${contextPath}/heathcare/goodsActualQuantity/showSectionExport?q=1';
		if(startTime != ""){
			link = link + "&startDate=" + startTime;
		}
		if(endTime != ""){
			link = link  + "&endDate="+endTime;
		}
		window.open(link);	
	}

</script>
</head>

<body>
<div style="margin:10px;"></div>  
&nbsp;&nbsp;<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">统计报表</span>
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <br><br>
  <table class="easyui-form" style="width:95%;" align="center">
    <tbody>
	<tr>
		<td width="100%" align="left" height="35">
		   请选择导出日期：<input id="date" name="date" type="text" class="easyui-datebox x-text" style="width:100px;">
		   &nbsp;<input type="button" value="导出" class="btnGray" onclick="javascript:exportXls('DailyGoodsActualQuantity');">
		   &nbsp;<input type="button" value="成分" class="btnGray" onclick="javascript:showDayExport();">
		   &nbsp;<input type="button" value="每日膳费表" class="btnGray" onclick="javascript:exportXls2('DailyGoodsActualQuantity2');">
		</td>
	</tr>
	<!-- <tr>
		<td width="100%" align="left" height="35">
		   请选择导出日期：<input id="date2" name="date2" type="text" class="easyui-datebox x-text" style="width:100px;">
		   &nbsp;<input type="button" value="每日膳费表" class="btnGray" onclick="javascript:exportXls2('DailyGoodsActualQuantity2');">
		</td>
	</tr> -->
	<tr>
	 <td align="left" height="35"> 
	      &nbsp;时间段&nbsp;开始&nbsp;
		  <input id="startTime" name="startTime" type="text" class="easyui-datebox x-text" style="width:100px"
		         <#if startTime?exists> value="${startTime}"</#if>>
		  &nbsp;结束&nbsp;
		  <input id="endTime" name="endTime" type="text" class="easyui-datebox x-text" style="width:100px"
		         <#if endTime?exists> value="${endTime}"</#if>>
		  &nbsp;<input type="button" value="导出" class="btnGray" onclick="javascript:doWeeklyExport12();">
		  &nbsp;<input type="button" value="成分" class="btnGray" onclick="javascript:showSectionExport();">
	  </td>
	</tr>
	<tr>
	 <td align="left" height="35"> 
		  <input type="button" value="食物用量汇总表" class="btnGray" style="width:160px" onclick="javascript:doWeeklyExport3();">
		  &nbsp;
		  <input type="button" value="膳食营养分析统计表" class="btnGray" style="width:160px"
		         onclick="javascript:doDietaryAnalyzeExport12();">
	 </td>
	</tr>
	<tr>
	 <td align="left" height="35"> 
		  <input type="button" value="食物用量表2" class="btnGray" style="width:160px" onclick="javascript:doWeeklyExport5x();">
          &nbsp;
		  <input type="button" value="食物用量表2(单页)" class="btnGray" style="width:160px" onclick="javascript:doWeeklyExport5();">
	 </td>
	</tr>
	<tr>
	 <td align="left" height="35"> 
		  <input type="button" value="营养成分统计表" class="btnGray" style="width:160px" onclick="javascript:doWeeklyExport8();">
          &nbsp;
		  <input type="button" value="营养成分统计表(打印)" class="btnGray" style="width:160px" onclick="javascript:doWeeklyExport9();">
	 </td>
	</tr>
	<tr>
	 <td align="left"> 
		  
	 </td>
	</tr>
   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>