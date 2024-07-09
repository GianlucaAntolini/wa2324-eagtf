<%@ page contentType="text/html;charset=utf-8" %>
        <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

                <c:choose>
                        <c:when
                                test="${sessionScope.verificationError1 || sessionScope.verificationError2 || sessionScope.changePasswordError1}">
                                <div class="red-alerts fixed">
                                        <c:choose>
                                                <c:when test="${sessionScope.verificationError1}">
                                                        <div id="verificationError1"
                                                                class="p-5 red-alert mb-4 text-sm text-red-900 rounded-lg bg-red-200 opacity-90"
                                                                role="alert">
                                                                <span class="font-medium">
                                                                        Verification failed because of incorrect or
                                                                        expired code, please try again.
                                                                </span>
                                                                <span class="font-mono cursor-pointer float-right mr-5">
                                                                        x
                                                                </span>
                                                        </div>
                                                </c:when>
                                        </c:choose>
                                        <c:choose>
                                                <c:when test="${sessionScope.verificationError2}">
                                                        <div id="verificationError2"
                                                                class="p-5 red-alert mb-4 text-sm text-red-900 rounded-lg bg-red-200 opacity-90"
                                                                role="alert">
                                                                <span class="font-medium">
                                                                        Verification failed because of expired session,
                                                                        please try again.
                                                                </span>
                                                                <span class="font-mono cursor-pointer float-right mr-5">
                                                                        x
                                                                </span>
                                                        </div>
                                                </c:when>
                                        </c:choose>
                                        <c:choose>
                                                <c:when test="${sessionScope.changePasswordError1}">
                                                        <div id="changePasswordError1"
                                                                class="p-5 red-alert mb-4 text-sm text-red-900 rounded-lg bg-red-200 opacity-90"
                                                                role="alert">
                                                                <span class="font-medium">
                                                                        Change of user password failed because of
                                                                        expired session, please try again.
                                                                </span>
                                                                <span class="font-mono cursor-pointer float-right mr-5">
                                                                        x
                                                                </span>
                                                        </div>
                                                </c:when>
                                        </c:choose>
                                </div>
                                <script src="js/home.js"></script>
                        </c:when>
                </c:choose>