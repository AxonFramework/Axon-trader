<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

Sell : <c:out value='${identifier}'/>

<form:form commandName="order">
    <form:errors/>
    <form:hidden path="tradeItemId"/>
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
                  <input type="submit" value="Save Changes" />
              </td>
          </tr>
      </table>
</form:form>