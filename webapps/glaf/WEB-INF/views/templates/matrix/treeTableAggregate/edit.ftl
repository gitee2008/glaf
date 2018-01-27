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

   .easyui-validatebox x-text {
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
	    var link = "${request.contextPath}/sys/treeTableAggregate/saveTreeTableAggregate";
	    var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: link,
				   dataType: 'json',
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
        var form = document.getElementById("iForm");
	    var link = "${request.contextPath}/sys/treeTableAggregate/saveAs";
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

	function openDatabaseX(){
        var selected = jQuery("#databaseIds").val();
        var link = '${request.contextPath}/sys/treeTableAggregate/chooseDatabases?elementId=databaseIds&elementName=selectedDB&selected='+selected+"&treeTableAggregateId=${treeTableAggregate.id}";
        var x=100;
        var y=100;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link,self,x, y, 695, 480);
    }
 
    function openColumnsX(){
        var selected = jQuery("#syncColumns").val();
		var sourceDatabaseIds = jQuery("#databaseIds").val();
		var sourceTableName = jQuery("#sourceTableName").val();
        var link = '${request.contextPath}/sys/treeTableAggregate/chooseColumns?elementId=syncColumns&elementName=syncColumns&selected='+
			selected+"&treeTableAggregateId=${treeTableAggregate.id}&databaseIds="+sourceDatabaseIds+"&tableName="+sourceTableName;
        var x=100;
        var y=100;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link,self,x, y, 695, 480);
    }

	function openConditionWin(){
        var x=100;
        var y=100;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
		var link = "${request.contextPath}/sys/sqlCriteria?moduleId=treetable_aggregate&businessKey=${treeTableAggregate.id}&tableName=${treeTableAggregate.sourceTableName}";
        openWindow(link, self, x, y, 1080, 520);
	}
 
 </script>
</head>
<body style="margin-top:0px;">
<div id="main_content" class="k-content ">
<br>
<div class="x_content_title"><img
	src="${request.contextPath}/static/images/window.png" alt="编辑聚合表信息">&nbsp;
    编辑聚合表信息</div>
