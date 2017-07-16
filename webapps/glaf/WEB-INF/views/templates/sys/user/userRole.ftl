<!DOCTYPE html>
<html>
<head>
<title>用户角色</title>
<#include "/inc/init_easyui_import.ftl"/>

<script language="javascript">

    var contextPath = "${contextPath}";

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
		for (var i=0;i<len;i++) {
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

		document.getElementById("objectIds").value=result;

		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/user/saveUserRoles',
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

    function closeXY(){
     if(window.opener != null){
		 window.close();
	 } else {
        history.back();
	 }
    }

</script>
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0">
<center> <br>
<form id="iForm" name="iForm" class="x-form" method="post">
<input type="hidden" id="objectIds" name="objectIds"> 
<input type="hidden" id="userId" name="userId" value="${userId}"> 
<div class="content-block" style="width: 785px;"> 
<div class="x_content_title"><img
	src="${contextPath}/static/images/window.png"
	alt="用户角色设置">&nbsp;用户角色设置</div>

 
<table class="table-border" align="center" cellpadding="4"
	cellspacing="1" width="90%">
	<tbody>		 
		<tr>
			<td colspan="2" width="212">
			<div align="center">
			   <div align="center">可选角色</div>
			</div>
			</td>
			<td></td>
			<td colspan="2" width="212">
			<div align="center">已选角色</div>
			</td>
		</tr>
		<tr>
			<td width="2">&nbsp;</td>
			<td height="26" valign="top" width="182">
			<div align="center"><select class="list" id="noselected" name="noselected" 
				style="width: 240px; height: 280px;" multiple="multiple" size="12"
				ondblclick="addElement()">${bufferx}
			</select></div>
			</td>
			<td width="114">
			<div align="center"><input name="add" value="添加->"
				onclick="addElement()" class="btnGray" type="button"> <br>
			<br>
			<input name="remove" value="<-删除" onclick="removeElement()"
				class="btnGray" type="button"></div>
			</td>
			<td height="26" valign="top" width="212">
			<div align="center"><select id="selected" name="selected" class="list"
				style="width: 240px; height: 280px;" multiple="multiple" size="12"
				ondblclick="removeElement()">${buffery}
			</select></div>
			</td>
			<td width="23">&nbsp;</td>
		</tr>
	</tbody>
</table>
 
<div align="center"><br />
 
 <input value=" 确 定 " class=" btnGray " name="button" type="button"
		onclick="javacsript:saveData();">
<br />
</div>

</div>
</form>
</center>

 