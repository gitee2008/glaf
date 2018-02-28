<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改用户</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script language="javascript">

    var contextPath = "${contextPath}";

    function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/user/saveModify',
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
					       window.close();
					   }  
				   }
			 });
	}

	function setValue(obj){
	  obj.value=obj[obj.selectedIndex].value;
	}

	function chooseOrganization(){
		var selected = jQuery("#organizationId").val();
        var link = '${contextPath}/sys/organization/showTreeRadio?elementId=organizationId&elementName=organizationName&selected='+selected;
        var x=100;
        var y=100;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link,self,x, y, 795, 580);
	}

</script>
</head>

<body>
<div class="nav-title"><span class="Title">用户管理</span>&gt;&gt;修改用户</div>
<form id="iForm" name="iForm" action="${contextPath}/sys/user/saveModify" method="post"  > 
<input type="hidden" name="userId" value="${userId_encode}">
<table width="95%" border="0" align="center" cellpadding="0" cellspacing="0" class="box">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr class="box">
        <td class="box-lt">&nbsp;</td>
        <td class="box-mt">&nbsp;</td>
        <td class="box-rt">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td class="box-mm"><table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
      <tr>
        <td width="20%" class="input-box2">用户名</td>
        <td width="80%">${user.userId}</td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">机 构</td>
        <td>
		    <input type="hidden" id="organizationId" name="organizationId" value="${user.organizationId}">
		 	<input id="organizationName" name="organizationName" type="text" size="30" class="x-text" datatype="string" nullable="no" 
			       maxsize="200" chname="机构名称" readonly onclick="javascript:chooseOrganization();" value="${organizationName}"> &nbsp;
			<img src="${contextPath}/static/images/orm_root.gif" border="0" onclick="javascript:chooseOrganization();"
			     style="cursor:pointer;">	
	   </td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">姓　名*</td>
        <td><input name="name" type="text" size="30" class="x-text" value="${user.name}" datatype="string" nullable="no" maxsize="20" chname="姓名"></td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">性　别</td>
        <td>
          <input type="radio" name="sex" value="0" <#if user.sex==0>checked</#if>>女
		  &nbsp;
          <input type="radio" name="sex" value="1" <#if user.sex==1>checked</#if>>男
		</td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">手　机</td>
        <td>
          <input name="mobile" type="text" size="30" class="x-text" datatype="string" 
		         value="${user.mobile}" nullable="yes" maxsize="12" chname="手机">        
		</td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">邮　件</td>
        <td>
          <input name="email" type="text" size="30" class="x-text" datatype="email" 
		         value="${user.email}" nullable="yes" maxsize="50" chname="邮件">       
		</td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">办公电话</td>
        <td>
          <input name="telephone" type="text" size="30" class="x-text" datatype="string" 
		         value="${user.telephone}"  nullable="yes" maxsize="20" chname="办公电话">        
		</td>
      </tr>
	  <tr>
        <td class="input-box2" valign="top">职位</td>
        <td>
		    <select id="headship" name="headship">
			  <option value="0">----请选择----</option>
			  <#list  dictories as a>
				<option value="${a.code}">${a.name} [${a.code}]</option>
			  </#list>
		   </select>
		   <script type="text/javascript">
		        document.getElementById("headship").value="${user.headship}";
		   </script>
		</td>
      </tr>

      <tr>
        <td class="input-box2" valign="top">是否有效</td>
        <td>
          <input type="radio" name="locked" value="0" <#if user.locked==0>checked</#if>>是
          <input type="radio" name="locked" value="1" <#if user.locked==1>checked</#if>>否
		</td>
      </tr>
 
      <tr>
        <td colspan="2" align="center" valign="bottom" height="30">&nbsp;
              <input name="btn_save2" type="button" value=" 确定 " class="btnGray" onclick="javascript:saveData();">
	    </td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td>
	 <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr class="box">
        <td class="box-lb">&nbsp;</td>
        <td class="box-mb">&nbsp;</td>
        <td class="box-rb">&nbsp;</td>
      </tr>
    </table>
	</td>
  </tr>
</table>
</form>
</body>
</html>
