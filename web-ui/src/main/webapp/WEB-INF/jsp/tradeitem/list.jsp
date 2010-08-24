<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<ul>
<c:forEach items="${items}" var="item">
        <li><c:out value='${item.name}'/>&nbsp<a href="${ctx}/tradeitem/buy/<c:out value='${item.identifier}'/>">buy</a>
            &nbsp<a href="${ctx}/tradeitem/sell/<c:out value='${item.identifier}'/>">sell</a></li>
</c:forEach>
</ul>