<br>
<form id="iForm" name="iForm" method="post"  novalidate="novalidate">
<input type="hidden" id="id" name="id" value="${treeTableAggregate.id}"/>
<table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="title" class="required">标题&nbsp;</label>
        <input id="title" name="title" type="text" class="easyui-validatebox x-text"  
	           data-bind="value: title" style="width:380px"
	           value="${treeTableAggregate.title}" validationMessage="请输入标题"/>
	    <span class="k-invalid-msg" data-for="title"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="selectedDB" class="required">转换数据库&nbsp;</label>
		 <input type="hidden" id="databaseIds" name="databaseIds" value="${treeTableAggregate.databaseIds}">
         <textarea id="selectedDB" name="selectedDB" rows="12" cols="68" class="x-textarea"
				  style="width:380px;height:80px;" onclick="javascript:openDatabaseX();"  
				  readonly="true" >${selectedDB}</textarea>&nbsp;
	    <a href="#" onclick="javascript:openDatabaseX();">
			<img src="${request.contextPath}/static/images/search_results.gif" border="0">
		</a>  
	    <span class="k-invalid-msg" data-for="selectedDB"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="sourceTableName" class="required">来源表&nbsp;</label>
        <input id="sourceTableName" name="sourceTableName" type="text" class="easyui-validatebox x-text"  
	           data-bind="value: sourceTableName" style="width:380px"
	           value="${treeTableAggregate.sourceTableName}" validationMessage="请输入来源表"/>
	    <span class="k-invalid-msg" data-for="sourceTableName"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="sourceIdColumn" class="required">来源表主键列&nbsp;</label>
        <input id="sourceIdColumn" name="sourceIdColumn" type="text" class="easyui-validatebox x-text"  
	           data-bind="value: sourceIdColumn" style="width:380px"
	           value="${treeTableAggregate.sourceIdColumn}" validationMessage="请输入来源表主键列"/>
	    <span class="k-invalid-msg" data-for="sourceIdColumn"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="sourceIndexIdColumn" class="required">来源表IndexId列&nbsp;</label>
        <input id="sourceIndexIdColumn" name="sourceIndexIdColumn" type="text" class="easyui-validatebox x-text"  
	           data-bind="value: sourceIndexIdColumn" style="width:380px"
	           value="${treeTableAggregate.sourceIndexIdColumn}" validationMessage="请输入来源表IndexId列"/>
		&nbsp;（提示：数据类型仅限整数型）
	    <span class="k-invalid-msg" data-for="sourceIndexIdColumn"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="sourceParentIdColumn" class="required">来源表ParentId列&nbsp;</label>
        <input id="sourceParentIdColumn" name="sourceParentIdColumn" type="text" class="easyui-validatebox x-text"  
	           data-bind="value: sourceParentIdColumn" style="width:380px"
	           value="${treeTableAggregate.sourceParentIdColumn}" validationMessage="请输入来源表ParentId列"/>
		&nbsp;（提示：数据类型仅限整数型）
	    <span class="k-invalid-msg" data-for="sourceParentIdColumn"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="sourceTreeIdColumn" class="required">来源表TreeId列&nbsp;</label>
        <input id="sourceTreeIdColumn" name="sourceTreeIdColumn" type="text" class="easyui-validatebox x-text"  
	           data-bind="value: sourceTreeIdColumn" style="width:380px"
	           value="${treeTableAggregate.sourceTreeIdColumn}" validationMessage="请输入来源表TreeId列"/>
		&nbsp;（提示：数据类型仅限字符串型）
	    <span class="k-invalid-msg" data-for="sourceTreeIdColumn"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="sourceTextColumn" class="required">来源表名称列&nbsp;</label>
        <input id="sourceTextColumn" name="sourceTextColumn" type="text" class="easyui-validatebox x-text"  
	           data-bind="value: sourceTextColumn" style="width:380px"
	           value="${treeTableAggregate.sourceTextColumn}" validationMessage="请输入来源表名称列"/>
		&nbsp;（提示：数据类型仅限字符串型）
	    <span class="k-invalid-msg" data-for="sourceTextColumn"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="sourceTableDateSplitColumn" class="required">来源表日期切分列&nbsp;</label>
        <input id="sourceTableDateSplitColumn" name="sourceTableDateSplitColumn" type="text" class="easyui-validatebox x-text"  
	           data-bind="value: sourceTableDateSplitColumn" style="width:380px"
	           value="${treeTableAggregate.sourceTableDateSplitColumn}" validationMessage="请输入来源表日期切分列"/>
		&nbsp;（提示：数据类型必须是日期型）
	    <span class="k-invalid-msg" data-for="sourceTableDateSplitColumn"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="startYear" class="required">开始年份&nbsp;</label>
        <input id="startYear" name="startYear" type="text" class="easyui-validatebox x-text"  
	           data-bind="value: startYear" style="width:60px" maxlength="4"
	           value="${treeTableAggregate.startYear}" validationMessage="请输入开始年份"/>
		&nbsp;（提示：如果不填写年份，开始年份默认为当年）
	    <span class="k-invalid-msg" data-for="startYear"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="stopYear" class="required">结束年份&nbsp;</label>
        <input id="stopYear" name="stopYear" type="text" class="easyui-validatebox x-text"  
	           data-bind="value: stopYear" style="width:60px" maxlength="4"
	           value="${treeTableAggregate.stopYear}" validationMessage="请输入结束年份"/>
		&nbsp;（提示：如果不填写年份，结束年份默认为当年）
	    <span class="k-invalid-msg" data-for="stopYear"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="syncColumns" class="required">同步列&nbsp;</label>
        <textarea id="syncColumns" name="syncColumns" rows="12" cols="68" class="x-textarea"
				  style="width:380px;height:150px;" onclick="javascript:openColumnsX();"  
				  readonly="true" >${treeTableAggregate.syncColumns}</textarea>&nbsp;
	    <a href="#" onclick="javascript:openColumnsX();">
			<img src="${request.contextPath}/static/images/search_results.gif" border="0">
		</a> 
	    <span class="k-invalid-msg" data-for="syncColumns"></span>
    </td>
  </tr>

  <#if treeTableAggregate?if_exists>
  <tr>
    <td width="2%" align="left" height="25">&nbsp;</td>
    <td align="left">
	  <label for="dynamicCondition" class="required">自定义参数条件&nbsp;</label>
	  <button type="button" id="qcButton"  class="btn btnGray" style="width: 120px" 
	          onclick="javascript:openConditionWin();">编辑条件</button>
    </td>
  </tr>
  </#if>

  <tr>
    <td width="2%" align="left" height="25">&nbsp;</td>
    <td align="left">
	  <label for="dynamicCondition" class="required">允许动态条件&nbsp;</label>
	  <select id="dynamicCondition" name="dynamicCondition">
	    <option value="N">否</option>
		<option value="Y">是</option>
	  </select>
	  <script type="text/javascript">
	       document.getElementById("dynamicCondition").value="${treeTableAggregate.dynamicCondition}";
	  </script>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="sqlCriteria" class="required">提取SQL条件&nbsp;</label>
        <textarea id="sqlCriteria" name="sqlCriteria" rows="12" cols="68" class="easyui-validatebox x-text"
				  style="width:380px;height:80px;" >${treeTableAggregate.sqlCriteria}</textarea>&nbsp;
	    <span class="k-invalid-msg" data-for="sqlCriteria"></span>
		&nbsp;（提示：添加and条件即可）
    </td>
  </tr>

 
  <#if treeTableAggregate.targetTableName?if_exists>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="targetTableName" class="required">目标表&nbsp;</label>
		${treeTableAggregate.targetTableName} 
    </td>
  </tr>
  <#else>
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
		<label for="targetTableName" class="required">目标表&nbsp;</label>
		<select id="targetTableName" name="targetTableName" >
			<option value="">----请选择----</option>    
			<#list  targetTables as item>
			<option value="${item.targetTableName}">${item.title}[${item.targetTableName}]</option>     
			</#list>
		</select>
		<div style="margin-top:5px;">
			<span style="color:blue; margin-left:110px;">
			 （提示：可以选择已经存在的表做目标表，如不选择，将生成新表做目标表。）
			</span>
	   </div>
    </td>
  </tr>
  </#if>

  <tr>
    <td width="2%" align="left" height="25">&nbsp;</td>
    <td align="left">
	    <label for="createTableFlag" class="required">是否需要建表&nbsp;</label>
        <select id="createTableFlag" name="createTableFlag">
			<option value="">----请选择----</option>
			<option value="N">否</option>
			<option value="Y">是</option>
	    </select>&nbsp;（提示：表不存在时自动创建）
	    <script type="text/javascript">
	       document.getElementById("createTableFlag").value="${treeTableAggregate.createTableFlag}";
	    </script>
	    <span class="k-invalid-msg" data-for="createTableFlag"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left" height="25">&nbsp;</td>
    <td align="left">
	    <label for="deleteFetch" class="required">同步前删除&nbsp;</label>
        <select id="deleteFetch" name="deleteFetch">
			<option value="">----请选择----</option>
			<option value="Y">是</option>
			<option value="N">否</option>
	   </select>&nbsp;（提示：同步前清空目标表数据，只有临时表tmp_或etl_开头的表才生效）
	   <script type="text/javascript">
	       document.getElementById("deleteFetch").value="${treeTableAggregate.deleteFetch}";
	   </script>
	   <span class="k-invalid-msg" data-for="deleteFetch"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left" height="25">&nbsp;</td>
    <td align="left">
	    <label for="genByMonth" class="required">是否生成月度表&nbsp;</label>
        <select id="genByMonth" name="genByMonth">
			<option value="">----请选择----</option>
			<option value="N">不生成</option>
			<option value="D">每天生成</option>
			<option value="L">每月最后一天生成</option>
	    </select>&nbsp;（提示：新生成一张月度表，月度目标表yyyyMM）
	    <script type="text/javascript">
	       document.getElementById("genByMonth").value="${treeTableAggregate.genByMonth}";
	    </script>
	    <span class="k-invalid-msg" data-for="genByMonth"></span>
    </td>
  </tr>

  <tr>
		<td width="2%" height="25"></td>
		<td class="x-content" colspan="3" height="28">
		    <label for="sortNo" class="required">执行次序&nbsp;</label>
		    <select id="sortNo" name="sortNo" class="span2" style="height:20px">
			    <option value="0">0</option>
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				<option value="8">8</option>
				<option value="9">9</option>
		    </select>&nbsp;（提示：数值小的先执行）
			<script type="text/javascript">
			    document.getElementById("sortNo").value="${treeTableAggregate.sortNo}";
			</script>
		</td>
	</tr>

  <tr>
    <td width="2%" align="left" height="25">&nbsp;</td>
    <td align="left">
	  <label for="scheduleFlag" class="required">是否实时调度&nbsp;</label>
	  <select id="scheduleFlag" name="scheduleFlag">
	    <option value="N">否</option>
		<option value="Y">是</option>
	  </select>
	  <script type="text/javascript">
	       document.getElementById("scheduleFlag").value="${treeTableAggregate.scheduleFlag}";
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
	       document.getElementById("locked").value="${treeTableAggregate.locked}";
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
 
</body>
</html>