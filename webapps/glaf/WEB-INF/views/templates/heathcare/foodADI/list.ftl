<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>每日食物参考摄入量列表</title>
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
                        "lowest": {
							"type": "number"
                        },
                        "average": {
							"type": "number"
                        },
						"highest": {
							"type": "number"
                        },
                        "ageGroup": {
                            "type": "string"
                        },
                        "type": {
                           "type": "string"
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
                    "url": "${contextPath}/rs/heathcare/foodADI/data"
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
				"field": "ageGroup",
				"title": "年龄段",
                "width": "120px",
				"lockable": false,
				"locked": false
                },
				{
				"field": "typeName",
				"title": "餐点",
                "width": "120px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "categoryName",
				"title": "类别",
                "width": "150px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "name",
				"title": "食物种类",
                "width": "150px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "lowest",
				"title": "最低推荐量",
				"width": "120px",
				"lockable": false,
				"locked": false
                },
                {
				"field": "average",
				"title": "平均值",
				"width": "120px",
				"lockable": false,
				"locked": false
                },
				{
				"field": "highest",
				"title": "最高推荐量",
				"width": "120px",
				"lockable": false,
				"locked": false
                },
		    {
			"command": [
			<#if heathcare_curd_perm == true>
			   {
                "text": "修改",
                "name": "edit",
                "click": function showDetails(e) {
								var dataItem = this.dataItem(jQuery(e.currentTarget).closest("tr"));
								//alert(JSON.stringify(dataItem));
								//alert(dataItem.id);
								var link = "${contextPath}/heathcare/foodADI/edit?id="+dataItem.id;
								editRow(link);
		           }
                },
			</#if>
			   {
                "text": "查看",
                "name": "view",
                "click": function showDetails(e) {
								var dataItem = this.dataItem(jQuery(e.currentTarget).closest("tr"));
								//alert(JSON.stringify(dataItem));
								//alert(dataItem.id);
								var link = "${contextPath}/heathcare/foodADI/edit?id="+dataItem.id;
								editRow(link);
		           }
                }]
          }
	],
        "scrollable": {},
        "resizable": true,
        "groupable": false
    });
  });


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
        var link = "${contextPath}/heathcare/foodADI/edit?id="+data.id;
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
        var link = "${contextPath}/heathcare/foodADI/edit?id="+data.id;
	    editRow(link);
	}

    function addRow(){
         editRow('${contextPath}/heathcare/foodADI/edit');
    }

    function editRow(link){
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑每日食物参考摄入量信息",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['620px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

 </script>
</head>
<body>
<div id="main_content" class="k-content">
<br>
 <div class="x_content_title">
  <img src="${contextPath}/static/images/window.png" alt="每日食物参考摄入量列表">&nbsp;
    每日食物参考摄入量列表
 </div>
<br>
<div id="grid"></div>
</div>
<br/>
<br/>
<br/>
<br/>
</body>
</html>