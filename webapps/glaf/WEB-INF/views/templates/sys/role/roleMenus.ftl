<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>应用模块</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	 var setting = {
			async: {
				enable: true,
				url: "${contextPath}/sys/role/roleMenusJson?roleId=${role.id}",
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

	function zTreeOnClick(event, treeId, treeNode, clickFlag) {
		//jQuery("#nodeId").val(treeNode.id);
		//loadData('${contextPath}/sys/application/json&parentId='+treeNode.id);
	}


    jQuery(document).ready(function(){
			jQuery.fn.zTree.init(jQuery("#myTree"), setting);
	});

	function saveRoleSiteMenus(){
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
				   url: '${contextPath}/sys/role/saveRoleMenus?roleId=${role.id}',
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
					   jQuery('#mydatagrid').datagrid('reload');
				   }
			 });
	}

	function exportSiteMenus(){
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
        document.iForm.action="${contextPath}/sys/application/exportMenus";
		document.iForm.submit();
	}

</script>
</head>
<body style="margin:2px;"> 
<form id="iForm" name="iForm" method="post">
<input type="hidden" id="nodeIds" name="nodeIds">
<div class="toolbar-backgroud" style="height:40px"> 
<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">设置角色【${role.name}】的菜单</span>
&nbsp;
<input type="button" name="save" value="保存" class="btnGray" onclick="javascript:saveRoleSiteMenus();"> 
&nbsp;
 </div> 
<div style="margin-top:0px;">
	<ul id="myTree" class="ztree"></ul> 
</div>
</form>
</body>
</html>