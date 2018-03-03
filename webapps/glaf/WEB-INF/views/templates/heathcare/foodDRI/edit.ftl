<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑膳食营养素参考摄入量标准</title>
<#include "/inc/init_kendoui_import.ftl"/>
<style scoped>

   .k-textbox {
        width: 18.8em;
    }

    .main-section {
        width: 800px;
        padding: 0;
     }

    label {
        display: inline-block;
        width: 100px;
        text-align: right;
        padding-right: 10px;
    }

    .required {
        font-weight: bold;
    }

    .accept, .status {
        padding-left: 90px;
    }
    .confirm {
        text-align: right;
    }

    .valid {
        color: green;
    }

    .invalid {
        color: red;
    }
    span.k-tooltip {
        margin-left: 6px;
    }
</style>
<script type="text/javascript">
                
  jQuery(function() {
    var viewModel = kendo.observable({
        "name": "${foodDRI.name}",
		"description": "${foodDRI.description}",
        "age": "${foodDRI.age}",
        "typeId": "${foodDRI.typeId}",
        "heatEnergy": "${foodDRI.heatEnergy}",
        "protein": "${foodDRI.protein}",
        "fat": "${foodDRI.fat}",
        "carbohydrate": "${foodDRI.carbohydrate}",
        "vitaminA": "${foodDRI.vitaminA}",
        "vitaminB1": "${foodDRI.vitaminB1}",
        "vitaminB2": "${foodDRI.vitaminB2}",
        "vitaminC": "${foodDRI.vitaminC}",
        "carotene": "${foodDRI.carotene}",
        "retinol": "${foodDRI.retinol}",
        "nicotinicCid": "${foodDRI.nicotinicCid}",
        "calcium": "${foodDRI.calcium}",
        "iron": "${foodDRI.iron}",
        "zinc": "${foodDRI.zinc}",
	    "iodine": "${foodDRI.iodine}",
		"phosphorus": "${foodDRI.phosphorus}",
        "enableFlag": "${foodDRI.enableFlag}",
        "id": "${foodDRI.id}"
    });

    kendo.bind(jQuery("#iForm"), viewModel);

   });

    jQuery(document).ready(function() {
          jQuery("#iconButton").kendoButton({
                spriteCssClass: "k-icon"
          });           
    });

  <#if heathcare_curd_perm == true>
   function saveData(){
	    //alert("保存数据......");
        var form = document.getElementById("iForm");
	    var link = "${contextPath}/heathcare/foodDRI/saveFoodDRI";
	    var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: link,
				   dataType:  'json',
				   data: params,
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
	    //alert("保存数据......");
		document.getElementById("id").value="";
        var form = document.getElementById("iForm");
	    var link = "${contextPath}/heathcare/foodDRI/saveFoodDRI";
	    var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: link,
				   dataType:  'json',
				   data: params,
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
					   //window.parent.location.reload();
				   }
			 });
   }
  </#if>
 </script>
</head>
<body style="margin-top:0px;">
<div id="main_content" class="k-content ">
<br>
<div class="x_content_title"><img
	src="${contextPath}/static/images/window.png" alt="编辑膳食营养素参考摄入量标准">&nbsp;
