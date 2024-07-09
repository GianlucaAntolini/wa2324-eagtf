<%@ page contentType="text/html;charset=utf-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <!DOCTYPE html>
    <html>
    <c:import url="/html/include/header.html" />

    <body class="bg-2">
      <c:import url="/jsp/include/log-banner.jsp" />
      <main>
        <div class="flex-grow w-full overflow-auto pt-24 px-6 flex flex-col items-center">
          <br />
          <br />

          <c:import url="/jsp/include/errlog-banner.jsp" />

          <form action="login" method="post" class="flex flex-col items-center">
            <label for="email" class="text-sm font-bold">Email</label>
            <input type="text" id="email" name="email" required
              class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-60 h-8 p-3 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" />

            <label for="passwd" class="text-sm font-bold mt-4">Password</label>
            <input type="password" id="passwd" name="passwd" required
              class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-60 h-8 p-3 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" />

            <input type="submit" value="Login"
              class="bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-8 px-3 mt-4" />
          </form>

          <br />
          <br />
          <h1 class="text-sm italic">Forgot password?</h1>

          <form action="changePsw" method="post" class="flex flex-col items-center">
            <label for="email_change" class="text-sm font-bold">Email</label>
            <input type="text" id="email_change" name="email_change" required
              class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-60 h-8 p-3 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" />

            <input type="submit" value="Change Password"
              class="bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-8 px-3 mt-4" />
          </form>
        </div>
        <br />
        <br />
      </main>
      <c:import url="/html/include/footer.html" />
    </body>

    </html>