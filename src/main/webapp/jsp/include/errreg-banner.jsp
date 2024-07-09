<%@ page contentType="text/html;charset=utf-8" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

                <c:if test="${err}">
                        <p>A user already signed with this email </p>
                </c:if>