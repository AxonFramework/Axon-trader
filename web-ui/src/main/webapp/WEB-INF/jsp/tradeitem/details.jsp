<%@include file="../include.jsp" %>
<html>
<head>
    <title>Trade item details</title>
</head>
<body>
<div id="tradeItemDetails">
    <span class="detailTitle"><c:out value="${tradeItem.name}"/></span>
    <span class="detailLabel">Value : </span>
    <span><c:out value="${tradeItem.value}"/></span>
    <span class="detailLabel"># Shares : </span>
    <span><c:out value="${tradeItem.amountOfShares}"/></span>
</div>
<div id="actions">
    <c:if test="${tradeItem.tradeStarted}">
        <a href="${ctx}/tradeitem/buy/<c:out value='${tradeItem.identifier}'/>">buy</a>
        &nbsp<a href="${ctx}/tradeitem/sell/<c:out value='${tradeItem.identifier}'/>">sell</a>
    </c:if>
</div>
<div id="orderBook">
    <div id="sellOrders">
        <h3>Sell Orders</h3>
        <table class="hor-minimalist-b">
            <thead>
            <tr>
                <th>Count</th>
                <th>Price</th>
                <th>Remaining</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${sellOrders}" var="order">
                <tr>
                    <td><c:out value='${order.tradeCount}'/></td>
                    <td><c:out value='${order.itemPrice}'/></td>
                    <td><c:out value='${order.itemsRemaining}'/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div id="buyOrders">
        <h3>Buy Orders</h3>
        <table class="hor-minimalist-b">
            <thead>
            <tr>
                <th>Count</th>
                <th>Price</th>
                <th>Remaining</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${buyOrders}" var="order">
                <tr>
                    <td><c:out value='${order.tradeCount}'/></td>
                    <td><c:out value='${order.itemPrice}'/></td>
                    <td><c:out value='${order.itemsRemaining}'/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>