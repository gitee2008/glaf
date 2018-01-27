<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据集列表</title>
<#include "/inc/init_easyui_import.ftl"/> 
<script type="text/javascript">

    function searchDataSets(){
		document.iForm.method="POST";
		document.iForm.submit();
	}

	function checkDataSets(){
        var obj = document.getElementsByName("id");
		var ids = "";
		var names = "";
		for ( var i = 0; i < obj.length; i++) {
			var e = obj.item(i);
			if (e.checked) {
				ids = ids + e.getAttribute("value") + ",";
				names = names  + e.getAttribute("text") + ",";
			}
		}
		//alert(ids);
		//alert(names);
		var closeWin = true;
		var parent_window = getOpener();
		if(parent_window == null){
			parent_window = window;
			closeWin = false;
			var index = parent.layer.getFrameIndex(window.name);
            var x_id = parent.document.getElementById("${elementId}");
			var x_name = parent.document.getElementById("${elementName}");
			x_id.value = ids;
			x_name.value = names;
			parent.layer.close(index);
		} else {
			//alert(parent_window);
			var x_id = parent_window.document.getElementById("${elementId}");
			var x_name = parent_window.document.getElementById("${elementName}");
			x_id.value = ids;
			x_name.value = names;
			if(closeWin){
			  window.close();
			}
		}
	}
  
</script>
</head>
<body leftmargin="0" topmargin="0">  
<center>
<br>
<div class="x_content_title"><img
	src="${contextPath}/static/images/window.png"
	alt="数据集列表">&nbsp;数据集列表</div>
<form id="iForm" name="iForm" method="post" action="${contextPath}/chart/chartDatasetList?elementId=${elementId}&elementName=${elementName}&chartId=${chart.id}&xtype=${xtype}">
<table width="95%" height="30" border="0" cellspacing="1" cellpadding="0" class="mainTable">
  <tr>
    <td width="100%" align="right">
		<select id="nodeId" name="nodeId">
			<option value="">----所有分类----</option>
			<#list  treeModels as treeModel>
			   <option value="${treeModel.id}">${treeModel.name}</option>
				  <#list  treeModel.children as child>
				  <option value="${child.id}">&nbsp;&nbsp;---->${child.name}</option>
					  <#list child.children as c>
					  <option value="${c.id}">&nbsp;&nbsp;&nbsp;&nbsp;-------->${c.name}</option>
					  </#list>
				  </#list>
			</#list>
		</select>
		<script type="text/javascript">
			document.getElementById("nodeId").value="${nodeId}";
		</script>
        &nbsp;
		<input type="text" id="keywordsLike" name="keywordsLike" value="${keywordsLike}" class=" x-searchtext">&nbsp;
		<input type="button" value="查找" class="btnGray" onclick="javascript:searchDataSets();">
		&nbsp;
	    <input type="button" value="确定" class="btnGray" onclick="javascript:checkDataSets();">
	</td>
  </tr>
</table>
</form>
 
<br>
<br>
<br>
<br>
</center>
</body>  
</html>