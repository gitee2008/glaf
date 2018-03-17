<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物品采购登记列表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

    function getLink(){
	    var link_ = "${contextPath}/heathcare/goodsPurchase/reviewJson?tenantId=${tenantId}&startTime=${startTime}&endTime=${endTime}";
		//alert(link_);
	    return link_;
	}

    jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:980,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/heathcare/goodsPurchase/reviewJson?tenantId=${tenantId}&startTime=${startTime}&endTime=${endTime}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				    {title:'序号', field:'startIndex', width:60, sortable:false},
					{title:'物品名称', field:'goodsName', width:220, align:'left', sortable:true, formatter:formatterName},
					{title:'重量(千克)', field:'quantity', width:120, align:'right', sortable:true},
					{title:'单价', field:'price', width:120, align:'right', sortable:true},
					{title:'总价', field:'totalPrice', width:120, align:'right', sortable:true},
					{title:'计量单位', field:'unit', width:100, align:'center', formatter:formatterUnit},
					{title:'采购日期', field:'purchaseTime', width:100, align:'center'},
					{title:'确认人', field:'confirmName', width:100, align:'center'},
					{title:'确认时间', field:'confirmTime', width:100, align:'center'},
					{title:'状态', field:'businessStatus', width:100, align:'center', formatter:formatterStatus}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
				pagePosition: 'both'
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


	function formatterStatus(val, row){
		if(val == 9){
           return "<font color='green'>已确认</font>";   
		}
        return "未确认";   
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
   <div data-options="region:'north',split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;">
	<form id="iForm" name="iForm" method="post">
	 <table>
		<tr>
		  <td>
			<img src="${contextPath}/static/images/window.png">
			&nbsp;<span class="x_content_title">物品采购登记列表</span>
		  </td>
		  <td>
			  &nbsp;日期&nbsp;开始&nbsp;
			  <input id="startTime" name="startTime" type="text" class="easyui-datebox x-text" style="width:100px;height:23px"
					 <#if startTime?exists> value="${startTime}"</#if>>
			  &nbsp;结束&nbsp;
			  <input id="endTime" name="endTime" type="text" class="easyui-datebox x-text" style="width:100px;height:23px"
					 <#if endTime?exists> value="${endTime}"</#if>>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
			     onclick="javascript:doSearch();">查找</a>
		  </td>
		  <td>
			
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