编辑膳食营养素参考摄入量标准</div>
<br>
<form id="iForm" name="iForm" method="post" >
<input type="hidden" id="id" name="id" value="${foodDRI.id}"/>
<table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
  
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	<label for="name" class="required">名称&nbsp;</label>
        <input id="name" name="name" type="text" class="k-textbox"  
	           data-bind="value: name" style="width:325px;"
	           value="${foodDRI.name}" validationMessage="请输入名称"/>
	<span class="k-invalid-msg" data-for="name"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="description" class="required">描述&nbsp;</label>
        <textarea  id="description" name="description" rows="6" cols="46" class="k-textbox" style="height:90px;width:325px;" >${foodDRI.description}</textarea>
	    <span class="k-invalid-msg" data-for="description"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	 <label for="age" class="required">年龄&nbsp;</label>
	 <select id="age" name="age">
		<option value="0">----请选择----</option>
		<option value="1">1岁</option>
		<option value="2">2岁</option>
		<option value="3">3岁</option>
		<option value="4">4岁</option>
		<option value="5">5岁</option>
		<option value="6">6岁</option>
	  </select>
	  <script type="text/javascript">
	       document.getElementById("age").value="${foodDRI.age}";
	  </script>
	  <span class="k-invalid-msg" data-for="age"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	  <label for="typeId" class="required">餐点&nbsp;</label>
      <select id="typeId" name="typeId">
		<option value="0">----请选择----</option>
		<#list dictoryList as d>
		<option value="${d.id}">${d.name}</option>
		</#list> 
	  </select>
	  <script type="text/javascript">
	       document.getElementById("typeId").value="${foodDRI.typeId}";
	  </script>
	  <span class="k-invalid-msg" data-for="typeId"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="heatEnergy" class="required">热能&nbsp;</label>
	    <input id="heatEnergy" name="heatEnergy" type="text" class="k-textbox" 
	           data-bind="value: heatEnergy" style="width:60px; text-align:right;"
	           value="${foodDRI.heatEnergy}" validationMessage="请输入热能"/>
		(单位：千卡kcal)
	    <span class="k-invalid-msg" data-for="heatEnergy"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="protein" class="required">蛋白质&nbsp;</label>
	    <input id="protein" name="protein" type="text" class="k-textbox" 
	           data-bind="value: protein" style="width:60px; text-align:right;"
	           value="${foodDRI.protein}" validationMessage="请输入蛋白质"/>
		(单位：克g)
	    <span class="k-invalid-msg" data-for="protein"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="fat" class="required">脂肪&nbsp;</label>
		<input id="fat" name="fat" type="text" class="k-textbox" 
		       data-bind="value: fat" style="width:60px; text-align:right;"
			   value="${foodDRI.fat}" validationMessage="请输入脂肪"/>
		(单位：克g)
		<span class="k-invalid-msg" data-for="fat"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="carbohydrate" class="required">碳水化合物&nbsp;</label>
		<input id="carbohydrate" name="carbohydrate" type="text" class="k-textbox" 
		       data-bind="value: carbohydrate" style="width:60px; text-align:right;"
			   value="${foodDRI.carbohydrate}" validationMessage="请输入碳水化合物"/>
		(单位：克g)
		<span class="k-invalid-msg" data-for="carbohydrate"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="vitaminA" class="required">微生素A&nbsp;</label>
		<input id="vitaminA" name="vitaminA" type="text" class="k-textbox" 
		       data-bind="value: vitaminA" style="width:60px; text-align:right;"
			   value="${foodDRI.vitaminA}" validationMessage="请输入微生素A"/>
		(单位：μgRE)
		<span class="k-invalid-msg" data-for="vitaminA"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="vitaminB1" class="required">微生素B1&nbsp;</label>
		<input id="vitaminB1" name="vitaminB1" type="text" class="k-textbox" 
		       data-bind="value: vitaminB1" style="width:60px; text-align:right;"
			   value="${foodDRI.vitaminB1}" validationMessage="请输入微生素B1"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="vitaminB1"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="vitaminB2" class="required">微生素B2&nbsp;</label>
		<input id="vitaminB2" name="vitaminB2" type="text" class="k-textbox" 
		       data-bind="value: vitaminB2" style="width:60px; text-align:right;"
			   value="${foodDRI.vitaminB2}" validationMessage="请输入微生素B2"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="vitaminB2"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="vitaminC" class="required">微生素C&nbsp;</label>
		<input id="vitaminC" name="vitaminC" type="text" class="k-textbox" 
		       data-bind="value: vitaminC" style="width:60px; text-align:right;"
			   value="${foodDRI.vitaminC}" validationMessage="请输入微生素C"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="vitaminC"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="carotene" class="required">胡萝卜素&nbsp;</label>
		<input id="carotene" name="carotene" type="text" class="k-textbox" 
		       data-bind="value: carotene" style="width:60px; text-align:right;"
			   value="${foodDRI.carotene}" validationMessage="请输入胡萝卜素"/>
		(单位：微克μg)
		<span class="k-invalid-msg" data-for="carotene"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	<label for="retinol" class="required">视黄醇&nbsp;</label>
		<input id="retinol" name="retinol" type="text" class="k-textbox" 
		       data-bind="value: retinol" style="width:60px; text-align:right;"
			   value="${foodDRI.retinol}" validationMessage="请输入视黄醇"/>
		(单位：微克μg)
		<span class="k-invalid-msg" data-for="retinol"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="nicotinicCid" class="required">尼克酸&nbsp;</label>
		<input id="nicotinicCid" name="nicotinicCid" type="text" class="k-textbox" 
		       data-bind="value: nicotinicCid" style="width:60px; text-align:right;"
			   value="${foodDRI.nicotinicCid}" validationMessage="请输入尼克酸"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="nicotinicCid"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="calcium" class="required">钙&nbsp;</label>
		<input id="calcium" name="calcium" type="text" class="k-textbox" 
		       data-bind="value: calcium" style="width:60px; text-align:right;"
			   value="${foodDRI.calcium}" validationMessage="请输入钙"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="calcium"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="iron" class="required">铁&nbsp;</label>
		<input id="iron" name="iron" type="text" class="k-textbox" 
		       data-bind="value: iron" style="width:60px; text-align:right;"
			   value="${foodDRI.iron}" validationMessage="请输入铁"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="iron"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="zinc" class="required">锌&nbsp;</label>
		<input id="zinc" name="zinc" type="text" class="k-textbox" 
		       data-bind="value: zinc" style="width:60px; text-align:right;"
			   value="${foodDRI.zinc}" validationMessage="请输入锌"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="zinc"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="iodine" class="required">碘&nbsp;</label>
		<input id="iodine" name="iodine" type="text" class="k-textbox" 
		       data-bind="value: iodine" style="width:60px; text-align:right;"
			   value="${foodDRI.iodine}" validationMessage="请输入碘"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="iodine"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="phosphorus" class="required">磷&nbsp;</label>
		<input id="phosphorus" name="phosphorus" type="text" class="k-textbox" 
		       data-bind="value: phosphorus" style="width:60px; text-align:right;"
			   value="${foodDRI.phosphorus}" validationMessage="请输入磷"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="phosphorus"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="enableFlag" class="required">是否有效&nbsp;</label>
		<select id="enableFlag" name="enableFlag">
			<option value="Y">是</option>
			<option value="N">否</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("enableFlag").value="${foodDRI.enableFlag}";
		  </script>    
		<span class="k-invalid-msg" data-for="enableFlag"></span>
    </td>
  </tr>
 
    <tr>
        <td colspan="2" align="center" valign="bottom" height="30">&nbsp;
		 <#if heathcare_curd_perm == true>
          <table width="92%">
			<tr>
				<td width="50%" align="right"><input type="button" id="saveButton" class="k-button k-primary" style="width:90px" 
		           onclick="javascript:saveData();" value="保存"></td>
				<td width="50%"><input type="button" id="saveAsButton" class="k-button " style="width:90px" 
		           onclick="javascript:saveAsData();" value="另存"></td>
			</tr>
		 </table>
		 </#if>
	   </td>
    </tr>
</table>   
</form>
</div>
</body>
</html>