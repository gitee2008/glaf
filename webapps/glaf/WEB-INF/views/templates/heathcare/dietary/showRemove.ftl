<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除食谱</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function deleteWeekData(){
		if(document.getElementById("year").value==""){
			alert("请选择年份。");
			document.getElementById("year").focus();
			return;
		}

		if(document.getElementById("week").value==""){
			alert("请选择周次。");
			document.getElementById("week").focus();
			return;
		}

        if(confirm("数据删除后不能恢复，确定删除数据吗？")){
		  var params = jQuery("#iForm").formSerialize();
		  jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/removeWeekPlan',
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
					       //window.parent.location.reload();
					   } 
				   }
			 });
		}
	}

    function deleteDayData(){
		if(document.getElementById("dateString").value==""){
			alert("请选择日期。");
			document.getElementById("dateString").focus();
			return;
		}

        if(confirm("数据删除后不能恢复，确定删除数据吗？")){
		  var params = jQuery("#iForm").formSerialize();
		  jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/removeDayPlan',
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
					       //window.parent.location.reload();
					   } 
				   }
			 });
		}
	}

</script>
</head>

<body>
<div style="margin:20px;"></div>  
<div class="easyui-layout" data-options="fit:true">  
 <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <table  valign="top">
  <tr>
	<td>
	  <table class="easyui-form" style="width:250px;" align="top">
		<tbody>
		<tr>
		   <td width="100%" colspan="2" align="left"><span class="x_content_title">&nbsp;&nbsp;删除一周数据</span></td>
		</tr>
		<tr>
			<td width="20%" align="left">&nbsp;&nbsp;年份</td>
			<td align="left">
				<select id="year" name="year">
				<#list years as year>
				<option value="${year}">${year}</option>
				</#list>
			  </select>
			</td>
		</tr>
		<tr>
			<td width="20%" align="left">&nbsp;&nbsp;周次</td>
			<td align="left">
			  <select id="week" name="week">
				<#list weeks as week>
				<option value="${week}">${week}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				  document.getElementById("week").value="${maxWeek}";
			  </script>
			</td>
		</tr>
		<tr>
		   <td width="20%" align="left">&nbsp;</td>
           <td align="left">
		       <input type="button" value="确定" onclick="javascript:deleteWeekData();" class="btnGray">
		   </td>
		</tr>
	   </tbody>
	  </table>
	</td>
	<td>
	  <table class="easyui-form" style="width:300px;" align="center" valign="top">
		<tbody>
		<tr>
		   <td width="100%" colspan="2" align="left"><span class="x_content_title">删除一天数据</span></td>
		</tr>
		<tr>
			<td width="20%" align="left">日期</td>
			<td align="left">
			  <input id="dateString" name="dateString" type="text" class="easyui-datebox x-text" style="width:120px"
			  <#if dateString?exists> value="${dateString}" </#if>>
			</td>
		</tr>
		<tr>
           <td width="20%" align="left">&nbsp;</td>
           <td align="left">
		       <input type="button" value="确定" onclick="javascript:deleteDayData();" class="btnGray">
		   </td>
		</tr>
	   </tbody>
	  </table>
	</td>
  </tr>
  </table>
 </form>
</div>
</div>
</body>
</html>