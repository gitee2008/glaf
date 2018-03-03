<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>膳食信息列表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
</head>
<body style="margin:0px;">
<div class="easyui-tabs" data-options="tools:'#tab-tools'" style="overflow:hidden; width:100%; height:auto">
  <div title="一周食谱" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/dietaryExport/showTenantExport?tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="每日食谱" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/dietaryExport/showTenantDayExport?tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="采购查询" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/goodsPurchase/reviewlist?tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="入库查询" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/goodsInStock/reviewlist?tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="出库查询" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/goodsOutStock/reviewlist?tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="用量查询" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/goodsActualQuantity/reviewlist?tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="膳费查询" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/goodsActualQuantity/report?tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="就餐人数查询" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/actualRepastPerson/reviewlist?tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
</div>
</body>
</html>