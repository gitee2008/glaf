<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物品验收信息</title>
<#include "/inc/init_easyui_import.ftl"/>
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
				   url: '${contextPath}/heathcare/goodsAcceptance/saveBatch',
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

	function setVal(){
        <#list purchaseList as purchase>
          jQuery("#qty_${purchase.goodsId}").val(jQuery("#purchase_qty_${purchase.goodsId}").val());
		  //document.getElementById("qty_${purchase.goodsId}").value=jQuery("#out_qty_${purchase.goodsId}").val();
		</#list>
	}

    function submitReq(){
        document.getElementById("iForm").submit();
    }

</script>
</head>

<body>
<div style="margin:0px;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:40px;margin-top:0px;" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">验收信息</span>
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
	     <input type="button" value="验收量=采购量" onclick="javascript:setVal();" class="btnGray">
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

  <table class="table-border" cellspacing="1" cellpadding="4" width="95%" nowrap align="center">
    <thead>
	  <tr>
	  <td class="table-head" width="40%" align="center">食物名称</td>
	  <td class="table-head" width="30%" align="center">采购量(千克)</td>
	  <td class="table-head" width="30%" align="center">验收量(千克)</td>
	  </tr>
	</thead>
    <tbody>
	<#list purchaseList as purchase>
	<tr>
		<td width="40%" class="table-content" align="left"> ${purchase.goodsName} </td>
		<td width="30%" class="table-content" align="right"> ${purchase.quantity} </td>
		<td width="30%" class="table-content" align="center">
		    <input type="hidden" id="purchase_qty_${purchase.goodsId}" name="purchase_qty_${purchase.goodsId}" 
			       value="${purchase.quantity}">
		    <input id="qty_${purchase.goodsId}" name="qty_${purchase.goodsId}" type="text" style="width:90px; text-align:right;"
			       class="x-small-text" precision="1" 
				  <#if purchase.quantity2 != 0 > value="${purchase.quantity2}" </#if>>
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