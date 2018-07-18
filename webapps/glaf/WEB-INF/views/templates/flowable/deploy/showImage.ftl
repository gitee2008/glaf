
 <#if path?if_exists>
  <img src="${request.contextPath}/${path}">
 <#elseif processDefinition?if_exists>
  <div align="left" style="padding-left: 60px;">
    <br>
    <br>  部署编号： ${processDefinition.deploymentId}
	<br>  定义编号： ${processDefinition.id}
	<br>  流程名称： ${processDefinition.name}
	<br>  流程Key：  ${processDefinition.key}
	<br>  流程版本： ${processDefinition.version}
  </div>
 <#else>
     流程发布完成！  
 </#if>