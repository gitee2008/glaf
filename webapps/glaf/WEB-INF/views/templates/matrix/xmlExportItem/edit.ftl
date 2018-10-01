<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>输出项</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
    var contextPath="${request.contextPath}";

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/matrix/xmlExportItem/save',
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
					   if(data.statusCode == 200) { 
					       parent.location.reload(); 
					   }
				   }
			 });
	}

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/matrix/xmlExportItem/save',
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
				   }
			 });
	}

</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:40px"> 
    <div class="toolbar-backgroud"> 
	<span class="x_content_title">编辑输出项</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${xmlExportItem.id}"/>
  <input type="hidden" id="expId" name="expId" value="${expId}"/>
  <table class="easyui-form" style="width:98%;" align="center">
    <tbody>
	<tr>
		<td width="15%" align="left">名称</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox x-text" style="width:425px;" 
				   value="${xmlExportItem.name}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">标题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox x-text" style="width:425px;" 
				   value="${xmlExportItem.title}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">表达式</td>
		<td align="left">
            <input id="expression" name="expression" type="text" 
			       class="easyui-validatebox x-text" style="width:425px;" 
				   value="${xmlExportItem.expression}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">是否必须</td>
		<td align="left">
		    <select id="required" name="required">
			    <option value="N">否</option>
			    <option value="Y">是</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("required").value="${xmlExportItem.required}";
             </script>
		</td>
    </tr>
	<tr>
		<td width="15%" align="left">数据类型</td>
		<td align="left">
		    <select id="dataType" name="dataType">
			    <option value="Integer">整数型</option>
			    <option value="Long">长整数型</option>
				<option value="Double">数值型</option>
				<option value="Date">日期型</option>
				<option value="String">字符型</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("dataType").value="${xmlExportItem.dataType}";
             </script>
		</td>
    </tr>
	<tr>
		<td width="15%" align="left">默认值</td>
		<td align="left">
            <input id="defaultValue" name="defaultValue" type="text" 
			       class="easyui-validatebox x-text" style="width:425px;" 
				   value="${xmlExportItem.defaultValue}"/>
			<div style="margin-top:5px;">
		     （提示：当该字段的查询结果为空null时的取值。）
	        </div>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">类型</td>
		<td align="left">
		    <select id="tagFlag" name="tagFlag">
			    <option value="A">属性</option>
			    <option value="E">子元素</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("tagFlag").value="${xmlExportItem.tagFlag}";
             </script>
		</td>
    </tr>
    <tr>
		<td width="15%" align="left">顺序</td>
		<td align="left">
		    <select id="sortNo" name="sortNo">
			    <option value="0">----请选择----</option>
				<#list sortNoList as sortNo>
				<option value="${sortNo}">${sortNo}</option>
				</#list>
             </select>
             <script type="text/javascript">
                 document.getElementById("sortNo").value="${xmlExportItem.sortNo}";
             </script>
		</td>
	  </tr>

	  <tr>
		<td width="15%" align="left">是否有效</td>
		<td align="left">
		    <select id="locked" name="locked">
			    <option value="0">是</option>
				<option value="1">否</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("locked").value="${xmlExportItem.locked}";
             </script>
		</td>
	</tr>
	<tr>
	  <td><br><br><br><br><td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>