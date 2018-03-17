<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>计划用量表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

    function getLink(){
	    var link_ = "${contextPath}/heathcare/goodsPlanQuantity/json?startTime=${startTime}&endTime=${endTime}";
		//alert(link_);
	    return link_;
	}


   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/heathcare/goodsPlanQuantity/json?startTime=${startTime}&endTime=${endTime}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				    {title:'序号', field:'startIndex', width:60, sortable:false},
					{title:'物品名称', field:'goodsName', width:220, align:'left', sortable:true, formatter:formatterName},
					{title:'重量(千克)', field:'quantity', width:120, align:'right', sortable:true},
					{title:'计量单位', field:'unit', width:120, align:'center', formatter:formatterUnit},
					{title:'计划使用时间', field:'usageTime', width:100, align:'center'},
					{title:'自动生成', field:'sysFlag', width:120, align:"center", formatter:formatterSys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 50,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
				pagePosition: 'both',
				onDblClickRow: onMyRowClick 
			});

			var pgx = $("#mydatagrid").datagrid("getPager");
			if(pgx){
			   $(pgx).pagination({
				   onBeforeRefresh:function(){
					   //alert('before refresh');
				   },
				   onRefresh:function(pageNumber,pageSize){
					   //alert(pageNumber);
					   //alert(pageSize);
					   loadGridData(getLink()+"&page="+pageNumber+"&rows="+pageSize);
					},
				   onChangePageSize:function(){
					   //alert('pagesize changed');
					   loadGridData(getLink());
					},
				   onSelectPage:function(pageNumber, pageSize){
					   //alert(pageNumber);
					   //alert(pageSize);
					   loadGridData(getLink()+"&page="+pageNumber+"&rows="+pageSize);
					}
			   });
			}
	});

	function formatterName(val, row){
		return "<a href='#' onclick=javascript:showName('"+row.goodsId+"')>"+val+"</a>";
	}

	function showName(id){
	    var link="${contextPath}/heathcare/foodComposition/view?id="+id;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "查看食物成分",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['480px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function formatterSys(val, row) {
		if(val =="Y"){
			return "系统生成";
		}
		return "";
	}

	function formatterUnit(val, row){
		if(val == 'KG'){
			return "千克";
		} else if(val == 'G'){
			return "克"
		} else if(val == 'L'){
			return "升"
		} else if(val == 'ML'){
			return "毫升"
		}
	}

	function formatterKeys(val, row){
		if(row.businessStatus == 9){
			return "已审核";
		}
		var str = "";
		<#if audit == true>
		  str = "<a href='javascript:auditRow(\""+row.id+"\");'><img src='${contextPath}/static/images/audit.png' border='0'>&nbsp;审核</a>&nbsp;";
		<#else>
		  str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>";
		</#if>
	    return str;
	}

	function formatterStatus(val, row){
		if(val == 9){
           return "<font color='green'>已确认</font>";   
		}
        return "未确认";   
	}
	

	function addNew(){
	    var link="${contextPath}/heathcare/goodsPlanQuantity/edit";
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "新增记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}


	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/goodsPlanQuantity/edit?audit=${audit}&id='+row.id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

   function editRow(id){
	    var link = '${contextPath}/heathcare/goodsPlanQuantity/edit?id='+id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function auditRow(id){
	    var link = '${contextPath}/heathcare/goodsPlanQuantity/edit?audit=true&id='+id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "审核记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function deleteRow(id){
		if(confirm("数据删除后不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsPlanQuantity/delete?id='+id,
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
					       jQuery('#mydatagrid').datagrid('reload');
					   }
				   }
			 });
		  }
	}

	function onRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/goodsPlanQuantity/edit?id='+row.id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','计划用量表查询');
	    //jQuery('#searchForm').form('clear');
	}

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}

	function editSelected(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  alert("请选择其中一条记录。");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		  var link = '${contextPath}/heathcare/goodsPlanQuantity/edit?id='+selected.id;
		  jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑记录",
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

	function auditSelected(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  alert("请选择其中一条记录。");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		  var link = '${contextPath}/heathcare/goodsPlanQuantity/edit?audit=true&id='+selected.id;
		  jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "审核记录",
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

	function viewSelected(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		    var link='${contextPath}/heathcare/goodsPlanQuantity/edit?readonly=true&id='+selected.id;
		    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑记录",
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

	function deleteSelections(){
		var ids = [];
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		for(var i=0;i<rows.length;i++){
			ids.push(rows[i].id);
		}
		if(ids.length > 0 ){
		  if(confirm("数据删除后不能恢复，确定删除吗？")){
		    var str = ids.join(',');
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsPlanQuantity/delete?ids='+str,
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
					       jQuery('#mydatagrid').datagrid('reload');
					   }
				   }
			 });
		  }
		} else {
			alert("请选择至少一条记录。");
		}
	}

	function reloadGrid(){
	    jQuery('#mydatagrid').datagrid('reload');
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

	function loadGridData(url){
	    jQuery.ajax({
			type: "POST",
			url:  url,
			dataType:  'json',
			error: function(data){
				alert('服务器处理错误！');
			},
			success: function(data){
				jQuery('#mydatagrid').datagrid('loadData', data);
			}
		});
	}
 
	function doSearch(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime != ""){
			startTime = startTime + " 00:00:00";
		}
		if(endTime != ""){
			endTime = endTime + " 23:59:59";
			//jQuery("#endTime").val(endTime);
		}
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		document.iForm.submit();
	}
</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
    <form id="iForm" name="iForm" method="post" action="">
      <table valign="middle">
       <tr>
	    <td valign="middle">   
		   <img src="${contextPath}/static/images/window.png">
	       &nbsp;<span class="x_content_title">计划用量表列表</span>
		</td>
		<td valign="middle">
		  &nbsp;日期&nbsp;开始&nbsp;
		  <input id="startTime" name="startTime" type="text" class="easyui-datebox x-text" style="width:100px"
		         <#if startTime?exists> value="${startTime}"</#if>>
		  &nbsp;结束&nbsp;
		  <input id="endTime" name="endTime" type="text" class="easyui-datebox x-text" style="width:100px"
		         <#if endTime?exists> value="${endTime}"</#if>>
		  &nbsp;
		  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
	       onclick="javascript:doSearch();">查找</a>
		</td>
       </tr>
      </table>
	</form>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</div>
</body>
</html>