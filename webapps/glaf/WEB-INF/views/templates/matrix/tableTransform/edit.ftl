<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑表字段转换信息</title>
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
 
	function openDB(){
        var selected = jQuery("#databaseIds").val();
        var link = '${request.contextPath}/sys/database/chooseDatabases?elementId=databaseIds&elementName=selectedDB&selectedDatabaseIds='+selected;
        var x=100;
        var y=100;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link,self,x, y, 695, 480);
    }

    function openQx(){
        var selected = jQuery("#transformColumns").val();
		var databaseIds = jQuery("#databaseIds").val();
		var tableName = jQuery("#tableName").val();
        var link = '${request.contextPath}/sys/tableTransform/chooseColumns?elementId=transformColumns&elementName=transformColumns&selected='+
			selected+"&databaseIds="+databaseIds+"&tableName="+tableName+"&transformId=${tableTransform.transformId}";
        var x=100;
        var y=100;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link,self,x, y, 695, 480);
    }

    function save(){
        var form = document.getElementById("iForm");
	    var link = "${request.contextPath}/sys/tableTransform/saveTableTransform";
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
        var form = document.getElementById("iForm");
	    document.getElementById("transformId").value="";
	    var link = "${request.contextPath}/sys/tableTransform/saveTableTransform";
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
            	      // window.parent.location.reload();
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
	src="${request.contextPath}/static/images/window.png" alt="编辑表转换信息">&nbsp;
编辑表转换信息</div>
<br>
<form id="iForm" name="iForm" method="post" data-role="validator" novalidate="novalidate">
<input type="hidden" id="transformId" name="transformId" value="${tableTransform.transformId}">
<table width="95%" align="center" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="tableName" class="required">表名&nbsp;</label>
		<#if tableTransform.tableName?if_exists>
		${tableTransform.tableName}
		<input type="hidden" id="tableName" name="tableName" value="${tableTransform.tableName}">
		<#else>
        <input id="tableName" name="tableName" type="text" class="easyui-validatebox x-text"  
               data-bind="value: tableName" maxlength="50" style="width:380px;"
               value="" validationMessage="请输入表名"/>
	    <span class="k-invalid-msg" data-for="tableName"></span>
	    </#if>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="title" class="required">标题&nbsp;</label>
        <input id="title" name="title" type="text" class="easyui-validatebox x-text"  
               data-bind="value: title" maxlength="200" style="width:380px;"
               value="${tableTransform.title}" validationMessage="请输入标题"/>
	    <span class="k-invalid-msg" data-for="title"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="selectedDB" class="required">转换数据库&nbsp;</label>
		<input type="hidden" id="databaseIds" name="databaseIds" value="${tableTransform.databaseIds}">
        <textarea id="selectedDB" name="selectedDB" rows="12" cols="68" class="easyui-validatebox x-text"
				  style="width:380px;height:80px;" onclick="javascript:openDB();"  
				  readonly="true" >${selectedDB}</textarea>&nbsp;
	    <a href="#" onclick="javascript:openDB();">
			<img src="${request.contextPath}/static/images/search_results.gif" border="0">
		</a>  
	    <span class="k-invalid-msg" data-for="selectedDB"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="primaryKey" class="required">主键列&nbsp;</label>
        <input id="primaryKey" name="primaryKey" type="text" class="easyui-validatebox x-text"  
               data-bind="value: primaryKey" maxlength="50" style="width:380px;"
               value="${tableTransform.primaryKey}" validationMessage="请输入主键列"/>
	    <span class="k-invalid-msg" data-for="primaryKey"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="transformColumns" class="required">参与转换列&nbsp;</label>
        <textarea id="transformColumns" name="transformColumns" rows="12" cols="68" class="easyui-validatebox x-text"
				  style="width:380px;height:150px;" readonly="true">${tableTransform.transformColumns}</textarea>
		&nbsp;
	    <a href="#" onclick="javascript:openQx();">
			<img src="${request.contextPath}/static/images/search_results.gif" border="0">
		</a> 
	    <span class="k-invalid-msg" data-for="transformColumns"></span>
		&nbsp;（提示：默认全部列参与转换）
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="sqlCriteria" class="required">转换SQL条件&nbsp;</label>
        <textarea id="sqlCriteria" name="sqlCriteria" rows="12" cols="68" class="easyui-validatebox x-text"
				  style="width:380px;height:80px;" >${tableTransform.sqlCriteria}</textarea>&nbsp;
	    <span class="k-invalid-msg" data-for="sqlCriteria"></span>
		 <div style="margin-top:5px;">
			<span style="  margin-left:110px;">
			 （提示：添加and条件即可，动态参数也是支持的 column1 = <script>document.write("#");</script>{param1}）。
			</span>
	     </div>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
        <label for="sort" class="required">顺序&nbsp;</label>
        <select id="sort" name="sort" class="span2" style="height:20px">
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
            document.getElementById("sort").value="${tableTransform.sort}";
		</script>
        <span class="k-invalid-msg" data-for="sort"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="locked" class="required">是否有效&nbsp;</label>
	    <select id="locked" name="locked">
		    <option value="0">是</option>
		    <option value="1">否</option>
	    </select>
	    <script type="text/javascript">
            document.getElementById("locked").value="${tableTransform.locked}";
	    </script>
    </td>
  </tr>

  <tr>
    <td colspan="2" align="center" valign="bottom" height="30">&nbsp;
     <div>
       <button type="button" id="iconButton"  class="btnGray" style="width: 90px" onclick="javascript:save();">保存</button>
	   <button type="button" id="iconButton"  class="btnGray" style="width: 90px" onclick="javascript:saveAs();">另存</button>
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