<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>审核意见</title>
<style>

.subject { font-size: 13px;  text-decoration: none; font-weight:normal; font-family:"宋体"}
.table-border { background-color:#ccccff; height: 32px; font-family:"宋体"}
.table-content { background-color:#ffffff; height: 32px;font-size: 12px; font-family:"宋体"}

</style>
</head>
<body>
<div style="margin:0;"></div>  
    <#list comments as comment>
      <table class="table-border" cellspacing="1" cellpadding="4" width="98%" nowrap>
		<tr align="left">
			<td class="table-content" width="30%">
			<span class="subject">审核人</span> ${comment.username}
			</td>
			<td class="table-content" width="30%">
			<span class="subject">审核时间</span> ${comment.approvalDate}  
			</td>
			<td class="table-content" width="40%">
			<span class="subject">是否通过</span>
			<#if comment.approval == 1>  
			<span style="color:green;font-weight:bold;">通过</span>
			<#else>
			<span style="color:red;font-weight:bold;">不通过</span>
			</#if>
			</td>
		</tr>
		<#if comment.content?if_exists>
		<tr align="left">
			<td class="table-content" colspan="3">
			<span class="subject">审核意见</span>
			<br><br>&nbsp;&nbsp;&nbsp;&nbsp;${comment.content}     
			<br><br>&nbsp;
			</td>
		</tr>
		</#if>
	  </table>
	  <br>
	</#list> 
</body>
</html>