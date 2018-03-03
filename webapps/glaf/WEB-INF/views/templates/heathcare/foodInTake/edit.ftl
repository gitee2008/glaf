<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>进食量登记表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function saveData(){
		jQuery("#foodNodeId").val(jQuery("#nodeId"));
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/foodInTake/saveFoodInTake',
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

	function saveAsData(){
		document.getElementById("id").value="";
		jQuery("#foodNodeId").val(jQuery("#nodeId"));
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/foodInTake/saveFoodInTake',
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


   function switchFood(){
       var nodeId = document.getElementById("nodeId").value;
	   if(nodeId != ""){
		   var link = "${contextPath}/heathcare/foodComposition/json?nodeId="+nodeId+"&rows=1000";
		   //alert(link);
		   jQuery.getJSON(link, function(data){
			  var food = document.getElementById("foodId");
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
			  var food = document.getElementById("foodId");
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
  <div data-options="region:'north',split:false,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;编辑进食量登记表</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${foodInTake.id}"/>
  <input type="hidden" id="foodNodeId" name="foodNodeId">
  <table class="easyui-form" style="width:680px;" align="center">
   <tbody>
   <#if gradeInfos?exists>
   <tr>
		<td width="20%" align="left">班级</td>
		<td align="left">
		  <select id="gradeId" name="gradeId">
			<option value="">----请选择----</option>
			<#list gradeInfos as d>
			<option value="${d.id}">${d.name}</option>
			</#list> 
		  </select>
		  <script type="text/javascript">
			   document.getElementById("gradeId").value="${foodInTake.gradeId}";
		  </script>
		</td>
	</tr>
	</#if>
	<#if foodCategories?exists>
	<tr>
    <td width="20%" align="left">选用食物&nbsp;</td>
    <td align="left">
	    <select id="nodeId" name="nodeId" onchange="javascript:switchFood();">
			<option value="0">----请选择----</option>
			<#list foodCategories as cat>
			<option value="${cat.id}">${cat.name}</option>
			</#list> 
		</select>
		<script type="text/javascript">
	        document.getElementById("nodeId").value="${foodInTake.foodNodeId}";
	    </script>
		&nbsp;
		<select id="foodId" name="foodId" onchange="javascript:changeName();">
			<option value="">----请选择----</option>
			<#list foods as food>
			<option value="${food.id}">${food.name}</option>
			</#list> 
		</select>
		<script type="text/javascript">
	       document.getElementById("foodId").value="${foodInTake.foodId}";
	    </script>
		&nbsp;
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
	       onclick="javascript:searchGoods();">查找</a>
    </td>
   </tr>
   <#else>
   <tr>
    <td width="20%" align="left">选用食物</td>
    <td align="left">
	    ${foodInTake.foodName}
	</td>
   </tr>
   </#if>
   <tr>
		<td width="20%" align="left">餐点类型</td>
		<td align="left">
		  <select id="typeId" name="typeId">
			<option value="0">----请选择----</option>
			<#list dictoryList as d>
			<option value="${d.id}">${d.name}</option>
			</#list> 
		  </select>
		  <script type="text/javascript">
			   document.getElementById("typeId").value="${foodInTake.typeId}";
		  </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">用餐时间</td>
		<td align="left">
			<input id="mealTime" name="mealTime" type="text" 
			       class="easyui-datebox x-text" style="width:100px; text-align:center;"
			       <#if foodInTake.mealTime?exists>
				   value="${foodInTake.mealTime ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">就餐人数</td>
		<td align="left">
			<input id="person" name="person" type="text" 
			       class="easyui-numberbox x-text" 
				   increment="10" style="width:80px; text-align:right;" 
				   value="${foodInTake.person}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">分配熟食量</td>
		<td align="left">
			<input id="allocationWeight" name="allocationWeight" type="text"
			       class="easyui-numberbox  x-text"  precision="2" style="width:80px; text-align:right;"
				   value="${foodInTake.allocationWeight}"/>&nbsp;(单位：克g)
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">剩余熟食量</td>
		<td align="left">
			<input id="remainWeight" name="remainWeight" type="text"
			       class="easyui-numberbox  x-text"  precision="2" style="width:80px; text-align:right;"
				   value="${foodInTake.remainWeight}"/>&nbsp;(单位：克g)
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">摄入生重量</td>
		<td align="left">
			<input id="mealWeight" name="mealWeight" type="text"
			       class="easyui-numberbox  x-text"  precision="2" style="width:80px; text-align:right;"
				   value="${foodInTake.mealWeight}"/>&nbsp;(单位：克g)
		</td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>