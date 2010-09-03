<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title><decorator:title/></title>
    <link rel="stylesheet" href="${ctx}/style/main.css"/>
    <link rel="stylesheet" href="${ctx}/style/redmond/jquery-ui-1.8.4.custom.css"/>
    <script type="text/javascript" src="${ctx}/js/jquery-1.4.2.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery-ui-1.8.4.custom.min.js"></script>
    <script type="text/javascript">
        $(function() {
            $('#tabs').tabs({
                select: function(event, ui) {
                    var url = $.data(ui.tab, 'load.tabs');
                    if (url) {
                        location.href = url;
                        return false;
                    }
                    return true;
                }
            });

            $('#primaryNavigation ul li.current').removeClass('current');
            var loc = window.location.pathname;
            if (loc.indexOf('/tradeitem') > -1) {
                $('#primaryNavigation ul li.tradeitem').addClass('current');
            } else {
                $('#primaryNavigation ul li.home').addClass('current');
            }

        });
    </script>
</head>
<body>
<div id="header">
    <div id="primaryNavigation">
        <ul>
            <li class="home"><a href="/"><span>Home</span></a></li> <%-- TODO jettro : Make use of ctx and make it work --%>
            <li class="tradeitem"><a href="${ctx}/tradeitem"><span>Trade item</span></a></li>
        </ul>
    </div>
</div>
<div id="main">
    <decorator:body/>
</div>
</body>
</html>
