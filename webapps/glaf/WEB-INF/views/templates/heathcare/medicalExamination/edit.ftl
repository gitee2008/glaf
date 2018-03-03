<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>体格检查</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		if(jQuery("#personId").val() == ""){
			alert("请选择一个学生。");
			document.getElementById("personId").foucs();
		}
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/medicalExamination/save',
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
				   url: '${contextPath}/heathcare/medicalExamination/save',
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
<body>
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:42px"  class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑体格检查</span>
	<#if person?exists && hasWritePermission>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
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
  <table style="width:825px" align="center">
    <tbody>
	<#if person?exists >
	<tr>
		<td width="12%" align="left">姓名</td>
		<td width="38%" align="left">${person.name}</td>
		<td width="12%" align="left">类型</td>
		<td width="38%" align="left">
            <select id="type" name="type">
				<option value="">----请选择----</option> 
				<option value="3">开学体检</option>
				<option value="5">定期体检</option>
				<option value="6">常规体检</option>
				<option value="7">专项体检</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("type").value="${type}";
			</script>
		</td>
	</tr>
	<#elseif persons?exists >
	<tr>
		<td width="12%" align="left">姓名</td>
		<td width="38%" align="left" > 
            <select id="personId" name="personId">
			    <option value="">----请选择----</option>
				<#list persons as person>
			    <option value="${person.id}">${person.name}</option>
			    </#list> 
			</select>
		</td>
		<td width="12%" align="left">类型</td>
		<td width="38%" align="left">
            <select id="type" name="type">
				<option value="">----请选择----</option> 
				<option value="3">开学体检</option>
				<option value="4">自行体检</option>
				<option value="5">定期体检</option>
				<option value="6">常规体检</option>
				<option value="7">专项体检</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("type").value="${type}";
			</script>
		</td>
	</tr>
	</#if>
	<tr>
		<td width="12%" align="left">身高</td>
		<td width="38%" align="left">
			<input id="height" name="height" type="text"
			       class="easyui-numberbox  x-text" precision="0" style="width:60px; text-align:right;"
				   value="${medicalExamination.height}"/>&nbsp;(厘米cm)
		</td>
		<td width="12%" align="left">身高评价</td>
		<td width="38%" align="left">
            <input id="heightEvaluate" name="heightEvaluate" type="text"
			       class="easyui-validatebox  x-readonly" style="width:180px" readonly
				   value="${medicalExamination.heightEvaluate}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">体重</td>
		<td width="38%" align="left">
			<input id="weight" name="weight" type="text"
			       class="easyui-numberbox x-text" precision="1" style="width:60px; text-align:right;"
				   value="${medicalExamination.weight}"/>&nbsp;(千克kg)
		</td>
		<td width="12%" align="left">体重评价</td>
		<td width="38%" align="left">
            <input id="weightEvaluate" name="weightEvaluate" type="text" 
			       class="easyui-validatebox x-readonly" style="width:180px" readonly
				   value="${medicalExamination.weightEvaluate}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">BMI</td>
		<td align="left">
			<input id="bmi" name="bmi" type="text"
			       class="easyui-numberbox x-text x-readonly" precision="2" style="width:60px; text-align:right;" readonly
				   value="${medicalExamination.bmi}"/>
		</td>
		<td width="12%" align="left">综合评价</td>
		<td align="left">
			 <input id="bmiEvaluate" name="bmiEvaluate" type="text" 
			        class="easyui-validatebox x-readonly" style="width:180px" readonly
				    value="${medicalExamination.bmiEvaluate}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">左眼</td>
		<td width="38%" align="left">
				<select id="eyeLeft" name="eyeLeft"  onchange="javascript:switchEyeLeft();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="T">沙眼</option>
					<option value="A">弱视</option>
					<option value="O">未检查</option>
				</select>
			    <div id="eyeLeftRemarkDiv" style="display:none; margin-top:2px; ">
				   <input id="eyeLeftRemark" name="eyeLeftRemark" type="text" 
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
		</td>
		<td width="12%" align="left">右眼</td>
		<td width="38%" align="left">
				<select id="eyeRight" name="eyeRight" onchange="javascript:switchEyeRight();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="T">沙眼</option>
					<option value="A">弱视</option>
					<option value="O">未检查</option>
				</select>
			    <div id="eyeRightRemarkDiv" style="display:none; margin-top:2px; ">
				   <input id="eyeRightRemark" name="eyeRightRemark" type="text" 
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
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">左视力</td>
		<td width="38%" align="left">
			<input id="eyesightLeft" name="eyesightLeft" type="text"
			       class="easyui-numberbox x-text"  precision="2" style="width:60px; text-align:right;"
				   value="${medicalExamination.eyesightLeft}"/>
		</td>
		<td width="12%" align="left">右视力</td>
		<td width="38%" align="left">
			<input id="eyesightRight" name="eyesightRight" type="text"
			       class="easyui-numberbox x-text"  precision="2" style="width:60px; text-align:right;"
				   value="${medicalExamination.eyesightRight}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">左耳</td>
		<td width="38%" align="left">
				<select id="earLeft" name="earLeft" onchange="javascript:switchEarLeft();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="earLeftRemarkDiv" style="display:none; margin-top:2px; ">
					   <input id="earLeftRemark" name="earLeftRemark" type="text" 
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
		</td>
		<td width="12%" align="left">右耳</td>
		<td width="38%" align="left">
				<select id="earRight" name="earRight" onchange="javascript:switchEarRight();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
			   <div id="earRightRemarkDiv" style="display:none; margin-top:2px; ">
				   <input id="earRightRemark" name="earRightRemark" type="text" 
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
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">牙齿数</td>
		<td width="38%" align="left">
			<input id="tooth" name="tooth" type="text" 
			       class="easyui-numberbox x-text" increment="1" style="width:60px; text-align:right;"
				   value="${medicalExamination.tooth}"/>
		</td>
		<td width="12%" align="left">龋齿数</td>
		<td width="38%" align="left">
			<input id="saprodontia" name="saprodontia" type="text" 
			       class="easyui-numberbox x-text" increment="1" style="width:60px; text-align:right;"
				   value="${medicalExamination.saprodontia}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">头颅</td>
		<td width="38%" align="left">
				<select id="head" name="head" onchange="javascript:switchHead();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="headRemarkDiv" style="display:none; margin-top:2px; ">
					   <input id="headRemark" name="headRemark" type="text" 
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
		<td width="12%" align="left">胸廓</td>
		<td width="38%" align="left">
				<select id="thorax" name="thorax" onchange="javascript:switchThorax();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
			    <div id="thoraxRemarkDiv" style="display:none; margin-top:2px; ">
				   <input id="thoraxRemark" name="thoraxRemark" type="text" 
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
	</tr>
	<tr>
		<td width="12%" align="left">脊柱四肢</td>
		<td width="38%" align="left">
				<select id="spine" name="spine" onchange="javascript:switchSpine();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="spineRemarkDiv" style="display:none; margin-top:2px; ">
					   <input id="spineRemark" name="spineRemark" type="text" 
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
		<td width="12%" align="left">咽部</td>
		<td width="38%" align="left">
				<select id="pharyngeal" name="pharyngeal" onchange="javascript:switchPharyngeal();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="pharyngealRemarkDiv" style="display:none; margin-top:2px; ">
					   <input id="pharyngealRemark" name="pharyngealRemark" type="text" 
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
	</tr>
	<tr>
		<td width="12%" align="left">心肺</td>
		<td width="38%" align="left">
				<select id="cardiopulmonary" name="cardiopulmonary" onchange="javascript:switchCardiopulmonary();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="cardiopulmonaryRemarkDiv" style="display:none; margin-top:2px; ">
					   <input id="cardiopulmonaryRemark" name="cardiopulmonaryRemark" type="text" 
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
		<td width="12%" align="left">肝脾</td>
		<td width="38%" align="left">
				<select id="hepatolienal" name="hepatolienal" onchange="javascript:switchHepatolienal();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="hepatolienalRemarkDiv" style="display:none; margin-top:2px; ">
					   <input id="hepatolienalRemark" name="hepatolienalRemark" type="text" 
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
	</tr>
	<tr>
		<td width="12%" align="left">外生殖器</td>
		<td width="38%" align="left">
				<select id="pudendum" name="pudendum" onchange="javascript:switchPudendum();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="pudendumRemarkDiv" style="display:none; margin-top:2px; ">
					   <input id="pudendumRemark" name="pudendumRemark" type="text" 
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
		<td width="12%" align="left">皮肤</td>
		<td width="38%" align="left">
				<select id="skin" name="skin" onchange="javascript:switchSkin();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="O">未检查</option>
				</select>
				<div id="skinRemarkDiv" style="display:none; margin-top:2px; ">
					   <input id="skinRemark" name="skinRemark" type="text" 
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
	</tr>
	<tr>
		<td width="12%" align="left">淋巴结</td>
		<td width="38%" align="left">
		    <select id="lymphoid" name="lymphoid">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("lymphoid").value="${medicalExamination.lymphoid}";
			</script>
		</td>
		<td width="12%" align="left">前囟</td>
		<td width="38%" align="left">
		    <select id="bregma" name="bregma">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("bregma").value="${medicalExamination.bregma}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">口腔</td>
		<td width="38%" align="left">
		    <select id="oralogy" name="oralogy">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("oralogy").value="${medicalExamination.oralogy}";
			</script>
		</td>
		<td width="12%" align="left">扁桃体</td>
		<td width="38%" align="left">
		    <select id="tonsil" name="tonsil">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("tonsil").value="${medicalExamination.tonsil}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">乙肝表面抗体</td>
		<td width="38%" align="left">
		    <select id="hbsab" name="hbsab">
				<option value="">----请选择----</option> 
				<option value="X">阳性</option>
				<option value="Y">阴性</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("hbsab").value="${medicalExamination.hbsab}";
			</script>
		</td>
		<td width="12%" align="left">肝功能</td>
		<td width="38%" align="left">
		    <select id="sgpt" name="sgpt">
				<option value="">----请选择----</option> 
				<option value="Y">正常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("sgpt").value="${medicalExamination.sgpt}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">HVAIgM</td>
		<td width="38%" align="left">
		    <select id="hvaigm" name="hvaigm">
				<option value="">----请选择----</option> 
				<option value="X">阳性</option>
				<option value="Y">阴性</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("hvaigm").value="${medicalExamination.hvaigm}";
			</script>
		</td>
		<td width="12%" align="left">骨骼</td>
		<td width="38%" align="left">
            <select id="bone" name="bone">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("bone").value="${medicalExamination.bone}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">血红蛋白Hb</td>
		<td width="38%" align="left">
            <input id="hemoglobinValue" name="hemoglobinValue" type="text" 
			       class="easyui-numberbox  x-small-text  x-text" style="width:60px"
				   value="${medicalExamination.hemoglobinValue}"/>（g/L）&nbsp;${medicalExamination.hemoglobinHtml}
		</td>
		<td width="12%" align="left">丙氨酸氨基转移酶ALT</td>
		<td width="38%" align="left">
            <input id="altValue" name="altValue" type="text" 
			       class="easyui-numberbox  x-small-text  x-text" style="width:60px"
				   value="${medicalExamination.altValue}"/>（U/L）
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">过敏史</td>
		<td align="left" colspan="3">
            <input id="allergy" name="allergy" type="text" 
			       class="easyui-validatebox  x-text" style="width:592px"
				   value="${medicalExamination.allergy}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">先天缺陷</td>
		<td width="88%" align="left" colspan="3">
            <input id="birthDefect" name="birthDefect" type="text" 
			       class="easyui-validatebox  x-text" style="width:592px"
				   value="${medicalExamination.birthDefect}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">体检时间</td>
		<td width="38%" align="left">
			<input id="checkDate" name="checkDate" type="text" 
			       class="easyui-datebox x-text" style="width:100px"
			       <#if checkDate?exists>
				   value="${checkDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
		<td width="12%" align="left">体检医生</td>
		<td width="38%" align="left">
            <input id="checkDoctor" name="checkDoctor" type="text" 
			       class="easyui-validatebox  x-text" style="width:180px"
				   value="${medicalExamination.checkDoctor}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">体检结果</td>
		<td width="38%" align="left">
            <input id="checkResult" name="checkResult" type="text" 
			       class="easyui-validatebox  x-text" style="width:180px"
				   value="${medicalExamination.checkResult}"/>
		</td>
		<td width="12%" align="left">体检机构</td>
		<td width="38%" align="left">
            <input id="checkOrganization" name="checkOrganization" type="text" 
			       class="easyui-validatebox  x-text" style="width:180px"
				   value="${medicalExamination.checkOrganization}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">医生建议</td>
		<td align="left" colspan="3">		        
            <textarea id="doctorSuggest" name="doctorSuggest" type="text" 
			          class="x-textarea" style="width:592px;height:60px">${medicalExamination.doctorSuggest}</textarea>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">备注</td>
		<td align="left" colspan="3">
		    <textarea id="remark" name="remark" type="text" 
			          class="x-textarea" style="width:592px;height:90px">${medicalExamination.remark}</textarea>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="4"><br><br><br><br></td>
	</tr>
    </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>