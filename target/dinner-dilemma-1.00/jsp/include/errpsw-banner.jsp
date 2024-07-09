<%@ page contentType="text/html;charset=utf-8" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

                <c:choose>
                        <c:when test="${diffPsw}">
                                <p>The two passwords are not the same</p>
                        </c:when>
                        <c:when test="${err1}">
                                <p>Password must have at least 8 characters</p>
                        </c:when>
                </c:choose>