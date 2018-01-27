<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>执行日志</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
   var contextPath="${request.contextPath}";

   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${request.contextPath}/sys/executionLog/json?type=${type}&businessKey=${businessKey}&dateAfter=${dateAfter}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:50, sortable:false},
						{title:'用户', field:'createBy', width:60},
					    {title:'编号', field:'jobNo', width:285},
					    {title:'内容', field:'content', width:420},
						{title:'操作时间', field:'createTime_datetime', width:140},
						{title:'时间（毫秒）', field:'runTime', width:80},
					    {title:'状态', field:'status', align:'center', width:80, formatter:formatterStatus}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
				pagePosition: 'both'
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					//alert('before refresh');
				}
		    });
	});

 
	function formatterStatus(val, row){
       if(val == 1){
		   return "<span style='color:green;'>成功</span>";
	   } else if(val == -1) {
           return "<span style='color:red;'>失败</span>";
	   } else {
		   return "<span style='color:blue;'>"+val+"</span>";
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
        jQuery.post(url,{qq:'xx'},function(data){
            //var text = JSON.stringify(data); 
            //alert(text);
            jQuery('#mydatagrid').datagrid('loadData', data);
        },'json');
	}


    function myformatter(date){
        var y = date.getFullYear();
        var m = date.getMonth()+1;
        var d = date.getDate();
        return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
    }

    function myparser(s){
        if (!s) return new Date();
        var ss = (s.split('-'));
        var y = parseInt(ss[0],10);
        var m = parseInt(ss[1],10);
        var d = parseInt(ss[2],10);
        if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
            return new Date(y,m-1,d);
        } else {
            return new Date();
        }
    }

	function removeToday(){
		if(confirm("确定要删除当天的执行日志吗？")){
			var link = "${request.contextPath}/sys/executionLog/delete?type=${type}&businessKey=${businessKey}";
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
							   window.location.reload();
						   }
					   }
				 });
		}
	}

	function removeOverdue(){
		if(confirm("确定要删除已经过期的执行日志吗？")){
			var link = "${request.contextPath}/sys/executionLog/deleteOverdue";
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
							   window.location.reload();
						   }
					   }
				 });
		}
	}
	
    function searchData(){
        document.iForm.submit();
	}	

	function paramlogs(){
		location.href="${request.contextPath}/sys/parameterLog?businessKey=${type}_${businessKey}";
	}
	
</script>
</head>
<body style="margin:1px;"> 

<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<form id="iForm" name="iForm" method="post" action="${request.contextPath}/sys/executionLog">
	<img src="${request.contextPath}/static/images/window.png">
	&nbsp;<span class="x_content_title">操作日志</span>
	&nbsp;
	<input name="btn1" type="button" value="删除当天日志" class="btnGray" onclick="javascript:removeToday();">
	&nbsp;
	<input name="btn2" type="button" value="删除过期日志" class="btnGray" onclick="javascript:removeOverdue();">
    &nbsp;&nbsp;日期：&nbsp;
	<select id="dateAfter" name="dateAfter">
		<option value="" selected>----全部----</option>
		<option value="1D">最近一天</option>
		<option value="2D">最近两天</option>
		<option value="3D">最近三天</option>
		<option value="4D">最近四天</option>
		<option value="5D">最近五天</option>
		<option value="6D">最近六天</option>
		<option value="1W">最近一周</option>
		<option value="2W">最近两周</option>
		<option value="1M">最近一月</option>
		<option value="2M">最近两月</option>
	</select>
	<script type="text/javascript">
	     document.getElementById("dateAfter").value="${dateAfter}";
	</script>
    &nbsp;&nbsp;类型：&nbsp;
	<select id="type" name="type">
		<option value="" selected>----全部----</option>
		<option value="dataset_slow">较慢的数据集</option>
		<option value="dataset_fatal_error">严重错误的数据集</option>
	</select>
	<script type="text/javascript">
	     document.getElementById("type").value="${type}";
	</script>
    &nbsp;&nbsp;
	<button type="button" id="searchButton" class="btn btnGray" style="width: 90px" 
	        onclick="javascript:searchData();">查找</button>
	</form>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>

</body>
</html>
