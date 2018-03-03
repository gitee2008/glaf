<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑食物成分信息</title>
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

	
    .div-inline{ display:inline} 

</style>
<script type="text/javascript">
                
  jQuery(function() {
    var viewModel = kendo.observable({
        "name": "${foodComposition.name}",
        "alias": "${foodComposition.alias}",
        "code": "${foodComposition.code}",
        "discriminator": "${foodComposition.discriminator}",
        "description": "${foodComposition.description}",
        "radical": "${foodComposition.radical}",
        "heatEnergy": "${foodComposition.heatEnergy}",
        "protein": "${foodComposition.protein}",
        "fat": "${foodComposition.fat}",
        "carbohydrate": "${foodComposition.carbohydrate}",
        "vitaminA": "${foodComposition.vitaminA}",
        "vitaminB1": "${foodComposition.vitaminB1}",
        "vitaminB2": "${foodComposition.vitaminB2}",
        "vitaminC": "${foodComposition.vitaminC}",
		"vitaminE": "${foodComposition.vitaminE}",
        "carotene": "${foodComposition.carotene}",
        "retinol": "${foodComposition.retinol}",
        "nicotinicCid": "${foodComposition.nicotinicCid}",
        "calcium": "${foodComposition.calcium}",
        "iron": "${foodComposition.iron}",
        "zinc": "${foodComposition.zinc}",
	    "iodine": "${foodComposition.iodine}",
		"phosphorus": "${foodComposition.phosphorus}",
        "calcium": "${foodComposition.calcium}",
        "copper": "${foodComposition.copper}",
        "magnesium": "${foodComposition.magnesium}",
	    "manganese": "${foodComposition.manganese}",
		"potassium": "${foodComposition.potassium}",	
		"selenium": "${foodComposition.selenium}",	
	    "colorFlag": "${foodComposition.colorFlag}",
	    "cerealFlag": "${foodComposition.cerealFlag}",
		"beansFlag": "${foodComposition.beansFlag}",
		"dailyFlag": "${foodComposition.dailyFlag}",
        "enableFlag": "${foodComposition.enableFlag}",
        "id": "${foodComposition.id}"
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
	    var link = "${contextPath}/heathcare/foodComposition/saveFoodComposition";
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
					       //window.parent.location.reload();
					   }
				   }
			 });
   }

  function saveAsData(){
	    //alert("保存数据......");
		document.getElementById("id").value="";
        var form = document.getElementById("iForm");
	    var link = "${contextPath}/heathcare/foodComposition/saveFoodComposition";
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

 function switchXY(){
    var nodeId = document.getElementById("nodeId").value;
    if(nodeId == "4405"){
        jQuery("#div_colorFlag").show();
	} else {
        jQuery("#div_colorFlag").hide();
	}
	if(nodeId == "4402"){
        jQuery("#div_cerealFlag").show();
	} else {
        jQuery("#div_cerealFlag").hide();
	}
	if(nodeId == "4404"){
        jQuery("#div_beansFlag").show();
	} else {
        jQuery("#div_beansFlag").hide();
	}
 }
 </script>
</head>
<body style="margin-top:0px;">
<div id="main_content" class="k-content ">
<br>
<div class="x_content_title"><img
	src="${contextPath}/static/images/window.png" alt="编辑食物成分信息">&nbsp;
编辑食物成分信息</div>
<br>
<form id="iForm" name="iForm" method="post" >
<input type="hidden" id="id" name="id" value="${foodComposition.id}"/>
<table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	 <label for="nodeId" >食物分类&nbsp;</label>
	 <select id="nodeId" name="nodeId" onchange="switchXY();">
		<option value="0">----请选择----</option>
		<#list foodCategories as tree>
		<option value="${tree.id}">${tree.name}</option>
		</#list> 
	  </select>
	  <script type="text/javascript">
	       document.getElementById("nodeId").value="${foodComposition.nodeId}";
	  </script>
	  <span class="k-invalid-msg" data-for="nodeId"></span>
    </td>
  </tr>
  <tr id="div_cerealFlag" style="display:none">
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	 <label for="cerealFlag">粮食分类&nbsp;</label>
	 <select id="cerealFlag" name="cerealFlag">
		<option value="">----请选择----</option>
		<option value="R">米面</option>
        <option value="O">细粮</option>
		<option value="Z">粗粮</option>
		<option value="X">杂粮</option>
		<option value="C">糕点</option>
	  </select>
	  <script type="text/javascript">
	       document.getElementById("cerealFlag").value="${foodComposition.cerealFlag}";
	  </script>
	  <span class="k-invalid-msg" data-for="cerealFlag"></span>
    </td>
  </tr>
  <tr id="div_colorFlag" style="display:none">
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	 <label for="colorFlag">蔬菜分类&nbsp;</label>
	 <select id="colorFlag" name="colorFlag">
		<option value="">----请选择----</option>
        <option value="Y">绿橙蔬菜</option>
		<option value="N">非绿蔬菜</option>
	  </select>
	  <script type="text/javascript">
	       document.getElementById("colorFlag").value="${foodComposition.colorFlag}";
	  </script>
	  <span class="k-invalid-msg" data-for="colorFlag"></span>
    </td>
  </tr>
  <tr id="div_beansFlag" style="display:none">
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	 <label for="beansFlag">豆类分类&nbsp;</label>
	 <select id="beansFlag" name="beansFlag">
		<option value="">----请选择----</option>
        <option value="Y">干豆</option>
		<option value="X">杂豆</option>
		<option value="O">豆制品</option>
	  </select>
	  <script type="text/javascript">
	       document.getElementById("beansFlag").value="${foodComposition.beansFlag}";
	  </script>
	  <span class="k-invalid-msg" data-for="beansFlag"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="name" class="required">名称&nbsp;</label>
        <input id="name" name="name" type="text" class="k-textbox"  
	           data-bind="value: name" style="width:320px;"
	           value="${foodComposition.name}" validationMessage="请输入名称"/>
	    <span class="k-invalid-msg" data-for="name"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="alias" >别名&nbsp;</label>
        <input id="alias" name="alias" type="text" class="k-textbox"  
	           data-bind="value: alias" style="width:320px;"
	           value="${foodComposition.alias}" validationMessage="请输入别名"/>
	    <span class="k-invalid-msg" data-for="alias"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="code" >代码&nbsp;</label>
        <input id="code" name="code" type="text" class="k-textbox"  
			   data-bind="value: code" style="width:320px;"
			   value="${foodComposition.code}" validationMessage="请输入代码"/>
		<span class="k-invalid-msg" data-for="code"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="description" >描述&nbsp;</label>
        <textarea  id="description" name="description" rows="6" cols="46" class="k-textbox" 
		           style="height:90px;width:320px;" >${foodComposition.description}</textarea>
	    <span class="k-invalid-msg" data-for="description"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="radical" >食部&nbsp;</label>
		<input id="radical" name="radical" type="text" class="k-textbox" 
		       data-bind="value: radical" style="width:60px; text-align:right; "
			   value="${foodComposition.radical}" validationMessage="请输入食部"/>
		<span class="k-invalid-msg" data-for="radical"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="heatEnergy" >热能&nbsp;</label>
		<input id="heatEnergy" name="heatEnergy" type="text" class="k-textbox" 
		       data-bind="value: heatEnergy" style="width:60px; text-align:right; "
			   value="${foodComposition.heatEnergy}" validationMessage="请输入热能"/>
		(单位：千卡kcal)
		<span class="k-invalid-msg" data-for="heatEnergy"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="protein" >蛋白质&nbsp;</label>
		<input id="protein" name="protein" type="text" class="k-textbox" 
		       data-bind="value: protein" style="width:60px; text-align:right; "
			   value="${foodComposition.protein}" validationMessage="请输入蛋白质"/>
		(单位：克g)
		<span class="k-invalid-msg" data-for="protein"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="fat" >脂肪&nbsp;</label>
		<input id="fat" name="fat" type="text" class="k-textbox" 
		       data-bind="value: fat" style="width:60px; text-align:right; "
			   value="${foodComposition.fat}" validationMessage="请输入脂肪"/>
		(单位：克g)
		<span class="k-invalid-msg" data-for="fat"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="carbohydrate" >碳水化合物&nbsp;</label>
		<input id="carbohydrate" name="carbohydrate" type="text" class="k-textbox" 
		       data-bind="value: carbohydrate" style="width:60px; text-align:right; "
			   value="${foodComposition.carbohydrate}" validationMessage="请输入碳水化合物"/>
		(单位：克g)
		<span class="k-invalid-msg" data-for="carbohydrate"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="vitaminA" >微生素A&nbsp;</label>
		<input id="vitaminA" name="vitaminA" type="text" class="k-textbox" 
		       data-bind="value: vitaminA" style="width:60px; text-align:right; "
			   value="${foodComposition.vitaminA}" validationMessage="请输入微生素A"/>
		(单位：μgRE)
		<span class="k-invalid-msg" data-for="vitaminA"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="carotene" >胡萝卜素&nbsp;</label>
		<input id="carotene" name="carotene" type="text" class="k-textbox" 
		       data-bind="value: carotene" style="width:60px; text-align:right; "
			   value="${foodComposition.carotene}" validationMessage="请输入胡萝卜素"/>
		(单位：微克μg)
		<span class="k-invalid-msg" data-for="carotene"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="retinol" >视黄醇&nbsp;</label>
		<input id="retinol" name="retinol" type="text" class="k-textbox" 
		       data-bind="value: retinol" style="width:60px; text-align:right; "
			   value="${foodComposition.retinol}" validationMessage="请输入视黄醇"/>
		(单位：微克μg)
		<span class="k-invalid-msg" data-for="retinol"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="nicotinicCid" >烟酸(尼克酸)&nbsp;</label>
		<input id="nicotinicCid" name="nicotinicCid" type="text" class="k-textbox" 
		       data-bind="value: nicotinicCid" style="width:60px; text-align:right; "
			   value="${foodComposition.nicotinicCid}" validationMessage="请输入烟酸(尼克酸)"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="nicotinicCid"></span>
    </td>
  </tr>
   <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="vitaminC" >微生素C&nbsp;</label>
		<input id="vitaminC" name="vitaminC" type="text" class="k-textbox" 
		       data-bind="value: vitaminC" style="width:60px; text-align:right; "
			   value="${foodComposition.vitaminC}" validationMessage="请输入微生素C"/>
	    (单位：毫克mg)
		<span class="k-invalid-msg" data-for="vitaminC"></span>
    </td>
  </tr>
  <!-- <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="vitaminB1" >微生素B1&nbsp;</label>
		<input id="vitaminB1" name="vitaminB1" type="text" class="k-textbox" 
		       data-bind="value: vitaminB1" style="width:60px; text-align:right; "
			   value="${foodComposition.vitaminB1}" validationMessage="请输入微生素B1"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="vitaminB1"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="vitaminB2" >微生素B2&nbsp;</label>
		<input id="vitaminB2" name="vitaminB2" type="text" class="k-textbox" 
		       data-bind="value: vitaminB2" style="width:60px; text-align:right; "
			   value="${foodComposition.vitaminB2}" validationMessage="请输入微生素B2"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="vitaminB2"></span>
    </td>
  </tr> -->

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="vitaminE" >微生素E&nbsp;</label>
		<input id="vitaminE" name="vitaminE" type="text" class="k-textbox" 
		       data-bind="value: vitaminE" style="width:60px; text-align:right; "
			   value="${foodComposition.vitaminE}" validationMessage="请输入微生素E"/>
	    (单位：毫克mg)
		<span class="k-invalid-msg" data-for="vitaminE"></span>
    </td>
  </tr>
  
  
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="calcium" >钙&nbsp;</label>
		<input id="calcium" name="calcium" type="text" class="k-textbox" 
		       data-bind="value: calcium" style="width:60px; text-align:right; "
			   value="${foodComposition.calcium}" validationMessage="请输入钙"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="calcium"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="phosphorus" >磷&nbsp;</label>
		<input id="phosphorus" name="phosphorus" type="text" class="k-textbox" 
		       data-bind="value: phosphorus" style="width:60px; text-align:right; "
			   value="${foodComposition.phosphorus}" validationMessage="请输入磷"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="phosphorus"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="potassium" >钾&nbsp;</label>
		<input id="potassium" name="potassium" type="text" class="k-textbox" 
		       data-bind="value: potassium" style="width:60px; text-align:right; "
			   value="${foodComposition.potassium}" validationMessage="请输入钾"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="potassium"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="magnesium" >镁&nbsp;</label>
		<input id="magnesium" name="magnesium" type="text" class="k-textbox" 
		       data-bind="value: magnesium" style="width:60px; text-align:right; "
			   value="${foodComposition.magnesium}" validationMessage="请输入镁"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="magnesium"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="iron" >铁&nbsp;</label>
		<input id="iron" name="iron" type="text" class="k-textbox" 
		       data-bind="value: iron" style="width:60px; text-align:right; "
			   value="${foodComposition.iron}" validationMessage="请输入铁"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="iron"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="zinc" >锌&nbsp;</label>
		<input id="zinc" name="zinc" type="text" class="k-textbox" 
		       data-bind="value: zinc" style="width:60px; text-align:right; "
			   value="${foodComposition.zinc}" validationMessage="请输入锌"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="zinc"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="selenium" >硒&nbsp;</label>
		<input id="selenium" name="selenium" type="text" class="k-textbox" 
		       data-bind="value: selenium" style="width:60px; text-align:right; "
			   value="${foodComposition.selenium}" validationMessage="请输入碘"/>
		(单位：微克ug)
		<span class="k-invalid-msg" data-for="selenium"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="copper" >铜&nbsp;</label>
		<input id="copper" name="copper" type="text" class="k-textbox" 
		       data-bind="value: copper" style="width:60px; text-align:right; "
			   value="${foodComposition.copper}" validationMessage="请输入铜"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="copper"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="manganese" >锰&nbsp;</label>
		<input id="manganese" name="manganese" type="text" class="k-textbox" 
		       data-bind="value: manganese" style="width:60px; text-align:right; "
			   value="${foodComposition.manganese}" validationMessage="请输入锰"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="manganese"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="iodine" >碘&nbsp;</label>
		<input id="iodine" name="iodine" type="text" class="k-textbox" 
		       data-bind="value: iodine" style="width:60px; text-align:right; "
			   value="${foodComposition.iodine}" validationMessage="请输入碘"/>
		(单位：毫克mg)
		<span class="k-invalid-msg" data-for="iodine"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="dailyFlag" >是否每日采购&nbsp;</label>
		<select id="dailyFlag" name="dailyFlag">
			<option value="Y">是</option>
			<option value="N">否</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("dailyFlag").value="${foodComposition.dailyFlag}";
		  </script>    
		<span class="k-invalid-msg" data-for="dailyFlag"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="enableFlag" >是否有效&nbsp;</label>
		<select id="enableFlag" name="enableFlag">
			<option value="Y">是</option>
			<option value="N">否</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("enableFlag").value="${foodComposition.enableFlag}";
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
<script>
    jQuery(document).ready(function() {
	  switchXY();
    });
</script>
</body>
</html>