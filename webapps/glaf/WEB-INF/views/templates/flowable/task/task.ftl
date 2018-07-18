<!DOCTYPE html>
<html>
<head>
<title>查看流程实例</title>
<#include "/inc/init_easyui_import.ftl"/>
</head>
<body>
<br />
<div class="txt_content_frame" style=" width: 98%;  ">
<div class="txt_content_box">
 
<div style=" width: 92%; padding-left:40px; " >
<br>
<div class="x_content_title"> <img
	src="${request.contextPath}/static/images/window.png" alt="流程实例信息">
 流程实例信息
</div>
<br>
<fieldset class="x-fieldset" style=" width: 95%;" >
<legend>流程基本信息</legend>

<table align="left" cellspacing="1" cellpadding="4" width="90%" class="x-table-border table table-striped table-bordered table-condensed"  nowrap>
	<tr>
		<td width="12%" height="12" align="left">流程名称</td>
		<td width="38%">${processDefinition.name}
		</td>
		<td width="12%" height="12" align="left">流程版本</td>
		<td width="38%">${processDefinition.version}</td>
	</tr>
	<tr>
		<td width="12%" height="12" align="left">业务编号</td>
		<td width="38%"> 
		    ${historyProcessInstance.businessKey}
		</td>
		<td width="12%" height="12" align="left">启动者</td>
		<td width="38%"> 
            ${historyProcessInstance.startUserId}
		</td>
	</tr>
	<tr>
		<td width="12%" height="12" align="left">启动时间</td>
		<td width="38%">
            ${historyProcessInstance.startTime?string('yyyy-MM-dd HH:mm')}
		</td>
		<td width="12%" height="12" align="left">结束时间</td>
		<td width="38%"> 
				<#if historyProcessInstance.endTime?if_exists>
				  <font color="#00CC33" >
				  ${historyProcessInstance.endTime?string('yyyy-MM-dd HH:mm')}
				  </font>
				<#else>
                      <font color="#0066FF" ><b> 运行中 </b></font>
				</#if>
		</td>
	</tr>
</table>
</fieldset>

<#if historyTaskItems?if_exists>
<br>
<fieldset class="x-fieldset" style=" width: 95%;">
<legend>历史任务信息</legend>
<table align="left" cellspacing="1" cellpadding="4" width="90%" class="x-table-border table table-striped table-bordered table-condensed" nowrap>
  <tr align="left">
	  <td>&nbsp;任务名称</td>
	  <td>&nbsp;执行人</td>
	  <td>&nbsp;创建时间</td>
	  <td>&nbsp;处理时间</td>
 </tr>
  <#list historyTaskItems as taskItem>
    <tr align="left">
	  <td>&nbsp; ${taskItem.taskDefinitionKey} &nbsp;${taskItem.taskName} [${taskItem.taskInstanceId}]</td>
	  <td>&nbsp; ${taskItem.actorId} [${taskItem.actorName}] </td>
	  <td>&nbsp; ${taskItem.startTime?string('yyyy-MM-dd HH:mm')} </td>
	  <td>&nbsp; ${taskItem.endTime?string('yyyy-MM-dd HH:mm')} </td>
	</tr>
 </#list>
</table>
</fieldset>
</#if>

<#if taskItems?if_exists>
<br>
<fieldset class="x-fieldset" style=" width: 95%;">
<legend>待办任务</legend>
<table align="left" cellspacing="1" cellpadding="4" width="90%" class="x-table-border table table-striped table-bordered table-condensed" nowrap>
  <tr>
	  <td>&nbsp;任务编号</td>
	  <td>&nbsp;任务名称</td>
	  <td>&nbsp;执行人</td>
	  <td>&nbsp;创建时间</td>
	  <td>&nbsp;状态</td>
 </tr>
   <#list taskItems as taskItem>
    <tr>
	  <td>&nbsp;<font color="#0066FF"><b>${taskItem.taskInstanceId}</b></font> </td>
	  <td>&nbsp;【${taskItem.taskDefinitionKey} &nbsp; ${taskItem.taskName}】 </td>
	  <td>&nbsp;
	    <#if taskItem.actorId?if_exists>
			<font color="#FF6600"><b>${taskItem.actorId}[${taskItem.actorName}]</b></font>
		<#elseif taskItem.groupId?if_exists>
			组 ${taskItem.groupId}[${taskItem.groupName}] 
		<#else>
			尚未分配用户
		</#if>
	  </td>
	  <td>&nbsp;${taskItem.startTime?string('yyyy-MM-dd HH:mm')} </td>
	  <td>&nbsp;<font color="#0066FF" ><b> 未处理 </b></font> </td>
	</tr>
   </#list>
  </#if>
 </table>
</fieldset>
<br />
 <div align="center" style="width:100%">
  <iframe id="processimage" align="center" frameborder="0" width="1020" height="800" scrolling="no" 
       src="${request.contextPath}/flowable/view?processInstanceId=${processInstanceId}" >
  </iframe>
  </div>
  
 </div>
 </div>
</div>
<br />
</body>
</html>