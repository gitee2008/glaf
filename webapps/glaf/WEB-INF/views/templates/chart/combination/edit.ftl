<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑组合图表信息</title>
<#include "/inc/init_kendoui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/glaf-base.js"></script>
<script type="text/javascript" src="${contextPath}/static/scripts/glaf-core.js"></script>
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
        "name": "${chartCombination.name}",
        "title": "${chartCombination.title}",
        "serviceKey": "${chartCombination.serviceKey}",
        "category": "${chartCombination.category}",
        "chartIds": "${chartCombination.chartIds}",
        "id": "${chartCombination.id}"
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

   function save(){
       var form = document.getElementById("iForm");
       var validator = jQuery("#iForm").data("kendoValidator");
       if (validator.validate()) {
	   //form.method="post";
	   //form.action = "${contextPath}/chart/combination/saveChartCombination";
	   //form.submit();
	   var link = "${contextPath}/chart/combination/saveChartCombination";
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
   }

	function openChart(){
        var selected = jQuery("#chartIds").val();
        var link = '${contextPath}/chart/chartTree?elementId=chartIds&elementName=chartNames&nodeCode=report_category&selected='+selected;
		var x=100;
		var y=100;
		if(is_ie) {
			x=document.body.scrollLeft+event.clientX-event.offsetX-200;
			y=document.body.scrollTop+event.clientY-event.offsetY-200;
		}
		openWindow(link,self,x, y, 495, 480);
	}

    function openChart2(){
        var link = '${contextPath}/chart/combination/chooseChart?id=${chartCombination.id}&elementId=chartIds&elementName=chartNames';
		var x=100;
		var y=100;
		if(is_ie) {
				x=document.body.scrollLeft+event.clientX-event.offsetX-200;
				y=document.body.scrollTop+event.clientY-event.offsetY-200;
		}
		openWindow(link,self,x, y, 695, 480);
	}

 </script>
</head>
<body style="margin-top:0px;">
<div id="main_content" class="k-content ">
<br>
<div class="x_content_title"><img
	src="${contextPath}/static/images/window.png" alt="编辑组合图表信息">&nbsp;
编辑组合图表信息</div>
<br>
<form id="iForm" name="iForm" method="post" data-role="validator" novalidate="novalidate">
<input type="hidden" id="id" name="id" value="${chartCombination.id}"/>
<table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	<label for="name" class="required">名称&nbsp;</label>
        <input id="name" name="name" type="text" class="k-textbox"  
	           data-bind="value: name"
	           value="${chartCombination.name}" validationMessage="请输入名称"/>
	<span class="k-invalid-msg" data-for="name"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	<label for="title" class="required">标题&nbsp;</label>
        <input id="title" name="title" type="text" class="k-textbox"  
	           data-bind="value: title"
	           value="${chartCombination.title}" validationMessage="请输入标题"/>
	<span class="k-invalid-msg" data-for="title"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	<label for="serviceKey" class="required">标识&nbsp;</label>
        <input id="serviceKey" name="serviceKey" type="text" class="k-textbox"  
	           data-bind="value: serviceKey"
	           value="${chartCombination.serviceKey}" validationMessage="请输入标识"/>
	<span class="k-invalid-msg" data-for="serviceKey"></span>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	<label for="type" class="required">图表类别&nbsp;</label>
      <select id="type" name="type">
		<option value="highcharts">Highcharts</option>
	  </select>    
	  <script type="text/javascript">
	       document.getElementById("type").value="${chartCombination.type}";
	  </script>    
	<span class="k-invalid-msg" data-for="type"></span>
    </td>
  </tr>
  <!-- <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	<label for="category" class="required">分类&nbsp;</label>
         
	<span class="k-invalid-msg" data-for="category"></span>
    </td>
  </tr> -->
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="chartIds" class="required">相关图表&nbsp;</label>
        <input type="hidden" id="chartIds" name="chartIds" value="${chartCombination.chartIds}">
		<textarea type="textarea" id="chartNames" name="chartNames"
				  value="${chartNames}" readonly="true" class="k-textbox x-textarea"
				  onclick="javascript:openChart();"
				  style="width: 335px; height: 90px;">${chartNames}</textarea>
		&nbsp; <a href="#" onclick="javascript:openChart();">
		<img src="${contextPath}/static/images/process.gif" border="0">
		</a>
    </td>
  </tr>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	<label for="direction" class="required">方向&nbsp;</label>
      <select id="direction" name="direction">
		<option value="H">水平</option>
		<option value="V">垂直</option>
	  </select>
	  <script type="text/javascript">
	       document.getElementById("direction").value="${chartCombination.direction}";
	  </script>    
	<span class="k-invalid-msg" data-for="direction"></span>
    </td>
  </tr>
   
    <tr>
        <td colspan="2" align="center" valign="bottom" height="30">&nbsp;
        <div>
          <button type="button" id="iconButton"  class="k-button k-primary" style="width: 90px" onclick="javascript:save();">保存</button>
	</div>
	</td>
      </tr>
</table>   
</form>
</div>     
 
</body>
</html>