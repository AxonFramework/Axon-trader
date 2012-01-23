<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title><decorator:title/></title>
    <meta name="description" content="Website contaning the Axon sample using a trader application">
    <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js" type="text/javascript"></script>
    <![endif]-->
    <link rel="stylesheet" href="${ctx}/style/bootstrap-1.4.0.min.css">
    <link rel="stylesheet" href="${ctx}/style/main.css"/>
    <script type="text/javascript" src="${ctx}/js/jquery-1.6.4.min.js"></script>

    <decorator:head/>
</head>
<body>
<div class="topbar">
    <div class="fill">
        <div class="container">
            <a class="brand" href="${ctx}/">Axon Trader</a>
            <ul class="nav">
                <li class="active"><a href="${ctx}/">Home</a></li>
                <li><a href="${ctx}/company">Companies</a></li>
                <li><a href="${ctx}/data/collections">Data</a></li>
                <li><a href="${ctx}/admin/portfolio">Portfolio</a></li>
            </ul>
            <sec:authorize access="isAuthenticated()">
                <p class="pull-right credentials">
                    <sec:authentication property="principal.fullName"/>
                    &nbsp;&nbsp;<a href="${ctx}/j_spring_security_logout">logout</a>
                </p>
            </sec:authorize>
            <sec:authorize access="isAnonymous()">
                <form action="<c:url value='j_spring_security_check'/>" class="pull-right" method="POST">
                    <input class="input-small" type="text" placeholder="Username" name='j_username'
                           value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/>
                    <input class="input-small" type="password" placeholder="Password" name='j_password'/>
                    <button class="btn" type="submit">Sign in</button>
                </form>
            </sec:authorize>
        </div>
    </div>
</div>

<div class="container">
    <decorator:getProperty property="page.herounit"/>

    <div class="content">
        <div class="page-header">
            <h1><decorator:getProperty property="page.title"/>
                <small><decorator:getProperty property="page.tagline"/></small>
            </h1>
        </div>
        <decorator:getProperty property="page.breadcrumb"/>
        <div class="row">
            <div class="span14">
                <decorator:body/>
            </div>
        </div>
    </div>

    <footer>
        <p>&copy; Gridshore 2011</p>
    </footer>

</div>
<!-- /container -->
</body>
</html>
