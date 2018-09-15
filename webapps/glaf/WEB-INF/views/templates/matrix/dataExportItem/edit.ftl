<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>导出项</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
    var contextPath="${request.contextPath}";

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/matrix/dataExportItem/save',
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
				   url: '${request.contextPath}/matrix/dataExportItem/save',
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
	<span class="x_content_title">编辑导出项</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${dataExportItem.id}"/>
  <input type="hidden" id="expId" name="expId" value="${expId}"/>
  <table class="easyui-form" style="width:788px;" align="center">
    <tbody>
	<tr>
		<td width="15%" align="left">名称</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox x-text" style="width:525px;" 
				   value="${dataExportItem.name}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">标题</td>
		<td align="left">
            <input id="title" name="title" type="text" 
			       class="easyui-validatebox x-text" style="width:525px;" 
				   value="${dataExportItem.title}"/>
		</td>
	</tr>
	<tr>
		<td width="90" align="left">SQL循环条件语句</td>
		<td align="left">
		    <textarea id="recursionSql" name="recursionSql" rows="4" cols="46" class="x-textarea" style="width:525px;height:90px;" >${dataExportItem.recursionSql}</textarea>
		   <div style="margin-top:5px;">
		     （提示：SQL循环条件的结果当作下面取数语句的输入条件。）
			<br>
	       </div>
		</td>
	</tr>
	<tr>
		<td width="90" align="left">循环列</td>
		<td align="left">
            <input id="recursionColumns" name="recursionColumns" type="text" 
			       class="easyui-validatebox x-text" style="width:525px;"  
				   value="${dataExportItem.recursionColumns}"/>
			<br>（提示：写到目标表的条件列,多个列之间用半角的逗号隔开。）
		</td>
	</tr>
	<tr>
		<td width="90" align="left">主键列</td>
		<td align="left">
            <input id="primaryKey" name="primaryKey" type="text" 
			       class="easyui-validatebox x-text" style="width:525px;"  
				   value="${dataExportItem.primaryKey}"/>
			<br>
		</td>
	</tr>
	<tr>
		<td width="90" align="left">SQL取数语句</td>
		<td align="left">
		    <textarea id="sql" name="sql" rows="6" cols="46" class="x-textarea" style="width:525px;height:320px;" >${dataExportItem.sql}</textarea>
		   <div style="margin-top:5px;">
		     （提示：可以使用union语句组合结果。）
			<br>
			<span>
			 （可以使用动态参数,也可以使用循环语句的输出变量当输入,例如: column1 = {param1}。）
			</span>
			<br>
			<span>
			 （today_start内置变量代表当天的开始时间, today_end内置变量代表当天的结束时间。）
			</span>
			<br>
			<span>
			 （yesterday_start内置变量代表昨天的开始时间, yesterday_end内置变量代表昨天的结束时间。）
			</span>
			<br>
			<span>
			 （today_curr_hour_start内置变量代表当前小时的开始时间, today_curr_hour_end内置变量代表当前小时的结束时间。）
			</span>
			<br>
			<span>
			 （today_previous_hour_start内置变量代表前一小时的开始时间, today_previous_hour_end内置变量代表前一小时的结束时间。）
			</span>
			<span>
			 （the_end_of_last_month内置变量代表上月最后一天的时间。）
			</span>
	      </div>
		</td>
	</tr>

    <tr>
		<td width="15%" align="left">结果类型</td>
		<td align="left">
		    <select id="resultFlag" name="resultFlag">
			    <option value="M">多条记录</option>
			    <option value="S">单一记录</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("resultFlag").value="${dataExportItem.resultFlag}";
             </script>
		</td>
    </tr>

    <tr>
		<td width="15%" align="left">执行顺序</td>
		<td align="left">
		    <select id="sortNo" name="sortNo">
			    <option value="0">----请选择----</option>
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
			    <option value="8">8</option>
				<option value="9">9</option>
				<option value="10">10</option>
             </select>
             <script type="text/javascript">
                 document.getElementById("sortNo").value="${dataExportItem.sortNo}";
             </script>
			 &nbsp;（提示：顺序小的先执行。）
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
                 document.getElementById("locked").value="${dataExportItem.locked}";
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