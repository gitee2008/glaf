<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.glaf.base.modules.sys.model.*"%>
<%@ page import="com.glaf.core.config.SystemConfig"%>
<%
  SysUser user = com.glaf.base.utils.RequestUtil.getLoginUser(request);
  if (user != null) {
%>
<script type="text/javascript">
   location.href="<%=request.getContextPath()%>/my/home";
</script>
<% } else {%>
<script type="text/javascript">
	location.href="<%=request.getContextPath()%>/login";
</script>
<%
  }
%>