<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学生检查及巡视信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>

.subject { font-size: 13px; text-decoration: none; font-weight:normal; font-family:"宋体"}
.table-border { background-color:#eaf2ff; height: 32px; font-family:"宋体"}
.table-head { background-color:#5cb1f8; height: 30px; font-weight:bold; font-size: 13px; font-family:"宋体"}
.table-content { background-color:#ffffff; height: 28px; font-size: 12px; font-family:"宋体"}


</style>
<script type="text/javascript">
  <#if privilege_write == true>
	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personInspection/saveAll?gradeId=${gradeId}',
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
					       
					   } 
				   }
			 });
	}
  </#if>

    function submitReq(){
        document.getElementById("iForm").submit();
    }

    function changeXY(personId){
	    var status_ = document.getElementById("status_"+personId).value;
	    if(status_ == 0){
            jQuery("#div_remark_"+personId).hide();
			jQuery("#div_treat_"+personId).hide();
	    } else {
		    jQuery("#div_remark_"+personId).show();
			jQuery("#div_treat_"+personId).show();
	    }
    }

    jQuery(function(){
        jQuery('#inspectionDate').datebox().datebox('calendar').calendar({
                validator: function(date){
                    var now = new Date();
                    var d1 = new Date(now.getFullYear(), now.getMonth(), now.getDate() - 180);
                    var d2 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
                    return d1<=date && date<=d2;
                }
        });
    });

</script>
</head>

<body>
<div style="margin:0px;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:40px;margin-top:0px;"> 
    <div class="toolbar-backgroud"> 
	<span class="x_content_title">&nbsp;&nbsp;${gradeInfo.name}检查及巡视信息</span>
	<#if privilege_write == true>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:saveData();" > 保 存 
	</a> 
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post" action="${contextPath}/heathcare/personInspection">
  <input type="hidden" id="gradeId" name="gradeId" value="${gradeId}"/>
  <input type="hidden" id="type" name="type" value="${type}"/>
  <table cellspacing="1" cellpadding="4" width="95%" nowrap align="center">
    <tbody>
	  <tr>
	  <td width="20%" align="left">&nbsp;</td>
	  <td width="20%" align="left">&nbsp;</td>
	  <td width="60%" align="right">
	     日期&nbsp;
		 <input id="inspectionDate" name="inspectionDate" type="text"  
		        class="easyui-datebox x-text" style="width:100px;"
				data-options="onSelect:submitReq"
			    <#if inspectionDate?if_exists>
				value="${inspectionDate?string('yyyy-MM-dd')}"
				</#if>>
		 &nbsp;阶段&nbsp;
		 <select id="section" name="section" onchange="javascript:submitReq();">
			<option value="am">晨间检查</option>
			<option value="pm">午间巡视</option>
			<option value="day">全日观察</option>
	     </select>
		 <script type="text/javascript">
		     document.getElementById("section").value="${section}";  
		 </script>
	  </td>
	  </tr>
	</tbody>
  </table>
 
  <table class="table-border" cellspacing="1" cellpadding="4" width="95%" nowrap align="center">
    <thead>
	  <tr>
	  <td class="table-head" width="20%" align="left">姓名</td>
	  <td class="table-head" width="20%" align="left">检查情况</td>
	  <td class="table-head" width="60%" align="left">处理及备注</td>
	  </tr>
	</thead>
    <tbody>
	<#list persons as person>
	<tr>
		<td  class="table-content" align="left"> ${person.name} </td>
		<td  class="table-content" align="left">
            <select id="status_${person.id}" name="status_${person.id}" onchange="javascript:changeXY('${person.id}');">
				<option value="0">正常</option>
				<option value="1">异常</option>
            </select>
			<script type="text/javascript">
		     document.getElementById("status_${person.id}").value="${person.status}";  
		 </script>
		</td>
		<td  class="table-content" align="left">
		   <div id="div_remark_${person.id}"
		   <#if person.status == 0>
			style="display:none"
			<#else>
			style="display:block"
			</#if>
		    >
			处理情况&nbsp;
			<input id="treat_${person.id}" name="treat_${person.id}" style="width:250px;" 
				   class="easyui-validatebox  x-text"
				   value="${person.treat}" >
			<br><br>
			备&nbsp;&nbsp;注&nbsp;
		    <input id="remark_${person.id}" name="remark_${person.id}" style="width:250px;" 
				   class="easyui-validatebox  x-text"
				   value="${person.memo}" > 
		   </div>
		</td>
	</tr>
	</#list>
    </tbody>
  </table>
 </form>
</div>
</div>
<br>
<br>
<br>
<br>
</body>
</html>