<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>联系人信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personLinkman/savePersonLinkman',
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

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personLinkman/savePersonLinkman',
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
  <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑联系人信息</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${personLinkman.id}"/>
  <input type="hidden" id="personId" name="personId" value="${personId}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">姓名</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox  x-text" style="width:120px;" 
				   value="${personLinkman.name}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">手机号码</td>
		<td align="left">
            <input id="mobile" name="mobile" type="text" 
			       class="easyui-validatebox  x-text" style="width:120px;" maxlength="11" size="11"
				   value="${personLinkman.mobile}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">工作单位</td>
		<td align="left">
            <input id="company" name="company" type="text" 
			       class="easyui-validatebox  x-text"  style="width:350px;"
				   value="${personLinkman.company}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">关系</td>
		<td align="left">
		    <select id="relationship" name="relationship">
				<option value="">----请选择----</option>
				<option value="father">父亲</option>
				<option value="mother">母亲</option>
				<option value="stepfather">继父</option>
				<option value="stepmother">继母</option>
				<option value="grandpa">祖父</option>
				<option value="grandma">祖母</option>
				<option value="grandpa_2">外祖父</option>
				<option value="grandma_2">外祖母</option>
				<option value="brother">哥哥</option>
				<option value="sister">姐姐</option>
				<option value="uncle">叔叔</option>
				<option value="aunt">阿姨</option>
		    </select>
            <script type="text/javascript">
               document.getElementById("relationship").value="${personLinkman.relationship}";
            </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">是否监护人</td>
		<td align="left">
		    <select id="wardship" name="wardship">
				<option value="">----请选择----</option>
				<option value="Y">是</option>
				<option value="N">否</option>
		    </select>
            <script type="text/javascript">
               document.getElementById("wardship").value="${personLinkman.wardship}";
            </script>
		</td>
	</tr>

	<!-- <tr>
		<td width="20%" align="left">电子邮件</td>
		<td align="left">
            <input id="mail" name="mail" type="text" 
			       class="easyui-validatebox  x-text"  style="width:350px;"
				   value="${personLinkman.mail}"/>
		</td>
	</tr> -->

	<tr>
		<td width="20%" align="left">备注</td>
		<td align="left">
		    <textarea id="remark" name="remark" rows="8" cols="50" style="width:350px;height:90px;"  class="easyui-validatebox  x-text" >${personLinkman.remark}</textarea>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left"></td>
		<td align="left">
		    <br><br><br><br>
		</td>
	</tr>
 
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>