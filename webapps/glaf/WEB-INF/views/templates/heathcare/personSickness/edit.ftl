<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>疾病信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personSickness/savePersonSickness',
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
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personSickness/savePersonSickness',
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


	function switchInfectious(){
		flag = jQuery("#infectiousFlag").val();
        if(flag == 'Y'){
           jQuery("#infectiousDiseaseDiv").show();
		} else {
           jQuery("#infectiousDiseaseDiv").hide();
		   jQuery("#infectiousDisease").val("");
		}
	}


	function switchInfectious2(flag){
        if(flag == 'Y'){
           jQuery("#infectiousDiseaseDiv").show();
		   //alert("show");
		} else {
           jQuery("#infectiousDiseaseDiv").hide();
		   jQuery("#infectiousDisease").val("");
		}
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:40px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	&nbsp;<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;编辑疾病信息</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${personSickness.id}"/>
  <input type="hidden" id="gradeId" name="gradeId" value="${gradeId}"/>
  <table class="easyui-form" style="width:800px;margin-top:5px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">姓名</td>
		<td align="left">
		   <#if person?exists>
             ${person.name}
			 <input type="hidden" id="personId" name="personId" value="${personId}"/>
		   <#else>
			<select id="personId" name="personId">
			    <option value="">--请选择--</option>
				<#list persons as person>
			    <option value="${person.id}">${person.name}</option>
			    </#list> 
			</select>
			</#if>
		</td>
	</tr> 
	<tr>
		<td width="20%" align="left">病名</td>
		<td align="left">
            <input id="sickness" name="sickness" type="text" 
			       class="easyui-validatebox  x-text"  
			       style="width:480px"
				   value="${personSickness.sickness}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">是否传染性疾病</td>
		<td align="left">
		    <select  id="infectiousFlag" name="infectiousFlag" onchange="javascript:switchInfectious();">
				<option value="">----请选择----</option>
				<option value="Y">是</option>
				<option value="N">否</option>
		    </select>
			<script type="text/javascript">
			     document.getElementById("infectiousFlag").value="${personSickness.infectiousFlag}";
			</script>
			<span id="infectiousDiseaseDiv" style="display:none;">
              <select id="infectiousDisease" name="infectiousDisease">
				<option value="">----请选择----</option>
				<option value="hand-foot-and-mouth">手足口病</option>
				<option value="chicken-pox">水痘</option>
				<option value="mumps">流行性腮腺炎</option>
				<option value="scarlatina">猩红热</option>
				<option value="acute-hemorrhagic-conjunctivitis">急性出血性结膜炎</option>
				<option value="dysentery">痢疾</option>
				<option value="measles">麻疹</option>
				<option value="rubella">风疹</option>
				<option value="catarrhal-jaundice">传染性肝炎</option>
				<option value="other">其他</option>
		      </select>
			  <script type="text/javascript">
			     document.getElementById("infectiousDisease").value="${personSickness.infectiousDisease}";
			  </script>
			</span>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">发病时间</td>
		<td align="left">
			<input id="discoverTime" name="discoverTime" type="text" 
			       class="easyui-datebox x-text"
			       style="width:100px"
				   <#if personSickness.discoverTime?exists>
				   value="${personSickness.discoverTime?string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">确诊时间</td>
		<td align="left">
			<input id="confirmTime" name="confirmTime" type="text" 
			       class="easyui-datebox x-text"
			       style="width:100px"
				   <#if personSickness.confirmTime?exists>
				   value="${personSickness.confirmTime?string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">报告时间</td>
		<td align="left">
			<input id="reportTime" name="reportTime" type="text" 
			       class="easyui-datebox x-text"
			       style="width:100px"
				   <#if personSickness.reportTime?exists>
				   value="${personSickness.reportTime?string('yyyy-MM-dd')}"
				   </#if>/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">报告何处</td>
		<td align="left">
            <input id="reportOrg" name="reportOrg" type="text" 
			       class="easyui-validatebox  x-text"  
			       style="width:480px"
				   value="${personSickness.reportOrg}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">报告责任人</td>
		<td align="left">
            <input id="reportResponsible" name="reportResponsible" type="text" 
			       class="easyui-validatebox  x-text"  
			       style="width:180px"
				   value="${personSickness.reportResponsible}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">报告形式</td>
		<td align="left">
            <select name="reportWay" 
			        class="easyui-combobox" multiple="true" multiline="false" 
			        style="width:180px;height:30px;">
                 <option value="Tel" <#if personSickness.reportWay?index_of('Tel')!=-1>selected</#if>>电话</option>
                 <option value="Card" <#if personSickness.reportWay?index_of('Card')!=-1>selected</#if>>报告卡</option>
                 <option value="Report" <#if personSickness.reportWay?index_of('Report')!=-1>selected</#if>>报表</option>
			</select>
		</td>
	</tr>
	 

    <tr>
		<td width="20%" align="left">管理部门意见</td>
		<td align="left">
		    <textarea id="supervisionOpinion" name="supervisionOpinion" type="text" 
			          class="easyui-validatebox  x-text"  
			          style="width:480px;height:90px">${personSickness.supervisionOpinion}</textarea>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">县（区）CDC接报人</td>
		<td align="left">
            <input id="receiver1" name="receiver1" type="text" 
			       class="easyui-validatebox  x-text"  
			       style="width:180px"
				   value="${personSickness.receiver1}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">县（区）CDC</td>
		<td align="left">
            <input id="receiveOrg1" name="receiveOrg1" type="text" 
			       class="easyui-validatebox  x-text"  
			       style="width:480px"
				   value="${personSickness.receiveOrg1}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">县（区）教育局接报人</td>
		<td align="left">
            <input id="receiver2" name="receiver2" type="text" 
			       class="easyui-validatebox  x-text"  
			       style="width:180px"
				   value="${personSickness.receiver2}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">县（区）教育局</td>
		<td align="left">
            <input id="receiveOrg2" name="receiveOrg2" type="text" 
			       class="easyui-validatebox  x-text"  
			       style="width:480px"
				   value="${personSickness.receiveOrg2}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">就诊日期</td>
		<td align="left">
			<input id="clinicTime" name="clinicTime" type="text" 
			       class="easyui-datebox x-text"
			       style="width:100px"
				   <#if personSickness.clinicTime?exists>
				   value="${personSickness.clinicTime?string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">就诊医院</td>
		<td align="left">
            <input id="hospital" name="hospital" type="text" 
			       class="easyui-validatebox  x-text"  
			       style="width:480px"
				   value="${personSickness.hospital}"/>
		</td>
	</tr>
    <tr>
		<td width="20%" align="left">主要症状</td>
		<td align="left">
		    <textarea id="symptom" name="symptom" type="text" 
			          class="easyui-validatebox  x-text"  
			          style="width:480px;height:90px">${personSickness.symptom}</textarea>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">治疗情况</td>
		<td align="left">
		    <textarea id="treat" name="treat" type="text" 
			          class="easyui-validatebox  x-text"  
			          style="width:480px;height:90px">${personSickness.treat}</textarea>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">排查结果</td>
		<td align="left">
            <input id="result" name="result" type="text" 
			       class="easyui-validatebox  x-text"  
			       style="width:480px"
				   value="${personSickness.result}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">备注</td>
		<td align="left">
            <textarea id="remark" name="remark" 
			          class="easyui-validatebox  x-text"  
			          style="width:480px;height:120px">${personSickness.remark}</textarea>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left"></td>
		<td align="left">
            <br><br><br><br>
		</td>
	</tr>
 
   </tbody>
  </table>
 </form>
 <script type="text/javascript">
    switchInfectious2('${personSickness.infectiousFlag}');
 </script>
 <br>
 <br>
</div>
</div>
</body>
</html>