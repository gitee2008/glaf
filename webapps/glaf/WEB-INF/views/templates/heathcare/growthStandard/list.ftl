<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>生长标准值</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

    function getLink(){
	    var link_ = "${contextPath}/heathcare/growthStandard/json?type=${type}&standardType=${standardType}&sex=${sex}";
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
				url: '${contextPath}/heathcare/growthStandard/json?type=${type}&sex=${sex}&standardType=${standardType}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:60, sortable:false},
						{title:'月龄', field:'ageOfTheMoon', width:60},
						{title:'性别', field:'sex', width:60, formatter:formatterSex},
						{title:'身高', field:'height', width:60},
						{title:'体重', field:'weight', width:60},
						{title:'-3SD', field:'negativeThreeDSDeviation', width:80},
						{title:'-2SD', field:'negativeTwoDSDeviation', width:80},
						{title:'-1SD', field:'negativeOneDSDeviation', width:80},
						{title:'中位数', field:'median', width:90},
						{title:'+1SD', field:'oneDSDeviation', width:80},
						{title:'+2SD', field:'twoDSDeviation', width:80},
						{title:'+3SD', field:'threeDSDeviation', width:80},
					    {title:'类别', field:'type', width:120, formatter:formatterType},
						{title:'创建时间', field:'createTime', width:90},
						{title:'功能键', field:'functionKey',width:120, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100],
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


	function formatterKeys(val, row){
		<#if can_write == true>
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>";
	    return str;
		</#if>
		return "";
	}

	function formatterAge(val, row){
		if(row.type == "4"){
           return "-";
		}
		return val;
	}

	function formatterMonth(val, row){
		if(row.type == "4"){
           return "-";
		}
		return val;
	}

	function formatterType(val, row){
		if(val == "2"){
			return "年龄别身高";
		} else if(val == "3"){
			return "年龄别体重";
		} else if(val == "4"){
			return "身高别体重";
		} else if(val == "5"){
			return "体质指数（BMI）";
		}
		return "";
	}

	function formatterSex(val, row){
		if(val == "1"){
			return "男生";
		}
		return "女生";
	}


	function addNew(){
	    var link="${contextPath}/heathcare/growthStandard/edit?type=${type}&standardType=${standardType}";
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "新增记录",
		  area: ['680px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}


	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/growthStandard/edit?id='+row.id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['680px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

    function editRow(id){
	    var link = '${contextPath}/heathcare/growthStandard/edit?id='+id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['680px', (jQuery(window).height() - 50) +'px'],
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
				   url: '${contextPath}/heathcare/growthStandard/delete?id='+id,
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
	    var link = '${contextPath}/heathcare/growthStandard/edit?id='+row.id;
	    layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['680px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','生长标准值查询');
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
		  var link = '${contextPath}/heathcare/growthStandard/edit?id='+selected.id;
		  layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "编辑记录",
			  area: ['680px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
		  });
	    }
	}

	function showImp(){
		var standardType = jQuery("#standardType").val();
        var type = jQuery("#type").val();
		var sex = jQuery("#sex").val();
		var link = "${contextPath}/heathcare/growthStandard/showImport?type="+type+"&standardType="+standardType+"&sex="+sex;
		layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "导入数据",
			  area: ['680px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
		});
	}


	function showImp2(){
		var standardType = jQuery("#standardType").val();
        var type = jQuery("#type").val();
		var sex = jQuery("#sex").val();
		var link = "${contextPath}/heathcare/growthStandard/showImport2?type="+type+"&standardType="+standardType+"&sex="+sex;
		layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "导入数据",
			  area: ['680px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
		});
	}

	function viewSelected(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		    var link='${contextPath}/heathcare/growthStandard/edit?readonly=true&id='+selected.id;
		    layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "编辑记录",
			  area: ['680px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
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
				   url: '${contextPath}/heathcare/growthStandard/delete?ids='+str,
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

	function searchData(){
        var params = jQuery("#searchForm").formSerialize();
        jQuery.ajax({
                    type: "POST",
                    url: '${contextPath}/heathcare/growthStandard/json',
                    dataType:  'json',
                    data: params,
                    error: function(data){
                              alert('服务器处理错误！');
                    },
                    success: function(data){
                              jQuery('#mydatagrid').datagrid('loadData', data);
                    }
                  });

	    jQuery('#dlg').dialog('close');
	}

	function switchXY(){
		document.iForm.action="${contextPath}/heathcare/growthStandard";
        document.iForm.submit();
	}

	function showChart(){
		if(jQuery("#standardType").val()==""){
            alert('请选择标准！');
			return;
		}
		if(jQuery("#type").val()==""){
            alert('请选择类型！');
			return;
		}
		if(jQuery("#sex").val()==""){
            alert('请选择性别！');
			return;
		}
		var standardType = jQuery("#standardType").val();
        var type = jQuery("#type").val();
		var sex = jQuery("#sex").val();
		var link = "${contextPath}/heathcare/growthSpline/line?type="+type+"&standardType="+standardType+"&sex="+sex;
		var x=20;
	    var y=20;
		if(is_ie) {
			x=document.body.scrollLeft+event.clientX-event.offsetX-200;
			y=document.body.scrollTop+event.clientY-event.offsetY-200;
		}
		var x_height2 = Math.floor(window.screen.height * 0.84);
		var x_width2 = Math.floor(window.screen.width * 0.96);
		openWindow(link, self, x, y, x_width2, x_height2);
		/**
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "成长图表",
		  area: ['1280px', (jQuery(window).height() - 30) +'px'],
		  shade: 0.85,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
		**/
	}
		 
</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
     <form id="iForm" name="iForm" method="post" action="">
      <table>
       <tr>
	    <td>
		<img src="${contextPath}/static/images/window.png">
		&nbsp;<span class="x_content_title">生长标准值列表</span>
		<#if can_write == true>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
		   onclick="javascript:addNew();">新增</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
		   onclick="javascript:editSelected();">修改</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
		   onclick="javascript:deleteSelections();">删除</a> 
		</#if>
		</td>
		<td>
		  &nbsp;&nbsp;标准
	      <select id="standardType" name="standardType" onchange="switchXY();">
			<option value="">----请选择----</option>
			<option value="CN">中国标准</option>
			<option value="ISO">国际标准</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("standardType").value="${standardType}";
		  </script>
		</td>
		<td>
		  &nbsp;&nbsp;类型
	      <select id="type" name="type" onchange="switchXY();">
			<option value="">----请选择----</option>
			<!-- <option value="1">年龄别头围标准差</option> -->
			<option value="2">年龄别身高标准差</option>
			<option value="3">年龄别体重标准差</option>
			<option value="4">身高别体重标准差</option>
			<option value="5">体质指数（BMI）</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("type").value="${type}";
		  </script>
		</td>
		<td>
		  &nbsp;&nbsp;性别
	      <select id="sex" name="sex" onchange="switchXY();">
			<option value="">----请选择----</option>
			<option value="0">女</option>
			<option value="1">男</option>
		  </select>
		  <script type="text/javascript">
			   document.getElementById("sex").value="${sex}";
		  </script>
		</td>
		<td>
		  &nbsp;&nbsp; 
          <!-- <button type="button" id="searchButton" class="btn btnGrayMini" style="width:60px" 
				  onclick="javascript:showImp();">导入</button> -->
		  &nbsp;&nbsp; 
          <button type="button" id="searchButton" class="btn btnGrayMini" style="width:60px" 
				  onclick="javascript:showImp2();">导入</button>
		</td>
		<td>
		  &nbsp;&nbsp; 
          <button type="button" id="searchButton" class="btn btnGrayMini" style="width:60px" 
				  onclick="javascript:showChart();">图表</button>
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