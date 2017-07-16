<!DOCTYPE html>
<html>
<head>
<title>任务历史信息</title>
<#include "/inc/init_easyui_import.ftl"/>
  
<body id="document" style="padding-left:120px;padding-right:120px">
 
<br> 
 
<div class="x_content_title"><img
	src="${contextPath}/static/images/window.png"
	alt="调度历史信息"> &nbsp;调度历史信息
</div>
<br>
 
<table border="0" cellpadding="0" cellspacing="0"  class="table table-striped table-bordered table-condensed">
	<tr>
		<td width="25%" height="24">名称&nbsp;</td>
		<td height="24">${schedulerLog.taskName}</td>
	</tr>

	<tr>
		<td width="25%" height="24">内容&nbsp;&nbsp;</td>
		<td height="24">${schedulerLog.content}</td>
	</tr>

	<tr>
		<td width="25%" height="24">开始日期&nbsp;&nbsp;</td>
		<td height="24"> 
		  ${schedulerLog.startDate}
		</td>
	</tr>

	<tr>
		<td width="25%" height="24">结束日期&nbsp;&nbsp;</td>
		<td height="24">
            ${schedulerLog.endDate}
		</td>
	</tr>

	<tr>
		<td width="25%" height="24">运行状态&nbsp;</td>
		<td height="24">
		     <#if schedulerLog.status==0>等待运行</#if>
			 <#if schedulerLog.status==1>运行中</#if>
			 <#if schedulerLog.status==2>运行成功</#if>
			 <#if schedulerLog.status==3>运行失败</#if>
		</td>
	</tr>
 
    <#if  schedulerLog.status== >
	<tr>
		<td width="25%" height="24">错误信息&nbsp;</td>
		<td height="24">
		    <pre><div style="color:#ff0000">${schedulerLog.exitMessage}" /></div></pre>
		</td>
	</tr>
	</#if>

    </table> 

	<!-- <div align="center">
	 <input name="close" type="button" value="关闭" class="btn btn-primary" onclick="javascript:window.close();">
	</div> -->
 
<br/>
 
</body> 
</html>