<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱构成</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>

.table-border { background-color:#3399cc;  font-family:"宋体"}
.table-content { background-color:#ffffff; font-size: 12px; font-family:"宋体"}
.red { color:#ff0000;font-weight: bold;}
.blue { color:#3333ff;font-weight: bold;}

.x_y_title {
	text-transform: uppercase;
	background-color: inherit;
	color: #000033;
	font-size: 16px;
	font-weight: bold;
	text-align: center;
}

</style>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function doSubmit(){
        document.iForm.submit();
	}

	function switchDay(){
		document.iForm.submit();
	}

</script>
</head>

<body style="margin-left:5px;">

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:45px"> 
    <div class="toolbar-backgroud">
	  <form id="iForm" name="iForm" method="post">
	   <table width="100%" align="left">
		<tbody>
		 <tr>
		    <td width="15%" align="left">
			<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">食谱构成</span>
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
			<td>
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-ok'" 
	             onclick="javascript:doSubmit();" >确定</a>
			  &nbsp;
			</td>
		</tr>
	   </tbody>
	  </table>
	 </form>
    </div> 
  </div>
  <div data-options="region:'center',border:false,cache:true">
  <#if dayRptModel?exists>
    <table width="1280" height="99%" cellpadding='2' cellspacing='2' class="table-border" nowrap>
	  <tr>
	    <td colspan="6" align="center"  class="table-content">
		   <table border='0' cellpadding='0' cellspacing='0' >
		   <tr>
		     <td width="80%" align="center">
		       <span class="x_y_title">  ${fullDay}  帯  量  食  谱  </span>
		     </td>
		     <td width="20%" align="right">
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
		  <td width="1080" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0'   >
			  <tr>
				<td  width="180">&nbsp;食谱&nbsp;</td>
				<td  width="360">
					<table>
					<tr>
					  <td align="left"  width="240">&nbsp;食物&nbsp;</td>
					  <td align="right" width="120">&nbsp;重量&nbsp;</td>
					</tr>
					</table>
				</td>
				<td align="right" width="120">&nbsp;&nbsp;</td>
				<td align="right" width="120">&nbsp;热能(kcal)&nbsp;</td>
				<td align="right" width="120">&nbsp;碳水化合物(g)&nbsp;</td>
				<td align="right" width="120">&nbsp;蛋白质(g)&nbsp;</td>
				<td align="right" width="120">&nbsp;脂肪(g)&nbsp;</td>
				<td align="right" width="120">&nbsp;钙(mg)&nbsp;</td>
			  </tr>

			</table>
		  </td>
        </tr>
	  <#if breakfastTime?exists>
	    <tr>
		  <td width="50" class="table-content">
		  &nbsp;&nbsp;早餐<br>
		  </td>
		  <td width="1080" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0'   >
			  <#list breakfastList as r1>
			  <tr>
				<td  width="180">${r1.name}</td>
				<td  width="360">
					<table>
					<#list r1.items as item1>
					<tr>
					  <td align="left"  width="240">${item1.name}</td>
					  <td align="right" width="120">${item1.quantity2}</td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="120">&nbsp;&nbsp;</td>
				<td align="right" width="120">${r1.heatEnergy}</td>
				<td align="right" width="120">${r1.carbohydrate}</td>
				<td align="right" width="120">${r1.protein}</td>
				<td align="right" width="120">${r1.fat}</td>
				<td align="right" width="120">${r1.calcium}</td>
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
		  <td width="1080" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="180"></td>
				<td align="left" width="240">&nbsp;</td>
				<td align="right" width="120">&nbsp;</td>
				<td align="right" width="120">&nbsp;</td>
				<td align="right" width="120">
				 ${dietaryCount_breakfast.heatEnergy}
                 <br>${dietaryCountPercent_breakfast.heatEnergy}%
				</td>
				<td align="right" width="120">
				${dietaryCount_breakfast.carbohydrate}
                <br>${dietaryCountPercent_breakfast.carbohydrate}%
				</td>
				<td align="right" width="120">
				${dietaryCount_breakfast.protein}
				<br>${dietaryCountPercent_breakfast.protein}%
				</td>
				<td align="right" width="120">
				${dietaryCount_breakfast.fat}
				<br>${dietaryCountPercent_breakfast.fat}%
				</td>
				<td align="right" width="120">
				${dietaryCount_breakfast.calcium}
				<br>${dietaryCountPercent_breakfast.calcium}%
				</td>
			  </tr>
			</table>
		  </td>
       </tr>
	  </#if>
	  <#if breakfastMidTime?exists>
	    <tr>
		  <td class="table-content">
		  &nbsp;&nbsp;早点
		  </td>
		  <td width="1080" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0'  >
			  <#list breakfastMidList as r2>
			  <tr>
				<td  width="180">${r2.name}</td>
				<td  width="360">
					<table>
					<#list r2.items as item2>
					<tr>
					  <td align="left"  width="240">${item2.name}</td>
					  <td align="right" width="120">${item2.quantity2}</td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="120">&nbsp;&nbsp;</td>
				<td align="right" width="120">${r2.heatEnergy}</td>
				<td align="right" width="120">${r2.carbohydrate}</td>
				<td align="right" width="120">${r2.protein}</td>
				<td align="right" width="120">${r2.fat}</td>
				<td align="right" width="120">${r2.calcium}</td>
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
		  <td width="1080" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="180"></td>
				<td align="left" width="240">&nbsp;</td>
				<td align="right" width="120">&nbsp;</td>
				<td align="right" width="120">&nbsp;</td>
				<td align="right" width="120">
				 ${dietaryCount_breakfastMid.heatEnergy}
                 <br>${dietaryCountPercent_breakfastMid.heatEnergy}%
				</td>
				<td align="right" width="120">
				${dietaryCount_breakfast.carbohydrate}
                <br>${dietaryCountPercent_breakfastMid.carbohydrate}%
				</td>
				<td align="right" width="120">
				${dietaryCount_breakfast.protein}
				<br>${dietaryCountPercent_breakfastMid.protein}%
				</td>
				<td align="right" width="120">
				${dietaryCount_breakfast.fat}
				<br>${dietaryCountPercent_breakfastMid.fat}%
				</td>
				<td align="right" width="120">
				${dietaryCount_breakfast.calcium}
				<br>${dietaryCountPercent_breakfastMid.calcium}%
				</td>
			   </tr>
			  </table>
		  </td>
       </tr>
	  </#if>
	  <#if lunchTime?exists>
	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;午餐<br>
		  </td>
		  <td width="1080" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <#list lunchList as r3>
			  <tr>
				<td  width="180">${r3.name}</td>
				<td  width="360">
					<table>
					<#list r3.items as item3>
					<tr>
					  <td align="left"  width="240">${item3.name}</td>
					  <td align="right" width="120">${item3.quantity2}</td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="120">&nbsp;&nbsp;</td>
				<td align="right" width="120">${r3.heatEnergy}</td>
				<td align="right" width="120">${r3.carbohydrate}</td>
				<td align="right" width="120">${r3.protein}</td>
				<td align="right" width="120">${r3.fat}</td>
				<td align="right" width="120">${r3.calcium}</td>
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
		  <td width="1080" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="180"></td>
				<td align="left" width="240">&nbsp;</td>
				<td align="right" width="120">&nbsp;</td>
				<td align="right" width="120">&nbsp;</td>
				<td align="right" width="120">
				 ${dietaryCount_lunch.heatEnergy}
                 <br>${dietaryCountPercent_lunch.heatEnergy}%
				</td>
				<td align="right" width="120">
				${dietaryCount_lunch.carbohydrate}
                <br>${dietaryCountPercent_lunch.carbohydrate}%
				</td>
				<td align="right" width="120">
				${dietaryCount_lunch.protein}
				<br>${dietaryCountPercent_lunch.protein}%
				</td>
				<td align="right" width="120">
				${dietaryCount_lunch.fat}
				<br>${dietaryCountPercent_lunch.fat}%
				</td>
				<td align="right" width="120">
				${dietaryCount_lunch.calcium}
				<br>${dietaryCountPercent_lunch.calcium}%
				</td>
			  </tr>
			</table>
		  </td>
       </tr>
	  </#if>
	  <#if snackTime?exists>
	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;午点<br>
		  </td>
		  <td width="1080" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <#list snackList as r4>
			  <tr>
				<td  width="180">${r4.name}</td>
				<td  width="360">
					<table>
					<#list r4.items as item4>
					<tr>
					  <td align="left"  width="240">${item4.name}</td>
					  <td align="right" width="120">${item4.quantity2}</td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="120">&nbsp;&nbsp;</td>
				<td align="right" width="120">${r4.heatEnergy}</td>
				<td align="right" width="120">${r4.carbohydrate}</td>
				<td align="right" width="120">${r4.protein}</td>
				<td align="right" width="120">${r4.fat}</td>
				<td align="right" width="120">${r4.calcium}</td>
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
		  <td width="1080" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="180"></td>
				<td align="left" width="240">&nbsp;</td>
				<td align="right" width="120">&nbsp;</td>
				<td align="right" width="120">&nbsp;</td>
				<td align="right" width="120">
				 ${dietaryCount_snack.heatEnergy}
                 <br>${dietaryCountPercent_snack.heatEnergy}%
				</td>
				<td align="right" width="120">
				${dietaryCount_snack.carbohydrate}
                <br>${dietaryCountPercent_snack.carbohydrate}%
				</td>
				<td align="right" width="120">
				${dietaryCount_snack.protein}
				<br>${dietaryCountPercent_snack.protein}%
				</td>
				<td align="right" width="120">
				${dietaryCount_snack.fat}
				<br>${dietaryCountPercent_snack.fat}%
				</td>
				<td align="right" width="120">
				${dietaryCount_snack.calcium}
				<br>${dietaryCountPercent_snack.calcium}%
				</td>
			  </tr>
			</table>
		  </td>
       </tr>
	  </#if>
	  <#if dinnerTime?exists>
	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;晚餐<br>
		  </td>
		  <td width="1080" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <#list dinnerList as r5>
			  <tr>
				<td  width="180">${r5.name}</td>
				<td  width="360">
					<table>
					<#list r5.items as item5>
					<tr>
					  <td align="left"  width="240">${item5.name}</td>
					  <td align="right" width="120">${item5.quantity2}</td>
					</tr>
					</#list>
					</table>
				</td>
				<td align="right" width="120">&nbsp;&nbsp;</td>
				<td align="right" width="120">${r5.heatEnergy}</td>
				<td align="right" width="120">${r5.carbohydrate}</td>
				<td align="right" width="120">${r5.protein}</td>
				<td align="right" width="120">${r5.fat}</td>
				<td align="right" width="120">${r5.calcium}</td>
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
		  <td width="1080" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="180"></td>
				<td align="left" width="240">&nbsp;</td>
				<td align="right" width="120">&nbsp;</td>
				<td align="right" width="120">&nbsp;</td>
				<td align="right" width="120">
				 ${dietaryCount_dinner.heatEnergy}
                 <br>${dietaryCountPercent_dinner.heatEnergy}%
				</td>
				<td align="right" width="120">
				${dietaryCount_dinner.carbohydrate}
                <br>${dietaryCountPercent_dinner.carbohydrate}%
				</td>
				<td align="right" width="120">
				${dietaryCount_dinner.protein}
				<br>${dietaryCountPercent_dinner.protein}%
				</td>
				<td align="right" width="120">
				${dietaryCount_dinner.fat}
				<br>${dietaryCountPercent_dinner.fat}%
				</td>
				<td align="right" width="120">
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
		  <td width="1080" class="table-content">
			<table border='0' cellpadding='0' cellspacing='0' >
			  <tr>
				<td width="180"></td>
				<td align="left" width="240">&nbsp;</td>
				<td align="right" width="120">&nbsp;</td>
				<td align="right" width="120">&nbsp;</td>
				<td align="right" width="120">
				${dietaryCountSum.heatEnergy}
                <br>
				${foodDRI.heatEnergy}
				<br>
				${dayRptModel.heatEnergyPercent}%
				<br>
				${dayRptModel.heatEnergyEvaluate}
				</td>
				<td align="right" width="120">
				${dietaryCountSum.carbohydrate}
				<br>
				${foodDRI.carbohydrate}
				<br>
				${dayRptModel.carbohydratePercent}%
				<br>
				${dayRptModel.carbohydrateEvaluate}
				</td>
				<td align="right" width="120">
				${dietaryCountSum.protein}
				<br>
				${foodDRI.protein}
				<br>
				${dayRptModel.proteinPercent}%
				<br>
				${dayRptModel.proteinEvaluate}
				</td>
				<td align="right" width="120">
				${dietaryCountSum.fat}
				<br>
				${foodDRI.fat}
				<br>
				${dayRptModel.fatPercent}%
				<br>
				${dayRptModel.fatEvaluate}
				</td>
				<td align="right" width="120">
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
   </#if>
   <br>&nbsp;
   <br>&nbsp;
   <br>&nbsp;
  </div>
</div>

</body>
</html>