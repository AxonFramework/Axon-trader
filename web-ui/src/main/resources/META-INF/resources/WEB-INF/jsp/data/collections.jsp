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
    <title>MongoDB collections</title>
</head>
<body>
<content tag="title">MongoDB collections</content>
<content tag="tagline">Available collections in this Mongo instance.</content>
<content tag="breadcrumb">
    <ul class="breadcrumb">
        <li><a href="${ctx}/">Home</a> <span class="divider">/</span></li>
        <li class="active">Collections</li>
    </ul>
</content>

<p>The collections</p>
<ul>
    <c:forEach items="${collections}" var="collection">
        <li><a href="${ctx}/data/collection/${collection}">${collection}</a></li>
    </c:forEach>
</ul>
</body>
</html>
