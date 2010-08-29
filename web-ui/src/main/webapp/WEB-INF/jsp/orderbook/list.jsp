<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<ul>
<c:forEach items="${items}" var="item">
        <li><c:out value='${item.tradeItemName}'/> &nbsp;<a href="${ctx}/orderbook/<c:out value='${item.identifier}'/>">orders</a></li>
</c:forEach>
</ul>