<%@include file="../include.jsp"%>
<html>
<head>
    <title>Trade items</title>
</head>
<body>
<table class="hor-minimalist-b">
    <thead>
    <tr>
        <th>Name</th>
        <th>Value</th>
        <th># Shares</th>
        <th>&nbsp;</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${items}" var="item">
        <tr>
            <td><c:out value='${item.name}'/></td>
            <td><c:out value='${item.value}'/></td>
            <td><c:out value='${item.amountOfShares}'/></td>
            <td><a href="${ctx}/tradeitem/<c:out value='${item.identifier}'/>">details</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
