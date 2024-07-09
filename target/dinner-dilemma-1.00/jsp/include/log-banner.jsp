<%@ page contentType="text/html;charset=utf-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    <div
      class="log-banner fixed top-0 w-full bg-1 text-black justify-between items-center mx-auto flex py-3 px-6 font-semibold text-white h-20">
      <div></div> <!-- space for the logo -->
      <img src="media/logo/logo-full.png" class="logo-fixed" onclick="window.location.replace('home');">

      <c:choose>
        <c:when test="${role_id >= 0}">
          <div class="space-x-6 flex justify-between items-center">
            <p>
              <a class="hover:underline" href="usr_control">User Control Panel</a>
            </p>
            <p>
              <a class="hover:underline" href="logout">Logout</a>
            </p>
          </div>
        </c:when>
        <c:otherwise>
          <div class="space-x-6 flex justify-between items-center">
            <p>
              <a class="hover:underline" href="login">Login</a>
            </p>
            <p>
              <a class="hover:underline" href="register">Register</a>
            </p>
          </div>
        </c:otherwise>
      </c:choose>
    </div>