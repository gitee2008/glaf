<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实际用量录入</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<style>

.subject { font-size: 13px; text-decoration: none; font-weight:normal; font-family:"宋体"}
.table-border { background-color:#eaf2ff; height: 32px; font-family:"宋体"}
.table-head { background-color:#5cb1f8; height: 30px; font-weight:bold; font-size: 13px; font-family:"宋体"}
.table-content { background-color:#ffffff; height: 28px; font-size: 12px; font-family:"宋体"}

</style>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsActualQuantity/saveBatch',
				   data: params,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
					   if(data.statusCode == 200){
					       window.parent.location.reload();
					   } 
				   }
			 });
	}

    function submitReq(){
        document.getElementById("iForm").submit();
    }

    function changeXY(personId){
	    var status_ = document.getElementById("status_"+personId).value;
	    if(status_ == 0){
            jQuery("#div_remark_"+personId).hide();
			jQuery("#div_treat_"+personId).hide();
	    } else {
		    jQuery("#div_remark_"+personId).show();
			jQuery("#div_treat_"+personId).show();
	    }
    }

	function setVal(){
        <#list planList as plan>
          jQuery("#qty_${plan.goodsId}").val(jQuery("#out_qty_${plan.goodsId}").val());
		  //document.getElementById("qty_${plan.goodsId}").value=jQuery("#out_qty_${plan.goodsId}").val();
		</#list>
	}

</script>
</head>

<body>
<div style="margin:0px;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:42px;margin-top:0px;" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">&nbsp;实际用量录入</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:saveData();" > 确 定 
	</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <table cellspacing="1" cellpadding="4" width="95%" nowrap align="center">
    <tbody>
	  <tr>
	  <td width="20%" align="left">&nbsp;</td>
	  <td width="20%" align="left">&nbsp;</td>
	  <td width="60%" align="right">
	     <input type="button" value="实际量=出库量" onclick="javascript:setVal();" class="btnGray">
	     &nbsp;${now ?string('yyyy-MM-dd') }&nbsp;周次&nbsp;
		 <select id="week" name="week">
			<#list weeks as week>
			<option value="${week}">${week}</option>
			</#list>
		  </select>
		  <script type="text/javascript">
		      document.getElementById("week").value="${nowWeek}";
		  </script>
	  </td>
	  </tr>
	</tbody>
  </table>

  <table class="table-border" cellspacing="1" cellpadding="4" width="95%" nowrap align="center">
    <thead>
	  <tr>
	  <td class="table-head" width="30%" align="center">食物名称</td>
	  <td class="table-head" width="20%" align="center">计划用量(千克)</td>
	  <td class="table-head" width="20%" align="center">出库量(千克)</td>
	  <td class="table-head" width="20%" align="center">实际用量(千克)</td>
	  </tr>
	</thead>
    <tbody>
	<#list planList as plan>
	<tr>
		<td width="30%" class="table-content" align="left"> ${plan.goodsName} </td>
		<td width="20%" class="table-content" align="right"> ${plan.quantity} </td>
		<td width="20%" class="table-content" align="right"> ${plan.outStockQuantity} </td>
		<td width="20%" class="table-content" align="center">
		    <input type="hidden" id="out_qty_${plan.goodsId}" name="out_qty_${plan.goodsId}" value="${plan.outStockQuantity}">
		    <input id="qty_${plan.goodsId}" name="qty_${plan.goodsId}" type="text" style="width:90px; text-align:right;"
			       class="x-small-text" precision="1" 
				   <#if plan.realQuantity !=0 > value="${plan.realQuantity}" </#if>>
		</td>
	</tr>
	</#list>
	<tr><td colspan="4">&nbsp;<br></td></tr>
    </tbody>
  </table>
 </form>
 <br><br><br><br><br>
</div>
</div>
</body>
</html>