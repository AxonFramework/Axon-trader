<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="hor-minimalist-b">
    <thead>
    <tr>
        <th>Name</th>
        <th>actions</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${items}" var="item">
        <tr>
            <td><c:out value='${item.tradeItemName}'/></td>
            <td><a href="${ctx}/orderbook/<c:out value='${item.identifier}'/>">orders</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>