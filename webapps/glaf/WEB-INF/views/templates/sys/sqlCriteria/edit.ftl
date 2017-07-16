<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SQL条件配置</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
 
  function addElement() {
        var list = document.iForm.noselected;
        for (i = 0; i < list.length; i++) {
            if (list.options[i].selected) {
                var value = list.options[i].value;
                var text = list.options[i].text;
                addToList(value, text);
				list.remove(i);
				i=i-1;
            }
        }
    }


  function addToList(value, text) {
        var list = document.iForm.selected;
        if (list.length > 0) {
            for (k = 0; k < list.length; k++) {
                if (list.options[k].value == value) {
                    return;
                }
            }
        }

        var len = list.options.length;
        list.length = len + 1;
        list.options[len].value = value;
        list.options[len].text = text;
    }


  function removeElement() {
        var list = document.iForm.selected;
		var slist = document.iForm.noselected;
        if (list.length == 0 || list.selectedIndex < 0 || list.selectedIndex >= list.options.length)
            return;

        for (i = 0; i < list.length; i++) {
            if (list.options[i].selected) {
			    var value = list.options[i].value;
                var text = list.options[i].text;
                list.options[i] = null;
                i--;
				var len = slist.options.length;
				slist.length = len+1;
                slist.options[len].value = value;
                slist.options[len].text = text;				
            }
        }
    }
 

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/sqlCriteria/saveSqlCriteria',
				   data: params,
				   dataType:  'json',
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
					   window.close();
				   }
			 });
	}

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/sqlCriteria/saveSqlCriteria',
				   data: params,
				   dataType:  'json',
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
					       window.close();
					   }  
				   }
			 });
	}

	function displaySeparator(collectionFlag){
       //var collectionFlag = document.iForm.collectionFlag.value;
	   //alert(collectionFlag);
	   if(collectionFlag == "Y"){
          jQuery("#separatorDiv").show();
	   } else {
          jQuery("#separatorDiv").hide();
	   }
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:40px"> 
    <div class="toolbar-backgroud"> 
		<span class="x_content_title">编辑记录</span>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
		   onclick="javascript:saveData();" >保存</a>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${sqlCriteria.id}"/>
  <input type="hidden" id="moduleId" name="moduleId" value="${moduleId}" > 
  <input type="hidden" id="businessKey" name="businessKey" value="${businessKey}">

  <table class="easyui-form" style="width:600px;" align="left">
  <tbody>
    <tr>
        <td width="20%" class="input-box">上级节点</td>
        <td>
		  <select id="parentId" name="parentId">
		   <option value="0">/根节点</option>
           <#list  sqlCriterias as t>
			<#if t.locked == 0>
           <option value="${t.id}">${t.blank}${t.name}</option>
			</#if>
		  </#list>
          </select>
		  <script language="javascript">
		    document.all.parentId.value="${parentId}";	
	      </script>
		</td>
    </tr>
	<tr>
		<td width="20%" align="left">标题</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox  x-text" style="width:375px;"
				   value="${sqlCriteria.name}" size="50"/>
		</td>
	</tr>

	<#if moduleId == "sys_sql">
	<#else>
	<tr>
		<td width="20%" align="left">表名称</td>
		<td align="left">
           <#if table?exists> 
		      ${table.title} [${table.tableName}]
			  <input type="hidden" id="tableName" name="tableName" value="${table.tableName}">
		   <#else>
              <select id="tableName" name="tableName">
			   <#list  tables as t>
				<#if t.locked == 0>
			     <option value="${t.tableName}">${table.title} [${t.tableName}]</option>
				</#if>
			  </#list>
			  </select>
			  <script language="javascript">
				document.all.tableName.value="${tableName}";	
			  </script>
		   </#if>
		<br>
		</td>
	</tr>
	</#if>
	<!-- <tr>
		<td width="20%" align="left">表别名</td>
		<td align="left">
            <input id="tableAlias" name="tableAlias" type="text" 
			       class="easyui-validatebox  x-text"  
				   value="${sqlCriteria.tableAlias}" size="10"/>
		   <br>
		</td>
	</tr> -->

	<tr>
		<td width="20%" align="left">连接条件</td>
		<td align="left">
            <select id="condition" name="condition">
				<option value="">----请选择----</option>
				<option value="AND">AND</option>
				<option value="OR">OR</option>
            </select>
			<script language="javascript">
		      document.all.condition.value="${sqlCriteria.condition}";	
	        </script>
		   <br>
		</td>
	</tr>

	<#if moduleId == "sys_sql">
	<tr>
		<td width="20%" align="left">表别名</td>
		<td align="left">
            <input id="tableAlias" name="tableAlias" type="text" 
			       class="easyui-validatebox  x-text" style="width:95px;" 
				   value="${sqlCriteria.tableAlias}" size="10"/>
		   <br>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">列名称</td>
		<td align="left">
		    <input id="columnName" name="columnName" type="text" 
			       class="easyui-validatebox  x-text" style="width:375px;"
				   value="${sqlCriteria.columnName}"/>
		</td>
	</tr>
	<#else>
	<tr>
		<td width="20%" align="left">列名称</td>
		<td align="left">
		    <select id="columnName" name="columnName">
			  <#list  columns as c>
			     <option value="${c.columnName}">${c.title} [${c.columnName}]</option>
			  </#list>
			  </select>
			  <script language="javascript">
				document.all.columnName.value="${sqlCriteria.columnName}";	
			  </script>
		   <br>
		</td>
	</tr>
	</#if>
    <tr>
		<td width="20%" align="left">列类型</td>
		<td align="left">
            <select id="columnType" name="columnType">
				<option value="">----请选择----</option>
				<option value="Date">日期型</option>
				<option value="Integer">整数型</option>
				<option value="Long">长整数型</option>
				<option value="Double">数值型</option>
				<option value="String">文本型</option>
            </select>
			<script language="javascript">
		      document.all.columnType.value="${sqlCriteria.columnType}";	
	        </script>
		</td>
	</tr>

	<tr>
		<td width="20%" align="left">运算符</td>
		<td align="left">
            <select id="operator" name="operator">
				<option value="">----请选择----</option>
				<option value="=">等于=</option>
				<option value="!=">不等于!=</option>
				<option value=">">大于></option>
				<option value=">=">大于等于>=</option>
				<option value="<">小于&lt;</option>
				<option value="<=">小于等于&lt;=</option>
				<option value="LIKE">LIKE</option>
				<option value="NOT LIKE">NOT LIKE</option>
            </select>
			<script language="javascript">
		        document.all.operator.value="${sqlCriteria.operator}";	
	        </script>
		   <br>
		</td>
	</tr>

    <tr>
		<td width="20%" align="left">参数标题</td>
		<td align="left">
            <input id="paramTitle" name="paramTitle" type="text" 
			       class="easyui-validatebox  x-text" style="width:375px;" 
				   value="${sqlCriteria.paramTitle}" size="50"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">参数名称</td>
		<td align="left">
            <input id="paramName" name="paramName" type="text" 
			       class="easyui-validatebox  x-text" style="width:375px;" 
				   value="${sqlCriteria.paramName}" size="50"/>
		   <br>
		</td>
	</tr>

	<tr id="collectionFlagDiv">
		<td width="20%" align="left">是否集合参数</td>
		<td align="left">
		  <input type="radio" name="collectionFlag" value="Y" onclick="displaySeparator('Y');"
		         <#if  sqlCriteria.collectionFlag == 'Y'>checked</#if>>是&nbsp;&nbsp;
	      <input type="radio" name="collectionFlag" value="N" onclick="displaySeparator('N');"
		         <#if  sqlCriteria.collectionFlag == 'N'>checked</#if>>否
		</td>
	</tr>

	<tr id="separatorDiv" style="display:none;">
		<td width="20%" align="left">参数分隔符</td>
		<td align="left">
            <input id="separator" name="separator" type="text" 
			       class="easyui-validatebox  x-text"  style="width:95px;" 
				   value="${sqlCriteria.separator}" size="50"/>
		   <br>
		</td>
	</tr>

  <tr>
    <td width="20%" align="left">SQL条件</td>
    <td align="left">
        <textarea id="sql" name="sql" rows="12" cols="68" class="x-text"
				  style="width:375px;height:90px;" >${sqlCriteria.sql}</textarea>&nbsp;
	    <span class="k-invalid-msg" data-for="sql"></span>
		<br>&nbsp;（提示：如果列条件不能满足要求，添加and条件即可）
    </td>
  </tr>

	<tr>
		<td width="20%" align="left">是否启用</td>
		<td align="left">
		  <input type="radio" name="locked" value="0" <#if sqlCriteria.locked == 0 >checked</#if>>启用&nbsp;&nbsp;
	      <input type="radio" name="locked" value="1" <#if sqlCriteria.locked == 1 >checked</#if>>禁用
		</td>
	</tr>

	<tr>
	 <td colspan="2"><br><br><br><br></td>
	</tr>

    </tbody>
  </table>
 </form>
</div>
</div>

</body>
</html>