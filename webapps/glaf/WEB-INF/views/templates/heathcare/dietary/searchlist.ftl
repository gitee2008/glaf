<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱列表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

    var x_height = Math.floor(window.screen.height * 0.68);
	var x_width = Math.floor(window.screen.width * 0.80);

	if(window.screen.height <= 768){
        x_height = Math.floor(window.screen.height * 0.66);
	}

	if(window.screen.width < 1200){
        x_width = Math.floor(window.screen.width * 0.82);
	} else if(window.screen.width > 1280){
        x_width = Math.floor(window.screen.width * 0.72);
	}  


   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1950,
				height:x_height,
				fit:false,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/heathcare/dietary/json?year=${year}&week=${week}&fullDay=${fullDay}&typeId=${typeId}&nameLike_enc=${nameLike_enc}',
				remoteSort: false,
				idField: 'id',
				columns:[[
						//{field:'ck', checkbox:true},
				        {title:'序号',field:'startIndex', width:60, sortable:false},
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
						{title:'状态',field:'purchaseFlag', width:120, align:"center", formatter:formatterStatus},
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
		var s = '<input name="check" type="checkbox" value=\"'+row.id+'\" "/> ';
		return s;
	}

	function formatterStatus(val, row) {
		if(val =="Y"){
			return "已加入采购";
		}
		return "未处理";
	}

    function selectedRx(id, name){
		//alert(id+"->"+name);
		var parent_window = getOpener();
		//alert(parent_window);
	    var elementId = parent_window.document.getElementById("${elementId}");
        var elementName = parent_window.document.getElementById("${elementName}");
		elementId.value = id;
	    elementName.value = name;
	}

    var id = undefined;//公共变量

    //触发单元格事件
    function onClickCell(rowIndex, field, value) {
        var row = $("#tt").datagrid('selectRow', rowIndex);//返回触发单元格的行标
        var r1 = $("#tt").datagrid('getSelected');//返回被选中的行
        id = r1.id;//返回该行的id
    }


	function formatterKeys(val, row){
		var str = "<a href='javascript:addDietary(\""+row.id+"\",\""+row.name+"\");'><img src='${contextPath}/static/images/statistics.png' border='0'>加入</a>";
	    return str;
	}

	function addDietary(id, name){
         if(confirm("确定将"+name+"加入食谱表中吗？")){
            jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/addDietary?templateId='+id,
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
					       //window.parent.location.reload();
					   } 
				   }
			 });
		 }
	}

	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/dietaryItem/datalist?dietaryId='+row.id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "食品组成列表",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['698px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function searchData(){
       document.iForm.submit();
	}


	function parchasePlan(){ 
		var checkedItems = $('#mydatagrid').datagrid('getChecked'); 
		var rowIds = []; 
		jQuery.each(checkedItems, function(index, item){  
			if(item.day > 0){
			    rowIds.push(item.id); 
			}
		});                 
		//alert(rowIds.join(","));
		if(rowIds.join(",").length > 0){
           if(confirm("确定将选中的食谱中需要的物品加入采购单中吗？")){
			  if(confirm("物品加入采购单中后将结单且不能再修改及删除，确实要加入采购计划并结单吗？")){
               var params = jQuery("#iForm").formSerialize();
		       jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/addParchasePlan?objectIds='+rowIds.join(","),
				   data: params,
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
					       window.parent.location.reload();
					   } 
				   }
			   });
			  }
		   }
		} else {
			alert("请选择确定日期的食谱。");
		}
	}

	function addParchasePlan(){
         if(confirm("确定将选中日期的食谱中需要的物品加入采购单中吗？")){
			 if(confirm("物品加入采购单中后将结单且不能再修改及删除，确实要加入采购计划并结单吗？")){
               var params = jQuery("#iForm").formSerialize();
		       jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/addParchasePlan',
				   data: params,
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
					       window.parent.location.reload();
					   } 
				   }
			   });
		   }
		 }
	}

	function addDailyParchasePlan(){
         if(confirm("确定将选中日期的食谱中需要的物品加入采购单中吗？")){
			 if(confirm("物品加入采购单中后将结单且不能再修改及删除，确实要加入采购计划并结单吗？")){
               var params = jQuery("#iForm").formSerialize();
		       jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/addDailyParchasePlan',
				   data: params,
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
					       window.parent.location.reload();
					   } 
				   }
			   });
		   }
		 }
	}

</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north',split:false,border:true" style="height:42px"  class="toolbar-backgroud"> 
    <div style="margin:4px;">
	<form id="iForm" name="iForm" method="post" action="">
    <table>
    <tr>
	    <td>
		<img src="${contextPath}/static/images/window.png">
	     &nbsp;<span class="x_content_title">食谱列表</span>
		</td>
		<td>
		   名称&nbsp;
		   <input id="nameLike" name="nameLike" type="text" class="x-searchtext"  
	              style="width:125px;" value="${nameLike}">
		</td>
		<!-- <td>
		    &nbsp;餐点&nbsp;
			<select id="typeId" name="typeId">
				<option value="">----请选择----</option>
				<#list dictoryList as d>
				<option value="${d.id}">${d.name}</option>
				</#list> 
		    </select>
		    <script type="text/javascript">
			   document.getElementById("typeId").value="${typeId}";
		    </script>
		</td> -->
		<td>&nbsp;年份&nbsp;
		    <select id="year" name="year">
				<#list years as year>
				<option value="${year}">${year}</option>
				</#list>
			</select>
			<script type="text/javascript">
				document.getElementById("year").value="${year}";
			</script>
	    </td>
		<td>
		    &nbsp;周次&nbsp;
		    <select id="week" name="week">
			    <option value="0">--请选择--</option>
				<#list weeks as week>
			    <option value="${week}">${week}</option>
			    </#list> 
			</select>
			<script type="text/javascript">
				document.getElementById("week").value="${week}";
			</script>
	    </td>
		<td>
		    &nbsp;日期&nbsp;
		    <select id="fullDay" name="fullDay">
			    <option value="0">--请选择--</option>
				<#list items as item>
			    <option value="${item.value}">${item.name}</option>
			    </#list> 
			</select>
			<script type="text/javascript">
				document.getElementById("fullDay").value="${fullDay}";
			</script>
	    </td>
		<td>
		    <button type="button" id="searchButton" class="btn btnGrayMini" style="width: 60px" 
	                onclick="javascript:searchData();">查找</button>
			<#if fullDay gt 0>
			<button type="button" id="butonCheck" class="btn btnGrayMini" style="width: 135px" 
                    onclick="javascript:addDailyParchasePlan();" >加入采购计划</button>
		    </#if>
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