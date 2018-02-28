<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改机构</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script language="javascript">

    var contextPath = "${contextPath}";

    function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/organization/saveModify',
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
 
 	function chooseParent(){
		var selected = jQuery("#parentId").val();
        var link = '${contextPath}/sys/organization/showTreeRadio?elementId=parentId&elementName=parentName&selected='+selected;
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
<div class="nav-title">
<span class="Title">机构管理</span>&gt;&gt;修改机构</div>
<form id="iForm" name="iForm"  method="post" > 
<input type="hidden" name="id" value="${organization.id}">
<input type="hidden" name="organizationId" value="${organization.id}">
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
    <td class="box-mm">
	<table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
      <tr>
        <td class="input-box2">上级机构</td>
        <td>
		<input type="hidden" id="parentId" name="parentId" value="${organization.parentId}">
		 	<input id="parentName" name="parentName" type="text" size="37" class="x-text" datatype="string" nullable="no" 
			       maxsize="200" chname="上级机构名称" readonly onclick="javascript:chooseParent();" value="${parentName}">
			 &nbsp;<img src="${contextPath}/static/images/orm_root.gif" border="0" onclick="javascript:chooseParent();"
			            style="cursor:pointer;">
		</td>
      </tr>
      <tr>
        <td class="input-box2">机构名称*</td>
        <td><input name="name" type="text" class="x-text" id="name" value="${organization.name}" size="37" datatype="string" nullable="no" maxsize="200" chname="机构名称"></td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">描　述</td>
        <td><textarea name="description" cols="39" rows="8" class="x-textarea" id="description" datatype="string" nullable="yes" maxsize="1000" chname="描述">${organization.description}</textarea></td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">代　码</td>
        <td><input name="code" type="text" class="x-text" id="code" value="${organization.code}" size="37"  datatype="string" nullable="no" maxsize="10" chname="代码"></td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">编　码</td>
        <td><input name="no" type="text" class="x-text" id="no" value="${organization.no}" size="37"  datatype="string" nullable="no" maxsize="10" chname="编码"></td>
      </tr>
	  <tr>
        <td class="input-box2" valign="top">地　址</td>
        <td><input name="address" type="text" size="37" class="x-text" value="${organization.address}" datatype="string" nullable="no" maxsize="100" chname="地址"></td>
      </tr>
	  <tr>
        <td class="input-box2" valign="top">电　话</td>
        <td><input name="telphone" type="text" size="37" class="x-text"  value="${organization.telphone}" datatype="string" nullable="no" maxsize="100" chname="电话"></td>
      </tr>
	  <tr>
        <td class="input-box2" valign="top">负责人</td>
        <td><input name="principal" type="text" size="37" class="x-text"  value="${organization.principal}" datatype="string" nullable="no" maxsize="100" chname="负责人"></td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">机构区分</td>
        <td><input name="code2" type="text" size="37" class="x-text" value="${organization.code2}" datatype="string" nullable="no" maxsize="10" chname="机构区分"></td>
      </tr>
	  <tr>
        <td class="input-box2" valign="top">机构类型</td>
        <td>
		    <select id="type" name="type">
			  <option value="">----请选择----</option>
			  <#list  dictories as a>
				<option value="${a.ext11}">${a.name} [${a.ext11}]</option>
			  </#list>
		   </select>
		   <script type="text/javascript">
		        document.getElementById("level").value="${organization.level}";
		   </script>
		</td>
      </tr>
      <tr>
        <td class="input-box2" valign="top">是否有效</td>
        <td>
          <input type="radio" name="locked" value="0" <#if organization.locked==0>checked</#if>>是
          <input type="radio" name="locked" value="1" <#if organization.locked==1>checked</#if>>否
		</td>
      </tr>

	  <tr>
        <td colspan="2" align="center" valign="bottom" height="30">&nbsp;
             <input name="btn_save" type="button" value=" 确定 " class="btnGray" onclick="javascript:saveData();">
		</td>
      </tr>
    </table>
   </td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr class="box">
        <td class="box-lb">&nbsp;</td>
        <td class="box-mb">&nbsp;</td>
        <td class="box-rb">&nbsp;</td>
      </tr>
    </table></td>
  </tr>
</table>
</form> 
</body>
</html>
