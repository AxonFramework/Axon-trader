<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<p>Sell order for : <c:out value='${order.tradeItemName}'/></p>

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
                  <input type="submit" value="Place Order" />
              </td>
          </tr>
      </table>
</form:form>