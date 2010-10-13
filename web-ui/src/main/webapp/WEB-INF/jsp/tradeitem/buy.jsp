<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

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

<p>Buy order for : <c:out value='${order.tradeItemName}'/></p>

<form:form commandName="order">
    <form:errors path="*" cssClass="errorBox"/>
    <form:hidden path="tradeItemId"/>
    <form:hidden path="tradeItemName"/>
    <table>
          <tr>
              <td>Price:</td>
              <td><form:input path="itemPrice" /></td>
          </tr>
          <tr>
              <td>Trade count:</td>
              <td><form:input path="tradeCount" /></td>
          </tr>
          <tr>
              <td colspan="2">
                  <input type="submit" name="submit" value="Place Order" />
              </td>
          </tr>
      </table>
</form:form>