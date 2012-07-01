<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%--
  ~ Copyright (c) 2012. Axon Framework
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
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">

<head>
    <script type="text/javascript" src="${ctx}/js/sockjs-0.2.1.min.js"></script>
    <script type="text/javascript" src="${ctx}/js/vertxbus-1.1.0-final.js"></script>
</head>
<body>

<div>
    <input type="button" id="connectButton" value="Open connection"/><br>
    <input type="button" id="closeButton" value="Close connection"/><br>
    Connection Status:&nbsp;
    <div id="connectionStatus">Not connected</div>
</div>
<div id="lastUpdate"></div>
<div id="trades">
    <table class="table table-striped" id="tradesTable">
        <thead>
        <tr>
            <th>Company</th>
            <th>#items</th>
            <th>Price</th>
        </tr>
        </thead>
        <tbody></tbody>
    </table>
</div>

<%-- The script for sockjs --%>
<script type="text/javascript">
    var eb = null;

    function subscribe() {
        if (eb) {
            eb.registerHandler("updates.trades", function (msg, replyTo) {
                var results = msg.tradeExecuted;
                $('#tradesTable tbody').prepend(
                        "<tr><td>" + results.companyName +
                                "</td><td>" + results.count +
                                "</td><td>" + results.price + "</td></tr>");
                $('#lastUpdate').text(" " + new Date());
            });
        }
    }

    function closeConn() {
        if (eb) {
            eb.close();
        }
    }

    function openConn() {
        if (!eb) {
            eb = new vertx.EventBus("${externalServerurl}");

            eb.onopen = function () {
                $("#connectionStatus").text("Connected");
                subscribe();
            };

            eb.onclose = function () {
                $("#connectionStatus").text("Not connected");
                eb = null;
            };
        }
    }

    $(document).ready(function () {
        $("#closeButton").click(function () {
            closeConn();
        });

        $("#connectButton").click(function () {
            openConn();
        });
    });

</script>

</body>
</html>