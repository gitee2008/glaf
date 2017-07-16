<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户注册</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">
var contextPath = "${contextPath}";

function isEmail(strEmail) {
	if (strEmail.search(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) != -1){
	    return true;
	} else {
	    return false;
	}
}


function regXY(){
   var buff = '{';
   if(jQuery("#x").val()==""){
       alert("请输入用户名");
	   document.getElementById("x").focus();
       return;
   }
   buff +='"x":"'+jQuery("#x").val()+'"';

   if(jQuery("#name").val()==""){
       alert("请输入姓名");
	   document.getElementById("name").focus();
       return;
   }

   buff +=',"name":"'+jQuery("#name").val()+'"';

   if(jQuery("#y").val()==""){
       alert("请输入密码");
	   document.getElementById("y").focus();
       return;
   }

   if(jQuery("#y").val()!=jQuery("#y2").val()){
       alert("密码与确认密码不一致，请重新输入");
	   document.getElementById("password2").focus();
       return;
   }

   buff +=',"y":"'+jQuery("#y").val()+'"';


   if(jQuery("#tenantName").val()==""){
       alert("请输入机构名称");
	   document.getElementById("tenantName").focus();
       return;
   }

   buff +=',"tenantName":"'+jQuery("#tenantName").val()+'"';

   if(jQuery("#mail").val()==""){
       alert("请输入邮件地址");
	   document.getElementById("mail").focus();
       return;
   }

    if(!isEmail(jQuery("#mail").val())){
       alert("请输入合法的邮件地址");
	   document.getElementById("mail").focus();
       return;
   }

   buff +=',"mail":"'+jQuery("#mail").val()+'"';
   buff +='}';
   //alert(buff);
   document.getElementById("json").value=buff;
   var params = jQuery("#iForm").formSerialize();
   jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/register/create',
				   dataType:  'json',
				   data: params,
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						 alert(data.message);
						 if(data.status==200){
							 var x = jQuery("#x").val();
							 var y = jQuery("#y").val();
							 location.href="${contextPath}/login";
						 }
					   } 
				   }
			 });
}

function setValue(obj){
  obj.value=obj[obj.selectedIndex].value;
}
</script>
</head>

<body>
<div class="content-block" style="width: 80%;"><br>
<div class="x_content_title"><img
	src="${contextPath}/static/images/window.png"
	alt="用户注册">&nbsp;用户注册
</div>
<br>
<form id="iForm" name="iForm" method="post" > 
<input type="hidden" id="json" name="json">
  <table width="600" border="0" align="center" cellpadding="0" cellspacing="0" class="box">
      <tr>
        <td height="40">用户名*</td>
        <td>
		<input id="x" name="x" type="text" size="20" class="easyui-validatebox x-text" style="width:180px"  
		       data-options="required:true" datatype="string"  maxlength="20"
		       nullable="no" maxsize="50" chname="用户名">（只能包含数字，大小写字母及下划线）   
		</td>
      </tr>
	  <tr>
        <td height="40">姓　名*</td>
        <td>
		<input id="name" name="name" type="text" size="20" class="easyui-validatebox x-text" style="width:180px"  
		       data-options="required:true" datatype="string"  maxlength="20"
		       nullable="no" maxsize="50" chname="姓名">（可以包含数字，大小写字母及汉字）   
		</td>
      </tr>
      <tr>
        <td class="input-box2"  height="40">密　码*</td>
        <td align="left">
		<input id="y" name="y" type="password" size="20" datatype="string" nullable="no" 
		       minsize="6" maxsize="20" chname="密码" class="easyui-validatebox x-text" style="width:180px"   
			   data-options="required:true"  maxlength="20">（6至20个字符，可以使用数字，大小写字母）  
		</td>
      </tr>
      <tr>
        <td class="input-box2"  height="40">确认密码*</td>
        <td align="left">
		<input id="y2" name="y2" type="password" size="20"  datatype="string" nullable="no" 
		       minsize="6" maxsize="20" chname="确认密码" class="easyui-validatebox x-text" style="width:180px" 
			   data-options="required:true"  maxlength="20">（6至20个字符，可以使用数字，大小写字母）  
		</td>
      </tr>
	  <tr>
        <td height="40">机构名称*</td>
        <td>
		<input id="tenantName" name="tenantName" type="text" size="20" class="easyui-validatebox x-text" style="width:180px"  
		       data-options="required:true" datatype="string"  maxlength="200"
		       nullable="no" maxsize="50" chname="机构名称">  
		</td>
      </tr>
      <tr>
        <td height="40">邮　件*</td>
        <td>
          <input id="mail" name="mail" type="text" size="20" class="easyui-validatebox x-text" style="width:180px"
		         data-options="required:true" datatype="mail" 
		         nullable="no" maxsize="50" chname="邮件"  maxlength="50">（用于邮箱验证及密码重置等）        
		</td>
      </tr>
	  <tr>
        <td height="40">手　机</td>
        <td>
          <input id="mobile" name="mobile" type="text" size="20" class="easyui-validatebox x-text" style="width:180px"
		         data-options="required:false" datatype="string" 
		         nullable="yes" maxsize="50" chname="手机"  maxlength="20">（用于接收消息及密码重置等）         
		</td>
      </tr>
	  <tr>
		<td height="40" align="left">性质</td>
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
		</td>
	  </tr>
      <tr>
		<td height="40" align="left">省份</td>
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
		<td height="40" align="left">市</td>
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
		<td height="40" align="left">区县</td>
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
		<td height="40" align="left">镇</td>
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
        <td class="input-box2"  height="30">&nbsp;</td>
        <td  align="left" height="40">&nbsp;
		   <br>
           <input name="btn_save2" type="button" value=" 确 定 " class="btnGreen" onclick="javascript:regXY();">
	    </td>
      </tr>

</table>
</form>

</body>
</html>
