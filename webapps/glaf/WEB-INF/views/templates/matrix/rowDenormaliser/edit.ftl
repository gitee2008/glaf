<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
		var tableName = jQuery("#sourceTableName").val();
        var link = '${request.contextPath}/sys/rowDenormaliser/chooseColumns?elementId=syncColumns&elementName=syncColumns&selected='+
			selected+"&databaseIds="+databaseIds+"&tableName="+tableName+"&transformId=${rowDenormaliser.id}";
        var x=100;
        var y=100;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link,self,x, y, 695, 480);
    }

    function openQx2(){
        var selected = jQuery("#aggregateColumns").val();
		var databaseIds = jQuery("#databaseIds").val();
		var tableName = jQuery("#sourceTableName").val();
        var link = '${request.contextPath}/sys/rowDenormaliser/chooseAaggregateColumns?elementId=aggregateColumns&elementName=aggregateColumns&selected='+
			selected+"&databaseIds="+databaseIds+"&tableName="+tableName+"&transformId=${rowDenormaliser.id}";
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
	    var link = "${request.contextPath}/sys/rowDenormaliser/save";
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
	    document.getElementById("id").value="";
	    var link = "${request.contextPath}/sys/rowDenormaliser/save";
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
 </script>
</head>
<body style="margin-top:0px;">
<div id="main_content" class="k-content ">
<br>
<div class="x_content_title"><img
	 src="${request.contextPath}/static/images/window.png" alt="编辑行转列信息">&nbsp;
编辑行转列信息</div>
<br>
<form id="iForm" name="iForm" method="post" data-role="validator" novalidate="novalidate">
<input type="hidden" id="id" name="id" value="${rowDenormaliser.id}">
<table width="98%" align="center" border="0" cellspacing="0" cellpadding="5">
  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="title" class="required">标题&nbsp;</label>
        <input id="title" name="title" type="text" class="easyui-validatebox x-text"  
               data-bind="value: title" maxlength="200" style="width:380px;"
               value="${rowDenormaliser.title}" validationMessage="请输入标题"/>
	    <span class="k-invalid-msg" data-for="title"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="sourceTableName" class="required">源表名&nbsp;</label>
        <input id="sourceTableName" name="sourceTableName" type="text" class="easyui-validatebox x-text"  
               data-bind="value: sourceTableName" maxlength="50" style="width:380px;"
               value="${rowDenormaliser.sourceTableName}" validationMessage="请输入表名"/>
	    <span class="k-invalid-msg" data-for="sourceTableName"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="selectedDB" class="required">转换数据库&nbsp;</label>
		<input type="hidden" id="databaseIds" name="databaseIds" value="${rowDenormaliser.databaseIds}">
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
	    <label for="aggregateColumns" class="required">聚合列&nbsp;</label>
        <textarea id="aggregateColumns" name="aggregateColumns" rows="12" cols="68" class="easyui-validatebox x-text"
				  style="width:380px;height:60px;" readonly="true">${rowDenormaliser.aggregateColumns}</textarea>
		&nbsp;
	    <a href="#" onclick="javascript:openQx2();">
			<img src="${request.contextPath}/static/images/search_results.gif" border="0">
		</a> 
	    <span class="k-invalid-msg" data-for="aggregateColumns"></span>
	    <div style="margin-top:5px;">
			<span style="  margin-left:110px;">
			 （提示：所选的列用于组合构成新的主键。）
			</span>
	    </div>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="primaryKey" class="required">主键列&nbsp;</label>
        <input id="primaryKey" name="primaryKey" type="text" class="easyui-validatebox x-text"  
               data-bind="value: primaryKey" maxlength="50" style="width:380px;"
               value="${rowDenormaliser.primaryKey}" validationMessage="请输入主键列"/>
	    <span class="k-invalid-msg" data-for="primaryKey"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="transformColumn" class="required">转换列&nbsp;</label>
        <input id="transformColumn" name="transformColumn" rows="12" cols="68" class="easyui-validatebox x-text"
			   style="width:380px;" value="${rowDenormaliser.transformColumn}"> 
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="syncColumns" class="required">同步列&nbsp;</label>
        <textarea id="syncColumns" name="syncColumns" rows="12" cols="68" class="easyui-validatebox x-text"
				  style="width:380px;height:90px;" readonly="true">${rowDenormaliser.syncColumns}</textarea>
		&nbsp;
	    <a href="#" onclick="javascript:openQx();">
			<img src="${request.contextPath}/static/images/search_results.gif" border="0">
		</a> 
	    <span class="k-invalid-msg" data-for="syncColumns"></span>
		<div style="margin-top:5px;">
			<span style="  margin-left:110px;">
		    &nbsp;（提示：同步列是要原样复制到目标表的列.）
		    </span>
	    </div>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="dateDimensionColumn" class="required">日期维度列&nbsp;</label>
        <input id="dateDimensionColumn" name="dateDimensionColumn" rows="12" cols="68" class="easyui-validatebox x-text"
			   style="width:380px;" value="${rowDenormaliser.dateDimensionColumn}"> 
		<div style="margin-top:5px;">
			<span style="  margin-left:110px;">
		    &nbsp;（提示：可以对某个日期维度列进行拆分.）
		    </span>
	    </div>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="incrementColumn" class="required">增量列&nbsp;</label>
        <input id="incrementColumn" name="incrementColumn" rows="12" cols="68" class="easyui-validatebox x-text"
			   style="width:380px;" value="${rowDenormaliser.incrementColumn}"> 
		<div style="margin-top:5px;">
			<span style="  margin-left:110px;">
		    &nbsp;（提示：可以是数值或日期型字段做增量.）
		    </span>
	    </div>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="delimiter" class="required">分隔符&nbsp;</label>
        <input id="delimiter" name="delimiter" rows="12" cols="68" class="easyui-validatebox x-text"
			   style="width:380px;" value="${rowDenormaliser.delimiter}"> 
		<div style="margin-top:5px;">
			<span style="  margin-left:110px;">
		     &nbsp;(默认分隔符为半角的逗号",")	
		    </span>
	    </div>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="sqlCriteria" class="required">转换SQL条件&nbsp;</label>
        <textarea id="sqlCriteria" name="sqlCriteria" rows="12" cols="68" class="easyui-validatebox x-text"
				  style="width:380px;height:80px;" >${rowDenormaliser.sqlCriteria}</textarea>&nbsp;
	    <span class="k-invalid-msg" data-for="sqlCriteria"></span>
		<div style="margin-top:5px;">
			<span style="margin-left:110px;">
			 （提示：添加and条件即可，动态参数也是支持的 column1 = <%="#"%>{param1}。）
			</span>
			<br>
			<span style="margin-left:110px;">
			 （today_start内置变量代表当天的开始时间, today_end内置变量代表当天的结束时间。）
			</span>
			<br>
			<span style="margin-left:110px;">
			 （yesterday_start内置变量代表昨天的开始时间, yesterday_end内置变量代表昨天的结束时间。）
			</span>
			<br>
			<span style="margin-left:110px;">
			 （today_curr_hour_start内置变量代表当前小时的开始时间, today_curr_hour_end内置变量代表当前小时的结束时间。）
			</span>
			<br>
			<span style="margin-left:110px;">
			 （today_previous_hour_start内置变量代表前一小时的开始时间, today_previous_hour_end内置变量代表前一小时的结束时间。）
			</span>
	    </div>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="targetTableName" class="required">目标表名&nbsp;</label>
        <input id="targetTableName" name="targetTableName" rows="12" cols="68" class="easyui-validatebox x-text"
			   style="width:380px;" value="${rowDenormaliser.targetTableName}"> 
		<div style="margin-top:5px;">
			<span style="color:red; margin-left:110px;">
			（提示：为了保证系统安全，目标表只能以useradd_、etl_、sync_、tmp_开头。）
			</span>
	   </div>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="targetColumn" class="required">目标列&nbsp;</label>
        <input id="targetColumn" name="targetColumn" rows="12" cols="68" class="easyui-validatebox x-text"
			   style="width:380px;" value="${rowDenormaliser.targetColumn}"> 
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="targetColumnType" class="required">目标列类型&nbsp;</label>
        <select id="targetColumnType" name="targetColumnType">
		    <option value="">----请选择----</option>
			<option value="Date">日期时间</option>
			<option value="Integer">整数型</option>
			<option value="Long">长整数型</option>
			<option value="Double">数值型</option>
			<option value="String">字符串型</option>
		</select>
		<script type="text/javascript">
			document.getElementById("targetColumnType").value="${rowDenormaliser.targetColumnType}";
		</script> 
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
            document.getElementById("sort").value="${rowDenormaliser.sort}";
		</script>
        <span class="k-invalid-msg" data-for="sort"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="scheduleFlag" class="required">是否调度执行&nbsp;</label>
	    <select id="scheduleFlag" name="scheduleFlag">
		    <option value="">----请选择----</option>
		    <option value="Y">是</option>
		    <option value="N">否</option>
	    </select>
	    <script type="text/javascript">
            document.getElementById("scheduleFlag").value="${rowDenormaliser.scheduleFlag}";
	    </script>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="deleteFetch" class="required">同步前删除&nbsp;</label>
	    <select id="deleteFetch" name="deleteFetch">
		    <option value="">----请选择----</option>
		    <option value="Y">是</option>
		    <option value="N">否</option>
	    </select>
	    <script type="text/javascript">
            document.getElementById("deleteFetch").value="${rowDenormaliser.deleteFetch}";
	    </script>
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
            document.getElementById("locked").value="${rowDenormaliser.locked}";
	    </script>
    </td>
  </tr>

  <tr>
    <td colspan="2" align="center" valign="bottom" height="30">&nbsp;
     <div>
       <button type="button" id="iconButton" class="btn btnGray" style="width: 90px" onclick="javascript:save();">保存</button>
	   <button type="button" id="iconButton" class="btn btnGray" style="width: 90px" onclick="javascript:saveAs();">另存</button>
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