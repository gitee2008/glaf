<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构用户权限列表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
 
	var setting = {
		async: {
				enable: true,
				url:"${contextPath}/sys/organization/treeJson",
				dataFilter: filter
			},
			callback: {
				beforeClick: zTreeBeforeClick,
				onClick: zTreeOnClick
			}
	};

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
		jQuery("#nodeId").val(treeNode.id);
		window.parent.mainFrame.location.href ="${contextPath}/branch/user/permission?parentId="+treeNode.id;
	}


	$(document).ready(function(){
		$.fn.zTree.init($("#myTree"), setting);
	});

	
</script>
</head>
<body leftmargin="0" topmargin="0">  
<input type="hidden" id="nodeId" name="nodeId" value="" >
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
    <div data-options="region:'west',split:true" style="width:220px;">
	  <div class="easyui-layout" data-options="fit:true">        
			 <div data-options="region:'center',border:false">
			    <ul id="myTree" class="ztree"></ul>  
			 </div> 
        </div>  
	</div> 	
</div>  
</body>  
</html>