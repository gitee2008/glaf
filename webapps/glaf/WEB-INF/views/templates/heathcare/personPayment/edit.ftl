<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>缴费信息</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personPayment/savePersonPayment',
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

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personPayment/savePersonPayment',
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

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑缴费信息</span>
	<#if audit == true>
	  <#if personPayment.businessStatus == 0>
	   <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	      onclick="javascript:audit();" >确认通过</a> 
	  <#elseif personPayment.businessStatus == 9>
	  &nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已确认</span>&nbsp;
	  </#if>
	<#else>
	  <#if personPayment.businessStatus == 9>
	  &nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已确认</span>&nbsp;
	  <#else>
	  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	     onclick="javascript:saveData();" >保存</a> 
	  </#if>
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${personPayment.id}"/>
  <#if person?exists >
  <input type="hidden" id="personId" name="personId" value="${person.id}">
  </#if>
  <table class="easyui-form" style="width:100%;" align="center">
    <tbody>
	<#if person?exists >
	<tr>
		<td width="25%" align="left">姓名</td>
		<td width="75%" align="left">${person.name}</td>
	</tr>
	<#elseif persons?exists >
	<tr>
		<td width="25%" align="left">姓名</td>
		<td align="left" > 
            <select id="personId" name="personId">
			    <option value="">--请选择--</option>
				<#list persons as person>
			    <option value="${person.id}">${person.name}</option>
			    </#list> 
			</select>
		</td>
	</tr>
	</#if>
	<tr>
		<td width="20%" align="left">费用类型</td>
		<td align="left">
            <select id="type" name="type">
				<option value="">----请选择----</option> 
				<option value="1">学杂费</option>
				<option value="2">伙食费</option>
				<option value="3">服装费</option>
				<option value="4">保险费</option>
				<option value="5">其他</option>
			 </select>
			 <script type="text/javascript">
			     document.getElementById("type").value="${personPayment.type}";
			 </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">缴费金额</td>
		<td align="left">
			<input id="money" name="money" type="text" style="width:80px; text-align:right;"
			       class="easyui-numberbox  x-text"  precision="2" 
				   value="${personPayment.money}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">缴费时间</td>
		<td align="left">
			<input id="payTime" name="payTime" type="text" 
			       class="easyui-datebox x-text" style="width:100px;"
			       <#if personPayment.payTime?exists>
				   value="${personPayment.payTime ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">备注</td>
		<td align="left">
		    <textarea id="remark" name="remark" 
			          class="easyui-validatebox  x-text"  
			          style="width:350px;height:120px">${personPayment.remark}</textarea>
		</td>
	</tr>
 
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>