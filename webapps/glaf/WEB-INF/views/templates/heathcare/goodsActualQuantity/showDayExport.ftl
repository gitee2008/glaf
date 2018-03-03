<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食物成分构成</title>
<#include "/inc/init_easyui_import.ftl"/>
</head>
<body style="margin-left:5px;">
  <table height="100%" width="100%" style="height:450px">
	<tbody>
	   <tr>
		<td width="25%">
		  <iframe id="x1" name="x1" style="width:360px; height:420px" frameborder="0" ></iframe>		  
        </td>
		<td width="25%">
		  <iframe id="x2" name="x2" style="width:360px; height:420px" frameborder="0"></iframe>
		</td>
		<td width="50%" colspan="2" align="center">
		  <iframe id="x4" name="x4" style="width:420px; height:440px" frameborder="0"></iframe>
		</td>
	   </tr>
	</tbody>
  </table>
  <table height="100%" width="100%" style="height:420px">
	<tbody>
	   <tr>
		<td width="25%">
		  <iframe id="x5" name="x5" style="width:360px; height:390px" frameborder="0" ></iframe>		  
        </td>
		<td width="25%">
		  <iframe id="x6" name="x6" style="width:360px; height:380px" frameborder="0"></iframe>
		</td>
		<td width="25%">
		  <iframe id="x7" name="x7" style="width:360px; height:380px" frameborder="0"></iframe>
		</td>
		<td width="25%">
		  <iframe id="x8" name="x8" style="width:360px; height:380px" frameborder="0"></iframe>
		</td>
	   </tr>
	</tbody>
  </table>
  <br>&nbsp;
  <br>&nbsp;
  <br>&nbsp;
 <script type="text/javascript">
	jQuery(document).ready(function() { 

        $('#x1').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135903&suitNo=${suitNo}&type=heatEnergyX1PerDayFoodActualQuantity&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

		$('#x2').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135902&suitNo=${suitNo}&type=heatEnergyX2PerDayFoodActualQuantity&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

		$('#x4').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135302&suitNo=${suitNo}&type=proteinPercentPerDayFoodActualQuantity&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

        $('#x5').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135904&suitNo=${suitNo}&type=vitaminAAnimalsX2PercentPerDayFoodActualQuantity&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

        $('#x6').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135905&suitNo=${suitNo}&type=ironAnimalsX2PercentPerDayFoodActualQuantity&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

		$('#x7').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135906&suitNo=${suitNo}&type=fatAnimalsX2PercentPerDayFoodActualQuantity&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

		$('#x8').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135908&suitNo=${suitNo}&type=calciumPhosphorusX2PercentPerDayFoodActualQuantity&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

        }); 
</script>	
</body>
</html>