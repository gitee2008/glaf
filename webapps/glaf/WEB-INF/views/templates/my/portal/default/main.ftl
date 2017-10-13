<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>${sys_title}</title>
</head>
<frameset rows="62,*,30" cols="*" frameborder="no" border="0" framespacing="0">
  <frame src="${contextPath}/my/main/top?menu=true" name="topFrame" scrolling="no" noresize="noresize" 
         id="topFrame" title="topFrame" />
  <frameset cols="187,*" frameborder="no" border="0" framespacing="0">
    <frame src="${contextPath}/my/main/left" name="leftFrame" scrolling="no" noresize="noresize" 
	       id="leftFrame" title="leftFrame" />
    <frame src="${contextPath}/my/main/content?menu=true" name="rightFrame" id="rightFrame" title="rightFrame" />
  </frameset>
  <frame src="${contextPath}/my/main/footer" name="bottomFrame" scrolling="no" noresize="noresize" 
         id="bottomFrame" title="bottomFrame" />
</frameset>
<noframes>
<body>
</body>
</noframes>
</html>
