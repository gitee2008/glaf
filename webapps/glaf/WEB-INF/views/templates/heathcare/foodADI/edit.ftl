<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑每日食物参考摄入量信息</title>
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
        "name": "${foodADI.name}",
        "description": "${foodADI.description}",
        "lowest": "${foodADI.lowest}",
        "average": "${foodADI.average}",
        "highest": "${foodADI.highest}",
        "ageGroup": "${foodADI.ageGroup}",
        "typeId": "${foodADI.typeId}",
        "sortNo": "${foodADI.sortNo}",
        "enableFlag": "${foodADI.enableFlag}",
        "id": "${foodADI.id}"
    });

    kendo.bind(jQuery("#iForm"), viewModel);

   });

    jQuery(document).ready(function() {
          jQuery("#iconButton").kendoButton({
                spriteCssClass: "k-icon"
          });           
    });

    jQuery(function () {
        var container = jQuery("#iForm");
        kendo.init(container);
        container.kendoValidator({
                rules: {
                      greaterdate: function (input) {
                            if (input.is("[data-greaterdate-msg]") && input.val() != "") {                                    
                               var date = kendo.parseDate(input.val()),
                               otherDate = kendo.parseDate(jQuery("[name='" + input.data("greaterdateField") + "']").val());
                               return otherDate == null || otherDate.getTime() < date.getTime();
                             }

                             return true;
                          }
                      }
        });
    });

  <#if heathcare_curd_perm == true>
   function saveData(){
       var form = document.getElementById("iForm");
       var validator = jQuery("#iForm").data("kendoValidator");
       if (validator.validate()) {
	    var link = "${contextPath}/heathcare/foodADI/saveFoodADI";
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
   }

   function saveAsData(){
	   document.getElementById("id").value="";
       var form = document.getElementById("iForm");
       var validator = jQuery("#iForm").data("kendoValidator");
       if (validator.validate()) {
	    var link = "${contextPath}/heathcare/foodADI/saveFoodADI";
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
   }
  </#if>
 </script>
</head>
<body style="margin-top:0px;">
<div id="main_content" class="k-content ">
<br>
<div class="x_content_title"><img
	src="${contextPath}/static/images/window.png" alt="编辑每日食物参考摄入量信息">&nbsp;
编辑每日食物参考摄入量信息</div>
<br>
<form id="iForm" name="iForm" method="post" data-role="validator" novalidate="novalidate">
<input type="hidden" id="id" name="id" value="${foodADI.id}"/>
<table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	<label for="name" class="required">名称&nbsp;</label>
        <input id="name" name="name" type="text" class="k-textbox"  
	           data-bind="value: name" style="width:300px;"
	           value="${foodADI.name}" validationMessage="请输入名称"/>
	    <span class="k-invalid-msg" data-for="name"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="description" class="required">描述&nbsp;</label>
        <textarea  id="description" name="description" rows="6" cols="46" class="k-textbox" style="height:90px;width:300px;" >${foodADI.description}</textarea>
	    <span class="k-invalid-msg" data-for="description"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="ageGroup" class="required">年龄段&nbsp;</label>
        <select id="ageGroup" name="ageGroup">
		    <option value="1-2">1到2岁</option>
			<option value="3-6">3到6岁</option>
        </select>
		<script type="text/javascript">
		    document.getElementById("ageGroup").value="${foodADI.ageGroup}";
		</script>
	    <span class="k-invalid-msg" data-for="ageGroup"></span>
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
	       document.getElementById("typeId").value="${foodADI.typeId}";
	  </script>
	  <span class="k-invalid-msg" data-for="typeId"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	  <label for="nodeId" class="required">食物类别&nbsp;</label>
      <select id="nodeId" name="nodeId">
		<option value="0">----请选择----</option>
		<#list foodCategories as tree>
		<option value="${tree.id}">${tree.name}</option>
		</#list> 
	  </select>
	  <script type="text/javascript">
	       document.getElementById("nodeId").value="${foodADI.nodeId}";
	  </script>
	  <span class="k-invalid-msg" data-for="nodeId"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	<label for="lowest" class="required">最低推荐量&nbsp;</label>
	<input id="lowest" name="lowest" type="text" class="k-textbox" 
	       data-bind="value: lowest" style="width:60px; text-align:right;"
	       value="${foodADI.lowest}" validationMessage="请输入最低推荐量"/>
	<span class="k-invalid-msg" data-for="lowest"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	<label for="average" class="required">平均值&nbsp;</label>
	<input id="average" name="average" type="text" class="k-textbox" 
	       data-bind="value: average" style="width:60px; text-align:right;"
	       value="${foodADI.average}" validationMessage="请输入平均值"/>
	<span class="k-invalid-msg" data-for="average"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	<label for="highest" class="required">最高推荐量&nbsp;</label>
	<input id="highest" name="highest" type="text" class="k-textbox" 
	       data-bind="value: highest" style="width:60px; text-align:right;"
	       value="${foodADI.highest}" validationMessage="请输入最高推荐量"/>
	<span class="k-invalid-msg" data-for="highest"></span>
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
	       document.getElementById("enableFlag").value="${foodADI.enableFlag}";
	  </script>    
	<span class="k-invalid-msg" data-for="enableFlag"></span>
    </td>
  </tr>
  <tr>
      <td colspan="2" align="center" valign="bottom" height="30">&nbsp;
       <div>
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
	   </div>
	</td>
  </tr>
</table>   
</form>
</div>
</body>
</html>