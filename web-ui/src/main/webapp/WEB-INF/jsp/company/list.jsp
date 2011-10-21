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
    <title>Companies</title>
    <script type="text/javascript" src="${ctx}/js/jquery.tablesorter.min.js"></script>
    <script>
        $(function() {
            $("table#available-stock").tablesorter({ sortList: [
                [0,0]
            ] });
        });
    </script>
</head>
<body>
<content tag="title">All stock items</content>
<content tag="tagline">Choose the stock to start trading with</content>
<content tag="breadcrumb">
    <ul class="breadcrumb">
        <li><a href="/">Home</a> <span class="divider">/</span></li>
        <li class="active">Companies</li>
    </ul>
</content>

<p>You can sort the table by clicking on the headers.</p>
<table class="zebra-striped" id="available-stock">
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
            <td><a href="${ctx}/company/<c:out value='${item.identifier}'/>">details</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
