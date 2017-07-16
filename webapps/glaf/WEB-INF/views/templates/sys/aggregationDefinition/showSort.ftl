<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>节点列表</title>
<#include "/inc/init_kendoui_import.ftl"/>
<script type="text/javascript">

     $(document).ready(function() {
		$("#sortable").kendoSortable({ 
			change: function(e) {
				//alert("from " + e.oldIndex + " to " + e.newIndex);
			}
		});
        $("#sortable").kendoSortable({
                        handler: ".handler",
                        hint:function(element) {
                            return element.clone().addClass("hint");
                        }
        });
    });


    function saveData(){
		var sortable = $("#sortable").kendoSortable({ 
				filter: ">div label"
			}).data("kendoSortable");
        var items = sortable.items();
		var length = items.length;
		var str = "";
		for(var i=0; i<length; i++){
			str +=items[i].id+",";
		}
		//alert(str);
		jQuery.ajax({
				   type: "POST",
				   url: "${contextPath}/sys/aggregationDefinition/saveSort?serviceKey=${serviceKey}&items="+str,
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

</script>
</head>
<body>

        <div id="example">

            <div id="pages">
                <div id="pages-title"><span>节点列表</span></div>
                <div id="sortable">
				<#list  list as item>
				  <#if item.locked == 0>
                    <div class="item">
                        <span class="handler" title="选中后拖到指定位置">&nbsp;</span>
                        <label id="${item.id}" title="选中后拖到指定位置">${item.title}</label>
                    </div>
				  </#if>
				</#list> 
               </div>
			  <center>
			  <input type="button" class="btnGray" value="确定" onclick="javascript:saveData();">
			  <br>
			  </center>

            <style>
                #example {
                    -webkit-user-select: none;
                    -moz-user-select: none;
                    -ms-user-select: none;
                    user-select: none;
                }

                #pages {
                    margin: 5px auto;
                    width: 300px;
                    background-color: #f3f5f7;
                    border-radius: 4px;
                    border: 1px solid rgba(0,0,0,.1);
                }

                #pages-title {
                    height: 10px;
                }

                #pages-title span {
                    display: none;
                }

                .item {
                    margin: 2px;
                    padding: 0 10px 0 0;
                    min-width: 200px;
                    background-color: #fff;
                    border: 1px solid rgba(0,0,0,.1);
                    border-radius: 3px;
                    font-size: 1.3em;
                    line-height: 2.5em;
                }

                .handler {
                    display: inline-block;
                    width: 30px;
                    margin-right: 10px;
                    border-radius: 3px 0 0 3px;
                    background: url('${contextPath}/static/images/handle.png') no-repeat 50% 50% #52aef7;
                }

                .handler:hover {
				    color:#333;
                    background-color: #e62757;
                }

                .placeholder {
                    width: 298px;
                    border: 1px solid #2db245;
                }

                .hint {
                    border: 2px solid #2db245;
                    border-radius: 6px;
                }

                .hint .handler {
                    background-color: #2db245;
                }
            </style>
        </div>

</body>
</html> 