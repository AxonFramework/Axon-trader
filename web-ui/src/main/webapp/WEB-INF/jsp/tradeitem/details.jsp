<%@include file="../include.jsp" %>
<%--
  ~ Copyright (c) 2010. Gridshore
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~     http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>

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
    <div class="list">
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
    <div class="list">
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
    <div class="list">
        <h3>Executed trades</h3>
        <table class="hor-minimalist-b">
            <thead>
            <tr>
                <th>Count</th>
                <th>Price</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${executedTrades}" var="trade">
                <tr>
                    <td><c:out value='${trade.tradeCount}'/></td>
                    <td><c:out value='${trade.tradePrice}'/></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

</div>
</body>
</html>