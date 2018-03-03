<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>每日实际就餐人数</title>
<#include "/inc/init_easyui_import.ftl"/> 
<script type="text/javascript">

   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/heathcare/actualRepastPerson/reviewJson?tenantId=${tenantId}&year=${year}&month=${month}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				    {title:'序号', field:'startIndex', width:80, sortable:false},
					{title:'年级', field:'classTypeName', width:120},
					{title:'年龄', field:'age', width:120},
					{title:'男生人数', field:'male', width:120},
					{title:'女生人数', field:'female', width:120},
					{title:'日期', field:'fullDay', width:120}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 20,
				pageList: [20,25,30,40,50,100,200,500,1000],
				pagePosition: 'both'
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					//alert('before refresh');
				}
		    });
	});


	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','人员信息查询');
	    //jQuery('#searchForm').form('clear');
	}

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
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
                    url: '${contextPath}/heathcare/actualRepastPerson/json',
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

	function doSearch(){
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
	<table>
	 <tr>
	    <td width="42%">
          <img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">每日实际就餐人数列表</span>
		</td>
		<td>&nbsp;&nbsp;年&nbsp;
			<select id="year" name="year">
			    <option value="2017">2017</option>
				<option value="2018">2018</option>
			</select>
			<script type="text/javascript">
			  document.getElementById("year").value="${year}";
		   </script>
		   &nbsp;月&nbsp;
			<select id="month" name="month">
				<option value="">----请选择----</option>
				<option value="1">1</option>
				<option value="2">2</option>
				<option value="3">3</option>
				<option value="4">4</option>
				<option value="5">5</option>
				<option value="6">6</option>
				<option value="7">7</option>
				<option value="8">8</option>
				<option value="9">9</option>
				<option value="10">10</option>
				<option value="11">11</option>
				<option value="12">12</option>
			</select>
			<script type="text/javascript">
			  document.getElementById("month").value="${month}";
		    </script>
		</td>
		<td>
		    <input type="button" value="查找" onclick="doSearch();" class="btnGrayMini">
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