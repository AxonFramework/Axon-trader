<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<table>
    <tr>
        <th>Name</th>
        <th>Value</th>
        <th># Shares</th>
        <th>actions</th>
    </tr>
    <c:forEach items="${items}" var="item">
        <tr>
            <td><c:out value='${item.name}'/></td>
            <td><c:out value='${item.value}'/></td>
            <td><c:out value='${item.amountOfShares}'/></td>
            <td>
                <c:if test="${item.tradeStarted}">
                    <a href="${ctx}/tradeitem/buy/<c:out value='${item.identifier}'/>">buy</a>
                &nbsp<a href="${ctx}/tradeitem/sell/<c:out value='${item.identifier}'/>">sell</a>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>
