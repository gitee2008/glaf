<!DOCTYPE html>
<html>
<title>角色选择</title>
<script language="javascript">

 var contextPath="${contextPath}";

 function addElement() {
        var list = document.iForm.noselected;
        for (i = 0; i < list.length; i++) {
            if (list.options[i].selected) {
                var value = list.options[i].value;
                var text = list.options[i].text;
                addToList(value, text);
				list.remove(i);
				i=i-1;
            }
        }

    }

 function addToList(value, text) {
        var list = document.iForm.selected;
        if (list.length > 0) {
            for (k = 0; k < list.length; k++) {
                if (list.options[k].value == value) {
                    return;
                }
            }
        }

        var len = list.options.length;
        list.length = len + 1;
        list.options[len].value = value;
        list.options[len].text = text;
    }

 function removeElement() {
        var list = document.iForm.selected;
		var slist = document.iForm.noselected;
        if (list.length == 0 || list.selectedIndex < 0 || list.selectedIndex >= list.options.length)
            return;

        for (i = 0; i < list.length; i++) {
            if (list.options[i].selected) {
			    var value = list.options[i].value;
                var text = list.options[i].text;
                list.options[i] = null;
                i--;
				var len = slist.options.length;
				slist.length = len+1;
                slist.options[len].value = value;
                slist.options[len].text = text;				
            }
        }
    }

 
  function saveData() {
	//var parent_window = getOpener();
	//var x_roles = parent_window.document.getElementById("${elementId}");
    //var x_roles_name = parent_window.document.getElementById("${elementName}");

    var len= document.iForm.selected.length;
	var result = "";
	var names = "";
	for (var i=0; i<len; i++) {
      result = result + document.iForm.selected.options[i].value;
	  names = names + document.iForm.selected.options[i].text;
	  if(i < (len - 1)){
		  result = result + ",";
		  names = names + ",";
	   }
    }

	//x_roles.value = result;
	//x_roles_name.value = names;
     
	//window.close();
    var params = jQuery("#iForm").formSerialize();
    jQuery.ajax({
				   type: "POST",
				   url: "${contextPath}/sys/organization/saveRoles?organizationId=${organizationId}&items="+result,
				   dataType:  'json',
				   data: params,
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
						    //window.parent.location.reload();		        
					   }
				   }
			 });
  }

  function switchMenuX(){
	 var menuFlag = document.getElementById("menuFlag").value;
     location.href="${contextPath}/sys/organization/showRole?organizationId=${organizationId}&menuFlag="+menuFlag;
  }
</script>

<body >
<center>
<form id="iForm" name="iForm" class="x-form" method="post">
<input type="hidden" id="organizationId" name="organizationId" value="${organizationId}">
<div class="content-block" style="width: 100%;"><br>
<div class="x_content_title"><img
	src="${contextPath}/static/images/window.png" alt="机构角色设置">&nbsp;
机构角色设置</div>
<fieldset class="x-fieldset" style="width: 98%;">
<table class="table-border" align="center" cellpadding="4" cellspacing="1" width="90%">
	<tbody>
	    <tr>
		    <td class="beta" colspan="3" align="left">
			  &nbsp;&nbsp;角色选项&nbsp;
			  <select id="menuFlag" name="menuFlag" onchange="javascript:switchMenuX();">
				<option value="0">角色下放</option>
				<option value="9">角色菜单</option>
			  </select>
			  <script type="text/javascript">
			      document.getElementById("menuFlag").value="${menuFlag}";
			  </script>
			</td>
			<td class="beta" colspan="3" align="left">
			 &nbsp;${organization.name}下级机构是否使用同样设置&nbsp;
			 <select id="isPropagationAllowed" name="isPropagationAllowed">
				<option value="">----请选择----</option>
				<option value="Y">是</option>
				<option value="N">否</option>
			 </select>
			 <script type="text/javascript">
			      document.getElementById("isPropagationAllowed").value="${isPropagationAllowed}";
			 </script>
			</td>
		</tr>
		<tr>
			<td class="beta" colspan="2">
			<div align="left">&nbsp;&nbsp;<b>可选角色</b></div>
			</td>
			<td class="beta"></td>
			<td class="beta" colspan="3">
			<div align="left">&nbsp;&nbsp;<b>已选角色</b></div>
			</td>
		</tr>
		<tr>
			<td class="beta" width="18">&nbsp;</td>
			<td class="table-content" height="26" valign="top" width="390">
			<div align="center"><select class="list"
				style="width: 300px; height: 250px;" multiple="multiple" size="12"
				name="noselected" ondblclick="addElement()">
			</select></div>
			</td>
			<td class="beta" width="114">
			<div align="center"><input name="add" value="添加 ->"
				onclick="addElement()" class="btn" type="button"> <br>
			<br>
			<input name="remove" value="<- 删除" onclick="removeElement()"
				class="btn" type="button"></div>
			</td>
			<td class="table-content" height="26" valign="top" width="359">
			<div align="center"><select class="list"
				style="width: 300px; height: 250px;" multiple="multiple" size="12"
				name="selected" ondblclick="removeElement()">
			</select></div>
			</td>
			<td class="beta" width="23">&nbsp;</td>
		</tr>
	</tbody>
</table>
</fieldset>

<div align="center">
<br>
<input value="确 定" class="btn btn-primary" name="submit252" type="button" onclick="javacsript:saveData();">
</div>
</div>
</form>
</center>