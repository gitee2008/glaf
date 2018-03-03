<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食物不相宜约束</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/foodRestriction/saveFoodRestriction',
				   data: params,
				   dataType: 'json',
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

	function saveAsData(){
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/foodRestriction/saveFoodRestriction',
				   data: params,
				   dataType: 'json',
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

 function switchFood(){
       var nodeId = document.getElementById("nodeId").value;
	   if(nodeId != ""){
		   var link = "${contextPath}/heathcare/foodComposition/json?nodeId="+nodeId+"&rows=1000";
		   //alert(link);
		   jQuery.getJSON(link, function(data){
			  var food = document.getElementById("restrictionFoodId");
			  food.options.length=0;
			  jQuery.each(data.rows, function(i, item){
				 food.options.add(new Option(item.name, item.id));
			  });
			});
	   }
   }

   function switchFood2(selected){
       var nodeId = document.getElementById("nodeId").value;
	   if(nodeId != ""){
		   var link = "${contextPath}/heathcare/foodComposition/json?nodeId="+nodeId+"&rows=1000";
		   //alert(link);
		   jQuery.getJSON(link, function(data){
			  var food = document.getElementById("restrictionFoodId");
			  food.options.length=0;
			  var selectedIndex = 0;
			  jQuery.each(data.rows, function(i, item){
				 if(item.id == selected){
					 selectedIndex = i;
				 }
				 food.options.add(new Option(item.name, item.id));
			  });
             food.selectedIndex = selectedIndex;
			 //alert(selectedIndex);
			});
	   }
   }

   function changeName(){
	    var name = document.getElementById("name");
        var food = document.getElementById("restrictionFoodId");
		//if(name.value == ""){
        name.value = food.options[food.selectedIndex].text;
		//}

		//var selectedItem = select.options(select.selectedIndex);//获取当前选中项 
		//selectedItem.text; //选中项的文本 
		//selectedItem.value; //选中项的值 
   }

   function searchGoods() {
	    var link = '${contextPath}/heathcare/foodComposition/searchlist';
        var x=80;
        var y=80;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 980, 500);
	}

</script>
</head>
<body>
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;" >  
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑食物不相宜约束</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${foodRestriction.id}"/>
  <input type="hidden" id="foodId" name="foodId" value="${foodComposition.id}"/>
  <table class="easyui-form" style="width:680px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">食物名称</td>
		<td align="left">
			 ${foodComposition.name}
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">不相宜食物</td>
		<td align="left">
			<select id="nodeId" name="nodeId" onchange="javascript:switchFood();">
				<option value="0">----请选择----</option>
				<#list foodCategories as cat>
				<option value="${cat.id}">${cat.name}</option>
				</#list> 
			</select>
			<script type="text/javascript">
				document.getElementById("nodeId").value="${nodeId}";
			</script>
			&nbsp;
			<select id="restrictionFoodId" name="restrictionFoodId" onchange="javascript:changeName();">
				<option value="">----请选择----</option>
				<#list foods as food>
				<option value="${food.id}">${food.name}</option>
				</#list> 
			</select>
			<script type="text/javascript">
			   document.getElementById("restrictionFoodId").value="${foodRestriction.restrictionFoodId}";
			</script>
			&nbsp;
			<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
			   onclick="javascript:searchGoods();">查找</a>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">描述</td>
		<td align="left">
		    <textarea id="description" name="description" rows="4" cols="38" class="easyui-validatebox  x-text" 
		           style="height:90px;width:385px;" >${foodRestriction.description}</textarea>
		</td>
	</tr>
	
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>