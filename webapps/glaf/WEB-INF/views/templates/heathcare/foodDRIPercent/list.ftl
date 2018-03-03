<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>膳食营养素参考摄入量标准列表百分比</title>
<#include "/inc/init_kendoui_import.ftl"/>
<script id="template" type="text/x-kendo-template">
   <div class="toolbar">
     <#if heathcare_curd_perm == true>
      <button type="button" id="newButton"  class="k-button" style="width: 90px" 
	          onclick="javascript:addRow();">新增</button>
	  <button type="button" id="editButton"  class="k-button" style="width: 90px" 
	          onclick="javascript:updateRow();">修改</button>
	</#if>
	  <button type="button" id="viewButton"  class="k-button" style="width: 90px" 
	          onclick="javascript:viewRow();">查看</button>
   </div>
</script>
<script type="text/javascript">
	
  jQuery(function() {
      jQuery("#grid").kendoGrid({
        "columnMenu": true,
        "dataSource": {
            "schema": {
                "total": "total",
                "model": {
                    "fields": {
                        "id": {
                            "type": "string"
                        },
                        "name": {
                            "type": "string"
                        },
                        "radical": {
							"type": "number"
                        },
                        "heatEnergy": {
							"type": "number"
                        },
                        "protein": {
							"type": "number"
                        },
                        "carbohydrate": {
							"type": "number"
                        },
                        "vitaminA": {
							"type": "number"
                        },
                        "vitaminB1": {
							"type": "number"
                        },
                        "vitaminB2": {
							"type": "number"
                        },
                        "vitaminC": {
							"type": "number"
                        },
                        "carotene": {
							"type": "number"
                        },
                        "retinol": {
							"type": "number"
                        },
                        "nicotinicCid": {
							"type": "number"
                        },
                        "calcium": {
							"type": "number"
                        },
                        "iron": {
							"type": "number"
                        },
                        "zinc": {
							"type": "number"
                        },
                        "startIndex": {
                            "type": "number"
                        }
                    }
                },
                "data": "rows"
            },
            "transport": {
                "parameterMap": function(options) {
                    return JSON.stringify(options);
                },
                "read": {
		            "contentType": "application/json",
                    "type": "POST",
                    "url": "${contextPath}/rs/heathcare/foodDRIPercent/data?wordLike_base64=${wordLike_base64}&nodeId=${nodeId}"
                }
            },
	        "serverFiltering": true,
            "serverSorting": true,
            "pageSize": 10,
            "serverPaging": true
        },
        "height": x_height,
        "reorderable": true,
        "filterable": true,
        "sortable": true,
		"pageable": {
                       "refresh": true,
                       "pageSizes": [5, 10, 15, 20, 25, 50, 100, 200, 500, 1000],
                       "buttonCount": 10
                     },
		"selectable": "single",
		"toolbar": kendo.template(jQuery("#template").html()),
        "columns": [
                {
				"field": "name",
				"title": "名称",
                "width": "220px",
				"lockable": false,
				"locked": false
                },
				{
				"field": "ageGroup",
				"title": "年龄段",
                "width": "120px",
				"lockable": false,
				"locked": false
                },
				{
				"field": "typeName",
				"title": "餐点",
                "width": "150px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "heatEnergy",
				"title": "热能",
				"width": "90px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "protein",
				"title": "蛋白质",
				"width": "90px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "fat",
				"title": "脂肪",
				"width": "90px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "carbohydrate",
				"title": "碳水化合物",
				"width": "110px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "carotene",
				"title": "胡萝卜素",
				"width": "100px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "retinol",
				"title": "视黄醇",
				"width": "90px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "vitaminA",
				"title": "微生素A",
				"width": "90px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "vitaminB1",
				"title": "微生素B1",
				"width": "100px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "vitaminB2",
				"title": "微生素B2",
				"width": "100px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "vitaminC",
				"title": "微生素C",
				"width": "100px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "nicotinicCid",
				"title": "尼克酸",
				"width": "90px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "calcium",
				"title": "钙",
				"width": "90px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "iron",
				"title": "铁",
				"width": "90px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "zinc",
				"title": "锌",
				"width": "90px",
				"lockable": false,
				"locked": false
                }
	    ],
        "scrollable": {},
        "resizable": true,
        "groupable": false
    });

  });


    function addRow(){
         editRow('${contextPath}/heathcare/foodDRIPercent/edit');
    }

    function editRow(link){
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑膳食营养素参考摄入量标准百分比",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function updateRow(){
        var grid = $("#grid").data("kendoGrid");
		//alert(grid.select().length);
        if(grid.select().length == 0){
			alert("请选择其中一条记录进行修改！");
			return ;
		}
		var dataRows = grid.items();
		// 获取行号
		var rowIndex = dataRows.index(grid.select());
		// 获取行对象
		var data = grid.dataItem(grid.select());
        var link = "${contextPath}/heathcare/foodDRIPercent/edit?id="+data.id;
	    editRow(link);
	}

	function viewRow(){
        var grid = $("#grid").data("kendoGrid");
		//alert(grid.select().length);
        if(grid.select().length == 0){
			alert("请选择其中一条记录！");
			return ;
		}
		var dataRows = grid.items();
		// 获取行号
		var rowIndex = dataRows.index(grid.select());
		// 获取行对象
		var data = grid.dataItem(grid.select());
        var link = "${contextPath}/heathcare/foodDRIPercent/edit?id="+data.id;
	    editRow(link);
	}

    function createGridDataSource(link){
		var dataSource = new kendo.data.DataSource({
			type: "json",
            transport: {
            	parameterMap: function(options) {
            		options.rows = options.pageSize;
                    return JSON.stringify(options);
                },
                read: {
		            "contentType": "application/json",
                    "type": "POST",
                    "url": link
                }
            },
			serverFiltering: true,
            serverSorting: true,
            pageSize: 10,
            serverPaging: true,
            schema:{
            	total: "total",
            	data: "rows",
            	model:{
            		fields:{
                        "id": {
                            "type": "string"
                        },
                        "startIndex": {
                            "type": "number"
                        }
                    }
            	}
            }
		});
		return dataSource;
	}

    function searchData(){
		var wordLike=document.getElementById("wordLike").value;
		var link="${contextPath}/heathcare/foodDRIPercent/data?wordLike="+wordLike;
		
		$("#grid").data("kendoGrid").dataSource.read({
			wordLike : wordLike,
			nodeId : jQuery("#nodeId").val()
		});
	}

	function doSearch(){
        document.iForm.submit();
	}

 </script>
</head>
<body>
<div id="main_content" class="k-content">
<br>
 <div class="x_content_title">
  <img src="${contextPath}/static/images/window.png" alt="膳食营养素参考摄入量标准百分比列表">&nbsp;
    膳食营养素参考摄入量标准百分比列表
 </div>
<form id="iForm" name="iForm" method="post" action="${contextPath}/heathcare/foodDRIPercent">
<table>
<tr>
    <td>
	</td>
	<td>
	  名称&nbsp;<input type="text" id="wordLike" name="wordLike" value="${wordLike}" size="50" class="k-textbox">
	  &nbsp;&nbsp;
	</td>
	<td><button type="button" id="searchButton"  class="k-button" style="width: 90px" 
	            onclick="javascript:doSearch();">查找</button>
	</td>
</tr>
<tr>
	<td></td>
	<td></td>
	<td></td>
</tr>
</table>
</form>
<div id="grid"></div>
</div>
<br/>
<br/>
<br/>
<br/>
</body>
</html>