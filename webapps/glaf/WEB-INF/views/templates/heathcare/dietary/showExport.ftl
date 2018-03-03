<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导出食谱</title>
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

.dietary_title {
	height: 20px;
	line-height: 20px;
	text-align: center;
	font: bold 13px 宋体;
	color: #484848;
	cursor: pointer;
}

</style>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function exportWeek(){
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
		window.open('${contextPath}/heathcare/dietaryExport/exportWeek?exportType=xls&year='+year+'&week='+week); 
	}

	function modifyItem(id){
		var quantity = jQuery("#item_"+id).val();
		//alert(id + "=" + quantity);
		var link="${contextPath}/heathcare/dietaryItem/updateQuantity?id="+id+"&quantity="+quantity;
        jQuery.ajax({
				   type: "POST",
				   url: link,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					  
				   }
			 });
	}

    function editItems(dietaryId) {
		var link = "${contextPath}/heathcare/dietaryItem/datalist?dietaryId="+dietaryId;
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "食物构成",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['880px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
		});
	}

	function execute(){
		var form = document.getElementById("iForm");
	    var link = "${contextPath}/heathcare/dietaryStatistics/execute";
	    var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: link,
				   dataType: 'json',
				   data: params,
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
					   window.location.reload();
				   }
			 });
	}

	function doSubmit(){
        document.iForm.submit();
	}

 	function exportNutrition(){
        var year = jQuery("#year").val();
        var week = jQuery("#week").val();
		if(year == ""){
            alert('请选择年份！');
			return;
		}
		if(week == ""){
            alert('请选择周次！');
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=WeeklyDietaryNutritionCount";
		if(year != ""){
			link = link + "&year=" + year;
		}
		if(week != ""){
			link = link  + "&week="+week;
		}
        window.open(link);
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:false" style="height:45px"> 
    <div class="toolbar-backgroud">
	  <form id="iForm" name="iForm" method="post">
	   <table width="100%" align="left">
		<tbody>
		 <tr>
		    <td width="10%" align="left">
			<img src="${contextPath}/static/images/window.png"><span class="x_content_title">导出食谱</span>
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
			</td>
			<td width="45%">
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-ok'" 
	             onclick="javascript:doSubmit();" >确定</a>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
	             onclick="javascript:exportWeek();" >导出</a>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
	             onclick="javascript:exportNutrition();" >营养成分统计表</a>
			  <#if dietary_copy_add_perm == true>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-formula'" 
	             onclick="javascript:execute();" >统计成分</a>
			  </#if>
			</td>
		</tr>
	   </tbody>
	  </table>
	 </form>
    </div> 
  </div>
  <div data-options="region:'center',border:false,cache:true">
  <#if weekList?exists>
    <table width="99%" height="99%" cellpadding='2' cellspacing='2' class="table-border" nowrap>
	  <tr>
	    <td colspan="6" align="center"  class="table-content">
		   <span class="x_y_title">  ${year} 第 ${week} 周  帯  量  食  谱 </span>
		</td>
	  </tr>
	  <tr>
	    <td colspan="6" align="right"  class="table-content" height="15">
		   <span>&nbsp;配餐均龄：4岁  &nbsp;&nbsp; 一人可食均量：克&nbsp;</span>
		</td>
	  </tr>
	  <tr>
		  <td width="5%" class="table-content"  align="center" >
		  餐别及<br>
		  用餐时间
		  </td>
		  <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border">
			  <tr>
				<td colspan="3" align="center" class="table-content">
				${wkdata.weekName}
				</td>
			  </tr>
			  <tr>
				<td width="40%" class="table-content">&nbsp;食谱&nbsp;</td>
				<td width="40%" class="table-content">&nbsp;食物&nbsp;</td>
				<td width="20%" align="right" class="table-content">&nbsp;重量&nbsp;</td>
			  </tr>
			  </table>
		  </td>
		  </#list>
        </tr>
	  <#if breakfastTime?exists>
	    <tr>
		  <td width="5%" class="table-content">
		  &nbsp;&nbsp;早餐<br>
		  （${breakfastTime}）
		  </td>
		  <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.breakfastList as r1>
			  <tr>
				<td valign="top" width="40%">
				    <span class="dietary_title" onclick="javascript:editItems('${r1.dietary.id}');">${r1.name}</span>
				</td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%">${item.name}</td>
					  <td align="right" width="10%">
					   <#if item.name?exists>
					    <input type="text" id="item_${item.id}" name="myInput" value="${item.quantity2}"
						       onchange="javascript:modifyItem('${item.id}');" size="5" class="xz_input">
					   </#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
			  </tr>
			  </#list>
			  </table>
		  </td>
		  </#list>
        </tr>
	  </#if>
	  <#if breakfastMidTime?exists>
	    <tr>
		  <td class="table-content">
		  &nbsp;&nbsp;早点
		  <br>（${breakfastMidTime}）
		  </td>
          <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.breakfastMidList as r1>
			  <tr>
				<td valign="top" width="40%">
				    <span class="dietary_title" onclick="javascript:editItems('${r1.dietary.id}');">${r1.name}</span>
				</td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%">${item.name}</td>
					  <td align="right" width="10%">
					   <#if item.name?exists>
					    <input type="text" id="item_${item.id}" name="myInput" value="${item.quantity2}"
						       onchange="javascript:modifyItem('${item.id}');" size="5" class="xz_input">
					   </#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
			  </tr>
			  </#list>
			  </table>
		  </td>
		  </#list>
        </tr>
	  </#if>
	  <#if lunchTime?exists>
	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;午餐<br>
		  （${lunchTime}）
		  </td>
          <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.lunchList as r1>
			  <tr>
				<td valign="top" width="40%">
				    <span class="dietary_title" onclick="javascript:editItems('${r1.dietary.id}');">${r1.name}</span>
				</td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%">${item.name}</td>
					  <td align="right" width="10%">
					   <#if item.name?exists>
					    <input type="text" id="item_${item.id}" name="myInput" value="${item.quantity2}"
						       onchange="javascript:modifyItem('${item.id}');" size="5" class="xz_input">
					   </#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
			  </tr>
			  </#list>
			  </table>
		  </td>
		  </#list>
        </tr>
	  </#if>
	  <#if snackTime?exists>
	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;午点<br>
		  （${snackTime}）
		  </td>
          <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.snackList as r1>
			  <tr>
				<td valign="top" width="40%">
				    <span class="dietary_title" onclick="javascript:editItems('${r1.dietary.id}');">${r1.name}</span>
				</td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%">${item.name}</td>
					  <td align="right" width="10%">
					   <#if item.name?exists>
					    <input type="text" id="item_${item.id}" name="myInput" value="${item.quantity2}"
						       onchange="javascript:modifyItem('${item.id}');" size="5" class="xz_input">
					   </#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
			  </tr>
			  </#list>
			  </table>
		  </td>
		  </#list>
        </tr>
	  </#if>
	  <#if dinnerTime?exists>
	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;晚餐<br>
		  （${dinnerTime}）
		  </td>
          <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.dinnerList as r1>
			  <tr>
				<td valign="top" width="40%">
				    <span class="dietary_title" onclick="javascript:editItems('${r1.dietary.id}');">${r1.name}</span>
				</td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%">${item.name}</td>
					  <td align="right" width="10%">
					   <#if item.name?exists>
					    <input type="text" id="item_${item.id}" name="myInput" value="${item.quantity2}"
						       onchange="javascript:modifyItem('${item.id}');" size="5" class="xz_input">
					   </#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
			  </tr>
			  </#list>
			  </table>
		  </td>
		  </#list>
       </tr>
	 </#if>
	   <tr>
		  <td class="table-content">
		   <table border='0' cellpadding='0' cellspacing='0' width="100%" height="100%" class="table-border">
		   <tr>
			<td rowspan="5" class="table-content" height="60">
			<br>
			当日<br>
		    营养<br>
		    分析<br>
			<br>
			</td>
			<td>
             <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border">
             <tr>
				<td class="table-content" height="12">营养</td>
             </tr>
             <tr>
				<td class="table-content" height="12">标准</td>
             </tr>
             <tr>
				<td class="table-content" height="12">实际</td>
             </tr>
             <tr>
				<td class="table-content" height="12">实给%</td>
             </tr>
			 <tr>
				<td class="table-content" height="12">评价</td>
             </tr>
             </table>
			</td>
		   </tr>
		   </table>
		  </td>
          <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border" align="center">
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">&nbsp;热能(kcal)</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">蛋白质(g)</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">钙(mg)</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${foodDRI.heatEnergy}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${foodDRI.protein}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${foodDRI.calcium}</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${wkdata.heatEnergy}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${wkdata.protein}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${wkdata.calcium}</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${wkdata.heatEnergyPercent}%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${wkdata.proteinPercent}%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${wkdata.calciumPercent}%</td>	
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${wkdata.heatEnergyEvaluate}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${wkdata.proteinEvaluate}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${wkdata.calciumEvaluate}</td>
			  </tr>
			  </table>
		  </td>
		  </#list>
       </tr>
	   <tr>
		  <td class="table-content">
		   <table border='0' cellpadding='0' cellspacing='0' width="100%" height="100%" class="table-border">
		   <tr>
			<td rowspan="5" class="table-content" height="60">
			一周<br>
			日均<br>
		    营养<br>
		    分析<br>
			<br>
			</td>
			<td height="60">
             <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border">
             <tr>
				<td class="table-content" height="12" valign="top">营养</td>
             </tr>
             <tr>
				<td class="table-content" height="12" valign="top">标准</td>
             </tr>
             <tr>
				<td class="table-content" height="12" valign="top">实际</td>
             </tr>
             <tr>
				<td class="table-content" height="12" valign="top">实给%</td>
             </tr>
			 <tr>
				<td class="table-content" height="12" valign="top">评价</td>
             </tr>
             </table>
			</td>
		   </tr>
		   </table>
		  </td>
		  <td width="18%" class="table-content">
			 <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border" align="center">
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">&nbsp;总热能(kcal)</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">碳水热</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">脂肪热</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${foodDRI.heatEnergy}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">50-65%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">20-35%</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekAgv.heatEnergy}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.heatEnergyCarbohydrate}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.heatEnergyFat}</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekRptModel.heatEnergyPercent}%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.heatEnergyCarbohydratePercent}%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.heatEnergyFatPercent}%</td>	
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekRptModel.heatEnergyEvaluate}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.heatEnergyCarbohydrateEvaluate}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.heatEnergyFatEvaluate}</td>
			  </tr>
			 </table>
		  </td>
		  <td width="18%" class="table-content">
			 <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border" align="center">
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">蛋白质总量(g)</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">动物类</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">动豆类</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${foodDRI.protein}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">30%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">50%</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekAgv.protein}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.proteinAnimal}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.proteinAnimalBeans}</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekRptModel.proteinPercent}%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.proteinAnimalPercent}%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.proteinAnimalBeansPercent}%</td>	
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekRptModel.proteinEvaluate}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.proteinAnimalEvaluate}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.proteinAnimalBeansEvaluate}</td>
			  </tr>
			 </table>
		  </td>
		  <td width="18%" class="table-content">
			 <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border" align="center">
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">&nbsp;V-A(ug)</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">V-B1(mg)</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">V-B2(mg)</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${foodDRI.vitaminA}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${foodDRI.vitaminB1}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${foodDRI.vitaminB2}</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekAgv.vitaminA}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekAgv.vitaminB1}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekAgv.vitaminB2}</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekRptModel.vitaminAPercent}%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.vitaminB1Percent}%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.vitaminB2Percent}%</td>	
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekRptModel.vitaminAEvaluate}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.vitaminB1Evaluate}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.vitaminB2Evaluate}</td>
			  </tr>
			 </table>
		  </td>
		  <td width="18%" class="table-content">
			 <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border" align="center">
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">&nbsp;V-C(mg)</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">尼克酸(mg)</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">钙(mg)</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${foodDRI.vitaminC}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${foodDRI.nicotinicCid}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${foodDRI.calcium}</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekAgv.vitaminC}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekAgv.nicotinicCid}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekAgv.calcium}</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekRptModel.vitaminCPercent}%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.nicotinicCidPercent}%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.calciumPercent}%</td>	
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekRptModel.vitaminCEvaluate}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.nicotinicCidEvaluate}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.calciumEvaluate}</td>
			  </tr>
			 </table>
		  </td>
		  <td width="18%" class="table-content">
			 <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border" align="center">
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">&nbsp;铁(mg)</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">锌(mg)</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">磷(mg)</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${foodDRI.iron}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${foodDRI.zinc}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${foodDRI.phosphorus}</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekAgv.iron}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekAgv.zinc}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekAgv.phosphorus}</td>
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekRptModel.ironPercent}%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.zincPercent}%</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.phosphorusPercent}%</td>	
			  </tr>
			  <tr>
				<td valign="top" height="12" width="40%" class="table-content" align="center">${weekRptModel.ironEvaluate}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.zincEvaluate}</td>
				<td valign="top" height="12" width="30%" class="table-content" align="center">${weekRptModel.phosphorusEvaluate}</td>
			  </tr>
			 </table>
		  </td>
       </tr>
     <!-- <tr>
	    <td colspan="6" align="right"  class="table-content" height="15">
		  &nbsp;
		</td>
	  </tr> -->
    </table>
   </#if>
   <br>&nbsp;
   <br>&nbsp;
   <table width="99%" height="99%" border="0">
   <tr>
	<td width="50%">
	    <iframe id="x1" name="x1" style="width:380px; height:380px" frameborder="0" ></iframe>
	</td>
	<td width="50%">
	    <iframe id="x2" name="x2" style="width:380px; height:380px" frameborder="0"></iframe>
	</td>
   </tr>
   <tr>
	<td width="50%">
	    <iframe id="x3" name="x3" style="width:380px; height:380px" frameborder="0" ></iframe>
	</td>
	<td width="50%">
	    <iframe id="x4" name="x4" style="width:420px; height:480px" frameborder="0" ></iframe>
	</td>
   </tr>
   <tr>
	<td width="50%">
	    <iframe id="x5" name="x5" style="width:380px; height:380px" frameborder="0" ></iframe>
	</td>
	<td width="50%">
	    <iframe id="x6" name="x6" style="width:420px; height:480px" frameborder="0" ></iframe>
	</td>
   </tr>
   <tr>
	<td width="50%">
	    <iframe id="x7" name="x7" style="width:380px; height:380px" frameborder="0" ></iframe>
	</td>
	<td width="50%">
	    <iframe id="x8" name="x8" style="width:420px; height:480px" frameborder="0" ></iframe>
	</td>
   </tr>
   </table>
  </div>
