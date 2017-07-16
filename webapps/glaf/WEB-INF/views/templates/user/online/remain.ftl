<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="refresh" content="${timeoutSeconds}"/>
<title>用户在线</title>
<script type="text/javascript" src="${contextPath}/static/scripts/jquery.min.js"></script>
<script type="text/javascript">
	function remainOnline(){
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/user/online/doRemain',
				   dataType:  'json',
				   error: function(data){
					    
				   },
				   success: function(data){
					   
				   }
			 });
	}
</script>
</head>
<body onload="javascript:remainOnline();">
</body>
</html>