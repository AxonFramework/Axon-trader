<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
  <head>
    <title><decorator:title /></title>
  </head>
  <body>
    <div id="header">
       <a href="${ctx}/orderbook">Order book</a> &nbsp; <a href="${ctx}/tradeitem">Trade item</a>
    </div>
    <div id="main">
      <decorator:body />
    </div>
  </body>
</html>
