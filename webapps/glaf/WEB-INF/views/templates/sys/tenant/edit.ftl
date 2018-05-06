<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>租户信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">
    var contextPath = "${contextPath}";

	function saveData(){
		if(document.iForm.locked.value == "1"){
			if(confirm("租户锁定后该租户用户将无法登录系统，确定吗？")){
             var params = jQuery("#iForm").formSerialize();
		     jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tenant/saveSysTenant',
				   data: params,
				   dataType: 'json',
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
		} else {
              var params = jQuery("#iForm").formSerialize();
		      jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/tenant/saveSysTenant',
				   data: params,
				   dataType: 'json',
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
	}

</script>
</head>
<body>
<div style="margin:0;"></div>
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<span class="x_content_title">&nbsp;<img src="${contextPath}/static/images/window.png">&nbsp;编辑租户信息</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center', border:false, cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${sysTenant.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">名称</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox  x-text" style="width:350px;"
				   value="${sysTenant.name}">
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">代码</td>
		<td align="left">
            <input id="code" name="code" type="text" 
			       class="easyui-validatebox  x-text" style="width:350px;"			
				   value="${sysTenant.code}">
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">负责人</td>
		<td align="left">
            <input id="principal" name="principal" type="text" 
			       class="easyui-validatebox  x-text" style="width:350px;"
				   value="${sysTenant.principal}">
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">电话</td>
		<td align="left">
            <input id="telephone" name="telephone" type="text" 
			       class="easyui-validatebox  x-text" style="width:350px;" 
				   value="${sysTenant.telephone}">
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">等级</td>
		<td align="left">
			<select id="level" name="level">
			    <option value="0">----请选择----</option>
				<option value="9999">省级示范</option>
				<option value="999">市级示范</option>
				<option value="99">县/区级示范</option>
				<option value="1">非示范</option>
			</select>
            <script type="text/javascript">
                document.getElementById("level").value="${sysTenant.level}";
            </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">性质</td>
		<td align="left">
			<select id="property" name="property">
			    <option value="0">----请选择----</option>
				<option value="Public">公立</option>
				<option value="Private">私立</option>
				<option value="Gov_Ent">政企联办</option>
				<option value="Gov_Pri">民办公助</option>
				<option value="Collectivity">集体</option>
				<option value="Enterprise">企业</option>
				<option value="Other">其他</option>
			</select>
            <script type="text/javascript">
                document.getElementById("property").value="${sysTenant.property}";
            </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">系统类型</td>
		<td align="left">
			<select id="type" name="type">
			    <option value="">----请选择----</option>
				<option value="NS">托幼机构</option>
				<option value="ED">教育机构</option>
				<option value="BH">卫生保健机构</option>
				<option value="Other">其他</option>
			</select>
            <script type="text/javascript">
                document.getElementById("type").value="${sysTenant.type}";
            </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">实名认证</td>
		<td align="left">
			<select id="verify" name="verify">
			    <option value="">----请选择----</option>
				<option value="Y">已认证</option>
				<option value="N">未认证</option>
			</select>
            <script type="text/javascript">
                document.getElementById("verify").value="${sysTenant.verify}";
            </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">省份</td>
		<td align="left">
			<select id="provinceId" name="provinceId" onchange="javascript:selectDistrict('provinceId', 'cityId');">
			    <option value="">----请选择----</option>
				<#list provinces as province>
				<option value="${province.id}">${province.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    //selectProvince("provinceId");
                document.getElementById("provinceId").value="${sysTenant.provinceId}";
            </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">市</td>
		<td align="left">
			<select id="cityId" name="cityId" onchange="javascript:selectDistrict('cityId', 'areaId');">
			    <option value="">----请选择----</option>
				<#list citys as city>
				<option value="${city.id}">${city.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    <#if citys?exists>
				  document.getElementById("cityId").value="${sysTenant.cityId}";
				<#else>
				  selectDistrict("cityId", document.getElementById("provinceId").value);
				</#if>
            </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">区县</td>
		<td align="left">
		    <select id="areaId" name="areaId" onchange="javascript:selectDistrict('areaId', 'townId');">
			    <option value="">----请选择----</option>
				<#list areas as area>
				<option value="${area.id}">${area.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    <#if areas?exists>
				  document.getElementById("areaId").value="${sysTenant.areaId}";
				<#else>
				  selectDistrict("areaId", document.getElementById("cityId").value);
				</#if>
            </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">镇</td>
		<td align="left">
		    <select id="townId" name="townId">
			    <option value="">----请选择----</option>
				<#list towns as town>
				<option value="${town.id}">${town.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    <#if towns?exists>
				  document.getElementById("townId").value="${sysTenant.townId}";
				<#else>
				  selectDistrict("townId", document.getElementById("areaId").value);
				</#if>
            </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">详细地址</td>
		<td align="left">
		    <input id="address" name="address" type="text" style="height:60px; width:350px;text-align:left;"
				   class=" x-textarea" value="${sysTenant.address}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">允许上传票据</td>
		<td align="left">
            <select id="ticketFlag" name="ticketFlag">
			    <option value="Y">是</option>
				<option value="N">否</option>
			</select>
			<script type="text/javascript">
				document.getElementById("ticketFlag").value="${sysTenant.ticketFlag}";
			</script> 
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">限制数据录入时间</td>
		<td align="left">
		  <input type="radio" name="limit" value="0" <#if sysTenant.limit == 0>checked</#if>>是&nbsp;&nbsp;
	      <input type="radio" name="limit" value="9999" <#if sysTenant.limit == 9999>checked</#if>>否&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">禁用数据约束</td>
		<td align="left">
		  <input type="radio" name="disableDataConstraint" value="Y" <#if sysTenant.disableDataConstraint == 'Y'>checked</#if>>是&nbsp;&nbsp;
	      <input type="radio" name="disableDataConstraint" value="N" <#if sysTenant.disableDataConstraint == 'N'>checked</#if>>否&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">是否有效</td>
		<td align="left">
		  <input type="radio" name="locked" value="0" <#if sysTenant.locked == 0>checked</#if>>是&nbsp;&nbsp;
	      <input type="radio" name="locked" value="1" <#if sysTenant.locked == 1>checked</#if>>否&nbsp;&nbsp;
		</td>
	</tr>
   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>