<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>用户权限设置</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	 var setting = {
			async: {
				enable: true,
				url: getTreeUrl,
				autoParam:["id", "name=n", "level=lv"],
				dataFilter: filter
			},
			check: {
				enable: true
			},
			callback: {
				onClick: zTreeOnClick
			}
		};
  
  	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
            //if(childNodes[i].iconCls=='icon-user'){
			   // childNodes[i].icon="${contextPath}/static/images/user.gif";
		    //}
		}
		return childNodes;
	}

	function getTreeUrl(){
		return "${contextPath}/sys/district/treeJson3?parentId=${parentId}&userId=${user.userId}&type=${type}";
	}

	function zTreeOnClick(event, treeId, treeNode, clickFlag) {
		//jQuery("#nodeId").val(treeNode.id);
		//loadData('${contextPath}/sys/district/json?parentId='+treeNode.id);
	}


    jQuery(document).ready(function(){
			jQuery.fn.zTree.init(jQuery("#myTree"), setting);
	});

	function saveData(){
		var zTree = $.fn.zTree.getZTreeObj("myTree");
        var selectedNodes  = zTree.getCheckedNodes(true);

        var sx = '';  
		var id='';
        for(var i=0; i<selectedNodes.length; i++){  
            if (sx != ''){ 
				sx += ','; 
			}
			id = selectedNodes[i].id;
            sx += id;  
        }  
        $("#nodeIds").val(sx);
		//alert(sx);
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/treePermission/saveTreePermission?userId=${user.userId}&type=${type}&privilege=${privilege}',
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
					   jQuery('#mydatagrid').datagrid('reload');
				   }
			 });
	}


</script>
</head>

<body style="margin:2px;"> 
<form id="iForm" name="iForm" method="post">
<input type="hidden" id="nodeIds" name="nodeIds">
<input type="hidden" id="privilege" name="privilege" value="${privilege}">
<div class="toolbar-backgroud" style="height:42px"> 
    <img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">设置用户【${user.name}】的节点权限</span>
    &nbsp;&nbsp;
    <input type="button" name="save" value="保存" class="btnGray" onclick="javascript:saveData();"> 
 </div> 
<div style="margin-top:0px;">
	<ul id="myTree" class="ztree"></ul> 
</div>
</form>
</body>
</html>