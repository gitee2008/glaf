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
            var link = '${contextPath}/heathcare/tenantReportMain/exportXls?tenantId=${tenantId}&date='+date+'&reportId='+reportId;
		    window.open(link);	
		}
	}

	function exportXls2(reportId){
		if(verifyForm2()){
		    var date = document.getElementById("date2").value;
            var link = '${contextPath}/heathcare/tenantReportMain/exportXls?tenantId=${tenantId}&date='+date+'&reportId='+reportId;
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

    function exportXls3(){
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
		var link = '${contextPath}/heathcare/mealFeeCount/exportXls?tenantId=${tenantId}&year='+year+'&month='+month;
		window.open(link);		   
	}

</script>
</head>

<body>
<div style="margin:10px;"></div>  
<span class="x_content_title" style="height:35px;margin:5px">&nbsp;统计报表</span>
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <br><br>
  <table class="easyui-form" style="width:80%;" align="center">
    <tbody>
	<tr>
		<td width="100%" align="left">
		   请选择导出日期:
		   &nbsp;<input id="date2" name="date2" type="text" class="easyui-datebox x-text" style="width:100px;height:23px">
		   &nbsp;<input type="button" value="每日膳费" class="btnGray" 
		                onclick="javascript:exportXls2('TenantDailyGoodsActualQuantity2');">
		</td>
	</tr>
	<tr>
	 <td align="left"> 
	        <br>
			<select id="year" name="year">
			    <option value="2017">2017</option>
				<option value="2018">2018</option>
				<!-- <option value="2019">2019</option>
				<option value="2020">2020</option> -->
			</select>
			&nbsp;&nbsp;年&nbsp;
			<script type="text/javascript">
			  document.getElementById("year").value="${year}";
		    </script>
		    
			<select id="month" name="month">
				<option value="">----请选择----</option>
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				<option value="8">8</option>
				<option value="9">9</option>
				<option value="10">10</option>
				<option value="11">11</option>
				<option value="12">12</option>
			</select>
			&nbsp;月&nbsp;
			<script type="text/javascript">
			  document.getElementById("month").value="${month}";
		    </script>
			&nbsp;<input type="button" value="每月膳费" class="btnGray" onclick="javascript:exportXls3();">
	 </td>
	</tr>
   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>