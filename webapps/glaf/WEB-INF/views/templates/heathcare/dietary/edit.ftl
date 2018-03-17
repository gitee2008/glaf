<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function saveData(){
		if(document.getElementById("templateId").value==""){
			alert("食谱不能为空。");
			document.getElementById("name").focus();
			return;
		}
		if(document.getElementById("name").value==""){
			alert("名称不能为空。");
			document.getElementById("name").focus();
			return;
		}
        if(document.getElementById("typeId").value==""){
			alert("餐点不能为空。");
			document.getElementById("typeId").focus();
			return;
		}
		if(document.getElementById("dateString").value==""){
			alert("请选择日期。");
			document.getElementById("dateString").focus();
			return;
		}
		if(document.getElementById("week").value==""){
			alert("请选择周次。");
			document.getElementById("week").focus();
			return;
		}
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/saveDietary',
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
		if(document.getElementById("typeId").value=="0"){
			alert("餐点不能为空。");
			document.getElementById("typeId").focus();
			return;
		}
		if(document.getElementById("name").value==""){
			alert("名称不能为空。");
			document.getElementById("name").focus();
			return;
		}
		if(document.getElementById("dateString").value==""){
			alert("请选择日期。");
			document.getElementById("dateString").focus();
			return;
		}
		if(document.getElementById("week").value==""){
			alert("请选择周次。");
			document.getElementById("week").focus();
			return;
		}
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/saveDietary',
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

	function chooseDietaryTemplate(){
		var selected = jQuery("#templateId").val();
        var link = '${contextPath}/heathcare/dietaryTemplate/search?elementId=templateId&elementName=name&selected='+selected;
        var x=100;
        var y=100;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 985, 460);
	}

</script>
</head>
<body>
<div style="margin:0px;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;编辑食谱</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
	<#if dietary.id?exists>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveAsData();" >另存</a> 
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${dietary.id}"/>
  <input type="hidden" id="templateId" name="templateId" value="${dietary.templateId}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">名称</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox  x-text" style=" width:385px;"
				   value="${dietary.name}" onclick="javascript:chooseDietaryTemplate();" readonly/>&nbsp;
			<img src="${contextPath}/static/images/application_view_tile.png" border="0" 
			     onclick="javascript:chooseDietaryTemplate();">
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">描述</td>
		<td align="left">
		    <textarea  id="description" name="description" rows="6" cols="46" class="x-text" style="height:90px;width:385px;" >${dietary.description}</textarea>
		</td>
	</tr>
 
	<tr>
		<td width="20%" align="left">餐点</td>
		<td align="left">
           <select id="typeId" name="typeId">
			<option value="">----请选择----</option>
			<#list dictoryList as d>
			<option value="${d.id}">${d.name}</option>
			</#list> 
		  </select>
		  <script type="text/javascript">
			   document.getElementById("typeId").value="${dietary.typeId}";
		  </script>
		</td>
	</tr>

    <tr>
		<td width="20%" align="left">日期</td>
		<td align="left">
           <input id="dateString" name="dateString" type="text" 
			       class="easyui-datebox x-text" style="width:120px"
			       <#if date?exists>
				   value="${date?string('yyyy-MM-dd')}"
				   </#if>>
		</td>
	</tr>

	<tr>
		<td width="20%" align="left">周次</td>
		<td align="left">
          <select id="week" name="week">
			<#list weeks as week>
			<option value="${week}">${week}</option>
			</#list>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("week").value="${dietary.week}";
		  </script>
		</td>
	</tr>
   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>