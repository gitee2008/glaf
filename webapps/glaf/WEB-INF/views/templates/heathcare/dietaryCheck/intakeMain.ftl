<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>膳食种类与进食量</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
</head>
<body style="margin:0px;">
<div class="easyui-tabs" data-options="tools:'#tab-tools'" style="overflow:hidden; width:99%; height:auto">
  <div title="膳食种类" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0" style="width:100%;height:680px;" 
	        src=""></iframe>
  </div>
  <div title="当日进食量" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0" style="width:100%;height:680px;" 
	        src=""></iframe>
  </div>
  <div title="人均进食量" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0" style="width:100%;height:680px;" 
	        src=""></iframe>
  </div>
</div>
</body>
</html>