<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table id="hor-minimalist-b">
    <thead>
    <tr>
        <th>Type</th>
        <th>Count</th>
        <th>Price</th>
        <th>Remaining</th>
        <th>User</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${orderBook.orders}" var="order">
        <tr>
            <td><c:out value='${order.type}'/></td>
            <td><c:out value='${order.tradeCount}'/></td>
            <td><c:out value='${order.itemPrice}'/></td>
            <td><c:out value='${order.itemsRemaining}'/></td>
            <td><c:out value='${order.userId}'/></td>
        </tr>
    </c:forEach>
    </tbody>
</table>