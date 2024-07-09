<%@ page contentType="text/html;charset=utf-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html lang="en">

        <c:import url="/html/include/header.html" />

        <body class="bg-2 flex flex-col h-screen items-center">
            <c:import url="/jsp/include/log-banner.jsp" />
            <div class="flex-grow pt-24 px-6 flex flex-col items-center w-full">
                <br>
                <br>

                <h1 class="text-xl font-bold">User Control Panel</h1>

                <a href="usr-data"
                    class="chevron-hover bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-9 py-2 px-6 mt-3">User
                    Data</a>

                <a href="create_recipe"
                    class="chevron-hover bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-9 py-2 px-6 mt-3">Create
                    Recipe</a>

                <a href="search_created_recipes"
                    class="chevron-hover bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-9 py-2 px-6 mt-3">Created
                    Recipes</a>

                <a href="search_liked_recipes"
                    class="chevron-hover bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-9 py-2 px-6 mt-3">Liked
                    Recipes</a>

                <c:if test="${session != null && role_id == 0}">
                    <hr class="w-full border-t border-brown-300 my-6" />

                    <h1 class="text-lg font-bold">Admin Section</h1>
                    <hr class="w-full border-t border-brown-300 my-6" />

                    <a href="recipe-approval"
                        class="chevron-hover bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-9 py-2 px-6 mt-3">Recipes
                        Approval</a>

                    <a href="recipe-recovery-or-removal"
                        class="chevron-hover bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-9 py-2 px-6 mt-3">Recipes
                        Recovery/Removal</a>

                    <form action="search-user" method="post" class="flex flex-col items-center mt-6">
                        <label for="email" class="text-sm font-bold">Input the user's email:</label>
                        <input type="text" id="email" name="email"
                            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-60 h-8 p-3 mt-3 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500">
                        <input type="submit" value="Search User"
                            class="chevron-hover bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-9 py-2 px-6 mt-3">
                    </form>
                    <hr class="w-full border-t border-gray-300 my-6" />
                </c:if>

                <!-- <h1 class="text-sm font-bold mt-6">Logs</h1>
        <a href="/my-logs/dinner-dilemma.log"
            class="bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-8 px-6 mt-3">Your Logs</a> -->

                <br>
                <br>
            </div>
            <c:import url="/html/include/footer.html" />
        </body>

        </html>