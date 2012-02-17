<%@include file="../../include.jsp" %>
<%--
  ~ Copyright (c) 2010-2012. Axon Framework
  ~
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
    <title>Users</title>
    <script type="text/javascript" src="${ctx}/js/jquery.tablesorter.min.js"></script>
</head>
<body>
<content tag="title">Profile detail : <c:out value="${portfolio.userName}"/></content>
<content tag="tagline">Here you can add money and items to the portfolio.</content>
<h2>Money</h2>

<div class="row">
    <div class="span6">
        <div class="row">
            <div class="span3">Available</div>
            <div class="span3"><c:out value="${portfolio.amountOfMoney}"/></div>
        </div>
        <div class="row">
            <div class="span3">Reserved</div>
            <div class="span3"><c:out value="${portfolio.reservedAmountOfMoney}"/></div>
        </div>
    </div>
    <div class="span8">
        <form method="GET" action="${ctx}/admin/portfolio/<c:out value="${portfolio.identifier}"/>/money/">
            <input name="amount" value="0" class="span2">
            <input type="submit" class="btn" value="Add Money">
        </form>
    </div>
</div>
<h2>Items</h2>

<div class="row">
    <div class="span6">
        <h3>In possession</h3>
        <ul class="unstyled">
            <c:forEach var="item" items="${portfolio.itemsInPossession}">
                <li><c:out value="${item.value.companyName}"/>:&nbsp<c:out value="${item.value.amount}"/></li>
            </c:forEach>
        </ul>
        <h3>Reserved</h3>
        <ul class="unstyled">
            <c:forEach var="item" items="${portfolio.itemsReserved}">
                <li><c:out value="${item.value.companyName}"/>:&nbsp<c:out value="${item.value.amount}"/></li>
            </c:forEach>
        </ul>
    </div>
    <div class="span8">
        <form method="GET" action="${ctx}/admin/portfolio/<c:out value="${portfolio.identifier}"/>/item/">
            <select name="orderbook" class="span3">
                <c:forEach items="${orderbooks}" var="orderbook">
                    <option value="<c:out value="${orderbook.identifier}"/>"><c:out
                            value="${orderbook.companyName}"/></option>
                </c:forEach>
            </select>
            <input name="amount" value="0" class="span2"/>
            <input type="submit" class="btn" value="Add Items">
        </form>
    </div>
</div>
</body>
</html>
