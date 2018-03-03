<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看食物成分信息</title>
<#include "/inc/init_easyui_import.ftl"/>
</head>
<body style="margin-top:0px;">
<div id="main_content" class="k-content ">
<br>
<div class="x_content_title">
  <img src="${contextPath}/static/images/window.png" alt="查看食物成分信息">&nbsp;查看食物成分信息
</div>
<br>
<table width="95%" align="center" border="0" cellspacing="1" cellpadding="5">
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="name" class="x_name">名称&nbsp;</label>
        <label class="x_text_value">&nbsp;${foodComposition.name}</label>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="alias" class="x_name">别名&nbsp;</label>
        <label class="x_text_value">&nbsp;${foodComposition.alias}</label>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="description" class="x_name">描述&nbsp;</label>
        <label class="x_text_value">&nbsp;${foodComposition.description}</label>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="radical" class="x_name">食部&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.radical}</label>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="heatEnergy" class="x_name">热能&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.heatEnergy}</label>
		&nbsp;(单位：千卡kcal)
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="protein" class="x_name">蛋白质&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.protein}</label>
		&nbsp;(单位：克g)
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="fat" class="x_name">脂肪&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.fat}</label>
		&nbsp;(单位：克g)
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="carbohydrate" class="x_name">碳水化合物&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.carbohydrate}</label>
		&nbsp;(单位：克g)
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="vitaminA" class="x_name">微生素A&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.vitaminA}</label>
		&nbsp;(单位：μgRE)
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="carotene" class="x_name">胡萝卜素&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.carotene}</label>
		&nbsp;(单位：微克ug)
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="retinol" class="x_name">视黄醇&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.retinol}</label>
		&nbsp;(单位：微克ug)
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="nicotinicCid" class="x_name">烟酸(尼克酸)&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.nicotinicCid}</label> 
		&nbsp;(单位：毫克mg)
    </td>
  </tr>
   <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="vitaminC" class="x_name">微生素C&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.vitaminC}</label>
	    &nbsp;(单位：毫克mg)
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="vitaminE" class="x_name">微生素E&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.vitaminE}</label>
	    &nbsp;(单位：毫克mg)
    </td>
  </tr>
  
  
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="calcium" class="x_name">钙&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.calcium}</label>
		&nbsp;(单位：毫克mg)
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="phosphorus" class="x_name">磷&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.phosphorus}</label>
		&nbsp;(单位：毫克mg)
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="potassium" class="x_name">钾&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.potassium}</label>
		&nbsp;(单位：毫克mg)
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="magnesium" class="x_name">镁&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.magnesium}</label>
		&nbsp;(单位：毫克mg)
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="iron" class="x_name">铁&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.iron}</label>
		&nbsp;(单位：毫克mg)
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="zinc" class="x_name">锌&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.zinc}</label>
		(单位：毫克mg)
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="selenium" class="x_name">硒&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.selenium}</label>
		&nbsp;(单位：微克ug)
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="iodine" class="x_name">碘&nbsp;</label>
		<label class="x_num_value">&nbsp;${foodComposition.iodine}</label>
		&nbsp;(单位：毫克mg)
    </td>
  </tr>

</table>   
</form>
</div>     
</body>
</html>