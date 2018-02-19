<!DOCTYPE html>
<html>
<head>
<title>参数设置</title>
<#include "/inc/init_bootstrap_import.ftl"/>
<script type="text/javascript">
	     function saveForm(){
			 var params = jQuery("#iForm").formSerialize();
			  jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/property/saveCfg',
				   data: params,
				   dataType:  'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
						if(data.message != null){
						   alert(data.message);
					    } else {
						   alert('操作成功完成！');
					    }
				   }
			 });
	}
	</script>
</head>
<body style="padding-left:20px;padding-right:20px">
 <br />
 <div class="x_content_title"><img
	src="${contextPath}/static/images/window.png"
	alt="参数设置">&nbsp;参数设置
</div>
<br>
<form id="iForm"  name="iForm" method="post" action="">
<input type="hidden" id="category" name="category" value="${category}">
  <table align="center" class="x-table-border table table-striped table-bordered table-condensed" cellspacing="1"
	     cellpadding="4" width="95%">
 
   <tr class="x-title">
        <td>代码</td>
		<td>标题</td>
		<td>说明</td>
		<td>参数值</td>
	</tr>
     <#list  rows as p>
      <tr >
		<td>${p.name}</td>
		<td>${p.title}</td>
		<td>${p.description}</td>
		<td valign="middle">
		     <#if p.inputType == 'combobox'>
			    <select id="${p.name}" name="${p.name}">
					 ${p.selectedScript}
			    </select>
                <script type="text/javascript">
                    document.getElementById("${p.name}").value="${p.value}";
                </script>
			 <#elseif  p.inputType == 'imgbox'>
			 	<input type="text" id="${p.name}" name="${p.name}" 
			 	       value="${p.value}" class="span3 x-text" 	
				       onclick="javascript:openWindow('${contextPath}/image/choose?elementId=${p.name}', self, 50, 50, 1098, 520);" />
			 <#else>
			    <input type="text" name="${p.name}" class="span3 x-text" 
		               value="${p.value}" size="60" maxLength="500"/>
			 </#if>
		</td>
	</tr>
	</#list>
  </table>
	<div align="center">
	    <input type="button"  name="save" value=" 保存 " class="btn btnGray" onclick="javascript:saveForm('');"/>
	</div>
</form>
 <br/>
 <br/>
</body>
</html>