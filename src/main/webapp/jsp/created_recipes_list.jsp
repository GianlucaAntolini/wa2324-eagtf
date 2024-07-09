<%@ page contentType="text/html;charset=utf-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html lang="en">

        <c:import url="/html/include/header.html" />
        <link rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

        <body class="bg-2">
            <c:import url="/jsp/include/log-banner.jsp" />
            <main>
                <div class="flex-grow w-full pt-24 px-6 flex flex-col items-center">
                    <!-- display the message -->
                    <c:import url="/jsp/include/show-message.jsp" />

                    <!-- display the list of found recipes, if any -->
                    <c:choose>
                        <c:when test="${not empty recipesList}">
                            <div class="w-full overflow-x-auto">
                                <div class="rounded-lg">
                                    <c:forEach var="recipe" items="${recipesList}" varStatus="status">
                                        <a href="recipeDetails?recipeID=${recipe.id}" class="block 
                                        <c:if test='${status.first}'>rounded-t-lg</c:if>
                                        <c:if test='${status.last}'>rounded-b-lg</c:if>">
                                            <div class="flex items-start p-4 bg-white border-b last:border-none hover:bg-gray-100 
                                            <c:if test='${status.first}'>rounded-t-lg</c:if>
                                            <c:if test='${status.last}'>rounded-b-lg</c:if>">
                                                <img src="${recipe.getImage_url()}" alt="${recipe.name}"
                                                    onerror="this.onerror=null;this.src='media/logo/logo-full.png';"
                                                    class="w-32 h-32 object-cover rounded-md mr-4">
                                                <div class="flex-1 mr-2">
                                                    <div class="text-lg font-semibold">
                                                        <c:out value="${recipe.name}" />
                                                    </div>
                                                    <div class="text-sm text-gray-600 truncate-description">
                                                        <c:out value="${recipe.description}" escapeXml="false" />
                                                    </div>
                                                </div>
                                                <div class="flex-1 mr-2">
                                                    <div class="flex flex-col">
                                                        <div class="text-sm text-gray-600 scrollable-ing-tag">
                                                            <p>Ingredients:</p>
                                                            <ul class="flex flex-wrap">
                                                                <c:forEach items="${recipe.getIngredients()}"
                                                                    var="ingredient">
                                                                    <li
                                                                        class="bg-5 text-white px-3 py-1 rounded-full mr-2 mb-2">
                                                                        ${ingredient.getName()}</li>
                                                                </c:forEach>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="flex-1 mr-2">
                                                    <div class="flex flex-col">
                                                        <div class="text-sm text-gray-600 scrollable-ing-tag">
                                                            <p>Tags:</p>
                                                            <ul class="flex flex-wrap">
                                                                <c:forEach items="${recipe.getTags()}" var="tag">
                                                                    <li
                                                                        class="bg-5 text-white px-3 py-1 rounded-full mr-2 mb-2">
                                                                        ${tag.getName()}</li>
                                                                </c:forEach>
                                                            </ul>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div
                                                    class="flex flex-col items-end items-center my-auto justify-between">

                                                    <div class="text-xs">
                                                        <c:choose>
                                                            <c:when test="${recipe.getDifficulty() == 'hard'}">
                                                                <div class="difficulty-box hard">Hard</div>
                                                            </c:when>
                                                            <c:when test="${recipe.getDifficulty() == 'medium'}">
                                                                <div class="difficulty-box medium">Medium</div>
                                                            </c:when>
                                                            <c:when test="${recipe.getDifficulty() == 'easy'}">
                                                                <div class="difficulty-box easy">Easy</div>
                                                            </c:when>
                                                        </c:choose>
                                                    </div>

                                                    <div class="text-xs mt-1">
                                                        <p class="flex items-center"><i class="fa fa-clock-o"></i> <span
                                                                class="font-semibold ml-2">${recipe.getTime_minutes_formatted()}</span>
                                                        </p>

                                                    </div>

                                                    <div class="mt-4">
                                                        <c:choose>
                                                            <c:when test="${recipe.approved == false}">
                                                                <div class="difficulty-box hard">Rejected</div>
                                                            </c:when>
                                                            <c:when test="${recipe.approved == null}">
                                                                <div class="difficulty-box medium">To be approved</div>
                                                            </c:when>
                                                            <c:when test="${recipe.approved == true}">
                                                                <div class="difficulty-box easy">Approved</div>
                                                            </c:when>
                                                        </c:choose>
                                                    </div>

                                                </div>
                                            </div>
                                        </a>
                                    </c:forEach>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            No recipes created by yourself found in the database.
                        </c:otherwise>
                    </c:choose>
                </div>
                <br>
                <br>
            </main>
            <c:import url="/html/include/footer.html" />
        </body>

        </html>