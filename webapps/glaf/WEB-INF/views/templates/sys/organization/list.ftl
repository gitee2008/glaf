<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>机构列表</title>
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
		jQuery("#organizationId").val(treeNode.id);
		loadData('${contextPath}/sys/organization/json?parentId='+treeNode.id);
	}

	function loadData(url){
		$.post(url,{qq:'xx'},function(data){
		      //var text = JSON.stringify(data); 
              //alert(text);
			  $('#mydatagrid').datagrid('loadData', data);
		},'json');
	}


	$(document).ready(function(){
		$.fn.zTree.init($("#myTree"), setting);
	});

	function onMyDbClickRow(rowIndex, row){
		var link = '${contextPath}/sys/organization/prepareModify?id='+row.id;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "修改机构",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

    function formatterFunctions(val, row){
		var str = "<a href='javascript:editOrg(\""+row.id+"\");'>修改</a>";
        //str+="&nbsp;<a href='javascript:organizationUsers2(\""+row.id+"\");'>机构用户</a>";
	    //str+="&nbsp;<a href='javascript:organizationRoles2(\""+row.id+"\");'>机构角色</a>";
	    return str;
	}

	function editOrg(organizationId){
	    var link = '${contextPath}/sys/organization/prepareModify?id='+organizationId;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "修改机构",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

    function addNew(){
		var link = "${contextPath}/sys/organization/prepareAdd";
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "修改机构",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function onRowClick(rowIndex, row){
	    var link = '${contextPath}/sys/organization/prepareModify?id='+row.id;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "修改机构",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function reloadGrid(){
	    jQuery('#mydatagrid').datagrid('reload');
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','机构列表查询');
	}

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}

	function deleteSelected(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  //alert("请选择其中一条记录。");
		    var organizationId = jQuery("#organizationId").val();
			if(organizationId != null && organizationId != ""){
                 if(confirm("机构删除后不能恢复，确定删除吗？")){
			        jQuery.ajax({
							   type: "POST",
							   url: '${contextPath}/sys/organization/deleteById?organizationId='+organizationId,
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
								       window.location.reload();
								   }
							   }
						 });
				 }
				 return;
			} else {
				 alert("请选择其中一条记录。");
		         return;
			}
		}

		var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if ( selected ){
             if(confirm("机构删除后不能恢复，确定删除吗？")){
			        jQuery.ajax({
							   type: "POST",
							   url: '${contextPath}/sys/organization/deleteById?organizationId='+selected.id,
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
								       window.location.reload();
								   }
							   }
						 });
				 }
		} else {
		      alert("请选择其中一条记录。");
		      return;
		}
	}

	function editSelected(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  //alert("请选择其中一条记录。");
		    var organizationId = jQuery("#organizationId").val();
			if(organizationId != null && organizationId != ""){
				var link = "${contextPath}/sys/organization/prepareModify?id="+organizationId;
				jQuery.layer({
					type: 2,
					maxmin: true,
					shadeClose: true,
					title: "修改机构",
					closeBtn: [0, true],
					shade: [0.8, '#000'],
					border: [10, 0.3, '#000'],
					offset: ['20px',''],
					fadeIn: 100,
					area: ['680px', (jQuery(window).height() - 50) +'px'],
					iframe: {src: link}
				});
			} else {
				 alert("请选择其中一条记录。");
		         return;
			}
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if ( selected ){
		    var link = "${contextPath}/sys/organization/prepareModify?id="+selected.id;
			jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "修改机构",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['680px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
	    }
	}

	function organizationRoles(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  alert("请选择其中一条记录。");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if ( selected && selected.id != null && selected.id != 'undefined'){
		    var link = "${contextPath}/sys/organization/organizationRole?organizationId="+selected.id;
			jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "机构角色",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['780px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
	    }
	}

	function organizationRoles2(organizationId){
		var link = "${contextPath}/sys/organization/showRole?organizationId="+organizationId;
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "机构角色",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['780px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
	}

	function organizationUsers(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		    //alert("请选择其中一条记录。");
            var organizationId = jQuery("#organizationId").val();
			if(organizationId != null && organizationId != ""){
				var link = "${contextPath}/sys/user?organizationId="+organizationId;
				jQuery.layer({
					type: 2,
					maxmin: true,
					shadeClose: true,
					title: "机构用户",
					closeBtn: [0, true],
					shade: [0.8, '#000'],
					border: [10, 0.3, '#000'],
					offset: ['20px',''],
					fadeIn: 100,
					area: ['980px', (jQuery(window).height() - 50) +'px'],
					iframe: {src: link}
				});
			 } else {
				 alert("请选择其中一条记录。");
		         return;
			}
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if ( selected && selected.id != null && selected.id != 'undefined'){
		    var link = "${contextPath}/branch/user?organizationId="+selected.id;
			jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "机构用户",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['980px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
	    }
	}

	function organizationRoles(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		    //alert("请选择其中一条记录。");
            var organizationId = jQuery("#organizationId").val();
			if(organizationId != null && organizationId != ""){
				var link = "${contextPath}/sys/organization/showRole?organizationId="+organizationId;
				jQuery.layer({
					type: 2,
					maxmin: true,
					shadeClose: true,
					title: "机构角色",
					closeBtn: [0, true],
					shade: [0.8, '#000'],
					border: [10, 0.3, '#000'],
					offset: ['20px',''],
					fadeIn: 100,
					area: ['980px', (jQuery(window).height() - 50) +'px'],
					iframe: {src: link}
				});
			 } else {
				 alert("请选择其中一条记录。");
		         return;
			}
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if ( selected && selected.id != null && selected.id != 'undefined'){
		    var link = "${contextPath}/sys/organization/showRole?organizationId="+selected.id;
			jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "机构角色",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['980px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
	    }
	}

	function organizationUsers2(organizationId){
		var link = "${contextPath}/sys/user?organizationId="+organizationId;
			jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "机构用户",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['980px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
	}

	function viewSelected(){
		var rows = jQuery('#mydatagrid').datagrid('getSelected');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		    location.href="${contextPath}/sys/organization/prepareModify?id="+selected.id;
		}
	}

	function deleteSelections(){
		var ids = [];
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		for(var i=0;i<rows.length;i++){
			ids.push(rows[i].id);
		}
		if(ids.length > 0 && confirm("数据删除后不能恢复，确定删除吗？")){
		    var rowIds = ids.join(',');
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/organization/delete?organizationIds='+rowIds,
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
					   jQuery('#mydatagrid').datagrid('reload');
				   }
			 });
		} else {
			alert("请选择至少一条记录。");
		}
	}


	function getSelected(){
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected){
		  alert(selected.code+":"+selected.name+":"+selected.addr+":"+selected.col4);
	    }
	}

	function getSelections(){
	    var ids = [];
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    for(var i=0;i<rows.length;i++){
		ids.push(rows[i].code);
	    }
	    alert(ids.join(':'));
	}

	function clearSelections(){
	    jQuery('#mydatagrid').datagrid('clearSelections');
	}

	function searchData(){
	    var params = jQuery("#searchForm").formSerialize();
	    var queryParams = jQuery('#mydatagrid').datagrid('options').queryParams;
	    jQuery('#mydatagrid').datagrid('reload');	
	    jQuery('#dlg').dialog('close');
	}

	function sortOrg(){
	    var organizationId = jQuery("#organizationId").val();
		var link = "${contextPath}/sys/organization/showSort?parentId="+organizationId;
		var width=680;
		var height=430;
		var scroll="yes";
		jQuery.layer({
					type: 2,
					maxmin: true,
					shadeClose: true,
					title: "节点排序",
					closeBtn: [0, true],
					shade: [0.8, '#000'],
					border: [10, 0.3, '#000'],
					offset: ['20px',''],
					fadeIn: 100,
					area: ['650px', (jQuery(window).height() - 50) +'px'],
					iframe: {src: link}
				});
   }

   function organizationRoles3(){
	    var organizationId = jQuery("#organizationId").val();
		var link = "${contextPath}/sys/organization/showRole?organizationId="+organizationId;
		var width=680;
		var height=430;
		var scroll="yes";
		jQuery.layer({
					type: 2,
					maxmin: true,
					shadeClose: true,
					title: "机构角色",
					closeBtn: [0, true],
					shade: [0.8, '#000'],
					border: [10, 0.3, '#000'],
					offset: ['20px',''],
					fadeIn: 100,
					area: ['850px', (jQuery(window).height() - 50) +'px'],
					iframe: {src: link}
				});
   }

	function formatterStatus(val, row){
		if(val == 0){
			return "<font color='green'>启用</font>";
		} else {
            return "<font color='red'>禁用</font>";
		}
	}

	
	function loadGridData(url){
	    jQuery.ajax({
			type: "POST",
			url:  url,
			dataType: 'json',
			error: function(data){
				alert('服务器处理错误！');
			},
			success: function(data){
				jQuery('#mydatagrid').datagrid('loadData', data);
			}
		});
	}

	function searchXY(namePinyinLike){
        var link = "${contextPath}/sys/organization/json?namePinyinLike="+namePinyinLike;
		loadGridData(link);
	}

</script>
</head>
<body>  
<input type="hidden" id="organizationId" name="organizationId" value="" >
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  

    <div data-options="region:'west',split:true" style="width:220px;">
	  <div class="easyui-layout" data-options="fit:true">  
           
			 <div data-options="region:'center',border:false">
			    <ul id="myTree" class="ztree"></ul>  
			 </div> 
			 
        </div>  
	</div> 
	
    <div data-options="region:'center'">  
        <div class="easyui-layout" data-options="fit:true">  
           <div data-options="region:'center', split:false, border:true, fit:true" style="height:68px" class="toolbar-backgroud">
             <div style="margin:4px;"> 
			 	 <table width="100%" align="left">
					<tbody>
					  <tr>
						<td align="left">
							<img src="${contextPath}/static/images/window.png">
							&nbsp;<span class="x_content_title">机构列表</span>
							<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
							   onclick="javascript:addNew();">新增</a>  
							<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
							   onclick="javascript:editSelected();">修改</a>  
							<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
							   onclick="javascript:deleteSelected();">删除</a>  
							<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-user'"
							   onclick="javascript:organizationUsers();">机构用户</a> 
							<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-actor'"
							   onclick="javascript:organizationRoles();">机构角色</a> -->
							<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-sort'"
							   onclick="javascript:sortOrg();">同级排序</a>
							<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-reload'"
							   onclick="javascript:reloadGrid();">全部</a> 
						</td>
					  </tr>
					  <tr>
						<td>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<#list charList as item>
							&nbsp;<span class="x_char_name" onclick="javascript:searchXY('${item}');">${item}</span>&nbsp;
							</#list>
						</td>
					</tr>
				   </tbody>
				  </table>
             </div>  

			 <table id="mydatagrid" class="easyui-datagrid" 
					data-options="url:'${contextPath}/sys/organization/json', fit:true,fitColumns:true,nowrap:false,rownumbers:false,showFooter:true,singleSelect:true,onDblClickRow:onMyDbClickRow">
				<thead>
					<tr>
						<th data-options="field:'startIndex',width:60">序号</th>
						<th data-options="field:'name',width:150">名称</th>
						<th data-options="field:'description',width:180">描述</th>
						<th data-options="field:'code',width:120">代码</th>
						<th data-options="field:'no',width:120">编码</th>
						<th data-options="field:'code2',width:120">机构区分</th>
                        <th data-options="field:'locked',width:100, formatter:formatterStatus">是否启用</th>
						<th data-options="field:'xx',width:150, formatter:formatterFunctions">功能键</th>
					</tr>
				</thead>
			</table>  
	    
        </div>  
    </div>  
  </div>  

</body>  
</html>