<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱构成</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>

.table-border { background-color:#3399cc; height: 32px; font-family:"宋体"}
.table-content { background-color:#ffffff; height: 32px;font-size: 12px; font-family:"宋体"}

.x_y_title {
	text-transform: uppercase;
	background-color: inherit;
	color: #000033;
	font-size: 15px;
	font-weight: bold;
	text-align: center;
}

.dietary_title {
	height: 20px;
	line-height: 20px;
	text-align: center;
	font: bold 13px 宋体;
	color: #484848;
	cursor: pointer;
}


.xz_input {
    background-color: #fff;
	border: 1px solid #fff;
	color: #666;
	padding: 2px 2px;
	line-height: 22px;
	height: 22px;
	font-size: 13px;
	text-align: right;
}

.xz_input:hover {
	color: rgb(255, 0, 0);
	font-weight: bold;
	box-shadow: 1px 1px 1px 1px #aaa;
	background-color: #ffff99;
	font-size: 15px;
	-moz-box-shadow: 0 1px 1px #aaa;
	-webkit-box-shadow: 0 1px 1px #aaa;
}

</style>
<script type="text/javascript">

 
	function doSubmit(){
		document.iForm.action="${contextPath}/heathcare/dietaryExport/showTenantDayExport?tenantId=${tenantId}";
        document.iForm.submit();
	}

 	function exportNutrition(){
		var fullDay = jQuery("#fullDay").val();
		if(fullDay == ""){
            alert('请选择模板日期！');
			return;
		}
		var link="${contextPath}/heathcare/tenantReportMain/exportXls?reportId=TenantDailyDietaryNutritionCount&tenantId=${tenantId}";
		if(fullDay != ""){
			link = link  + "&fullDay="+fullDay;
		}
        window.open(link);
	}

</script>
</head>
<body style="margin-left:5px;">
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:false" style="height:42px"> 
    <div class="toolbar-backgroud">
	  <form id="iForm" name="iForm" method="post">
	   <table width="1050" align="left">
		<tbody>
		 <tr>
		    <td width="15%" align="left">
			<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;食谱构成</span>
			</td>
			<td width="45%" align="left">
			  &nbsp;年份&nbsp; 
			  <select id="year" name="year">
				<#list years as year>
				<option value="${year}">${year}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("year").value="${year}";
			  </script>
			  &nbsp;周次&nbsp;
			  <select id="week" name="week">
				<#list weeks as week>
				<option value="${week}">${week}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("week").value="${week}";
			  </script>
			  &nbsp;日期&nbsp;
			  <select id="fullDay" name="fullDay" onchange="javascript:switchDay();">
				<#list days as day>
				<option value="${day}">${day}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("fullDay").value="${fullDay}";
			  </script>
			</td>
			<td width="5%"></td>
			<td>
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-ok'" 
	             onclick="javascript:doSubmit();" >确定</a>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
	             onclick="javascript:exportNutrition();" >营养成分统计表</a>
			</td>
		</tr>
	   </tbody>
	  </table>
	 </form>
    </div> 
   </div>
   <div data-options="region:'center',border:false,cache:true">
   <#if dayRptModel?exists>
   <table width="100%" height="99%">
    <tr>
    <td width="1050" valign="top">
     <table width="1050" height="99%" cellpadding='2' cellspacing='2' class="table-border" nowrap>
	  <tr>
		<td colspan="6" align="center" width="100%"  class="table-content">
		  <table border='0' cellpadding='0' cellspacing='0' width="100%">
		   <tr>
		     <td width="70%" align="center">
		       <span class="x_y_title">  ${fullDay}  帯  量  食  谱 </span>
		     </td>
		     <td width="30%" align="right">
		       <span >&nbsp;配餐均龄：4岁 &nbsp;一人可食均量：克&nbsp;</span>
		     </td>
		     </tr>
		   </table>
		</td>
	  </tr>
	  <tr>
		  <td width="50" class="table-content">
		  &nbsp;&nbsp;餐别
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0'   >
			  <tr>
				<td  width="150">&nbsp;食谱&nbsp;</td>
				<td  width="300">
					<table>
					<tr>
					  <td align="left"  width="200">&nbsp;食物&nbsp;</td>
					  <td align="right" width="100">&nbsp;重量&nbsp;</td>
					</tr>
					</table>
				</td>
				<td align="right" width="30">&nbsp;&nbsp;</td>
				<td align="right" width="100">&nbsp;热能(kcal)&nbsp;</td>
				<td align="right" width="100">&nbsp;碳水化合物(g)&nbsp;</td>
				<td align="right" width="100">&nbsp;蛋白质(g)&nbsp;</td>
				<td align="right" width="100">&nbsp;脂肪(g)&nbsp;</td>
				<td align="right" width="100">&nbsp;钙(mg)&nbsp;</td>
			  </tr>

			</table>
		  </td>
        </tr>
	  <#if breakfastList?exists>
	    <tr>
		  <td width="50" class="table-content">
		  &nbsp;&nbsp;早餐<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0'   >
			  <#list breakfastList as r1>
			  <tr>
				<td  width="150">
				   <span class="dietary_title" onclick="javascript:editItems('${r1.dietary.id}');">${r1.name}</span>
				</td>
				<td  width="300">
					<table>
					<#list r1.items as item1>
					<tr>
					  <td align="left"  width="200">${item1.name}</td>
					  <td align="right" width="100">
					    <#if item1.name?exists>
					      <input type="text" id="item_${item1.id}" name="myInput" value="${item1.quantity2}"
						         size="5" class="xz_input">
						</#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="30">&nbsp;&nbsp;</td>
				<td align="right" width="100">${r1.heatEnergy}</td>
				<td align="right" width="100">${r1.carbohydrate}</td>
				<td align="right" width="100">${r1.protein}</td>
				<td align="right" width="100">${r1.fat}</td>
				<td align="right" width="100">${r1.calcium}</td>
			  </tr>
			  </#list>
			</table>
		  </td>
        </tr>
	  </#if>
	  <#if breakfastMidList?exists>
	    <tr>
		  <td class="table-content">
		  &nbsp;&nbsp;早点
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0'  >
			  <#list breakfastMidList as r2>
			  <tr>
				<td  width="150">
				    <span class="dietary_title">${r2.name}</span>
				</td>
				<td  width="300">
					<table>
					<#list r2.items as item2>
					<tr>
					  <td align="left"  width="200">${item2.name}</td>
					  <td align="right" width="100">
					    <#if item2.name?exists>
					      <input type="text" id="item_${item2.id}" name="myInput" value="${item2.quantity2}"
						         size="5" class="xz_input">
						</#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="30">&nbsp;&nbsp;</td>
				<td align="right" width="100">${r2.heatEnergy}</td>
				<td align="right" width="100">${r2.carbohydrate}</td>
				<td align="right" width="100">${r2.protein}</td>
				<td align="right" width="100">${r2.fat}</td>
				<td align="right" width="100">${r2.calcium}</td>
			  </tr>
			  </#list>
			</table>
		  </td>
        </tr>
	  </#if>
	  <#if momingTotal?exists>
	  <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;小计<br>
           &nbsp;&nbsp;占比<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="150"></td>
				<td align="left" width="130">&nbsp;</td>
				<td align="right" width="100">&nbsp;</td>
				<td align="right" width="100">&nbsp;</td>
				<td align="right" width="100">
				 ${momingTotal.heatEnergy}
                 <br>${momingTotalPercent.heatEnergy}%
				</td>
				<td align="right" width="100">
				${momingTotal.carbohydrate}
                <br>${momingTotalPercent.carbohydrate}%
				</td>
				<td align="right" width="100">
				${momingTotal.protein}
				<br>${momingTotalPercent.protein}%
				</td>
				<td align="right" width="100">
				${momingTotal.fat}
				<br>${momingTotalPercent.fat}%
				</td>
				<td align="right" width="100">
				${momingTotal.calcium}
				<br>${momingTotalPercent.calcium}%
				</td>
			   </tr>
			  </table>
		  </td>
       </tr>
	  </#if>
	  <#if lunchList?exists>
	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;午餐<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <#list lunchList as r3>
			  <tr>
				<td  width="150">
				    <span class="dietary_title">${r3.name}</span>
				</td>
				<td  width="300">
					<table>
					<#list r3.items as item3>
					<tr>
					  <td align="left"  width="200">${item3.name}</td>
					  <td align="right" width="100">
					    <#if item3.name?exists>
					      <input type="text" id="item_${item3.id}" name="myInput" value="${item3.quantity2}"
						         size="5" class="xz_input">
						</#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="30">&nbsp;&nbsp;</td>
				<td align="right" width="100">${r3.heatEnergy}</td>
				<td align="right" width="100">${r3.carbohydrate}</td>
				<td align="right" width="100">${r3.protein}</td>
				<td align="right" width="100">${r3.fat}</td>
				<td align="right" width="100">${r3.calcium}</td>
			  </tr>
			  </#list>
			</table>
		  </td>
        </tr>
	  </#if>
	  <#if snackList?exists>
	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;午点<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <#list snackList as r4>
			  <tr>
				<td  width="150">
				    <span class="dietary_title">${r4.name}</span>
				</td>
				<td  width="300">
					<table>
					<#list r4.items as item4>
					<tr>
					  <td align="left"  width="200">${item4.name}</td>
					  <td align="right" width="100">
					    <#if item4.name?exists>
					      <input type="text" id="item_${item4.id}" name="myInput" value="${item4.quantity2}"
						         size="5" class="xz_input">
						</#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="30">&nbsp;&nbsp;</td>
				<td align="right" width="100">${r4.heatEnergy}</td>
				<td align="right" width="100">${r4.carbohydrate}</td>
				<td align="right" width="100">${r4.protein}</td>
				<td align="right" width="100">${r4.fat}</td>
				<td align="right" width="100">${r4.calcium}</td>
			  </tr>
			  </#list>
			</table>
		  </td>
        </tr>
	  </#if>
      <#if noonTotal?exists>
	  <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;小计<br>
           &nbsp;&nbsp;占比<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="150"></td>
				<td align="left" width="130">&nbsp;</td>
				<td align="right" width="100">&nbsp;</td>
				<td align="right" width="100">&nbsp;</td>
				<td align="right" width="100">
				 ${noonTotal.heatEnergy}
                 <br>${noonTotalPercent.heatEnergy}%
				</td>
				<td align="right" width="100">
				${noonTotal.carbohydrate}
                <br>${noonTotalPercent.carbohydrate}%
				</td>
				<td align="right" width="100">
				${noonTotal.protein}
				<br>${noonTotalPercent.protein}%
				</td>
				<td align="right" width="100">
				${noonTotal.fat}
				<br>${noonTotalPercent.fat}%
				</td>
				<td align="right" width="100">
				${noonTotal.calcium}
				<br>${noonTotalPercent.calcium}%
				</td>
			  </tr>
			</table>
		  </td>
       </tr>
      </#if>
	  <#if dinnerList?exists>
	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;晚餐<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <#list dinnerList as r5>
			  <tr>
				<td  width="150">
				    <span class="dietary_title">${r5.name}</span>
				</td>
				<td  width="300">
					<table>
					<#list r5.items as item5>
					<tr>
					  <td align="left"  width="200">${item5.name}</td>
					  <td align="right" width="100">
					    <#if item5.name?exists>
					      <input type="text" id="item_${item5.id}" name="myInput" value="${item5.quantity2}"
						         size="5" class="xz_input">
						</#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="30">&nbsp;&nbsp;</td>
				<td align="right" width="100">${r5.heatEnergy}</td>
				<td align="right" width="100">${r5.carbohydrate}</td>
				<td align="right" width="100">${r5.protein}</td>
				<td align="right" width="100">${r5.fat}</td>
				<td align="right" width="100">${r5.calcium}</td>
			  </tr>
			  </#list>
			</table>
		  </td>
       </tr>
	   <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;小计<br>
           &nbsp;&nbsp;占比<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="150"></td>
				<td align="left" width="130">&nbsp;</td>
				<td align="right" width="100">&nbsp;</td>
				<td align="right" width="100">&nbsp;</td>
				<td align="right" width="100">
				 ${dietaryCount_dinner.heatEnergy}
                 <br>${dietaryCountPercent_dinner.heatEnergy}%
				</td>
				<td align="right" width="100">
				${dietaryCount_dinner.carbohydrate}
                <br>${dietaryCountPercent_dinner.carbohydrate}%
				</td>
				<td align="right" width="100">
				${dietaryCount_dinner.protein}
				<br>${dietaryCountPercent_dinner.protein}%
				</td>
				<td align="right" width="100">
				${dietaryCount_dinner.fat}
				<br>${dietaryCountPercent_dinner.fat}%
				</td>
				<td align="right" width="100">
				${dietaryCount_dinner.calcium}
				<br>${dietaryCountPercent_dinner.calcium}%
				</td>
			  </tr>
			</table>
		  </td>
       </tr>
	 </#if>

	  <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;合计<br>
		   &nbsp;&nbsp;标准<br>
		   &nbsp;&nbsp;占比<br>
		   &nbsp;&nbsp;评价<br>
		  </td>
		  <td width="1050" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="150"></td>
				<td align="left" width="130">&nbsp;</td>
				<td align="right" width="100">&nbsp;</td>
				<td align="right" width="100">&nbsp;</td>
				<td align="right" width="100">
				${dietaryCountSum.heatEnergy}
                <br>
				${foodDRI.heatEnergy}
				<br>
				${dayRptModel.heatEnergyPercent}%
				<br>
				${dayRptModel.heatEnergyEvaluate}
				</td>
				<td align="right" width="100">
				${dietaryCountSum.carbohydrate}
				<br>
				${foodDRI.carbohydrate}
				<br>
				${dayRptModel.carbohydratePercent}%
				<br>
				${dayRptModel.carbohydrateEvaluate}
				</td>
				<td align="right" width="100">
				${dietaryCountSum.protein}
				<br>
				${foodDRI.protein}
				<br>
				${dayRptModel.proteinPercent}%
				<br>
				${dayRptModel.proteinEvaluate}
				</td>
				<td align="right" width="100">
				${dietaryCountSum.fat}
				<br>
				${foodDRI.fat}
				<br>
				${dayRptModel.fatPercent}%
				<br>
				${dayRptModel.fatEvaluate}
				</td>
				<td align="right" width="100">
				${dietaryCountSum.calcium}
				<br>
				${foodDRI.calcium}
				<br>
				${dayRptModel.calciumPercent}%
				<br>
				${dayRptModel.calciumEvaluate}
				</td>
			  </tr>
			</table>
		  </td>
       </tr>
    </table>
    </td>
	<td height="98%" width="100%">
	 <table height="100%" width="100%">
	  <tbody>
	   <tr height="45%">
		<td>
		  <iframe id="x1" name="x1" style="width:420px; height:390px" frameborder="0" ></iframe>		  
        </td>
	   </tr>
	   <tr height="55%">
		<td>
		  <iframe id="x2" name="x2" style="width:420px; height:380px" frameborder="0"></iframe>
		</td>
	   </tr>
	  </tbody>
	 </table>
	</td>
   </tr>
  </table>
  <table>
   <tr>
	<td width="45%">
	    <iframe id="x3" name="x3" style="width:420px; height:480px" frameborder="0" ></iframe>
	</td>
	<td width="30%">
	    <iframe id="x4" name="x4" style="width:520px; height:480px" frameborder="0" ></iframe>
	</td>
	<td width="25%">
	</td>
   </tr>
  </table>
  <table>
   <tr>
	<td width="45%">
	    <iframe id="x5" name="x5" style="width:420px; height:480px" frameborder="0" ></iframe>
	</td>
	<td width="30%">
	    <iframe id="x6" name="x6" style="width:520px; height:480px" frameborder="0" ></iframe>
	</td>
	<td width="25%">
	</td>
   </tr>
  </table>
  <table>
   <tr>
	<td width="45%">
	    <iframe id="x7" name="x7" style="width:420px; height:480px" frameborder="0" ></iframe>
	</td>
	<td width="30%">
	    <iframe id="x8" name="x8" style="width:520px; height:480px" frameborder="0" ></iframe>
	</td>
	<td width="25%">
	</td>
   </tr>
  </table>
  </#if>
  <br>&nbsp;
  <br>&nbsp;
  <br>&nbsp;
 </div>   
</div>
 <br>&nbsp;
 <br>&nbsp;
 <br>&nbsp;
 <script type="text/javascript">
	jQuery(document).ready(function() { 

        $('#x1').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135903&suitNo=${suitNo}&type=heatEnergyX1PerDayDietary&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

		$('#x2').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135902&suitNo=${suitNo}&type=heatEnergyX2PerDayDietary&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

		$('#x3').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135907&suitNo=${suitNo}&type=heatEnergyX3PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

		$('#x4').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135302&suitNo=${suitNo}&type=proteinPercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

        $('#x5').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135904&suitNo=${suitNo}&type=vitaminAAnimalsX2PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

        $('#x6').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135905&suitNo=${suitNo}&type=ironAnimalsX2PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

		$('#x7').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135906&suitNo=${suitNo}&type=fatAnimalsX2PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

		$('#x8').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135908&suitNo=${suitNo}&type=calciumPhosphorusX2PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantId=${tenantId}&sysFlag=N');

        }); 
</script>	
</body>
</html>