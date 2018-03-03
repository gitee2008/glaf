<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱模板成分</title>
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
<script type="text/javascript">

 
	function doSubmit(){
		document.iForm.action="${contextPath}/heathcare/dietaryTemplateExport/showDayExport";
        document.iForm.submit();
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

	function modifyItem(id){
		var quantity = jQuery("#item_"+id).val();
		//alert(id + "=" + quantity);
		var link="${contextPath}/heathcare/dietaryItem/updateTemplateQuantity?id="+id+"&quantity="+quantity;
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

    function editItems(id) {
		var link = "${contextPath}/heathcare/dietaryItem?templateId="+id;
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
 
 	function exportNutrition(){
        var sysFlag = jQuery("#sysFlag").val();
        var suitNo = jQuery("#suitNo").val();
		var dayOfWeek = jQuery("#dayOfWeek").val();
		if(suitNo == ""){
            alert('请选择模板序号！');
			return;
		}
		if(dayOfWeek == ""){
            alert('请选择模板日期！');
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=DietaryTemplateMultiAreaNutritionCount";
		if(sysFlag != ""){
			link = link + "&sysFlag=" + sysFlag;
		}
		if(suitNo != ""){
			link = link  + "&suitNo="+suitNo;
		}
		if(dayOfWeek != ""){
			link = link  + "&dayOfWeek="+dayOfWeek;
		}
        window.open(link);
	}
</script>
</head>
<body style="margin-left:5px;">
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:false" style="height:48px"> 
    <div class="toolbar-backgroud">
	  <form id="iForm" name="iForm" method="post">
	   <table width="100%" align="left">
		<tbody>
		 <tr>
		    <td width="15%" align="left">
			<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;食谱模板成分</span>
			</td>
			<td width="45%" align="left">
			  &nbsp;类型&nbsp;
			  <select id="sysFlag" name="sysFlag">
				<option value="">----请选择----</option> 
				<option value="N">我自己的</option>
				<option value="Y">系统内置</option>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("sysFlag").value="${sysFlag}";
			  </script>
			  &nbsp;餐点&nbsp;
			  <#if typeDict?exists>
			  <span style="color:#0066ff;font-weight:bold;">${typeDict.name}</span>
			  <input type="hidden" id="typeId" name="typeId" value="${typeDict.id}">
			  <#else>
			  <select id="typeId" name="typeId">
				<option value="">----请选择----</option>
				<#list dictoryList as d>
				<option value="${d.id}">${d.name}</option>
				</#list> 
			  </select>
			  <script type="text/javascript">
				 document.getElementById("typeId").value="${typeId}";
			  </script>
			  </#if>
			  &nbsp;序号&nbsp;
			  <select id="suitNo" name="suitNo">
				<#list suitNos as suitNo>
				<option value="${suitNo}">${suitNo}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("suitNo").value="${suitNo}";
			  </script>
			  &nbsp;日期&nbsp;
			  <select id="dayOfWeek" name="dayOfWeek" >
				<option value="1">周一</option>
				<option value="2">周二</option>
				<option value="3">周三</option>
				<option value="4">周四</option>
				<option value="5">周五</option>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("dayOfWeek").value="${dayOfWeek}";
			  </script>
			</td>
			<td width="2%"></td>
			<td width="38%" align="left">
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-ok'" 
	             onclick="javascript:doSubmit();" >确定</a>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
	             onclick="javascript:exportNutrition();" >营养成分统计表</a>
			  <#if dietary_execute_perm == true>
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
		       <span class="x_y_title">  帯  量  食  谱  模  板 </span>
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
				<td  width="180">&nbsp;食谱&nbsp;</td>
				<td  width="250">
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
				<td  width="180">
				   <span class="dietary_title" 
					       onclick="javascript:editItems('${r1.dietaryTemplate.id}');">${r1.name}</span>&nbsp;
				   <span><img src="${contextPath}/static/images/static.gif" 
				        onclick="javascript:editItems('${r1.dietaryTemplate.id}');" style="cursor:pointer;"></span>
				</td>
				<td  width="250">
					<table>
					<#list r1.items as item1>
					<tr>
					  <td align="left"  width="200">${item1.name}</td>
					  <td align="right" width="100">
					   <#if item1.name?exists>
						<input type="text" id="item_${item1.id}" name="myInput" value="${item1.quantity2}"
						       onchange="javascript:modifyItem('${item1.id}');" size="5" class="xz_input">
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
				<td  width="180">
                   <span class="dietary_title" 
					       onclick="javascript:editItems('${r2.dietaryTemplate.id}');">${r2.name}</span>&nbsp;
				   <span><img src="${contextPath}/static/images/static.gif" 
				        onclick="javascript:editItems('${r2.dietaryTemplate.id}');" style="cursor:pointer;"></span>
				</td>
				<td  width="250">
					<table>
					<#list r2.items as item2>
					<tr>
					  <td align="left"  width="200">${item2.name}</td>
					  <td align="right" width="100">
					   <#if item2.name?exists>
						<input type="text" id="item_${item2.id}" name="myInput" value="${item2.quantity2}"
						       onchange="javascript:modifyItem('${item2.id}');" size="5" class="xz_input">
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
				<td width="180"></td>
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
				<td  width="180"> 
                   <span class="dietary_title" 
					       onclick="javascript:editItems('${r3.dietaryTemplate.id}');">${r3.name}</span>&nbsp;
				   <span><img src="${contextPath}/static/images/static.gif" 
				        onclick="javascript:editItems('${r3.dietaryTemplate.id}');" style="cursor:pointer;"></span>
				</td>
				<td  width="250">
					<table>
					<#list r3.items as item3>
					<tr>
					  <td align="left"  width="200">${item3.name}</td>
					  <td align="right" width="100">
					   <#if item3.name?exists>
						<input type="text" id="item_${item3.id}" name="myInput" value="${item3.quantity2}"
						       onchange="javascript:modifyItem('${item3.id}');" size="5" class="xz_input">
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
				<td  width="180">
                   <span class="dietary_title" 
					       onclick="javascript:editItems('${r4.dietaryTemplate.id}');">${r4.name}</span>&nbsp;
				   <span><img src="${contextPath}/static/images/static.gif" 
				        onclick="javascript:editItems('${r4.dietaryTemplate.id}');" style="cursor:pointer;"></span>
				</td>
				<td  width="250">
					<table>
					<#list r4.items as item4>
					<tr>
					  <td align="left"  width="200">${item4.name}</td>
					  <td align="right" width="100">
					   <#if item4.name?exists>
						<input type="text" id="item_${item4.id}" name="myInput" value="${item4.quantity2}"
						       onchange="javascript:modifyItem('${item4.id}');" size="5" class="xz_input">
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
				<td width="180"></td>
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
				<td  width="180">
                   <span class="dietary_title" 
					       onclick="javascript:editItems('${r5.dietaryTemplate.id}');">${r5.name}</span>&nbsp;
				   <span><img src="${contextPath}/static/images/static.gif" 
				        onclick="javascript:editItems('${r5.dietaryTemplate.id}');" style="cursor:pointer;"></span>
				</td>
				<td  width="250">
					<table>
					<#list r5.items as item5>
					<tr>
					  <td align="left"  width="200">${item5.name}</td>
					  <td align="right" width="100">
					   <#if item5.name?exists>
						<input type="text" id="item_${item5.id}" name="myInput" value="${item5.quantity2}"
						       onchange="javascript:modifyItem('${item5.id}');" size="5" class="xz_input">
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
				<td width="180"></td>
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
				<td width="180"></td>
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
		  <iframe id="x1" name="x1" style="width:380px; height:380px" frameborder="0" ></iframe>		  
        </td>
	   </tr>
	   <tr height="55%">
		<td>
		  <iframe id="x2" name="x2" style="width:380px; height:380px" frameborder="0"></iframe>
		</td>
	   </tr>
	  </tbody>
	 </table>
	</td>
   </tr>
  </table>
  <table valign="top">
   <tr>
	<td width="45%">
	    <iframe id="x3" name="x3" style="width:420px; height:450px" frameborder="0" ></iframe>
	</td>
	<td width="30%">
	    <iframe id="x4" name="x4" style="width:520px; height:450px" frameborder="0" ></iframe>
	</td>
	<td width="25%">
	</td>
   </tr>
  </table>
  <table valign="top">
   <tr>
	<td width="45%">
	    <iframe id="x5" name="x5" style="width:420px; height:450px" frameborder="0" ></iframe>
	</td>
	<td width="30%">
	    <iframe id="x6" name="x6" style="width:520px; height:450px" frameborder="0" ></iframe>
	</td>
	<td width="25%">
	</td>
   </tr>
  </table>
  <table valign="top">
   <tr>
	<td width="45%">
	    <iframe id="x7" name="x7" style="width:420px; height:450px" frameborder="0" ></iframe>
	</td>
	<td width="30%">
	    <iframe id="x8" name="x8" style="width:520px; height:450px" frameborder="0" ></iframe>
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

		$('#x1').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135903&suitNo=${suitNo}&type=heatEnergyX1PerDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x2').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135902&suitNo=${suitNo}&type=heatEnergyX2PerDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

        $('#x3').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135907&suitNo=${suitNo}&type=heatEnergyX3PercentPerDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x4').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135302&suitNo=${suitNo}&type=proteinPercentPerDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');


        $('#x5').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135904&suitNo=${suitNo}&type=vitaminAAnimalsX3PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

        $('#x6').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135905&suitNo=${suitNo}&type=ironAnimalsX3PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x7').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135906&suitNo=${suitNo}&type=fatAnimalsX3PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x8').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135908&suitNo=${suitNo}&type=calciumPhosphorusX3PercentPerDayDietary&dayOfWeek=${dayOfWeek}&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

        }); 
</script>	
</body>
</html>