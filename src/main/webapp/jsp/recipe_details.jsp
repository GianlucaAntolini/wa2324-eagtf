<%@ page contentType="text/html;charset=utf-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html lang="en">

        <c:import url="/html/include/header.html" />

        <link rel="stylesheet"
            href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

        <script>
            const recipe = '${recipe.getId()}';
            var like = '${liked}' == 'true';
            var nLikes = parseInt('${recipe.getNLikes()}');
        </script>

        <body class="bg-2 pt-24 min-h-screen flex flex-col">
            <c:import url="/jsp/include/log-banner.jsp" />

            <c:if test="${recipe == null}">
                <div class="flex-grow flex items-center justify-center">
                    <h1 class="text-3xl font-bold text-fg-1">Recipe not found</h1>
                </div>
            </c:if>

            <c:if test="${recipe != null}">
                <div class="w-full mx-auto p-6">
                    <div class="flex justify-between items-start">
                        <h1 class="text-4xl font-bold mb-4">${recipe.getName()}</h1>
                        <div class="flex items-center space-x-4">
                            <p id="likesNumber" class="text-xl">Likes: <span
                                    class="font-semibold">${recipe.getNLikes()}</span>
                            </p>
                            <c:choose>
                                <c:when test="${liked != null}">
                                    <c:choose>
                                        <c:when test="${liked == true}">
                                            <i id="likeButton"
                                                class="fa fa-thumbs-up text-blue-600 text-4xl cursor-pointer"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i id="likeButton" class="fa fa-thumbs-up text-4xl cursor-pointer"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                    <div class="flex flex-col lg:flex-row lg:items-start lg:space-x-8 mt-4">
                        <div class="flex-shrink-0 lg:w-1/2 lg:sticky lg:top-24">
                            <img src="${recipe.getImage_url()}"
                                onerror="this.onerror=null;this.src='media/logo/logo-full.png';"
                                class="w-full h-auto object-contain rounded-lg shadow-md">
                        </div>
                        <div class="mt-6 lg:mt-0 lg:flex-1">
                            <ul class="space-y-4">
                                <li>
                                    <p class="text-xl">Made by <span class="font-semibold">${recipe.getUserName()}
                                            ${recipe.getUserSurname()}</span></p>
                                </li>
                                <li>
                                    <p class="text-xl">Description:</p>
                                    <p class="text-base mt-1">${recipe.getDescription()}</p>
                                </li>
                                <li>
                                    <p class="text-xl flex items-center"><i class="fa fa-clock-o"></i> <span
                                            class="font-semibold ml-2">${recipe.getTime_minutes_formatted()}
                                        </span> </p>
                                </li>
                                <li>
                                    <p class="text-xl">Difficulty:</p>
                                    <div class="flex items-center mt-2">
                                        <div
                                            class="difficulty-box ${recipe.getDifficulty() == 'hard' ? 'hard' : 'disabled'}">
                                            Hard</div>
                                        <div
                                            class="ml-1 difficulty-box ${recipe.getDifficulty() == 'medium' ? 'medium' : 'disabled'}">
                                            Medium
                                        </div>
                                        <div
                                            class="ml-1 difficulty-box ${recipe.getDifficulty() == 'easy' ? 'easy' : 'disabled'}">
                                            Easy</div>
                                    </div>
                                </li>
                                <li>
                                    <p class="text-xl">Tags:</p>
                                    <ul class="mt-1 flex flex-wrap">
                                        <c:forEach items="${recipe.getTags()}" var="item">
                                            <li class="bg-5 text-white px-3 py-1 rounded-full mr-2 mb-2">
                                                ${item.getName()}</li>
                                        </c:forEach>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </c:if>

            <script src="<c:url value='/js/recipeDetails.js'/>"></script>
            <c:import url="/html/include/footer.html" />
        </body>

        </html>