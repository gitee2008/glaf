<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>儿童入园健康检查表</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>
    .table-border { background-color:#3399cc; height: 32px; font-family:"宋体"}
    .table-content { background-color:#ffffff; height: 32px;font-size: 12px; font-family:"宋体"}
</style>
<script type="text/javascript">

	function saveData(){
		if(jQuery("#personId").val() == ""){
			alert("请选择一个学生。");
			document.getElementById("personId").foucs();
		}
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/medicalExamination/save?type=1',
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
		if(jQuery("#personId").val() == ""){
			alert("请选择一个学生。");
			document.getElementById("personId").foucs();
		}
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/medicalExamination/save?type=1',
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

</script>
</head>
<body style="margin:1px;">
<div style="margin:1px;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑儿童入园健康检查表</span>
		<#if person?exists && hasWritePermission>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();">保存</a> 
		</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${medicalExamination.id}"/>
  <input type="hidden" id="checkId" name="checkId" value="${checkId}"/>
  <input type="hidden" id="gradeId" name="gradeId" value="${gradeId}"/>
  <#if person?exists >
  <input type="hidden" id="personId" name="personId" value="${person.id}">
  <#else>
  <input type="hidden" id="personId" name="personId" value="${personId}"/>
  </#if>
  <table width="100%" height="99%" cellpadding='1' cellspacing='1' class="table-border" nowrap>
    <tbody>
	    <tr>
		    <td colspan="10" width="100%" align="center" class="table-content" style="line-height: 54px;">
			    <span style="font-size:14px; font-family:微软雅黑; font-weight:bold;">儿童入园健康检查表</span>
			</td>
		</tr>
	    <#if person?exists >
		<tr>
			<td width="10%" align="center" class="table-content">姓名</td>
			<td width="10%" align="center" class="table-content">${person.name}</td>
			<td width="10%" align="center" class="table-content">性别</td>
			<td width="10%" align="center" class="table-content">
				 <#if person.sex == '1' >男<#else>女</#if>
			</td>
			<td width="10%" align="center" class="table-content">年龄</td>
			<td width="10%" align="center" class="table-content">
			    <#if medicalExamination.ageOfTheMoon != 0 >${medicalExamination.ageOfTheMoon}月</#if>
			</td>
			<td width="10%" align="center" class="table-content">出生日期</td>
			<td width="30%" align="left" colspan="3" class="table-content">
			    <#if person.birthday?exists >${person.birthday?string('yyyy年MM月dd日')}</#if>
			</td>
		</tr>
		<tr>
		    <td width="10%" align="center" class="table-content">既往病史</td>
		    <td colspan="9" width="90%" align="left" class="table-content">
			     &nbsp;&nbsp;&nbsp;&nbsp;${person.previousHistory}
			</td>
		</tr>
		<tr>
		    <td width="10%" align="center" class="table-content">过敏史</td>
		    <td colspan="4" width="40%" align="left" class="table-content">
			     &nbsp;&nbsp;&nbsp;&nbsp;${person.medicineAllergy}
			</td>
			<td width="10%" align="center" class="table-content">家长签名</td>
		    <td colspan="4" width="40%" align="left" class="table-content">
			      
			</td>
		</tr>
		</#if>
		<tr>
		    <td width="10%" align="center" class="table-content">体重</td>
			<td width="10%" align="center" class="table-content">
			  <input id="weight" name="weight" type="text"
			         class="easyui-numberbox x-small-text" precision="1" style="width:50px; text-align:right;"
				     value="${medicalExamination.weight}"/>&nbsp;(千克kg)
            </td>
			<td width="10%" align="center" class="table-content">评价</td>
			<td width="10%" align="center" class="table-content"> 
			    &nbsp;&nbsp;${medicalExamination.weightEvaluate} 
			</td>
			<td width="10%" align="center" class="table-content">身长（高）</td>
			<td width="10%" align="center" class="table-content">
			    <input id="height" name="height" type="text"
			           class="easyui-numberbox  x-small-text" precision="0" style="width:50px; text-align:right;"
				       value="${medicalExamination.height}"/>&nbsp;(厘米cm)
			</td>
			<td width="10%" align="center" class="table-content">评价</td>
			<td width="10%" align="center" class="table-content">
			    &nbsp;&nbsp;${medicalExamination.heightEvaluate}
			</td>
			<td width="10%" align="center" class="table-content">身高别体重</td>
			<td width="10%" align="center" class="table-content">
			    &nbsp;&nbsp;${medicalExamination.bmiEvaluate}
			</td> 
		</tr>

		<tr>
		    <td width="10%" align="center" class="table-content" style="line-height: 94px;">眼</td>
			<td width="10%" align="left" class="table-content">
			    <br>&nbsp;左&nbsp;<select id="eyeLeft" name="eyeLeft" onchange="javascript:switchEyeLeft();">
				<option value="">--请选择--</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="T">沙眼</option>
				<option value="A">弱视</option>
				<option value="O">未检查</option>
				</select>
				<div id="eyeLeftRemarkDiv" style="display:none; margin:2px; ">
				   &nbsp;<input id="eyeLeftRemark" name="eyeLeftRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.eyeLeftRemark}"/>
				</div>
				<script type="text/javascript">
			         document.getElementById("eyeLeft").value="${medicalExamination.eyeLeft}";
					 if(document.getElementById("eyeLeft").value == "N"){
						 jQuery("#eyeLeftRemarkDiv").show();
					 } else {
                         jQuery("#eyeLeftRemarkDiv").hide();
					 }
					 function switchEyeLeft(){
                        if(document.getElementById("eyeLeft").value == "N"){
							jQuery("#eyeLeftRemarkDiv").show();
						} else {
                            jQuery("#eyeLeftRemarkDiv").hide();
						}
					 }
			    </script>
				<br><br>
				&nbsp;右&nbsp;<select id="eyeRight" name="eyeRight" onchange="javascript:switchEyeRight();">
				<option value="">--请选择--</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="T">沙眼</option>
				<option value="A">弱视</option>
				<option value="O">未检查</option>
			    </select>
				<div id="eyeRightRemarkDiv" style="display:none; margin:2px; ">
				   &nbsp;<input id="eyeRightRemark" name="eyeRightRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.eyeRightRemark}"/>
				</div>
				<script type="text/javascript">
					 document.getElementById("eyeRight").value="${medicalExamination.eyeRight}";
					 if(document.getElementById("eyeRight").value == "N"){
						  jQuery("#eyeRightRemarkDiv").show();
					 } else {
                          jQuery("#eyeRightRemarkDiv").hide();
					 }
					 function switchEyeRight(){
                        if(document.getElementById("eyeRight").value == "N"){
							 jQuery("#eyeRightRemarkDiv").show();
						} else {
                             jQuery("#eyeRightRemarkDiv").hide();
						}
					 }
				</script>
				<br>
            </td>
			<td width="10%" align="center" class="table-content">视力</td>
			<td width="10%" align="left" class="table-content"> 
			     <br>&nbsp;左&nbsp;
				 <input id="eyesightLeft" name="eyesightLeft" type="text"
			            class="easyui-numberbox x-small-text" precision="1" style="width:50px; text-align:right;"
				        value="${medicalExamination.eyesightLeft}"/>
				  <br><br>
				  &nbsp;右&nbsp;
				  <input id="eyesightRight" name="eyesightRight" type="text"
			             class="easyui-numberbox x-small-text" precision="1" style="width:50px; text-align:right;"
				         value="${medicalExamination.eyesightRight}"/>
				  <br><br>
			</td>
			<td width="10%" align="center" class="table-content">耳</td>
			<td width="10%" align="left" class="table-content">
			    <br>&nbsp;左&nbsp;
				<select id="earLeft" name="earLeft" onchange="javascript:switchEarLeft();">
					<option value="">--请选择--</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="earLeftRemarkDiv" style="display:none; margin:2px; ">
				   &nbsp;<input id="earLeftRemark" name="earLeftRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.earLeftRemark}"/>
				</div>
				<script type="text/javascript">
			         document.getElementById("earLeft").value="${medicalExamination.earLeft}";
					 if(document.getElementById("earLeft").value == "N"){
						 jQuery("#earLeftRemarkDiv").show();
					 } else {
                         jQuery("#earLeftRemarkDiv").hide();
					 }
					 function switchEarLeft(){
                        if(document.getElementById("earLeft").value == "N"){
							jQuery("#earLeftRemarkDiv").show();
						} else {
                            jQuery("#earLeftRemarkDiv").hide();
						}
					 }
			    </script>
				<br><br>&nbsp;右&nbsp;
				<select id="earRight" name="earRight" onchange="javascript:switchEarRight();">
					<option value="">--请选择--</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="earRightRemarkDiv" style="display:none; margin:2px; ">
				   &nbsp;<input id="earRightRemark" name="earRightRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.earRightRemark}"/>
				</div>
				<script type="text/javascript">
			         document.getElementById("earRight").value="${medicalExamination.earRight}";
					 if(document.getElementById("earRight").value == "N"){
						 jQuery("#earRightRemarkDiv").show();
					 } else {
                         jQuery("#earRightRemarkDiv").hide();
					 }
					 function switchEarRight(){
                        if(document.getElementById("earRight").value == "N"){
							jQuery("#earRightRemarkDiv").show();
						} else {
                            jQuery("#earRightRemarkDiv").hide();
						}
					 }
			    </script>
				<br>
			</td>
			<td width="10%" align="center" class="table-content">口腔</td>
			<td width="10%" align="center" class="table-content">
			     <br>&nbsp;牙齿数&nbsp;
				 <input id="tooth" name="tooth" type="text" 
			            class="easyui-numberbox x-small-text" increment="1" style="width:50px; text-align:right;"
				        value="${medicalExamination.tooth}"/>
				 <br><br>
				 &nbsp;龋齿数&nbsp;
				 <input id="saprodontia" name="saprodontia" type="text" 
			            class="easyui-numberbox x-small-text" increment="1" style="width:50px; text-align:right;"
				        value="${medicalExamination.saprodontia}"/>
			</td>
			<td width="10%" align="center" class="table-content">&nbsp;</td>
			<td width="10%" align="center" class="table-content">&nbsp;</td>
		</tr>

		<tr>
		    <td width="10%" align="center" class="table-content">头颅</td>
			<td width="10%" align="center" class="table-content">
			    <select id="head" name="head" onchange="javascript:switchHead();">
					<option value="">--请选择--</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="headRemarkDiv" style="display:none; margin:2px; ">
				   &nbsp;<input id="headRemark" name="headRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.headRemark}"/>
				</div>
				<script type="text/javascript">
			         document.getElementById("head").value="${medicalExamination.head}";
					 if(document.getElementById("head").value == "N"){
						 jQuery("#headRemarkDiv").show();
					 } else {
                         jQuery("#headRemarkDiv").hide();
					 }
					 function switchHead(){
                        if(document.getElementById("head").value == "N"){
							jQuery("#headRemarkDiv").show();
						} else {
                            jQuery("#headRemarkDiv").hide();
						}
					 }
			    </script>
            </td>
			<td width="10%" align="center" class="table-content">胸廓</td>
			<td width="10%" align="center" class="table-content"> 
			    <select id="thorax" name="thorax" onchange="javascript:switchThorax();">
					<option value="">--请选择--</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="thoraxRemarkDiv" style="display:none; margin:2px; ">
				   &nbsp;<input id="thoraxRemark" name="thoraxRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.thoraxRemark}"/>
				</div>
				<script type="text/javascript">
			         document.getElementById("thorax").value="${medicalExamination.thorax}";
					 if(document.getElementById("thorax").value == "N"){
						 jQuery("#thoraxRemarkDiv").show();
					 } else {
                         jQuery("#thoraxRemarkDiv").hide();
					 }
					 function switchThorax(){
                        if(document.getElementById("thorax").value == "N"){
							jQuery("#thoraxRemarkDiv").show();
						} else {
                            jQuery("#thoraxRemarkDiv").hide();
						}
					 }
			    </script>
			</td>
			<td width="10%" align="center" class="table-content">脊柱四肢</td>
			<td width="10%" align="center" class="table-content">
			    <select id="spine" name="spine" onchange="javascript:switchSpine();">
					<option value="">--请选择--</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="spineRemarkDiv" style="display:none; margin:2px; ">
				   &nbsp;<input id="spineRemark" name="spineRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.spineRemark}"/>
				</div>
				<script type="text/javascript">
			         document.getElementById("spine").value="${medicalExamination.spine}";
					 if(document.getElementById("spine").value == "N"){
						 jQuery("#spineRemarkDiv").show();
					 } else {
                         jQuery("#spineRemarkDiv").hide();
					 }
					 function switchSpine(){
                        if(document.getElementById("spine").value == "N"){
							jQuery("#spineRemarkDiv").show();
						} else {
                            jQuery("#spineRemarkDiv").hide();
						}
					 }
			    </script>
			</td>
			<td width="10%" align="center" class="table-content">咽部</td>
			<td width="10%" align="center" class="table-content">
			    <select id="pharyngeal" name="pharyngeal" onchange="javascript:switchPharyngeal();">
					<option value="">--请选择--</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="pharyngealRemarkDiv" style="display:none; margin:2px; ">
				   &nbsp;<input id="pharyngealRemark" name="pharyngealRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.pharyngealRemark}"/>
				</div>
				<script type="text/javascript">
			         document.getElementById("pharyngeal").value="${medicalExamination.pharyngeal}";
					 if(document.getElementById("pharyngeal").value == "N"){
						 jQuery("#pharyngealRemarkDiv").show();
					 } else {
                         jQuery("#pharyngealRemarkDiv").hide();
					 }
					 function switchPharyngeal(){
                        if(document.getElementById("pharyngeal").value == "N"){
							jQuery("#pharyngealRemarkDiv").show();
						} else {
                            jQuery("#pharyngealRemarkDiv").hide();
						}
					 }
			    </script>
			</td>
			<td width="10%" align="center" class="table-content">&nbsp;</td>
			<td width="10%" align="center" class="table-content">
			    &nbsp;&nbsp;
			</td> 
		</tr>
		
		<tr>
		    <td width="10%" align="center" class="table-content">心肺</td>
			<td width="10%" align="center" class="table-content">
			    <select id="cardiopulmonary" name="cardiopulmonary" onchange="javascript:switchCardiopulmonary();">
					<option value="">--请选择--</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="cardiopulmonaryRemarkDiv" style="display:none; margin:2px; ">
				   &nbsp;<input id="cardiopulmonaryRemark" name="cardiopulmonaryRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.cardiopulmonaryRemark}"/>
				</div>
				<script type="text/javascript">
			         document.getElementById("cardiopulmonary").value="${medicalExamination.cardiopulmonary}";
					 if(document.getElementById("cardiopulmonary").value == "N"){
						 jQuery("#cardiopulmonaryRemarkDiv").show();
					 } else {
                         jQuery("#cardiopulmonaryRemarkDiv").hide();
					 }
					 function switchCardiopulmonary(){
                        if(document.getElementById("cardiopulmonary").value == "N"){
							jQuery("#cardiopulmonaryRemarkDiv").show();
						} else {
                            jQuery("#cardiopulmonaryRemarkDiv").hide();
						}
					 }
			    </script>
            </td>
			<td width="10%" align="center" class="table-content">肝脾</td>
			<td width="10%" align="center" class="table-content"> 
			    <select id="hepatolienal" name="hepatolienal" onchange="javascript:switchHepatolienal();">
					<option value="">--请选择--</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="hepatolienalRemarkDiv" style="display:none; margin:2px; ">
				   &nbsp;<input id="hepatolienalRemark" name="hepatolienalRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.hepatolienalRemark}"/>
				</div>
				<script type="text/javascript">
			         document.getElementById("hepatolienal").value="${medicalExamination.hepatolienal}";
					 if(document.getElementById("hepatolienal").value == "N"){
						 jQuery("#hepatolienalRemarkDiv").show();
					 } else {
                         jQuery("#hepatolienalRemarkDiv").hide();
					 }
					 function switchHepatolienal(){
                        if(document.getElementById("hepatolienal").value == "N"){
							jQuery("#hepatolienalRemarkDiv").show();
						} else {
                            jQuery("#hepatolienalRemarkDiv").hide();
						}
					 }
			    </script>
			</td>
			<td width="10%" align="center" class="table-content">外生殖器</td>
			<td width="10%" align="center" class="table-content">
			    <select id="pudendum" name="pudendum" onchange="javascript:switchPudendum();">
					<option value="">--请选择--</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="pudendumRemarkDiv" style="display:none; margin:2px; ">
				   &nbsp;<input id="pudendumRemark" name="pudendumRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.pudendumRemark}"/>
				</div>
				<script type="text/javascript">
			         document.getElementById("pudendum").value="${medicalExamination.pudendum}";
					 if(document.getElementById("pudendum").value == "N"){
						 jQuery("#pudendumRemarkDiv").show();
					 } else {
                         jQuery("#pudendumRemarkDiv").hide();
					 }
					 function switchPudendum(){
                        if(document.getElementById("pudendum").value == "N"){
							jQuery("#pudendumRemarkDiv").show();
						} else {
                            jQuery("#pudendumRemarkDiv").hide();
						}
					 }
			    </script>
			</td>
			<td width="10%" align="center" class="table-content">皮肤</td>
			<td width="10%" align="center" class="table-content">
			    <select id="skin" name="skin" onchange="javascript:switchSkin();">
					<option value="">--请选择--</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="skinRemarkDiv" style="display:none; margin:2px; ">
				   &nbsp;<input id="skinRemark" name="skinRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.skinRemark}"/>
				</div>
				<script type="text/javascript">
			         document.getElementById("skin").value="${medicalExamination.skin}";
					 if(document.getElementById("skin").value == "N"){
						 jQuery("#skinRemarkDiv").show();
					 } else {
                         jQuery("#skinRemarkDiv").hide();
					 }
					 function switchSkin(){
                        if(document.getElementById("skin").value == "N"){
							jQuery("#skinRemarkDiv").show();
						} else {
                            jQuery("#skinRemarkDiv").hide();
						}
					 }
			    </script>
			</td>
			<td width="10%" align="center" class="table-content">其他</td>
			<td width="10%" align="center" class="table-content">
			    &nbsp;&nbsp;
			</td> 
		</tr>
		
		<tr>
		  <td width="10%" align="center" class="table-content">血红蛋白Hb</td>
		  <td width="40%" align="left" class="table-content" colspan="4">
		      &nbsp;&nbsp;<input id="hemoglobinValue" name="hemoglobinValue" type="text" 
							     class="easyui-numberbox x-small-text" style="width:60px"
							     value="${medicalExamination.hemoglobinValue}"/>（g/L）&nbsp;${medicalExamination.hemoglobinHtml}
		  </td>
		  <td width="10%" align="center" class="table-content">丙氨酸氨基转移酶ALT</td>
		  <td width="40%" align="left" class="table-content" colspan="4">
              &nbsp;&nbsp;<input id="altValue" name="altValue" type="text" 
							     class="easyui-numberbox x-small-text" style="width:60px"
							     value="${medicalExamination.altValue}"/>（U/L）
		  </td>
		</tr>

		<tr>
		  <td width="10%" align="center" class="table-content">其他</td>
		  <td width="90%" align="left" class="table-content" colspan="9">
		      
		  </td>
		</tr>

		<tr>
		  <td width="10%" align="center" class="table-content" style="line-height: 64px;" valign="middle">体检结果</td>
		  <td width="40%" align="left" class="table-content" colspan="4" style="line-height: 64px;" valign="middle">
		      &nbsp;
		      <textarea id="checkResult" name="checkResult" type="text" 
			            class="x-textarea" style="margin-top:15px;width:392px;height:60px">${medicalExamination.checkResult}</textarea>
		  </td>
		  <td width="10%" align="center" class="table-content" style="line-height: 64px;" valign="middle">医生意见</td>
		  <td width="40%" align="left" class="table-content" colspan="4" style="line-height: 64px;" valign="middle">
		      &nbsp;
              <textarea id="doctorSuggest" name="doctorSuggest" type="text" 
			            class="x-textarea" style="margin-top:15px;width:392px;height:60px">${medicalExamination.doctorSuggest}</textarea>
		  </td>
		</tr>

		<tr>
		  <td width="10%" align="center" class="table-content">医生签名</td>
		  <td width="40%" align="left" class="table-content" colspan="4">
		       <br>&nbsp;<input id="checkDoctor" name="checkDoctor" type="text" 
			                    class="easyui-validatebox x-small-text" style="width:120px"
				                value="${medicalExamination.checkDoctor}"/>
				   &nbsp;体检日期
				   &nbsp;<input id="checkDate" name="checkDate" type="text" 
						        class="easyui-datebox x-small-text" style="width:120px"
						        <#if checkDate?exists>
						        value="${checkDate ? string('yyyy-MM-dd')}"
						        </#if>/>
				 <br><br>
		  </td>
		  <td width="10%" align="center" class="table-content">体检单位</td>
		  <td width="40%" align="left" class="table-content" colspan="4">
              <br>&nbsp;<input id="checkOrganization" name="checkOrganization" type="text" 
			                   class="easyui-validatebox x-small-text" style="width:380px"
				               value="${medicalExamination.checkOrganization}"/>
			  <br>
		  </td>
		</tr>

    </tbody>
  </table>
  <br><br><br><br>
  </form>
</div>
</div>
</body>
</html>