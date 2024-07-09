<%@ page contentType="text/html;charset=utf-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <!DOCTYPE html>
    <html>
    <c:import url="/html/include/header.html" />

    <body class="bg-2 flex flex-col h-screen items-center">
      <c:import url="/jsp/include/log-banner.jsp" />
      <div class="flex-grow w-full overflow-auto pt-24 px-6 flex flex-col items-center">
        <br>
        <br>
        <c:import url="/jsp/include/errpsw-banner.jsp" />
        <form action="changePsw" method="post" class="flex flex-col items-center">
          <label for="passwd1" class="text-sm font-bold">New Password</label>
          <input type="password" id="passwd1" name="passwd1" required
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-60 h-8 p-3 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
          <label for="passwd2" class="pt-6 text-sm font-bold">Repeat Password</label>
          <input type="password" id="passwd2" name="passwd2" required
            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-60 h-8 p-3 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
          <input type="submit" value="Change Password"
            class="bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-8 px-3 mt-4">
        </form>
      </div>
      <c:import url="/html/include/footer.html" />

    </body>

    </html>