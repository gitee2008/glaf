<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构列表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">
 
	var setting = {
		check: {
				enable: true,
				chkStyle: "radio",
				radioType: "level"
			},
		async: {
				enable: true,
				url:"${contextPath}/sys/organization/treeJson",
				dataFilter: filter
			},
			callback: {
				beforeClick: zTreeBeforeClick,
				onClick: zTreeOnClick,
				onCheck: onCheck
			}
	};

	function onCheck(e, treeId, treeNode) {
		var id = treeNode.id;
		var name = treeNode.name;
		//alert(id+"    "+name);	
		var parent_window = getOpener();
		if(parent_window == null){
			parent_window = window;
			closeWin = false;
            var x_id = parent.document.getElementById("${elementId}");
			var x_name = parent.document.getElementById("${elementName}");
			x_id.value = id;
			x_name.value = name;
		} else {
			//alert(parent_window);
			var x_id = parent_window.document.getElementById("${elementId}");
			var x_name = parent_window.document.getElementById("${elementName}");
			x_id.value = id;
			x_name.value = name;
			if(closeWin){
			    window.close();
			}
		}
	}		

	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			childNodes[i].icon="${contextPath}/static/images/basic.gif";
		}
		return childNodes;
	}

	function zTreeOnExpand(treeId, treeNode){
        treeNode.icon="${contextPath}/static/scripts/ztree/css/zTreeStyle/img/diy/2.png";
	}

	function updateNode(treeId, treeNode){
		var zTree = jQuery.fn.zTree.getZTreeObj(treeId);
		zTree.setting.view.fontCss["color"] = "#0000ff";
		zTree.updateNode(treeNode);
	}

	function zTreeBeforeClick(treeId, treeNode, clickFlag) {
           
	}

	function zTreeOnClick(event, treeId, treeNode, clickFlag) {
	    var closeWin = true;
		var id = treeNode.id;
		var name = treeNode.name;
		//alert(id+"    "+name);
		var parent_window = getOpener();
		if(parent_window == null){
			parent_window = window;
			closeWin = false;
            var x_id = parent.document.getElementById("${elementId}");
			var x_name = parent.document.getElementById("${elementName}");
			x_id.value = id;
			x_name.value = name;
		} else {
			//alert(parent_window);
			var x_id = parent_window.document.getElementById("${elementId}");
			var x_name = parent_window.document.getElementById("${elementName}");
			x_id.value = id;
			x_name.value = name;
			if(closeWin){
			    window.close();
			}
		}
	}


	$(document).ready(function(){
		$.fn.zTree.init($("#myTree"), setting);
	});

	
</script>
</head>
<body leftmargin="0" topmargin="0">  
<div>
    <ul id="myTree" class="ztree"></ul>  
</div>  
</body>  
</html>