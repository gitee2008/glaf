<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑字段转换信息</title>
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

    function saveData(){
        var form = document.getElementById("iForm");
	    if(form.columnName == form.targetColumnName){
		    alert("转换列和目标列不能是同一列。");
		    return;
	    }
	    var link = "${request.contextPath}/sys/columnTransform/saveColumnTransform";
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
        var form = document.getElementById("iForm");
	    if(form.columnName == form.targetColumnName){
		   alert("转换列和目标列不能是同一列。");
		   return;
	    }
	    document.getElementById("id").value="";
	    var link = "${request.contextPath}/sys/columnTransform/saveColumnTransform";
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
            	  window.parent.location.reload();
               }
			 });
    }
 </script>
</head>
<body style="margin-top:0px;">
<div id="main_content" class="k-content ">
<br>
<div class="x_content_title"><img
	src="${request.contextPath}/static/images/window.png" alt="编辑字段转换信息">&nbsp;
编辑字段转换信息</div>
<br>
<form id="iForm" name="iForm" method="post" data-role="validator" novalidate="novalidate">
<input type="hidden" id="id" name="id" value="${columnTransform.id}"/>
<input type="hidden" id="transformId" name="transformId" value="${transformId}"/>
<input type="hidden" id="tableName" name="tableName" value="${tableName}"/>
<table width="98%" align="center" border="0" cellspacing="0" cellpadding="5">

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="title" class="required">标题&nbsp;</label>
        <input id="title" name="title" type="text" class="easyui-validatebox x-text"  
               data-bind="value: title" maxlength="200" style="width:320px;"
               value="${columnTransform.title}" validationMessage="请输入标题"/>
	    <span class="k-invalid-msg" data-for="title"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="columnName" class="required">转换列&nbsp;</label>
        <select id="columnName" name="columnName" >
			<option value="">----请选择----</option>
			<option value="">----新列----</option> 
			<#list columns as item>
			<option value="${item.columnName}">${item.columnName}</option>     
			</#list>
		</select>
		<script type="text/javascript">
			document.getElementById("columnName").value="${columnTransform.columnName}";
		</script>
	    <span class="k-invalid-msg" data-for="columnName"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="targetColumnName" class="required">目标列&nbsp;</label>
        <select id="targetColumnName" name="targetColumnName" >
			<option value="">----请选择----</option>
			<option value="">----新列----</option> 
			<#list columns as item>
			<option value="${item.columnName}">${item.columnName}</option>     
			</#list>
		</select>
		<script type="text/javascript">
			document.getElementById("targetColumnName").value="${columnTransform.targetColumnName}";
		</script>
	    <span class="k-invalid-msg" data-for="targetColumnName"></span>
		<div style="margin-top:5px;">
			<span style="color:red; margin-left:110px;">
			 （提示：可以选择已经存在的列做目标列，也可以选择新列做目标列。）
			</span>
	    </div>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="targetType" class="required">转换后新列类型&nbsp;</label>
        <select id="targetType" name="targetType">
		    <option value="">----请选择----</option>
			<option value="Date">日期时间</option>
			<option value="Integer">整数型</option>
			<option value="Long">长整数型</option>
			<option value="Double">数值型</option>
			<option value="String">字符串型</option>
		</select>
		<script type="text/javascript">
			document.getElementById("targetType").value="${columnTransform.targetType}";
		</script>
	    <span class="k-invalid-msg" data-for="targetType"></span>
    </td>
  </tr>

    <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="targetColumnPrecision" class="required">新列小数位数&nbsp;</label>
        <select id="targetColumnPrecision" name="targetColumnPrecision">
		    <option value="0">----请选择----</option>
			<option value="1">1位小数</option>
			<option value="2">2位小数</option>
			<option value="3">3位小数</option>
			<option value="4">4位小数</option>
			<option value="5">5位小数</option>
			<option value="6">6位小数</option>
			<option value="7">7位小数</option>
			<option value="8">8位小数</option>
			<option value="9">9位小数</option>
			<option value="-1">不限制</option>
		</select>
		<script type="text/javascript">
			document.getElementById("targetColumnPrecision").value="${columnTransform.targetColumnPrecision}";
		</script>
		（提示：如果目标列数据类型是数值类型才需要设置）
	    <span class="k-invalid-msg" data-for="targetColumnPrecision"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="type" class="required">转换规则&nbsp;</label>
        <select id="type" name="type">
		    <option value="">----请选择----</option>
			<option value="copy">原样复制</option> 
			<option value="expr">表达式</option>
			<option value="countSplitByComma">逗号分隔取个数</option>
			<option value="removeHtml">去除HTML标签</option>
			<option value="removeHtml2"><br>转换行后去除HTML标签</option>
			<option value="date_yyyyMMddHHmmss">日期转yyyyMMddHHmmss（如20180501120830）</option>
			<option value="date_yyyyMMdd">日期转yyyyMMdd（如20180501）</option>
			<option value="date_yyyyMM">日期转yyyyMM（如201805）</option>
			<option value="date_yyyy">日期转yyyy（如2018）</option>
			<option value="date_MM">日期转MM（如05）</option>
			<option value="date_dd">日期转dd（如01）</option>
			<option value="date_yyyyquarter">日期转yyyy季度（如2018Q2）</option>
			<option value="date_quarterQ">日期转季度（如Q2）</option>
			<option value="date_quarter">日期转季度（如2）</option>
		</select>
		<script type="text/javascript">
			document.getElementById("type").value="${columnTransform.type}";
		</script> 
	    <span class="k-invalid-msg" data-for="type"></span>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="transformIfTargetColumnNotEmpty" class="required">目标列有值是否转换</label>
	    <select id="transformIfTargetColumnNotEmpty" name="transformIfTargetColumnNotEmpty">
		    <option value="Y">是</option>
		    <option value="N">否</option>
	    </select>（提示：如果目标列有值，可以设置不转换）
	    <script type="text/javascript">
           document.getElementById("transformIfTargetColumnNotEmpty").value="${columnTransform.transformIfTargetColumnNotEmpty}";
	    </script>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="condition" class="required">转换条件&nbsp;</label>
        <!-- <input id="condition" name="condition" type="text" class="easyui-validatebox x-text"  
               data-bind="value: condition" maxlength="500" style="width:320px;"
               value="${columnTransform.condition}" validationMessage="请输入Java EL转换条件"/> -->
		<textarea id="condition" name="condition" rows="5" cols="48" class="easyui-validatebox x-text"
				  style="width:320px;height:90px;" >${columnTransform.condition}</textarea>
	    <span class="k-invalid-msg" data-for="condition"></span>
		<div style="margin-top:5px;">
			<span style="color:red; margin-left:110px;">
			 （提示：如果转换条件不为空，满足转换条件的记录才做转换，Java EL条件中使用的字段请转成小写。）
			</span>
	   </div>
    </td>
  </tr>

  <tr>
    <td width="2%" align="left">&nbsp;</td>
    <td align="left">
	    <label for="expression" class="required">转换表达式&nbsp;</label>
        <!-- <input id="expression" name="expression" type="text" class="easyui-validatebox x-text"  
               data-bind="value: expression" maxlength="500" style="width:320px;"
               value="${columnTransform.expression}" validationMessage="请输入转换表达式"/> -->
		<textarea id="expression" name="expression" rows="5" cols="48" class="easyui-validatebox x-text"
				  style="width:320px;height:90px;" >${columnTransform.expression}</textarea>
	    <span class="k-invalid-msg" data-for="expression"></span>
		<div style="margin-top:5px;">
			<span style="color:red; margin-left:110px;">
			 （提示：如果转换规则不满足，可以使用表达式，表达式中使用的字段请转成小写。）
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
            document.getElementById("sort").value="${columnTransform.sort}";
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
            document.getElementById("locked").value="${columnTransform.locked}";
	    </script>
    </td>
  </tr>

  <tr>
    <td colspan="2" align="center" valign="bottom" height="30">&nbsp;
     <div>
       <button type="button" id="iconButton"  class="btnGray" style="width: 90px" onclick="javascript:saveData();">保存</button>
	   <button type="button" id="iconButton"  class="btnGray" style="width: 90px" onclick="javascript:saveAsData();">另存</button>
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