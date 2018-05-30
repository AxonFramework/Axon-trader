<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
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

<p>You need to login to access this part of the site. Please provided your username and password</p>

<c:if test="${not empty param.login_error}">
    <div class="alert-message error">
        <p>
            <strong>Your login attempt was not successful, try again.</strong>
        </p>

        <p>
            Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
        </p>
    </div>
</c:if>

<form name="f" action="<c:url value='j_spring_security_check'/>" method="POST" class="form-stacked">
    <fieldset>
        <legend>Login to get access</legend>
        <div class="clearfix">
            <label for="j_username">Username</label>

            <div class="input">
                <input type='text' id="j_username" name='j_username'
                       value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'/>
            </div>
        </div>
        <div class="clearfix">
            <label for="j_password">Password</label>

            <div class="input">
                <input type='password' id="j_password" name='j_password'/>
            </div>
        </div>
        <div class="clearfix">
            <label>
                <input type="checkbox" name="_spring_security_remember_me">
                <span>Don't ask for my password for two weeks</span>
            </label>
        </div>
        <div class="actions">
            <input name="submit" type="submit" class="btn primary">
            <input name="reset" type="reset" class="btn">
        </div>
    </fieldset>

    <tr>
        <td colspan='2'></td>
    </tr>
    </table>

</form>
