<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  ~ Copyright (c) 2010. Gridshore
  ~ Licensed under the Apache License, Version 2.0 (the "License");
  ~ you may not use this file except in compliance with the License.
  ~ You may obtain a copy of the License at
  ~
  ~ http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~ Unless required by applicable law or agreed to in writing, software
  ~ distributed under the License is distributed on an "AS IS" BASIS,
  ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~ See the License for the specific language governing permissions and
  ~ limitations under the License.
  --%>
<form:form commandName="order">
    <form:errors path="*" cssClass="errorBox"/>
    <form:hidden path="companyId"/>
    <form:hidden path="companyName"/>
    <table>
        <tr>
            <td><spring:message code="order.price"/>:</td>
            <td><form:input path="itemPrice"/></td>
            <td><form:errors path="itemPrice" cssClass="errorBox"/></td>
        </tr>
        <tr>
            <td><spring:message code="order.tradeCount"/>:</td>
            <td><form:input path="tradeCount"/></td>
            <td><form:errors path="tradeCount" cssClass="error"/></td>
        </tr>
        <tr>
            <td colspan="3">
                <input class="btn primary" type="submit" name="submit" value="Place Order"/>
                <input class="btn" type="reset" name="reset" value="Reset"/>
                <a href="/company/<c:out value="${order.companyId}"/>" class="btn">Cancel</a>
            </td>
        </tr>
    </table>
</form:form>
