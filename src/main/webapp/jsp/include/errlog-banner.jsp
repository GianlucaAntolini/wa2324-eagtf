<%@ page contentType="text/html;charset=utf-8" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

                <c:choose>
                        <c:when test="${err0}">
                                <p>Something went wrong: password or email are wrong </p>
                        </c:when>
                        <c:when test="${err1}">
                                <p>Something went wrong: this user is banned and cannot access the site </p>
                        </c:when>
                        <c:when test="${sessionScope.verificationSuccess}">
                                <p>Verification succeded, you can now log in.</p>
                        </c:when>
                        <c:when test="${sessionScope.changePasswordError1}">
                                <p>To change your password you must insert your email.</p>
                        </c:when>
                        <c:when test="${sessionScope.pswUpdated}">
                                <p>Password updatet successfully, you can now log in.</p>
                        </c:when>
                </c:choose>