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

	   if(jQuery("#tenantName").val()==""){
		   alert("请输入机构名称");
		   document.getElementById("tenantName").focus();
		   return;
	   }

	   buff +='"tenantName":"'+jQuery("#tenantName").val()+'"';

	   if(jQuery("#x").val()==""){
		   alert("请输入用户名");
		   document.getElementById("x").focus();
		   return;
	   }
	   buff +=', "x":"'+jQuery("#x").val()+'"';

	   if(jQuery("#name").val()==""){
		   alert("请输入姓名");
		   document.getElementById("name").focus();
		   return;
	   }

	   buff +=', "name":"'+jQuery("#name").val()+'"';

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

	   buff +=', "y":"'+jQuery("#y").val()+'"';


	   if(jQuery("#mobile").val()=="" || jQuery("#mobile").val().length != 11){
		   alert("请输入合法的手机号码。");
		   document.getElementById("mobile").focus();
		   return;
	   }

	   buff +=', "mobile":"'+jQuery("#mobile").val()+'"';

	   <#if enableSmsRegVerification == true>
	   if(jQuery("#verificationCode").val()=="" || jQuery("#verificationCode").val().length != 4){
		   alert("请输入短信验证码。");
		   document.getElementById("verificationCode").focus();
		   return;
	   }
	   
	   var verificationCode = jQuery("#verificationCode").val();
       if(verificationCode != ""){
	     buff +=', "verificationCode":"'+verificationCode+'"';
	   }
	   </#if>

	   if(!isEmail(jQuery("#mail").val())){
		   alert("请输入合法的邮件地址");
		   document.getElementById("mail").focus();
		   return;
	   }

	   buff +=', "mail":"'+jQuery("#mail").val()+'"';

	   if(jQuery("#provinceId").val() != ""){
	       buff +=', "provinceId":"'+jQuery("#provinceId").val()+'"';
	   }
	   if(jQuery("#cityId").val() != ""){
	       buff +=', "cityId":"'+jQuery("#cityId").val()+'"';
	   }
	   if(jQuery("#areaId").val() != ""){
	       buff +=', "areaId":"'+jQuery("#areaId").val()+'"';
	   }
	   if(jQuery("#townId").val() != ""){
	       buff +=', "townId":"'+jQuery("#townId").val()+'"';
	   }
	   buff +='}';
	   //alert(buff);
	   document.getElementById("json").value=buff;

       var params = jQuery("#iForm").formSerialize();
       jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/register/verify',
				   data: params,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } 
					   if(data.statusCode == 200){
						    document.iForm.action="${request.contextPath}/register/create";
						    document.iForm.submit();
					   } 
				   }
			 });
    }

	function setValue(obj){
	  obj.value=obj[obj.selectedIndex].value;
	}

	function sendSms(){
        if(jQuery("#mobile").val()=="" || jQuery("#mobile").val().length != 11){
		   alert("请输入合法的手机号码。");
		   document.getElementById("mobile").focus();
		   return;
	    }
	    var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sms/smsVerifyMessage/createRegSms',
				   data: params,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						    
					   }
					   if(data.statusCode == 200){
					       
					   } 
				   }
			 });
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
<form id="iForm" name="iForm" method="post" enctype="multipart/form-data" > 
  <input type="hidden" id="json" name="json">
  <table width="880" border="0" align="center" cellpadding="0" cellspacing="0" class="box">
	  <tr>
        <td height="40"><span class="x_green_name">机构名称</span>&nbsp;<span class="required">*</span></td>
        <td>
		<input id="tenantName" name="tenantName" type="text" size="20" class="easyui-validatebox x-text" style="width:250px"  
		       data-options="required:true" datatype="string"  maxlength="200"
		       nullable="no" maxsize="50" chname="机构名称">  
		</td>
      </tr>
      <tr>
        <td height="40"><span class="x_green_name">用户名</span>&nbsp;<span class="required">*</span></td>
        <td>
		<input id="x" name="x" type="text" size="20" class="easyui-validatebox x-text" style="width:250px"  
		       data-options="required:true" datatype="string"  maxlength="20"
		       nullable="no" maxsize="50" chname="用户名">（只能包含数字，大小写字母及下划线）   
		</td>
      </tr>
	  <tr>
        <td height="40"><span class="x_green_name">姓　名</span>&nbsp;<span class="required">*</span></td>
        <td>
		<input id="name" name="name" type="text" size="20" class="easyui-validatebox x-text" style="width:250px"  
		       data-options="required:true" datatype="string"  maxlength="20"
		       nullable="no" maxsize="50" chname="姓名">（可以包含数字，大小写字母及汉字）   
		</td>
      </tr>
      <tr>
        <td class="input-box2"  height="40"><span class="x_green_name">密　码</span>&nbsp;<span class="required">*</span></td>
        <td align="left">
		<input id="y" name="y" type="password" size="30" datatype="string" nullable="no" 
		       minsize="6" maxsize="20" chname="密码" class="easyui-validatebox x-text" style="width:250px"   
			   data-options="required:true"  maxlength="30">（6至30个字符，可以使用数字，大小写字母）  
		</td>
      </tr>
      <tr>
        <td class="input-box2"  height="40"><span class="x_green_name">确认密码</span>&nbsp;<span class="required">*</span></td>
        <td align="left">
		<input id="y2" name="y2" type="password" size="30"  datatype="string" nullable="no" 
		       minsize="6" maxsize="30" chname="确认密码" class="easyui-validatebox x-text" style="width:250px" 
			   data-options="required:true"  maxlength="30">（6至30个字符，可以使用数字，大小写字母）  
		</td>
      </tr>
	  <tr>
        <td height="40"><span class="x_green_name">手　机</span>&nbsp;<span class="required">*</span></td>
        <td>
          <input id="mobile" name="mobile" type="text" size="20" class="easyui-validatebox x-text" style="width:250px"
		         data-options="required:false" datatype="string" 
		         nullable="yes" maxsize="50" chname="手机"  maxlength="20">
           <#if enableSmsRegVerification == true>
		       &nbsp;<input name="btn_send2" type="button" value=" 发送 " class="btnGray" onclick="javascript:sendSms();">
		   </#if>
		</td>
      </tr>
	  <#if enableSmsRegVerification == true>
	  <tr>
        <td height="40"><span class="x_green_name">验证码</span>&nbsp;<span class="required">*</span></td>
        <td>
          <input id="verificationCode" name="verificationCode" type="text" size="20" class="easyui-validatebox x-text" style="width:250px"
		         data-options="required:false" datatype="string" 
		         nullable="yes" maxsize="50" chname="短信验证码"  maxlength="20">          
		</td>
      </tr>
	  </#if>
      <tr>
        <td height="40"><span class="x_green_name">邮　件</span>&nbsp;<span class="required">*</span></td>
        <td>
          <input id="mail" name="mail" type="text" size="20" class="easyui-validatebox x-text" style="width:250px"
		         data-options="required:true" datatype="mail" 
		         nullable="no" maxsize="50" chname="邮件"  maxlength="50">（用于邮箱验证及密码重置等）        
		</td>
    </tr>
	<tr>
		<td height="40" align="left"><span class="x_green_name">营业执照</span></td>
		<td align="left">
		     <input type="file" id="file" name="file" size="38" class="input-file"> 
			 （请拍照上传营业执照，5MB以内的jpg或gif图片，用于实名验证）
		</td>
	</tr>	
	<tr>
		<td height="40" align="left"><span class="x_green_name">性质</span></td>
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
		<td height="40" align="left"><span class="x_green_name">省份/直辖市</span></td>
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
		<td height="40" align="left"><span class="x_green_name">市</span></td>
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
		<td height="40" align="left"><span class="x_green_name">区/县</span></td>
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
		<td height="40" align="left"><span class="x_green_name">镇/街道</span></td>
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
           <input name="btn_submit" type="button" value=" 确 定 " class="btnGray" onclick="javascript:regXY();">
	    </td>
      </tr>

</table>
</form>

</body>
</html>
