<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物品入库信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>

.subject { font-size: 13px; text-decoration: none; font-weight:normal; font-family:"宋体"}
.table-border { background-color:#eaf2ff; height: 32px; font-family:"宋体"}
.table-head { background-color:#5cb1f8; height: 30px; font-weight:bold; font-size: 13px; font-family:"宋体"}
.table-content { background-color:#ffffff; height: 28px; font-size: 12px; font-family:"宋体"}

</style>
<script type="text/javascript">

	function saveData(){
		<#list planList as plan>
         if(jQuery('#qty_${plan.goodsId}').val() != ''){
            if(jQuery('#totalPrice_${plan.goodsId}').val() == ''){
				alert("请输入“${plan.goodsName}”总价。");
				document.getElementById("totalPrice_${plan.goodsId}").focus();
				return;
			}
		 }
		</#list>
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsInStock/saveBatch',
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

	function calMTotal(goodsId){
		//alert("");
        var quantity = document.getElementById("qty_"+goodsId).value*1.0;
		var price = document.getElementById("price_"+goodsId).value*1.0;
		if(quantity*1.0 > 0 && price*1.0 > 0){
			var total = quantity * price;
			//alert("total:" + total);
			//alert(document.getElementById("totalPrice").value);
			total = Math.round(total * 100.00) / 100.00;
            document.getElementById("totalPrice_"+goodsId).value=total;
			//jQuery("#totalPrice").val(total);
			//document.getElementById("cal_totalPrice").html(total);
			//document.getElementById("totalPrice").text=total;
			//alert(document.getElementById("totalPrice").value);
		}
	}

</script>
</head>

<body>
<div style="margin:0px;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:40px;margin-top:0px;"  class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;批量入库信息</span>
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
	     ${now ?string('yyyy-MM-dd') }&nbsp;周次&nbsp;
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

  <table class="table-border" cellspacing="1" cellpadding="4" width="98%" nowrap align="center">
    <thead>
	  <tr>
	  <td class="table-head" width="20%" align="center">食物名称</td>
	  <td class="table-head" width="12%" align="center">当日采购量(千克)</td>
	  <td class="table-head" width="10%" align="center">入库量(千克)</td>
	  <td class="table-head" width="10%" align="center">单价(每千克)</td>
	  <td class="table-head" width="10%" align="center">总价</td>
	  <td class="table-head" width="10%" align="center">有效期</td>
	  <td class="table-head" width="12%" align="center">产地</td>
	  <td class="table-head" width="16%" align="center">供货单位</td>
	  </tr>
	</thead>
    <tbody>
	<#list planList as plan>
	<tr>
		<td  class="table-content" align="left"> ${plan.goodsName} </td>
		<td  class="table-content" align="right"> ${plan.quantity} </td>
		<td  class="table-content" align="right">
		    <input id="qty_${plan.goodsId}" name="qty_${plan.goodsId}" type="text" style="width:60px;text-align:right;"
			       class="x-small-text" precision="1" onkeyup="javascript:calMTotal('${plan.goodsId}');"
				   <#if plan.quantity !=0 > value="${plan.quantity}" </#if>>
		</td>
		<td  class="table-content" align="right">
		    <input id="price_${plan.goodsId}" name="price_${plan.goodsId}" type="text" style="width:60px;text-align:right;"
			       class="x-small-text" precision="2" onkeyup="javascript:calMTotal('${plan.goodsId}');"
				   <#if plan.price != 0 > value="${plan.price}" </#if> >
		</td>
		<td  class="table-content" align="right">
		    <input id="totalPrice_${plan.goodsId}" name="totalPrice_${plan.goodsId}" type="text" 
			       style="width:60px; text-align:right;"
			       class="x-small-text" precision="2"
				   <#if plan.totalPrice != 0 > value="${plan.totalPrice}" </#if> >
		</td>
		<td  class="table-content" align="center">
		    <input id="expiryDate_${plan.goodsId}" name="expiryDate_${plan.goodsId}" type="text" 
			       class="easyui-datebox x-small-text" style="width:100px" 
				   <#if plan.expiryDate?exists>value="${plan.expiryDate?string('yyyy-MM-dd')}" </#if>
				   >
		</td>
		<td  class="table-content" align="left">
		    <input id="address_${plan.goodsId}" name="address_${plan.goodsId}" type="text" 
			       class="easyui-validatebox x-small-text" style="width:120px" value="${plan.address}">
		</td>
		<td  class="table-content" align="left">
		    <input id="supplier_${plan.goodsId}" name="supplier_${plan.goodsId}" type="text" 
			       class="easyui-validatebox x-small-text" style="width:170px" value="${plan.supplier}">
		</td>
	</tr>
	</#list>
	<tr><td colspan="7">&nbsp;<br></td></tr>
    </tbody>
  </table>
 </form>
</div>
</div>
<div style="margin-top:10px;"><br></div>
<br>
</body>
</html>