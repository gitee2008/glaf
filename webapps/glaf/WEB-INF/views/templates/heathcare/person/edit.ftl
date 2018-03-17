<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学生信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

  var contextPath = "${contextPath}";

  <#if privilege_write == true>
	function saveData(){
		if(document.getElementById("name").value==""){
			alert("姓名不能为空。");
			document.getElementById("name").focus();
			return;
		}
		if(document.getElementById("birthday").value==""){
			alert("出生日期不能为空。");
			document.getElementById("birthday").focus();
			return;
		}
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/person/savePerson',
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
		if(document.getElementById("name").value==""){
			alert("姓名不能为空。");
			document.getElementById("name").focus();
			return;
		}
		if(document.getElementById("birthday").value==""){
			alert("出生日期不能为空。");
			document.getElementById("birthday").focus();
			return;
		}
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/person/savePerson',
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
  </#if>

</script>
</head>
<body>
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	&nbsp;<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑学生信息</span>
	<#if privilege_write == true>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存
	</a> 
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${person.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">班级</td>
		<td align="left">
            <select id="gradeId" name="gradeId">
				<#list gradeInfos as grade>
			    <option value="${grade.id}">${grade.name}</option>
			    </#list> 
			</select>
			<script type="text/javascript">
				document.getElementById("gradeId").value="${gradeId}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">姓名</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox  x-text" style="width:175px;" 
				   value="${person.name}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">身份证编号</td>
		<td align="left">
            <input id="idCardNo" name="idCardNo" type="text" precision="0" 
			       class="easyui-validatebox  x-text" style="width:180px;" 
				   value="${person.idCardNo}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">省份</td>
		<td align="left">
			<select id="provinceId" name="provinceId" onchange="javascript:selectDistrict('provinceId', 'cityId');">
			    <option value="">----请选择----</option>
				<#list provinces as province>
				<option value="${province.id}">${province.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    //selectProvince("provinceId");
                document.getElementById("provinceId").value="${person.provinceId}";
            </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">市</td>
		<td align="left">
			<select id="cityId" name="cityId" onchange="javascript:selectDistrict('cityId', 'areaId');">
			    <option value="">----请选择----</option>
				<#list citys as city>
				<option value="${city.id}">${city.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    <#if citys?exists>
				  document.getElementById("cityId").value="${person.cityId}";
				<#else>
				  selectDistrict("cityId", document.getElementById("provinceId").value);
				</#if> 
            </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">区县</td>
		<td align="left">
		    <select id="areaId" name="areaId" onchange="javascript:selectDistrict('areaId', 'townId');">
			    <option value="">----请选择----</option>
				<#list areas as area>
				<option value="${area.id}">${area.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    <#if areas?exists>
				  document.getElementById("areaId").value="${person.areaId}";
				<#else>
				  selectDistrict("areaId", document.getElementById("cityId").value);
				</#if>
            </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">镇</td>
		<td align="left">
		    <select id="townId" name="townId">
			    <option value="">----请选择----</option>
				<#list towns as town>
				<option value="${town.id}">${town.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    <#if towns?exists>
				  document.getElementById("townId").value="${person.townId}";
				<#else>
				  selectDistrict("townId", document.getElementById("areaId").value);
				</#if>
            </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">家庭住址</td>
		<td align="left">
            <input id="homeAddress" name="homeAddress" type="text" 
			       class="easyui-validatebox  x-text"  style="width:350px;"
				   value="${person.homeAddress}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">出生地</td>
		<td align="left">
            <input id="birthAddress" name="birthAddress" type="text" 
			       class="easyui-validatebox  x-text" style="width:350px;" 
				   value="${person.birthAddress}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">性别</td>
		<td align="left">
		  <input type="radio" name="sex" value="0" <#if person.sex == "0">checked</#if>>女&nbsp;&nbsp;
	      <input type="radio" name="sex" value="1" <#if person.sex == "1">checked</#if>>男&nbsp;&nbsp;
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">出生日期</td>
		<td align="left">
			<input id="birthday" name="birthday" type="text" 
			       class="easyui-datebox x-text" style="width:100px;"
				   <#if person.birthday?if_exists>
				   value="${person.birthday?string('yyyy-MM-dd')}"
				   </#if>>
            &nbsp;&nbsp;入园日期&nbsp;
			<input id="joinDate" name="joinDate" type="text" 
			       class="easyui-datebox x-text" style="width:100px;"
				   <#if person.joinDate?if_exists>
				   value="${person.joinDate?string('yyyy-MM-dd')}"
				   </#if>>
		</td>
	</tr>

	<tr>
		<td width="20%" align="left">入园时身高</td>
		<td align="left">
            <input id="height" name="height" type="text" maxlength="3"
			       class="easyui-numberbox  x-text"  style="width:60px; text-align:right;"
				   value="${person.height}">（单位：厘米cm）		 
		    &nbsp;&nbsp;入园时体重&nbsp;
            <input id="weight" name="weight" type="text" maxlength="5"
			       class="easyui-numberbox  x-text"  style="width:60px; text-align:right;"
				   value="${person.weight}">（单位：千克kg）
		</td>
	</tr>

	<tr>
		<td width="20%" align="left">健康状况</td>
		<td align="left">
            <input id="healthCondition" name="healthCondition" type="text" 
			       class="easyui-validatebox  x-text"  style="width:350px;"
				   value="${person.healthCondition}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">喂养史</td>
		<td align="left">
            <input id="feedingHistory" name="feedingHistory" type="text" 
			       class="easyui-validatebox  x-text"  style="width:350px;"
				   value="${person.feedingHistory}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">既往史</td>
		<td align="left">
            <input id="previousHistory" name="previousHistory" type="text" 
			       class="easyui-validatebox  x-text"  style="width:350px;"
				   value="${person.previousHistory}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">食物过敏史</td>
		<td align="left">
            <input id="foodAllergy" name="foodAllergy" type="text" 
			       class="easyui-validatebox  x-text"  style="width:350px;"
				   value="${person.foodAllergy}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">药物过敏史</td>
		<td align="left">
            <input id="medicineAllergy" name="medicineAllergy" type="text" 
			       class="easyui-validatebox  x-text"  style="width:350px;"
				   value="${person.medicineAllergy}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">备注</td>
		<td align="left">
		    <textarea id="remark" name="remark" rows="8" cols="50" style="width:350px;height:90px;"  class="easyui-validatebox  x-text" >${person.remark}</textarea>
		</td>
	</tr>
    <tr>
	  <td colspan="2">
	    <div>
		   <fieldset class="x-fieldset">
			   <legend>父亲</legend>
			   <table>
				   <tr>
					<td width="20%" align="left">姓名</td>
					<td>
						<input id="father" name="father" type="text" 
							   class="easyui-validatebox  x-text" style="width:180px;" 
							   value="${person.father}"/></td>
				   </tr>
				   <tr>
					<td>手机号码</td>
					<td>
						<input id="fatherTelephone" name="fatherTelephone" type="text" 
							   class="easyui-validatebox  x-text" style="width:180px;" maxlength="11" size="11"
							   value="${person.fatherTelephone}"/>
					</td>
				   </tr>
				   <tr>
					<td>工作单位</td>
					<td>
					    <textarea id="fatherCompany" name="fatherCompany" rows="8" cols="50" style="width:350px;height:90px;"  class="easyui-validatebox  x-text" >${person.fatherCompany}</textarea>
					</td>
				   </tr>
				   <tr>
					<td>是否监护人</td>
					<td>
					    <select id="fatherWardship" name="fatherWardship">
							<option value="">----请选择----</option>
							<option value="Y">是</option>
							<option value="N">否</option>
						</select>
						<script type="text/javascript">
						   document.getElementById("fatherWardship").value="${person.fatherWardship}";
						</script>
					</td>
				   </tr>
			   </table>
           </fieldset>
		</div>
	  </td>
	</tr>
	<tr>
	  <td colspan="2">
          <div>
		  <br>
		   <fieldset class="x-fieldset">
			   <legend>母亲</legend>
			   <table>
				   <tr>
					<td width="20%" align="left">姓名</td>
					<td>
						<input id="mother" name="mother" type="text" 
							   class="easyui-validatebox  x-text" style="width:180px;" 
							   value="${person.mother}"/></td>
				   </tr>
				   <tr>
					<td>手机号码</td>
					<td>
						<input id="motherTelephone" name="motherTelephone" type="text" 
							   class="easyui-validatebox  x-text" style="width:180px;" maxlength="11" size="11"
							   value="${person.motherTelephone}"/>
					</td>
				   </tr>
				   <tr>
					<td>工作单位</td>
					<td>
					    <textarea id="motherCompany" name="motherCompany" rows="8" cols="50" style="width:350px;height:90px;"  class="easyui-validatebox  x-text" >${person.motherCompany}</textarea>
					</td>
				   </tr>
				   <tr>
					<td>是否监护人</td>
					<td>
					    <select id="motherWardship" name="motherWardship">
							<option value="">----请选择----</option>
							<option value="Y">是</option>
							<option value="N">否</option>
						</select>
						<script type="text/javascript">
						   document.getElementById("motherWardship").value="${person.motherWardship}";
						</script>
					</td>
				   </tr>
			   </table>
           </fieldset>
		</div>
	  </td>
	</tr>
	<tr>
	  <td>
	    <br><br><br><br><br><br><br><br>
	  </td>
	</tr>
    </tbody>
  </table>
 </form>
</div>
</div>

</body>
</html>