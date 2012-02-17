<%@include file="../include.jsp" %>
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
<content tag="title">All users</content>
<content tag="tagline">Choose the user to watch the details for</content>
<p>You can sort the table by clicking on the headers.</p>
<table class="zebra-striped" id="available-users">
    <thead>
    <tr>
        <th>Name</th>
        <th>username</th>
        <th>&nbsp;</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${items}" var="item">
        <tr>
            <td><c:out value='${item.name}'/></td>
            <td><c:out value='${item.username}'/></td>
            <td><a href="${ctx}/user/<c:out value='${item.identifier}'/>">details</a></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
