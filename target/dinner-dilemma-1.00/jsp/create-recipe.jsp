<%@ page contentType="text/html;charset=utf-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html lang="en">
        <c:import url="/html/include/header.html" />
        <script src="js/create-recipe.js"></script>

        <body class="bg-2 ">

            <c:import url="/jsp/include/log-banner.jsp" />
            <main>

                <div class="flex-grow w-full overflow-auto pt-24 px-6 flex flex-col items-center">
                    <br><br>

                    <!-- Check if the user is logged in -->
                    <c:choose>
                        <c:when test="${empty sessionScope.user_id}">
                            <!-- If not logged in, display a message -->
                            <p>You must be logged in to submit new recipes. <a href="login"
                                    class="text-blue-500">Login</a></p>
                        </c:when>
                        <c:otherwise>
                            <!-- If logged in, display the recipe submission form -->
                            <h1 class="text-4xl font-medium mb-6" style="font-family: Georgia, serif;">Create Recipe
                                Form</h1>

                            <form id="recipeForm" action="create_recipe" method="post"
                                class="flex flex-wrap w-full max-w-4xl space-y-6 mb-12">
                                <div class="w-full flex flex-wrap">
                                    <div class="w-4/5 md:w-1/3 pr-4">
                                        <div class="mb-4">
                                            <div
                                                class="w-full border-t border-gray-300 flex justify-center items-center my-4">
                                                <span class="bg-white px-3 text-sm font-bold rounded-lg">Recipe
                                                    Name</span>
                                            </div>
                                            <input type="text" placeholder="Enter recipe name" id="recipeName"
                                                name="recipeName" required
                                                class="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-9 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm h-8 p-3">
                                        </div>

                                        <div class="mb-4">
                                            <div
                                                class="w-full border-t border-gray-300 flex justify-center items-center my-4">
                                                <span class="bg-white px-3 text-sm font-bold rounded-lg">Difficulty
                                                    Level</span>
                                            </div>
                                            <select id="difficulty" name="difficulty" required
                                                class="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-9 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm h-8 p-3">
                                                <option value="">--</option>
                                                <option value="easy">Easy</option>
                                                <option value="medium">Medium</option>
                                                <option value="hard">Hard</option>
                                            </select>
                                        </div>

                                        <!--Select Tags bar-->
                                        <div class="mb-4">
                                            <!--Title-->
                                            <div
                                                class="w-full border-t border-gray-300 flex justify-center items-center my-4">
                                                <span class="bg-white px-3 text-sm font-bold rounded-lg">Select
                                                    Tags</span>
                                            </div>
                                            <!--Input field, the list attribute is set to bind the input to a datalist element-->
                                            <input type="text" placeholder="Search for tags" value="${usr_src_tag}"
                                                id="usr_src_tag" list="tags_list" size="40"
                                                class="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-9 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm h-8 p-3">
                                            <!--datalist (bind to list attribute)-->
                                            <datalist id="tags_list">
                                                <c:forEach items="${tags}" var="item">
                                                    <option data-id="${item.getId()}">${item.getName()}</option>
                                                </c:forEach>
                                            </datalist>
                                            <!-- Tags container -->
                                            <div id="tagsContainer" class="flex flex-wrap mt-2">
                                                <!-- Tags will be dynamically added here by JavaScript -->
                                            </div>
                                        </div>
                                        <!-------------------------->

                                        <div class="mb-4">
                                            <div
                                                class="w-full border-t border-gray-300 flex justify-center items-center my-4">
                                                <span class="bg-white px-3 text-sm font-bold rounded-lg">Image
                                                    URL</span>
                                            </div>
                                            <input type="text" placeholder="Enter image URL" id="image_url"
                                                name="image_url"
                                                class="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-9 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm h-8 p-3">
                                        </div>

                                        <div class="mb-4">
                                            <div
                                                class="w-full border-t border-gray-300 flex justify-center items-center my-4">
                                                <span class="bg-white px-3 text-sm font-bold rounded-lg">Time in
                                                    minutes</span>
                                            </div>
                                            <input type="number" placeholder="Enter time in minutes" id="time_minutes"
                                                step="5" min="0" name="time_minutes" required
                                                class="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-9 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm h-8 p-3">
                                        </div>
                                    </div>

                                    <div class="w-4/5 md:w-2/3 pl-4 flex flex-col">
                                        <!--Search ingredient bar-->
                                        <div class="mb-4">
                                            <!--Title-->
                                            <div
                                                class="w-full border-t border-gray-300 flex justify-center items-center my-4">
                                                <span class="bg-white px-3 text-sm font-bold rounded-lg">Select
                                                    Ingredients</span>
                                            </div>
                                            <!--Input field, the list attribute is set to bind the input to a datalist element-->
                                            <input type="text" placeholder="Search for ingredients"
                                                value="${usr_src_ing}" id="usr_src_ing" list="ings_list" size="40"
                                                class="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-9 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm h-8 p-3">
                                            <!--datalist (bind to list attribute)-->
                                            <datalist id="ings_list">
                                                <c:forEach items="${ingredients}" var="item">
                                                    <option data-id="${item.getId()}">${item.getName()}</option>
                                                </c:forEach>
                                            </datalist>
                                            <!-- Ingredients container -->
                                            <div id="ingredientsContainer" class="flex flex-wrap mt-2">
                                                <!-- Ingredients will be dynamically added here by JavaScript -->
                                            </div>
                                        </div>
                                        <!-------------------------->

                                        <div class="mb-4">
                                            <div
                                                class="w-full border-t border-gray-300 flex justify-center items-center my-4">
                                                <span class="bg-white px-3 text-sm font-bold rounded-lg">New
                                                    Ingredients</span>
                                            </div>
                                            <input type="text" placeholder="Enter new ingredients separated by commas"
                                                id="newIngredients" name="newIngredients"
                                                class="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-9 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm h-8 p-3">
                                        </div>

                                        <div class="mb-4 flex-grow">
                                            <div
                                                class="w-full border-t border-gray-300 flex justify-center items-center my-4">
                                                <span class="bg-white px-3 text-sm font-bold rounded-lg">Recipe
                                                    Instructions</span>
                                            </div>
                                            <textarea id="comment" name="comment" rows="4" cols="50"
                                                placeholder="Enter recipe instructions" required
                                                class="placeholder:italic placeholder:text-slate-400 block bg-white w-full border border-slate-300 rounded-md py-2 pl-9 pr-3 shadow-sm focus:outline-none focus:border-sky-500 focus:ring-sky-500 focus:ring-1 sm:text-sm h-32 p-3 resize-none"></textarea>
                                        </div>

                                        <div class="w-full flex justify-end mt-6">
                                            <input type="submit" value="Submit Recipe"
                                                class="bg-4 text-white text-sm px-4 rounded-lg hover:bg-[#2a1b1b] h-8 px-3 mt-4">
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </main>
            <c:import url="/html/include/footer.html" />
        </body>

        </html>