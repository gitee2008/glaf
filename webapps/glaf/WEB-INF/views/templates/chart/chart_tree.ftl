<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图表列表</title>
<#include "/inc/init_easyui_import.ftl"/> 
<script type="text/javascript">

    var setting = {
		async: {
			enable: true,
			url: getUrl
		},
		check: {
			    enable: true
            <#if chooseType == 'link'>
				,chkStyle: "radio"
			    //,radioType = "all"
			</#if>
		}
	};


	function getUrl(treeId, treeNode) {
		if(treeNode != null){
		    var param = "&nodeId="+treeNode.id;
		    return "${contextPath}/rs/chart/treeJson?selected=${selected}"+param;
		}
		return "${contextPath}/rs/chart/treeJson?nodeCode=${nodeCode}&selected=${selected}";
	}

    jQuery(document).ready(function(){
		  jQuery.fn.zTree.init(jQuery("#myTree"), setting);
	});


	function setFormData(){
		var zTree = $.fn.zTree.getZTreeObj("myTree");
        var selectedNodes  = zTree.getCheckedNodes(true);

        var sx = '';  
		var name='';
		var sxv = '';  
		var value='';
        for(var i=0; i<selectedNodes.length; i++){  
            if (sx != ''){ 
				sx += ','; 
			}
			if (sxv != ''){ 
				sxv += ','; 
			}
			name = selectedNodes[i].name+' ['+selectedNodes[i].id+']';
            sx += name;  
			value = selectedNodes[i].id;
            sxv += value;  
        }  

		var parent_window = getOpener();
		var x_elementId = parent_window.document.getElementById("${elementId}");
        var x_element_name = parent_window.document.getElementById("${elementName}");
		if(confirm("您确定选择'"+sx+"'吗？")){
			  x_elementId.value=sxv;
			  x_element_name.value=sx;
			  <#if chooseType == 'link'>
                 x_elementId.value="/chart/showChart?chartId="+value;
			     x_element_name.value="/chart/showChart?chartId="+value;
			  </#if>
			  window.close();
		}
         
	}

</script>
<style type="text/css">
.ztree li span.button.tree_folder_ico_open{margin-right:2px; background: url(${contextPath}/static/images/folder-open.gif) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.tree_folder_ico_close{margin-right:2px; background: url(${contextPath}/static/images/folder.gif) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.tree_folder_ico_docu{margin-right:2px; background: url(${contextPath}/static/images/folder.gif) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}

.ztree li span.button.tree_leaf_ico_open{margin-right:2px; background: url(${contextPath}/static/images/orm.gif) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.tree_leaf_ico_close{margin-right:2px; background: url(${contextPath}/static/images/orm.gif) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
.ztree li span.button.tree_leaf_ico_docu{margin-right:2px; background: url(${contextPath}/static/images/orm.gif) no-repeat scroll 0 0 transparent; vertical-align:top; *vertical-align:middle}
</style>
</head>

<body>

<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;  
	<span class="x_content_title">图表列表</span>
	  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	     onclick="javascript:setFormData();" >确定</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
	<ul id="myTree" class="ztree"></ul> 
  </form>
</div>
</div>
 
</body>
</html>