</div>
 <script type="text/javascript">
  <#if week?exists>
	jQuery(document).ready(function() { 

        $('#x1').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=235903&type=heatEnergyX1PerDayDietaryOfWeekTenant&week=${week}&year=${year}&semester=${semester}&tenantCorrelation=true&sysFlag=N');

		$('#x2').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=235902&type=heatEnergyX2PerDayDietaryOfWeekTenant&week=${week}&year=${year}&semester=${semester}&tenantCorrelation=true&sysFlag=N');

		$('#x3').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=235907&type=heatEnergyX3PercentPerDayDietaryOfWeekTenant&week=${week}&year=${year}&semester=${semester}&tenantCorrelation=true&sysFlag=N');

		$('#x4').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=235302&type=proteinPercentPerDayDietaryOfWeekTenant&week=${week}&year=${year}&semester=${semester}&tenantCorrelation=true&sysFlag=N');

		$('#x5').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=235904&type=vitaminAAnimalsX3PercentPerDayDietaryOfWeekTenant&week=${week}&year=${year}&semester=${semester}&tenantCorrelation=true&sysFlag=N');

		$('#x6').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=235905&type=ironAnimalsX3PercentPerDayDietaryOfWeekTenant&week=${week}&year=${year}&semester=${semester}&tenantCorrelation=true&sysFlag=N');

		$('#x7').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=235906&type=fatAnimalsX3PercentPerDayDietaryOfWeekTenant&week=${week}&year=${year}&semester=${semester}&tenantCorrelation=true&sysFlag=N');

		$('#x8').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=235908&type=calciumPhosphorusX3PercentOfWeekTenant&week=${week}&year=${year}&semester=${semester}&tenantCorrelation=true&sysFlag=N');

        }); 

   </#if>
</script>	
</body>
</html>