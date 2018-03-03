<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱模板</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

    var x_height = Math.floor(window.screen.height * 0.54);
	var x_width = Math.floor(window.screen.width * 0.80);

	if(window.screen.height <= 768){
        x_height = Math.floor(window.screen.height * 0.52);
	}

	if(window.screen.width < 1200){
        x_width = Math.floor(window.screen.width * 0.82);
	} else if(window.screen.width > 1280){
        x_width = Math.floor(window.screen.width * 0.72);
	}  


   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1850,
				height:x_height,
				fit:false,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/heathcare/dietaryTemplate/json?season=${season}&typeId=${typeId}&nameLike_enc=${nameLike_enc}&selected=${selected}&week=${week}&dayOfWeek=${dayOfWeek}&suitNo=${suitNo}&sysFlag=${sysFlag}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        <#if sysFlag?exists && typeId?exists && suitNo?exists && dayOfWeek?exists>
				        {title:'选择',field: 'chk', width: 60, align: 'center', formatter: formatterKey},
						</#if>
				        {title:'序号', field:'startIndex', width:60, sortable:false},
						{title:'名称',field:'name', width:200, align:"left"},
						{title:'餐点',field:'typeName', width:130, align:"center"},
						{title:'日期',field:'dayOfWeekName', width:60, align:"center"},
						{title:'热能(千卡)',field:'heatEnergy', width:90, align:"right", sortable:true},
						{title:'蛋白质(克)',field:'protein', width:90, align:"right", sortable:true},
						{title:'脂肪(克)',field:'fat', width:90, align:"right", sortable:true},
						{title:'碳水化合物(克)',field:'carbohydrate', width:120, align:"right", sortable:true},
						{title:'微生素A(μgRE)',field:'vitaminA', width:120, align:"right", sortable:true},
						{title:'微生素B1(毫克)',field:'vitaminB1', width:120, align:"right", sortable:true},
						{title:'微生素B2(毫克)',field:'vitaminB2', width:120, align:"right", sortable:true},
						{title:'微生素C(毫克)',field:'vitaminC', width:120, align:"right", sortable:true},
						{title:'胡萝卜素(微克)',field:'carotene', width:120, align:"right", sortable:true},
						{title:'视黄醇(微克)',field:'retinol', width:90, align:"right", sortable:true},
						{title:'尼克酸(毫克)',field:'nicotinicCid', width:90, align:"right", sortable:true},
						{title:'钙(毫克)',field:'calcium', width:90, align:"right", sortable:true},
						{title:'铁(毫克)',field:'iron', width:90, align:"right", sortable:true},
						{title:'锌(毫克)',field:'zinc', width:90, align:"right", sortable:true},
						{title:'碘(毫克)',field:'iodine', width:90, align:"right", sortable:true},
						{title:'磷(毫克)',field:'phosphorus', width:90, align:"right", sortable:true},
						{title:'功能键',field:'funkey', width:150, align:"center", formatter: formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100,200],
				pagePosition: 'both',
				onDblClickRow: onMyRowClick2 
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					//alert('before refresh');
				}
		    });
	});


	function formatterKey(value, row, index) {
		var s = '<input name="isCheck" type="checkbox" value="'+row.id+'" /> ';
	    return s;
	}

   function formatterKeys(val, row){
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>&nbsp;<a href='javascript:foodDetail(\""+row.id+"\");'>构成项</a>";
	    return str;
	}
	

	function addNew(){
	    var link="${contextPath}/heathcare/dietaryTemplate/edit";
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
			area: ['780px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}


	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/dietaryTemplate/edit?id='+row.id;
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
			area: ['780px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function onMyRowClick2(rowIndex, row){
	    var link = "${contextPath}/heathcare/dietaryItem?templateId="+row.templateId;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "食物构成",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

   function editRow(id){
	    var link = '${contextPath}/heathcare/dietaryTemplate/edit?id='+id;
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
			area: ['780px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function deleteRow(id){
		if(confirm("数据删除后不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietaryTemplate/delete?id='+id,
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
	    var link = '${contextPath}/heathcare/dietaryTemplate/edit?id='+row.id;
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
			area: ['780px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','食谱模板库查询');
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
		  var link = '${contextPath}/heathcare/dietaryTemplate/edit?id='+selected.id;
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
			area: ['780px', (jQuery(window).height() - 50) +'px'],
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
		    var link='${contextPath}/heathcare/dietaryTemplate/edit?readonly=true&id='+selected.id;
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
			area: ['780px', (jQuery(window).height() - 50) +'px'],
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
				   url: '${contextPath}/heathcare/dietaryTemplate/delete?ids='+str,
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

	function editDetail() {
	   var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
			var link = "${contextPath}/heathcare/dietaryItem?templateId="+selected.id;
			jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "食物构成",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['880px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
		}
	}

	function foodDetail(id) {
		var link = "${contextPath}/heathcare/dietaryItem?templateId="+id;
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "食物构成",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['880px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
		});
	}

	function calSelected() {
	   var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		  if(confirm("确定重新计算该食谱的各个成分含量吗？")){
            var link = "${contextPath}/heathcare/dietaryTemplate/calculate?templateId="+selected.id;
		    jQuery.ajax({
				   type: "POST",
				   url: link,
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
	}

	function reloadGrid(){
	    jQuery('#mydatagrid').datagrid('reload');
	}

	function searchData(){
       document.iForm.submit();
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

	function showDayExport(){
        var link = '${contextPath}/heathcare/dietaryTemplateExport/showDayExport';
        var x=20;
        var y=20;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 1290, 650);
	}

	function checkAll() {
		//jQuery("input[name='isCheck']").attr("checked", true); 
		jQuery("[name = isCheck]:checkbox").attr("checked", true);
	}

	function sortSelections(){
         var objectIds = $("input[name='isCheck']:checked").map(function () {
               return $(this).val();
           }).get().join(',');
		//alert(objectIds);
        var link = '${contextPath}/heathcare/dietaryTemplate/showSort?objectIds='+objectIds;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "食谱排序",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['698px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}
</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north',split:false, border:true" style="height:72px" class="toolbar-backgroud"> 
    <div style="margin:4px;">
		<form id="iForm" name="iForm" method="post" action="">
			<input type="hidden" id="elementId" name="elementId" value="${elementId}">
			<input type="hidden" id="elementName" name="elementName" value="${elementName}">
			<input type="hidden" id="selected" name="selected" value="${selected}">
			<table>
				<tr>
					<td colspan="8">
						<img src="${contextPath}/static/images/window.png">
						&nbsp;<span class="x_content_title">食谱库列表</span>
						<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
						   onclick="javascript:addNew();">新增</a> 
						<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
						   onclick="javascript:editSelected();">修改</a>
						<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
						   onclick="javascript:deleteSelections();">删除</a>
						<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-formula'"
						   onclick="javascript:calSelected();">计算</a>
						<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-list'"
						   onclick="javascript:editDetail();">构成项</a> 
						<#if sysFlag?exists && typeId?exists && suitNo?exists && dayOfWeek?exists>
						<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-sort'"
						   onclick="javascript:sortSelections();">排序</a>
						<button type="button" id="checkButton111" class="btn btnGrayMini" style="width: 60px" 
								onclick="javascript:checkAll();">全选</button>
						</#if>
						&nbsp;&nbsp;名称&nbsp;&nbsp;
					   <input id="nameLike" name="nameLike" type="text" class="x-searchtext"  
							  style="width:295px;" value="${nameLike}">
					</td>
					</tr>
					<tr>
					<td>
						&nbsp;餐点&nbsp;
						<#if typeDict?exists>
						<span style="color:#0066ff;font-weight:bold;">${typeDict.name}</span>
						</#if>
						&nbsp;
						<select id="typeId" name="typeId">
							<option value="">----请选择----</option>
							<#list dictoryList as d>
							<option value="${d.id}">${d.name}</option>
							</#list> 
						</select>
						<script type="text/javascript">
						   document.getElementById("typeId").value="${typeId}";
						</script>
					</td>
					<td>
					  &nbsp;类型&nbsp;
					  <select id="sysFlag" name="sysFlag">
						<option value="">----请选择----</option> 
						<option value="Y">系统内置</option>
						<option value="N">我自己的</option>
					  </select>
					  <script type="text/javascript">
						   document.getElementById("sysFlag").value="${sysFlag}";
					  </script>
					</td>
					<td>
					  &nbsp;序号&nbsp;
					  <select id="suitNo" name="suitNo">
						<option value="">----请选择----</option>
						<#list suitNos as suitNo>
						<option value="${suitNo}">第${suitNo}套</option>
						</#list>  
					  </select>
					  <script type="text/javascript">
						   document.getElementById("suitNo").value="${suitNo}";
					  </script>
					</td>
					<td>
					  &nbsp;日期&nbsp;
					  <select id="dayOfWeek" name="dayOfWeek">
						<option value="">----请选择----</option>
						<option value="1">星期一</option>
						<option value="2">星期二</option>
						<option value="3">星期三</option>
						<option value="4">星期四</option>
						<option value="5">星期五</option>
						<option value="6">星期六</option>
						<option value="7">星期日</option>
					  </select>
					  <script type="text/javascript">
						   document.getElementById("dayOfWeek").value="${dayOfWeek}";
					  </script>
					</td>
					<!-- <td>
						&nbsp;省份&nbsp;
						<select id="province" name="province">
						 <option value="">----请选择----</option>
						 <#list districts as district>
						 <option value="${district.name}">${district.name}</option>
						 </#list>
						</select>
						<script type="text/javascript">
						   document.getElementById("province").value="${province}";
						</script>    
					</td> -->
					<td>
						<button type="button" id="searchButton" class="btn btnGrayMini" style="width:60px" 
								onclick="javascript:searchData();">查找</button>
						<button type="button" id="expButton" class="btn btnGrayMini" style="width:60px" 
								onclick="javascript:exportWin();">导出</button>
						<button type="button" id="expButton" class="btn btnGrayMini" style="width:90px" 
								onclick="javascript:showDayExport();">食谱成分</button>
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