<!DOCTYPE html>
<html>
<head>
</head>
<body>
<div style="margin:0px;"></div>
 <#if dataFile?exists>
  <img src="${request.contextPath}/tenant/tcImage?tenantId=${tenantId}">
 <#else>
  <h3><font color='red'>未上传相关证件！</font></h3>
 </#if>
 </body>