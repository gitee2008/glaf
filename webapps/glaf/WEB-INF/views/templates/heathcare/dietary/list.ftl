<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

    var x_height = Math.floor(window.screen.height * 0.60);
	var x_width = Math.floor(window.screen.width * 0.80);

	if(window.screen.height <= 768){
        x_height = Math.floor(window.screen.height * 0.54);
	}

	if(window.screen.width < 1200){
        x_width = Math.floor(window.screen.width * 0.82);
	} else if(window.screen.width > 1280){
        x_width = Math.floor(window.screen.width * 0.72);
	}  


   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:950,
				height:x_height,
				fit: true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/heathcare/dietary/json',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号',field:'startIndex', width:60, sortable:false},
				        {title:'选择',field: 'chk', width: 60, align: 'center', formatter: formatterKey},
						{title:'名称',field:'name', width:200, align:"left"},
						{title:'餐点',field:'typeName', width:130, align:"center"},
						{title:'年',field:'year', width:60, align:"center"},
						{title:'月',field:'month', width:60, align:"center"},
						{title:'日',field:'day', width:60, align:"center"},
						{title:'周',field:'week', width:60, align:"center"},
						{title:'热能(千卡)',field:'heatEnergy', width:90, align:"right", sortable:true},
						{title:'蛋白质(克)',field:'protein', width:90, align:"right", sortable:true},
						{title:'脂肪(克)',field:'fat', width:90, align:"right", sortable:true},
						{title:'碳水化合物(克)',field:'carbohydrate', width:120, align:"right", sortable:true},
						{title:'采购状态',field:'purchaseFlag', width:120, align:"center", formatter:formatterP},
						{title:'功能键', field:'functionKey',width:180, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100,200,500],
				pagePosition: 'both',
				onDblClickRow: onMyRowClick 
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					//alert('before refresh');
				}
		    });
	});

	function formatterKey(value, row, index) {
		if(row.purchaseFlag == "Y"){
           return "";
		}
		var s = '<input name="check" type="checkbox" value=\"'+row.id+'\" "/> ';
		return s;
	}

	function formatterStatus(val, row) {
		if(val == 9){
			return "已确认";
		}
		return "";
	}

	function formatterKeys(val, row){
		if(row.purchaseFlag == "Y"){
			return "<a href='javascript:details(\""+row.id+"\");'><img src='${contextPath}/static/images/statistics.png' border='0'>构成项</a>";
		}
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>&nbsp;<a href='javascript:calRow(\""+row.id+"\");'>计算</a>&nbsp;<a href='javascript:details(\""+row.id+"\");'><img src='${contextPath}/static/images/statistics.png' border='0'>组成</a>";
	    return str;
	}

    function formatterP(val, row) {
		if(val =="Y"){
			return "已加入采购";
		}
		return "未处理";
	}
 
   function details(dietaryId){
	    var link = '${contextPath}/heathcare/dietaryItem/datalist?dietaryId='+dietaryId;
		/*
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "食物构成列表",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['820px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
		*/

		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "食物构成列表",
		  area: ['820px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	
	}

	function addNew(){
	    var link="${contextPath}/heathcare/dietary/edit";
		/*
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
		*/
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "新增记录",
		  area: ['820px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}


	function calRow(id){
        if(confirm("确定按配置好的食谱计算各种成分的含量吗？")){
             
		}
	}


	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/dietary/edit?id='+row.id;
		/*
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
		*/
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['820px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

   function editRow(id){
	    var link = '${contextPath}/heathcare/dietary/edit?id='+id;
		/*
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
		*/
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['820px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

	function deleteRow(id){
		if(confirm("数据删除后不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/delete?id='+id,
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

	function calRow(id){
		if(confirm("确定重新计算食谱的成分吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/calculate?id='+id,
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
	    var link = '${contextPath}/heathcare/dietary/edit?id='+row.id;
		/*
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
		*/
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['820px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','食谱查询');
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
		  var link = '${contextPath}/heathcare/dietary/edit?id='+selected.id;
		  /*
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
		  */
		  layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "编辑记录",
			  area: ['820px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
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
		    var link='${contextPath}/heathcare/dietary/edit?readonly=true&id='+selected.id;
			/*
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
			*/
			layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "编辑记录",
			  area: ['820px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
		}
	}

	function deleteSelections(){
		var ids = $("input[name='check']:checked").map(function () {
               return $(this).val();
           }).get().join(',');
		if(ids.length > 0 ){
		  if(confirm("数据删除后不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/delete?ids='+ids,
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
			dataType: 'json',
			error: function(data){
				alert('服务器处理错误！');
			},
			success: function(data){
				jQuery('#mydatagrid').datagrid('loadData', data);
			}
		});
	}

	function parchasePlan(){
        var link = '${contextPath}/heathcare/dietary/searchlist';
        var x=30;
        var y=30;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 1280, 580);
		/*
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "采购计划列表",
		  area: ['1080px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
		*/
	}

	function searchWin() {
        var link = '${contextPath}/heathcare/dietaryTemplate/search';
        var x=30;
        var y=30;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 1280, 580);
		/*
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "查找",
		  area: ['1080px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
		*/
	}

	function makePlan() {
		var link = '${contextPath}/heathcare/dietaryTemplate/selectlist';
        var x=30;
        var y=30;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 1280, 610);
		/*
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "采购计划列表",
		  area: ['1080px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
		*/
	}

	function calSelected() {
		/**
	    var objectIds = jQuery("input[name='check']:checked").map(function () {
               return jQuery(this).val();
           }).get().join(',');
	    if(objectIds == ""){
           alert("请选择至少一条记录。");
		   return;
	    }
		**/
        var link = '${contextPath}/heathcare/dietaryCount';
        var x=30;
        var y=30;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 1280, 580);
		/*
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "统计结果",
		  area: ['1080px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
		*/
	}

	function showExport(){
        var link = '${contextPath}/heathcare/dietaryExport/showExport';
        var x=20;
        var y=20;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 1298, 650);
		/*
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "食谱列表",
		  area: ['1080px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
		*/
	}

	function showDayExport(){
        var link = '${contextPath}/heathcare/dietaryExport/showDayExport';
        var x=20;
        var y=20;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 1298, 650);
	}
	
	function exportWin(){
        var link = '${contextPath}/heathcare/dietaryTemplateExport/showExport';
        var x=20;
        var y=20;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 1290, 650);
    }

	function deletePlan(){
		var link="${contextPath}/heathcare/dietary/showRemove";
		layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "删除数据",
			  area: ['620px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
	}

	function doSearch(){
		var year = jQuery("#year").val();
		var week = jQuery("#week").val();
		var fullDay = jQuery("#fullDay").val();
        var link = "${contextPath}/heathcare/dietary/json?year="+year+"&week="+week;
		loadGridData(link);
		//document.iForm.action="${contextPath}/heathcare/dietary";
        //document.iForm.submit();
	}

</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north',split:false, border:true" style="height:38px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	  <table width="100%" align="left">
		<tbody>
		 <tr>
		    <td width="65%" align="left">
				&nbsp;<img src="${contextPath}/static/images/window.png">
				&nbsp;<span class="x_content_title">食谱列表</span>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
				   onclick="javascript:addNew();">新增</a>  
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
				   onclick="javascript:editSelected();">修改</a>  
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
				   onclick="javascript:deleteSelections();">删除</a> 
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_new'"
				   onclick="javascript:makePlan();">制定食谱</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_check'"
				   onclick="javascript:exportWin();">批量添加</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'"
				   onclick="javascript:showExport();">一周食谱</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-list'"
				   onclick="javascript:showDayExport();">每日食谱</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_table_row_add'"
				   onclick="javascript:parchasePlan();">生成采购计划</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-delete'"
				   onclick="javascript:deletePlan();">删除计划</a> 
				<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-formula'"
				   onclick="javascript:calSelected();">统计结果</a> -->
				<!-- <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
				   onclick="javascript:searchWin();">查找</a> -->
			</td>
			<td width="35%" align="left">
			  &nbsp;年份&nbsp; 
			  <select id="year" name="year">
				<#list years as year>
				<option value="${year}">${year}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("year").value="${year}";
			  </script>
			  &nbsp;周次&nbsp;
			  <select id="week" name="week">
				<#list weeks as week>
				<option value="${week}">${week}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("week").value="${week}";
			  </script>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'" 
	             onclick="javascript:doSearch();" >查找</a>
			</td>
		</tr>
	   </tbody>
	  </table>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</div>
</body>
</html>