<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>儿童生长发育暨健康监测记录</title>
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
  <div data-options="region:'north',split:false,border:true" style="height:42px"  class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑儿童生长发育暨健康监测记录</span>
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
  <input type="hidden" id="type" name="type" value="${type}"/>
  <#if person?exists >
  <input type="hidden" id="personId" name="personId" value="${person.id}">
  <#else>
  <input type="hidden" id="personId" name="personId" value="${personId}"/>
  </#if>
  <table style="width:825px;margin-top:10px;" align="center">
    <tbody>
	<#if person?exists >
	<tr>
		<td width="10%" align="left">姓名</td>
		<td width="40%" align="left">${person.name}</td>
		<td width="10%" align="left">出生日期</td>
		<td width="40%" align="left">
            <#if person.birthday?exists >${person.birthday?string('yyyy年MM月dd日')}</#if> 
			&nbsp;<#if person.sex == '1'>男<#else>女</#if>
		</td>
	</tr>
	</#if>
	<tr>
		<td width="10%" align="left">身高</td>
		<td width="40%" align="left">
			<input id="height" name="height" type="text"
			       class="easyui-numberbox x-text" precision="0" style="width:60px; text-align:right;"
				   value="${medicalExamination.height}"/>&nbsp;(厘米cm)
		</td>
		<td width="10%" align="left">身高评价</td>
		<td width="40%" align="left">
            <input id="heightEvaluate" name="heightEvaluate" type="text" 
			       class="easyui-validatebox x-readonly" style="width:180px" readonly
				   value="${medicalExamination.heightEvaluate}"/>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">体重</td>
		<td width="40%" align="left">
			<input id="weight" name="weight" type="text"
			       class="easyui-numberbox x-text" precision="1" style="width:60px; text-align:right;"
				   value="${medicalExamination.weight}"/>&nbsp;(千克kg)
		</td>
		<td width="10%" align="left">体重评价</td>
		<td width="40%" align="left">
            <input id="weightEvaluate" name="weightEvaluate" type="text" 
			       class="easyui-validatebox  x-readonly" style="width:180px" readonly
				   value="${medicalExamination.weightEvaluate}"/>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">BMI</td>
		<td align="left">
			<input id="bmi" name="bmi" type="text"
			       class="easyui-numberbox  x-text x-readonly" precision="2" style="width:60px" readonly
				   value="${medicalExamination.bmi}"/>
		</td>
		<td width="10%" align="left">综合评价</td>
		<td align="left">
			 <input id="bmiEvaluate" name="bmiEvaluate" type="text" 
			        class="easyui-validatebox  x-readonly" style="width:180px" readonly
				    value="${medicalExamination.bmiEvaluate}"/>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">左眼</td>
		<td width="40%" align="left">
		    <select id="eyeLeft" name="eyeLeft">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="T">沙眼</option>
				<option value="A">弱视</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("eyeLeft").value="${medicalExamination.eyeLeft}";
			</script>
		</td>
		<td width="10%" align="left">右眼</td>
		<td width="40%" align="left">
		    <select id="eyeRight" name="eyeRight">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="T">沙眼</option>
				<option value="A">弱视</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("eyeRight").value="${medicalExamination.eyeRight}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">左视力</td>
		<td width="40%" align="left">
			<input id="eyesightLeft" name="eyesightLeft" type="text"
			       class="easyui-numberbox  x-text" precision="1" style="width:60px; text-align:right;"
				   value="${medicalExamination.eyesightLeft}"/>
		</td>
		<td width="10%" align="left">右视力</td>
		<td width="40%" align="left">
			<input id="eyesightRight" name="eyesightRight" type="text"
			       class="easyui-numberbox  x-text" precision="1" style="width:60px; text-align:right;"
				   value="${medicalExamination.eyesightRight}"/>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">左耳</td>
		<td width="40%" align="left">
		    <select id="earLeft" name="earLeft">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("earLeft").value="${medicalExamination.earLeft}";
			</script>
		</td>
		<td width="10%" align="left">右耳</td>
		<td width="40%" align="left">
		    <select id="earRight" name="earRight">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("earRight").value="${medicalExamination.earRight}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">牙齿数</td>
		<td width="40%" align="left">
			<input id="tooth" name="tooth" type="text" 
			       class="easyui-numberbox x-text" increment="1" style="width:60px"
				   value="${medicalExamination.tooth}"/>
		</td>
		<td width="10%" align="left">龋齿数</td>
		<td width="40%" align="left">
			<input id="saprodontia" name="saprodontia" type="text" 
			       class="easyui-numberbox x-text" increment="1" style="width:60px"
				   value="${medicalExamination.saprodontia}"/>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">头颅</td>
		<td width="40%" align="left">
		    <select id="head" name="head">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("head").value="${medicalExamination.head}";
			</script>
		</td>
		<td width="10%" align="left">胸廓</td>
		<td width="40%" align="left">
		    <select id="thorax" name="thorax">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("thorax").value="${medicalExamination.thorax}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">脊柱四肢</td>
		<td width="40%" align="left">
		    <select id="spine" name="spine">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("spine").value="${medicalExamination.spine}";
			</script>
		</td>
		<td width="10%" align="left">咽部</td>
		<td width="40%" align="left">
		    <select id="pharyngeal" name="pharyngeal">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("pharyngeal").value="${medicalExamination.pharyngeal}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">心肺</td>
		<td width="40%" align="left">
		    <select id="cardiopulmonary" name="cardiopulmonary">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("cardiopulmonary").value="${medicalExamination.cardiopulmonary}";
			</script>
		</td>
		<td width="10%" align="left">肝脾</td>
		<td width="40%" align="left">
		    <select id="hepatolienal" name="hepatolienal">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("hepatolienal").value="${medicalExamination.hepatolienal}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">外生殖器</td>
		<td width="40%" align="left">
		    <select id="pudendum" name="pudendum">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("pudendum").value="${medicalExamination.pudendum}";
			</script>
		</td>
		<td width="10%" align="left">皮肤</td>
		<td width="40%" align="left">
		    <select id="skin" name="skin">
				<option value="">----请选择----</option> 
				<option value="Y">未见异常</option>
				<option value="N">异常</option>
				<option value="O">未检查</option>
			</select>
			<script type="text/javascript">
			     document.getElementById("skin").value="${medicalExamination.skin}";
			</script>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">淋巴结</td>
		<td width="40%" align="left">
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
		<td width="10%" align="left">前囟</td>
		<td width="40%" align="left">
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
		<td width="10%" align="left">口腔</td>
		<td width="40%" align="left">
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
		<td width="10%" align="left">扁桃体</td>
		<td width="40%" align="left">
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
		<td width="10%" align="left">乙肝表面抗体</td>
		<td width="40%" align="left">
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
		<td width="10%" align="left">肝功能</td>
		<td width="40%" align="left">
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
		<td width="10%" align="left">甲肝抗体</td>
		<td width="40%" align="left">
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
		<td width="10%" align="left">骨骼</td>
		<td width="40%" align="left">
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
		<td width="10%" align="left">血红蛋白Hb</td>
		<td width="40%" align="left">
            <input id="hemoglobinValue" name="hemoglobinValue" type="text" 
			       class="easyui-numberbox x-small-text" style="width:60px"
				   value="${medicalExamination.hemoglobinValue}"/>&nbsp;（g/L）&nbsp;${medicalExamination.hemoglobinHtml}
		</td>
		<td width="10%" align="left">丙氨酸氨基转移酶ALT</td>
		<td width="40%" align="left">
            <input id="altValue" name="altValue" type="text" 
			       class="easyui-numberbox x-small-text" style="width:60px"
				   value="${medicalExamination.altValue}"/>&nbsp;（U/L）
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">健康评价</td>
		<td align="left" colspan="3">
		    <input id="healthEvaluate" name="healthEvaluate" type="text" 
			       class="easyui-validatebox  x-text" style="width:592px"
				   value="${medicalExamination.healthEvaluate}"/>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">体检时间</td>
		<td width="40%" align="left">
			<input id="checkDate" name="checkDate" type="text" 
			       class="easyui-datebox x-text" style="width:100px"
			       <#if checkDate?exists>
				   value="${checkDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
		<td width="10%" align="left">体检医生</td>
		<td width="40%" align="left">
            <input id="checkDoctor" name="checkDoctor" type="text" 
			       class="easyui-validatebox  x-text" style="width:180px"
				   value="${medicalExamination.checkDoctor}"/>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">体检结果</td>
		<td width="40%" align="left">
            <input id="checkResult" name="checkResult" type="text" 
			       class="easyui-validatebox  x-text" style="width:180px"
				   value="${medicalExamination.checkResult}"/>
		</td>
		<td width="10%" align="left">体检机构</td>
		<td width="40%" align="left">
            <input id="checkOrganization" name="checkOrganization" type="text" 
			       class="easyui-validatebox  x-text" style="width:180px"
				   value="${medicalExamination.checkOrganization}"/>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">医生建议</td>
		<td align="left" colspan="3">		        
            <textarea id="doctorSuggest" name="doctorSuggest" type="text" 
			          class="x-textarea" style="width:592px;height:60px">${medicalExamination.doctorSuggest}</textarea>
		</td>
	</tr>
	<tr>
		<td width="10%" align="left">备注</td>
		<td align="left" colspan="3">
		    <textarea id="remark" name="remark" type="text" 
			          class="x-textarea" style="width:592px;height:90px">${medicalExamination.remark}</textarea>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="4">
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