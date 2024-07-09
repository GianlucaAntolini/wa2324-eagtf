<%@ page contentType="text/html;charset=utf-8" %>
  <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

    <!DOCTYPE html>
    <html>
    <c:import url="/html/include/header.html" />

    <body class="bg-2">
      <c:import url="/jsp/include/log-banner.jsp" />
      <main>
        <div class="flex-grow overflow-auto pt-24 px-6 flex flex-col items-center w-full">
          <br />
          <br />

          <c:import url="/jsp/include/errreg-banner.jsp" />
          <form action="register" method="post" class="flex flex-col items-center">
            <label for="email" class="text-sm font-bold">Email</label>
            <input type="text" id="email" name="email" required
              class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-60 h-8 p-3 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" />

            <label for="name" class="text-sm font-bold mt-4">Name</label>
            <input type="text" id="name" name="name"
              class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-60 h-8 p-3 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" />

            <label for="surname" class="text-sm font-bold mt-4">Surname</label>
            <input type="text" id="surname" name="surname"
              class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-60 h-8 p-3 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" />

            <label for="passwd" class="text-sm font-bold mt-4">Password</label>
            <input type="password" id="passwd" name="passwd" required
              class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-60 h-8 p-3 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" />

            <label for="cpasswd" class="text-sm font-bold mt-4">Confirm Password</label>
            <input type="password" id="cpasswd" name="cpasswd" required
              class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-60 h-8 p-3 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500" />

            <!-- <select id="role" name="role">
            <option value="base">User</option>
            <option value="admin">Admin</option>
        </select> -->
            <br />
            <input type="submit" value="Register"
              class="bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-8 px-3 mt-4" />
          </form>
        </div>
        <br />
        <br />
      </main>
      <c:import url="/html/include/footer.html" />
    </body>

    </html>