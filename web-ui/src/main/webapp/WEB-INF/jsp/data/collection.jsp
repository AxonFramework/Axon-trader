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
    <title>Initialize MongoDB</title>
</head>
<body>
<content tag="title">MongoDB collection : <c:out value="${collectionName}"/></content>
<content tag="tagline">Available collections in this Mongo instance.</content>
<content tag="breadcrumb">
    <ul class="breadcrumb">
        <li><a href="/">Home</a> <span class="divider">/</span></li>
        <li><a href="/data/collections">Collections</a> <span class="divider">/</span></li>
        <li class="active">${collectionName}</li>
    </ul>
</content>

<div class="pagination">
    <ul>
        <c:choose>
            <c:when test="${page == 1}">
                <li class="prev disabled"><a>&larr; Previous</a></li>
            </c:when>
            <c:otherwise>
                <li class="prev"><a href="?page=${page-1}">&larr; Previous</a></li>
            </c:otherwise>
        </c:choose>
        <c:if test="${page - 2 > 0}">
            <li><a href="?page=${page - 2}">${page - 2}</a></li>
        </c:if>
        <c:if test="${page - 1 > 0}">
            <li><a href="?page=${page - 1}">${page - 1}</a></li>
        </c:if>
        <li class="active"><a>${page}</a></li>
        <c:if test="${page + 1 <= numPages}">
            <li><a href="?page=${page + 1}">${page + 1}</a></li>
        </c:if>
        <c:if test="${page + 2 <= numPages}">
            <li><a href="?page=${page + 2}">${page + 2}</a></li>
        </c:if>
        <c:choose>
            <c:when test="${page == numPages}">
                <li class="next disabled"><a>Next &rarr;</a></li>
            </c:when>
            <c:otherwise>
                <li class="next"><a href="?page=${page+1}">Next &rarr;</a></li>
            </c:otherwise>
        </c:choose>
    </ul>
</div>
<p>Number of pages : <c:out value="${numPages}"/></p>
<table>
    <c:forEach items="${items}" var="item">
        <tr>
            <td>
                <table class="zebra-striped">
                    <c:forEach items="${item}" var="it">
                        <tr>
                            <td>${it.key}</td>
                            <td><c:out value="${it.value}" escapeXml="true"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
