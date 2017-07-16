<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<br>
<%
  out.println(session.getId());
  //System.out.println(session.getId());
%>
<%
 
  out.println("<br>");
  out.println(request.getRemoteAddr());

  out.println("<br>");
  out.println("x-real-ip:"+request.getHeader("x-real-ip"));
  out.println("<br>");
  out.println("x-forwarded-for:"+request.getHeader("x-forwarded-for"));
  out.println("<br>");
  out.println("Proxy-Client-IP:"+request.getHeader("Proxy-Client-IP"));
  out.println("<br>");
  out.println("WL-Proxy-Client-IP:"+request.getHeader("WL-Proxy-Client-IP"));
  out.println("<br>");
%>
<script type="text/javascript">
   window.open("<%=request.getContextPath()%>/my/main");
</script>