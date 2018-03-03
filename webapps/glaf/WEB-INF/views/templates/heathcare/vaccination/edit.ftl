<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>预防接种登记</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/vaccination/saveVaccination',
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

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/vaccination/saveVaccination',
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
    <div data-options="region:'north',split:false,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;">&nbsp;<img src="${contextPath}/static/images/window.png">&nbsp;编辑预防接种登记</span>
	    <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${vaccination.id}"/>
  <table class="easyui-form" style="width:600px; margin-top:8px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">姓名</td>
		<td align="left">
		   <#if person?exists>
             ${person.name}
			 <input type="hidden" id="personId" name="personId" value="${personId}"/>
		   <#else>
			<select id="personId" name="personId">
			    <option value="">--请选择--</option>
				<#list persons as person>
			    <option value="${person.id}">${person.name}</option>
			    </#list> 
			</select>
			</#if>
		</td>
	</tr> 
	<tr>
		<td width="20%" align="left">疫苗</td>
		<td align="left">
             <select  id="vaccine" name="vaccine">
				<option value="">----请选择----</option>
				<option value="KJ">卡介苗</option>
				<option value="JG">甲肝疫苗</option>
				<option value="YG">乙肝疫苗</option>
				<option value="GH">脊灰疫苗</option>
				<option value="BRP">百日破疫苗</option>
				<option value="MZ">麻疹疫苗</option>
				<option value="YL">乙脑苗</option>
				<option value="LA">流脑A苗</option>
				<option value="LAC">流脑A+C</option>
				<option value="FZ">风疹疫苗</option>
				<option value="SS">腮腺炎疫苗</option>
		    </select>
			<script type="text/javascript">
			     document.getElementById("vaccine").value="${vaccination.vaccine}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">接种次数</td>
		<td align="left">
			 <select id="sortNo" name="sortNo">
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
		    </select>
			<script type="text/javascript">
			     document.getElementById("sortNo").value="${vaccination.sortNo}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">接种日期</td>
		<td align="left">
			<input id="inoculateDate" name="inoculateDate" type="text" 
			       class="easyui-datebox x-text" style="width:100px"
			       <#if vaccination.inoculateDate?exists>
				   value="${vaccination.inoculateDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">接种医生</td>
		<td align="left">
            <input id="doctor" name="doctor" type="text" 
			       class="easyui-validatebox  x-text"  style="width:150px"			
				   value="${vaccination.doctor}"/>
		</td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>