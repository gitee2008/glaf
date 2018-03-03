<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱模板</title>
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
				url: '${contextPath}/heathcare/dietaryTemplate/json?season=${season}&typeId=${typeId}&nameLike_enc=${nameLike_enc}&selected=${selected}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        <#if elementId?exists>
						{title:'选择',field: 'chk', width: 60, align: 'center', formatter: formatterKey},
						</#if>
				        {title:'序号', field:'startIndex', width:60, sortable:false},
						{title:'功能键',field:'funkey2', width:90, align:"center", formatter: formatterKeys},
						{title:'名称',field:'name', width:200, align:"left"},
						{title:'餐点',field:'typeName', width:130, align:"center"},
						{title:'日期',field:'dayOfWeekName', width:60, align:"center"},
						{title:'季节',field:'seasonName', width:60, align:"center"},
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
						{title:'功能键',field:'funkey', width:90, align:"center", formatter: formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100,200],
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
		if (row.checked == 1) {
			var s = '<input name="isCheck" type="radio" checked="checked" onclick="javascript:selectedRx(\''+row.id+'\',\''+row.name+'\')"/> ';
			return s;
		} else {
		    var s = '<input name="isCheck" type="radio" onclick="javascript:selectedRx(\''+row.id+'\',\''+row.name+'\')"/> ';
			return s;
		}
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
	    var link = '${contextPath}/heathcare/dietaryItem/datalist?templateId='+row.templateId;
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


</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north',split:false,border:true" style="height:42px"  class="toolbar-backgroud"> 
    <div style="margin:4px;">
	<form id="iForm" name="iForm" method="post" action="">
	<input type="hidden" id="elementId" name="elementId" value="${elementId}">
	<input type="hidden" id="elementName" name="elementName" value="${elementName}">
	<input type="hidden" id="selected" name="selected" value="${selected}">
    <table>
    <tr>
	    <td>
		<img src="${contextPath}/static/images/window.png">
	     &nbsp;<span class="x_content_title">食谱库列表</span>
		</td>
		<td>
		   名称&nbsp;
		   <input id="nameLike" name="nameLike" type="text" class="x-searchtext"  
	              style="width:185px;" value="${nameLike}">
		</td>
		<td>
		    &nbsp;季节&nbsp;
		    <select id="season" name="season">
				<option value="">----请选择----</option>
				<option value="1">春季</option>
				<option value="2">夏季</option>
				<option value="3">秋季</option>
				<option value="4">冬季</option>
			</select>
			<script type="text/javascript">
				document.getElementById("season").value="${season}";
			</script>
	    </td>
		<td>
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
		</td>
		<td>
		    <button type="button" id="searchButton" class="btn btnGrayMini" style="width: 90px" 
	                onclick="javascript:searchData();">查找</button>
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