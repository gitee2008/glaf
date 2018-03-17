<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>膳费使用情况</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
</head>
<body style="margin:0px;">
<div class="easyui-tabs" data-options="tools:'#tab-tools'" style="overflow:hidden; width:99%; height:auto">
  <div title="采购登记" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0" style="width:100%;height:680px;"
	        src="${contextPath}/heathcare/goodsPurchase/reviewlist2?tenantId=${tenantId}"></iframe>
  </div>
  <div title="用量登记" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0" style="width:100%;height:680px;"
	        src="${contextPath}/heathcare/goodsActualQuantity/reviewlist2?tenantId=${tenantId}"></iframe>
  </div>
  <div title="膳费查询" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0" style="width:100%;height:680px;"
	        src="${contextPath}/heathcare/goodsActualQuantity/report?tenantId=${tenantId}"></iframe>
  </div>
  <div title="就餐人数查询" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0" style="width:100%;height:680px;" 
	        src="${contextPath}/heathcare/actualRepastPerson/reviewlist?tenantId=${tenantId}"></iframe>
  </div>
</div>
</body>
</html>