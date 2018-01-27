<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑聚合表信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
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

    function save(){
        var form = document.getElementById("iForm");
	    var link = "${request.contextPath}/sys/treeTableAggregateRule/saveRule";
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

    function saveAs(){
	    document.getElementById("id").value="";
        var form = document.getElementById("iForm");
	    var link = "${request.contextPath}/sys/treeTableAggregateRule/saveRule";
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

 </script>
</head>
<body style="margin-top:0px;">
<div id="main_content" class="k-content ">
<br>
<div class="x_content_title"><img
	src="${request.contextPath}/static/images/window.png" alt="编辑聚合表规则">&nbsp;
    编辑聚合表规则</div>
<br>
<form id="iForm" name="iForm" method="post" data-role="validator" novalidate="novalidate">
<input type="hidden" id="aggregateId" name="aggregateId" value="${aggregateId}"/>
<input type="hidden" id="id" name="id" value="${treeTableAggregateRule.id}"/>
<table width="98%" align="center" border="0" cellspacing="0" cellpadding="5">

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="title" class="required">标题&nbsp;</label>
        <input id="title" name="title" type="text" class="easyui-validatebox x-text"  
	           data-bind="value: title" style="width:380px"
	           value="${treeTableAggregateRule.title}" validationMessage="请输入标题"/>
	    <span class="k-invalid-msg" data-for="title"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="sourceColumnName" class="required">源字段&nbsp;</label>
		<select id="sourceColumnName" name="sourceColumnName" >
			<option value="">----请选择----</option>    
			<#list  columns as item>
			<option value="${item.columnName}">${item.columnName}</option>     
			</#list>
		</select>
        <script type="text/javascript">
	       document.getElementById("sourceColumnName").value="${treeTableAggregateRule.sourceColumnName}";
	    </script>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="targetColumnTitle" class="required">转换后列中文名称&nbsp;</label>
        <input id="targetColumnTitle" name="targetColumnTitle" type="text" class="easyui-validatebox x-text"  
	           data-bind="value: targetColumnTitle" style="width:380px"
	           value="${treeTableAggregateRule.targetColumnTitle}" validationMessage="请输入转换后列中文名称"/>
	    <span class="k-invalid-msg" data-for="targetColumnTitle"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="targetColumnType" class="required">字段类型&nbsp;</label>
		<select id="targetColumnType" name="targetColumnType" >
			<option value="">----请选择----</option>    
			<option value="String">字符串型</option>
			<option value="Integer">整数型</option>    
			<option value="Long">长整数型</option> 
			<option value="Double">数值型</option>    
		</select>
	    <span class="k-invalid-msg" data-for="targetColumnType"></span>
	    <script type="text/javascript">
	       document.getElementById("targetColumnType").value="${treeTableAggregateRule.targetColumnType}";
	    </script>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="aggregateType" class="required">聚合类型&nbsp;</label>
		<select id="aggregateType" name="aggregateType" >
			<option value="">----请选择----</option>    
			<option value="SUM">逐级汇总</option>   
		</select>
	    <span class="k-invalid-msg" data-for="aggregateType"></span>
	    <script type="text/javascript">
	       document.getElementById("aggregateType").value="${treeTableAggregateRule.aggregateType}";
	    </script>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left" height="25">&nbsp;</td>
    <td align="left">
	  <label for="locked" class="required">是否有效&nbsp;</label>
	  <select id="locked" name="locked">
		<option value="0">是</option>
		<option value="1">否</option>
	  </select>
	  <script type="text/javascript">
	       document.getElementById("locked").value="${treeTableAggregateRule.locked}";
	  </script>
    </td>
  </tr>

  <tr>
    <td colspan="2" align="center" valign="bottom" height="25">&nbsp;
    <div>
      <button type="button" id="iconButton"  class="btn btnGray" style="width: 90px" onclick="javascript:save();">保存</button>
	  &nbsp;
	  <button type="button" id="iconButton"  class="btn btnGray" style="width: 90px" onclick="javascript:saveAs();">另存</button>
	</div>
	</td>
  </tr>
</table>   
</form>
</div>     
<br/>
<br/>
</body>
</html>