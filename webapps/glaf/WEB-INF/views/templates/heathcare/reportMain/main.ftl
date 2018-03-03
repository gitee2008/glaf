<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>统计报表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">
   
    function verifyForm(){
		if(document.getElementById("year").value==""){
			alert("请选择年份。");
			document.getElementById("year").focus();
			return false;
		}
		if(document.getElementById("month").value==""){
			alert("请选择月份。");
			document.getElementById("month").focus();
			return false;
		}
		return true;
	}

	function verifyForm1(){
		if(document.getElementById("year").value==""){
			alert("请选择年份。");
			document.getElementById("year").focus();
			return false;
		}
		return true;
	}

	function verifyForm2(){
		if(document.getElementById("year").value==""){
			alert("请选择年份。");
			document.getElementById("year").focus();
			return false;
		}
		if(document.getElementById("month").value==""){
			alert("请选择月份。");
			document.getElementById("month").focus();
			return false;
		}
		if(document.getElementById("gradeId").value==""){
			alert("请选择班级。");
			document.getElementById("gradeId").focus();
			return false;
		}
		return true;
	}

	function exportXls(reportId){
		if(verifyForm()){
		    var year = document.getElementById("year").value;
		    var month = document.getElementById("month").value;
			var gradeId = document.getElementById("gradeId").value;
            var link = '${contextPath}/heathcare/reportMain/exportXls?year='+year+'&month='+month+'&reportId='+reportId;
		    window.open(link);	
		}
	}

    function exportXls1(reportId){
		if(verifyForm1()){
		    var year = document.getElementById("year").value;
		    var month = document.getElementById("month").value;
			var gradeId = document.getElementById("gradeId").value;
            var link = '${contextPath}/heathcare/reportMain/exportXls?year='+year+'&month='+month+'&reportId='+reportId;
		    window.open(link);	
		}
	}


	function exportXls2(reportId){
		if(verifyForm2()){
		    var year = document.getElementById("year").value;
		    var month = document.getElementById("month").value;
			var gradeId = document.getElementById("gradeId").value;
            var link = '${contextPath}/heathcare/reportMain/exportXls?year='+year+'&month='+month+'&gradeId='+gradeId+'&reportId='+reportId;
		    window.open(link);	
		}
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
		var link = '${contextPath}/heathcare/mealFeeCount/exportXls?year='+year+'&month='+month;
		window.open(link);		   
	}

</script>
</head>

<body>
 <div style="margin:0;"></div>  
 <br />
 <div class="x_content_title" style="width:80%;"><img
	src="${contextPath}/static/images/window.png"
	alt="统计报表">&nbsp;统计报表
</div>
<br><br>
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <table class="easyui-form" style="width:80%;" align="center">
    <tbody>
	<tr>
		<td width="100%" align="left" colspan="4">
		  年份&nbsp; 
          <select id="year" name="year">
			<#list years as year>
			<option value="${year}">${year}</option>
			</#list>
		  </select>
		  <script type="text/javascript">
		      document.getElementById("year").value="${year}";
		  </script>
		 &nbsp;月份&nbsp; 
          <select id="month" name="month">
		    <option value="">----请选择----</option>
			<#list months as month>
			<option value="${month}">${month}</option>
			</#list>
		  </select>
		  <script type="text/javascript">
		      document.getElementById("month").value="${month}";
		  </script>
		 &nbsp;班级&nbsp;
          <select id="gradeId" name="gradeId">
		    <option value="">----请选择----</option>
			<#list grades as grade>
			<option value="${grade.id}">${grade.name}</option>
			</#list>
		  </select>
		</td>
	</tr>
	<tr>
	 <td align="left" width="25%"> 
	    <br>
		<input type="button" value="伙食月结算表" class="btnGray" 
		       onclick="javascript:exportXls3();">
	  </td>
	  <td align="left" width="25%"> 
	    <br>
		 
	  </td>
	  <td align="left" width="25%">
	   <br>
	  </td>
	  <td align="left" width="25%">
	    <br>
	  </td>
	</tr>
	<tr>
	  <td align="left" width="25%"> 
	    <br>
		<input type="button" value="每月食品入库统计表" class="btnGray" 
		       onclick="javascript:exportXls('MonthGoodsInStock');">
	  </td>
	  <td align="left" width="25%"> 
	    <br>
	    <input type="button" value="每月食品用量统计表" class="btnGray" 
		       onclick="javascript:exportXls('MonthGoodsActualQuantity');">
	  </td>
	  <td align="left" width="25%">
	    <br>
	  </td>
	  <td align="left" width="25%">
	    <br>
	  </td>
	</tr>
	<!-- <tr>
	  <td align="left" width="25%"> 
	    <br>
		<input type="button" value="传染病统计表" class="btnGray" 
		       onclick="javascript:exportXls1('InfectiousDiseaseCount');">
	  </td>
	  <td align="left" width="25%"> 
	    <br>
	    <input type="button" value="疑似传染病统计表" class="btnGray" 
		       onclick="javascript:exportXls1('PersonAbsenceSicknessCount');">
	  </td>
	 </tr> -->
	 <tr>
	 <td align="left" width="25%"> 
	    <br>
		<input type="button" value="出勤统计表" class="btnGray" 
		       onclick="javascript:exportXls2('PersonAttendanceWeeklyCount');">
	  </td>
	  <td align="left" width="25%"> 
	    <br>
		<input type="button" value="出勤明细表" class="btnGray" 
		       onclick="javascript:exportXls2('PersonAttendanceCount');">
	  </td>
	  <td align="left" width="25%">
	   <br>
	  </td>
	  <td align="left" width="25%">
	    <br>
	  </td>
	</tr>
	<tr>
	  <td align="left" width="25%">
	    <br>
	  </td>
	  <td align="left" width="25%">
	    <br>
	  </td>
	</tr>
   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>