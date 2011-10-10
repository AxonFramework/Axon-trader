<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

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
<content tag="title">Buy order for : <c:out value='${order.tradeItemName}'/></content>
<content tag="tagline">Enter items to buy and for how much</content>
<content tag="breadcrumb">
    <ul class="breadcrumb">
        <li><a href="/">Home</a> <span class="divider">/</span></li>
        <li><a href="/tradeitem">Trade Items</a> <span class="divider">/</span></li>
        <li><a href="/tradeitem/<c:out value='${order.tradeItemId}'/>"><c:out value='${order.tradeItemName}'/></a> <span
                class="divider">/</span></li>
        <li class="active">Buy</li>
    </ul>
</content>

<jsp:include page="form-include.jsp"/>