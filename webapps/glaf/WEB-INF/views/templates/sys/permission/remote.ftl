<!DOCTYPE html>
<html>
<head>
<title>远程访问设置</title>
<#include "/inc/init_easyui_import.ftl"/>
<style type="text/css">
.datagrid-header td,th {
	border-right: 1px dotted #ccc;
	font-size: 12px;
	font-weight: normal;
	background: #fafafa url('${contextPath}/static/images/datagrid_header_bg.gif')repeat-x left bottom;
	border-bottom: 1px dotted #ccc;
	border-top: 1px dotted #ccc;
	height: 35px;
	}
.datagrid-header td:first-child,th:first-child {
		border-left: 1px dotted #ccc;
	}
.datagrid-tbody td {
	border-right: 1px solid #ccc;
	font-size: 12px;
	font-weight: normal;
	border-bottom: 1px solid #ccc;
	border-top: 1px solid #fff;
	height: 35px;
}
.datagrid-tbody td:first-child {
	border-left: 1px solid #ccc;
}
.datagrid-tbody tr {
	text-align: center;
}
.datagrid-tbody tr td input,.datagrid-tbody tr th input{
	width:98%;height:100%;border:none;
}
.datagrid-title td {
	font-size: 12px;
	font-weight: bolder;
	background: #E0ECFF repeat-x left bottom;
	height: 28px;
	vertical-align: middle;
}
.xz_input {
    background-color: #fff;
	border: 1px solid #3399ff;
	color: #666;
	padding: 2px 2px;
	line-height: 20px;
	height: 18px;
	font-size: 13px;
	text-align: left;
}

.xz_input:hover {
	font-weight: bold;
	box-shadow: 1px 1px 1px 1px #aaa;
	background-color: #ffff99;
	font-size: 13px;
	-moz-box-shadow: 0 1px 1px #aaa;
	-webkit-box-shadow: 0 1px 1px #aaa;
}

.add {
    padding: 3px 16px;
	cursor: pointer;
	background-color: rgb(244, 244, 244);
	border: 1px solid rgb(170, 170, 170);
	text-decoration: none;
	font-family: "微软雅黑";
	font-size: 12px;
}

.delete {
    padding: 3px 16px;
	cursor: pointer;
	background-color: rgb(244, 244, 244);
	border: 1px solid rgb(170, 170, 170);
	text-decoration: none;
	font-family: "微软雅黑";
	font-size: 12px;
}
</style>
<script type="text/javascript">

	function saveForm(){
	    var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/remotePermission/save',
				   data: params,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
						if(data.message != null){
						   alert(data.message);
					    } else {
						   alert('操作成功完成！');
					    }
					    if(data.statusCode == 200) { 
					         //parent.location.reload(); 
					    }
				   }
			 });
	}

	function switchType(){
		var type = document.getElementById("type").value;
        location.href="${contextPath}/sys/remotePermission/edit?type="+type;
	}

</script>
</head>
<body style="padding-left:20px;padding-right:20px">
 <br />
 <div class="x_content_title">
     <img src="${contextPath}/static/images/window.png" alt="远程访问设置">&nbsp;远程访问设置
 </div>
<form id="iForm" name="iForm" method="post" action="${contextPath}/sys/remotePermission/save" 
      onsubmit="return verifyAll(this);">
 <input type="hidden" id="items" name="items">
 <input type="hidden" id="jsonData" name="jsonData">
 <table>
	 <tr>
		<td width="25%" height="30">类型</td>
		<td>
		   <select id="type" name="type" onchange="javascript:switchType();">
			<option value="" selected>----请选择----</option>
			<#list dicts as dict>
			<option value="${dict.value}">${dict.name}</option>
			</#list>
		   </select>
		   <script type="text/javascript">
		      document.getElementById("type").value="${type}";
		   </script>  
		</td>
	 </tr>
	 <tr>
		<td width="25%" height="60">IP地址设置&nbsp;</td>
		<td height="50" >
			<table id="param-table" style="width: 400px; margin: 0px; padding: 0px; border-spacing: 0px;">
			     <thead class="datagrid-header">
				 <tr>
				   <th style="width:100px;">允许的IP地址</th>
				   <th style="width:60px;">
				   		<button type="button" class="topAdd add">新增</button>
				   	</th>
				 </tr>
				 </thead>
				 <tbody class="datagrid-tbody">
				  
				 </tbody>
			</table>
		</td>
	</tr> 
	<tr>
		<td width="25%" height="60">&nbsp;</td>
		<td height="50" align="left">
		 &nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" name="submit" value=" 保 存 " class="btn btnGray" />
		</td>
	</table>
</form>

<script type="text/html" id="tr-tmpl">
  <tr style="height:38px;">
	<td><input type="text" class="xz_input" field="remoteIP" /></td>
    <td><button type="button" class="add">新增</button>&nbsp;<button type="button" class="delete">删除</button></td>
  </tr>
</script>
<script type="text/javascript">
 $(function() {
	  $(document).on("click.table.add", "#param-table button.add", function(){
				 ParamTable.append();
			 }).on("click.table.delete", "#param-table button.delete", function(e){
				 ParamTable.remove(this);
			 });
			 
		});

  var ParamTable = (function(){
		var trTmpl = $("#tr-tmpl").html();
		
		var $tbody = $("#param-table tbody");
		
		var functions = {
			append : function(data){
				var $tr = $(trTmpl);
				
				$tbody.append($tr);
				
				functions.val($tr, data);
			},
			val : function($tr, data){
				$tr.find("input").each(function(){
					$(this).val(data[$(this).attr("field")]);
				});
			},
			remove : function(dom){
				$(dom).closest("tr").remove();
			},
			getDataList : function(){
				var dataList = [];
				var $trs = $tbody.find("tr");
				if(!$trs.get(0)){
					return dataList;
				}
				$tbody.find("tr").each(function(){
					var data = {};
					$(this).find("input").each(function(){
						data[$(this).attr("field")] = $(this).val();
					});
					dataList.push(data);
				});
				return dataList;
			}
		};
	   <#if perms?exists>
		 <#list perms as p>
			functions.append(${p.json});
		 </#list>
	   </#if>
		
		return functions;
	})();
	
	var verifyAll_ = window.verifyAll;
	
	window.verifyAll = function(form){
		var dataList = ParamTable.getDataList();
		var data = {};
		var items = "";
		if(dataList && dataList.length){
			$.each(dataList, function(i, v){
				data[v.remoteIP] = v;
				items = items+v.remoteIP+",";
			});
		}
		document.getElementById("items").value = items;
		document.getElementById("jsonData").value = JSON.stringify(data);
		return verifyAll_(form);
	};
	
</script>
<br/>
<br/>
</body>
</html>