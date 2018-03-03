<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱模板</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>

.table-border { background-color:#3399cc; height: 32px; font-family:"宋体"}
.table-content { background-color:#ffffff; height: 32px;font-size: 12px; font-family:"宋体"}

.x_y_title {
	text-transform: uppercase;
	background-color: inherit;
	color: #000033;
	font-size: 16px;
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

	function doExport(){
		if(document.getElementById("suitNo").value==""){
			alert("请选择模板。");
			document.getElementById("suitNo").focus();
			return;
		}
		var sysFlag = document.getElementById("sysFlag").value;
		//var year = document.getElementById("year").value;
		var suitNo = document.getElementById("suitNo").value;
		var link = '${contextPath}/heathcare/dietaryTemplateExport/export?exportType=xls&suitNo='+suitNo;
		    link = link+'&sysFlag='+sysFlag;
		window.open(link); 
	}

	function doSubmit(){
		document.iForm.action="${contextPath}/heathcare/dietaryTemplateExport/showExport";
        document.iForm.submit();
	}

	function doCopyX(){
		var suitNo = document.getElementById("suitNo").value;
		if(confirm("模板复制后不能删除,确定要复制第"+suitNo+"作为新模板吗?")){
			var sysFlag = document.getElementById("sysFlag").value;
            var link = '${contextPath}/heathcare/dietaryTemplateExport/copyTemplates?suitNo='+suitNo;
		    link = link+'&sysFlag='+sysFlag;
		    location.href=link; 
		}
	}

	function batchAdd(){
		var suitNo = document.getElementById("suitNo").value;
		var sysFlag = document.getElementById("sysFlag").value;
		var link="${contextPath}/heathcare/dietaryTemplate/batchAdd?suitNo="+suitNo+"&sysFlag="+sysFlag;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "批量增加",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px', ''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
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
		if(suitNo == ""){
            alert('请选择模板序号！');
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=WeeklyDietaryTemplateNutritionCount";
		if(sysFlag != ""){
			link = link + "&sysFlag=" + sysFlag;
		}
		if(suitNo != ""){
			link = link  + "&suitNo="+suitNo;
		}
        window.open(link);
	}

</script>
</head>

<body>
<div style="margin-left:5px;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:false" style="height:48px"> 
    <div class="toolbar-backgroud">
	  <form id="iForm" name="iForm" method="post">
	   <table width="99%" align="left">
		<tbody>
		 <tr>
		    <td align="left">
			<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;食谱模板</span>
			</td>
			<td width="25%" align="left">
			  &nbsp;类型&nbsp;
			  <select id="sysFlag" name="sysFlag">
				<option value="">----请选择----</option> 
				<option value="N">我自己的</option>
				<option value="Y">系统内置</option>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("sysFlag").value="${sysFlag}";
			  </script>
			  &nbsp;序号&nbsp;
			  <select id="suitNo" name="suitNo">
				<#list suitNos as suitNo>
				<option value="${suitNo}">${suitNo}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("suitNo").value="${suitNo}";
			  </script>
			</td>
			<td width="10%"><span style="font-size: 13px;font-weight: bold;color: #ff0033">${copy_msg}</span></td>
			<td width="45%" >
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-ok'" 
	             onclick="javascript:doSubmit();" >确定</a>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
	             onclick="javascript:doExport();" >导出</a>
			   &nbsp;
			  <#if dietary_copy_add_perm == true>
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	             onclick="javascript:doCopyX();" >复制</a>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
	             onclick="javascript:batchAdd();" >批量加入</a>
			  </#if>
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
  <#if weekList?exists>
    <table width="99%" height="99%" cellpadding='2' cellspacing='2' class="table-border" nowrap>
	  <tr>
	    <td colspan="8" align="center"  class="table-content">
		   <table border='0' cellpadding='0' cellspacing='0'  width="99%">
		    <tr>
		     <td width="70%" align="center">
		       <span class="x_y_title">  第 ${suitNo} 套  帯  量  食  谱  模  板 </span>
		     </td>
		     <td width="30%" align="right">
		       <span>&nbsp;配餐均龄：4岁 &nbsp;一人可食均量：克&nbsp;</span>
		     </td>
		     </tr>
		   </table>
		</td>
	  </tr>
	  <tr>
		  <td width="5%" class="table-content"  align="center" >
		      餐别
		  </td>
		  <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border">
			  <tr>
				<td colspan="3" align="center" class="table-content">
				${wkdata.weekName} <#if wkdata.dateName?exists></#if>
				</td>
			  </tr>
			  <tr>
				<td width="40%" align="center" class="table-content">食谱</td>
				<td width="40%" align="center" class="table-content">食物</td>
				<td width="20%" align="center" class="table-content">重量</td>
			  </tr>
			  </table>
		  </td>
		  </#list>
        </tr>
	    <tr>
		  <td width="5%" class="table-content">
		  &nbsp;&nbsp;早餐
		  </td>
		  <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.breakfastList as r1>
			  <tr>
				<td valign="top" width="40%" height="18">
				     <span class="dietary_title" 
					       onclick="javascript:editItems('${r1.dietaryTemplate.id}');">${r1.name}</span>&nbsp;
                </td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%" height="15">${item.name}</td>
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

	    <tr>
		  <td class="table-content">
		  &nbsp;&nbsp;早点
		  </td>
          <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.breakfastMidList as r1>
			  <tr>
				<td valign="top" width="40%" height="18">
				     <span class="dietary_title" 
					       onclick="javascript:editItems('${r1.dietaryTemplate.id}');">${r1.name}</span>&nbsp;
				</td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%" height="15">${item.name}</td>
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

	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;午餐
		  </td>
          <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.lunchList as r1>
			  <tr>
				<td valign="top" width="40%" height="18">
				     <span class="dietary_title" 
					       onclick="javascript:editItems('${r1.dietaryTemplate.id}');">${r1.name}</span>&nbsp;
				</td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%" height="15">${item.name}</td>
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

	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;午点
		  </td>
          <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.snackList as r1>
			  <tr>
				<td valign="top" width="40%" height="18"> 
				     <span class="dietary_title" 
					       onclick="javascript:editItems('${r1.dietaryTemplate.id}');">${r1.name}</span>&nbsp;
				</td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%" height="15">${item.name}</td>
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

	    <tr>
		  <td class="table-content">
		   &nbsp;&nbsp;晚餐
		  </td>
          <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.dinnerList as r1>
			  <tr>
				<td valign="top" width="40%" height="18">
				     <span class="dietary_title" 
					       onclick="javascript:editItems('${r1.dietaryTemplate.id}');">${r1.name}</span>&nbsp;
				</td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%" height="15">${item.name}</td>
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
     <!-- <tr>
	    <td colspan="6" align="right"  class="table-content" height="15">
		  &nbsp;
		</td>
	  </tr> -->
    </table>
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
  <#if suitNo?exists>
	jQuery(document).ready(function() { 

		$('#x1').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135903&suitNo=${suitNo}&type=heatEnergyX1PerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x2').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135902&suitNo=${suitNo}&type=heatEnergyX2PerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x4').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135302&suitNo=${suitNo}&type=proteinPercentPerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x3').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135907&suitNo=${suitNo}&type=heatEnergyX3PercentPerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x5').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135904&suitNo=${suitNo}&type=vitaminAAnimalsX3PercentPerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

        $('#x6').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135905&suitNo=${suitNo}&type=ironAnimalsX3PercentPerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

        $('#x7').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135906&suitNo=${suitNo}&type=fatAnimalsX3PercentPerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x8').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135908&suitNo=${suitNo}&type=calciumPhosphorusX3PercentPerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

        });
  </#if>
</script>	
</body>
</html>