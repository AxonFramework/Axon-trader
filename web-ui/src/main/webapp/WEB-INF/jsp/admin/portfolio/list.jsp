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
    <script>
        $(function () {
            $("table#available-users").tablesorter({ sortList:[
                [0, 0]
            ] });
        });
    </script>
</head>
<body>
<content tag="title">All portfolios</content>
<content tag="tagline">Choose the portfolio to watch the details for</content>
<p>You can sort the table by clicking on the headers.</p>
<table class="zebra-striped" id="available-users">
    <thead>
    <tr>
        <th>Name</th>
        <th>Money available</th>
        <th>Items available</th>
        <th>&nbsp;</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${portfolios}" var="portfolio">
        <tr>
            <td><c:out value='${portfolio.userName}'/></td>
            <td><c:out value='${portfolio.amountOfMoney}'/></td>
            <td>
                <c:forEach items="${portfolio.itemsInPossession}" var="item">
                    <c:out value="${item.value.companyName}"/>&nbsp;
                </c:forEach>
            </td>
            <td><a href="${ctx}/admin/portfolio/<c:out value='${portfolio.identifier}'/>">details</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
