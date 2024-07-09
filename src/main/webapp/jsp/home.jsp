<%@ page contentType="text/html;charset=utf-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html>
        <c:import url="/html/include/header.html" />

        <body class="bg-2">
            <c:import url="/jsp/include/log-banner.jsp" />
            <c:import url="/jsp/include/homeErr-banner.jsp" />
            <c:import url="/jsp/include/jsErr-banner.jsp" />
            <main class="flex flex-col">
                <!-- Logo and Search Section -->
                <section class="m-4 pt-8">
                    <div class="flex justify-center items-center" id="logo-container">
                        <img src="media/logo/logo-full.png" width="400rem" id="logo">
                    </div>

                    <div class="flex justify-center gap-4 searchBar" id="searchBar-container">
                        <form method="get" class="flex justify-center gap-4 sm:w-1/2 w-full">
                            <input class="placeholder:italic placeholder:text-slate-400 
                        block bg-white w-full border border-slate-300 rounded-md py-2 
                        pl-9 pr-3 shadow-sm focus:outline-none focus:border-sky-500 
                        focus:ring-sky-500 focus:ring-1 sm:text-sm" type="text"
                                placeholder="Search ingredients and tags ..." id="srcBar" name="usr_src_ing"
                                list="ings_tags">
                            <datalist id="ings_tags">
                                <c:forEach items="${ingredients}" var="item">
                                    <option data-id="ing_${item.getId()}">${item.getName()}</option>
                                </c:forEach>
                                <c:forEach items="${tags}" var="item">
                                    <option data-id="tag_${item.getId()}">#${item.getName()}</option>
                                </c:forEach>
                            </datalist>
                            <button type="button">
                                <i id="searchButton" class="fa fa-search"></i>
                            </button>
                        </form>
                        <form action="http://localhost:8080/dinner-dilemma-1.00/search_random_recipe" method="get"
                            class="flex">
                            <button type="submit">
                                <i class="fa fa-random"></i>
                            </button>
                        </form>
                    </div>
                    <div id="ingsContainer" class="m-2 flex flex-wrap justify-center gap-8"></div>

                    <div id="tagsContainer" class="m-3 flex flex-wrap justify-center gap-8"></div>

                </section>

                <div id="searchAnimationContainer" class="bg-3">
                    <div id="searchAnimationElements">
                        <img src="http://localhost:8080/dinner-dilemma-1.00/media/img/logo/thinghy1.png"
                            class="animationElement" id="thinghy1">
                        <img src="http://localhost:8080/dinner-dilemma-1.00/media/img/logo/thinghy2.png"
                            class="animationElement" id="thinghy2">
                        <img src="http://localhost:8080/dinner-dilemma-1.00/media/img/logo/thinghy3.png"
                            class="animationElement" id="thinghy3">
                        <img src="http://localhost:8080/dinner-dilemma-1.00/media/img/logo/thinghy4.png"
                            class="animationElement" id="thinghy4">
                        <img src="http://localhost:8080/dinner-dilemma-1.00/media/img/logo/taco.png"
                            class="animationElement" id="taco">
                    </div>
                </div>

                <!-- Carded results Section -->
                <section id="srcRec" class="m-4 bg-3 rounded-3xl">
                    <div class="bg-1 rounded-3xl w-full py-2">
                        <h1 class="ranked-title text-4xl font-medium font-mono font-semibold text-white text-center">Searched Recipes</h1>
                    </div>
                    <div id="srcRecCard" class="flex flex-wrap gap-4"></div>
                </section>

                <section id="suggRec" class="m-4 bg-3 rounded-3xl">
                    <div class="bg-1 rounded-3xl w-full py-2">
                        <h1 class="ranked-title text-4xl font-medium font-mono font-semibold text-white text-center">Suggested Recipes</h1>
                    </div>
                    <div class="ranked-title m-1 text-l font-mono font-semibold font-medium text-center text-lg">You only need a few more ingredients for these recipes!</div>
                    <div id="suggRecCard" class="flex flex-wrap gap-4 justify-center"></div>
                </section>
                
                <section id="rnkRec" class="m-4 bg-3 rounded-3xl">
                    <div class="bg-1 rounded-3xl w-full py-2">
                        <h1 class="ranked-title text-4xl font-medium font-mono font-semibold text-white text-center">Most Liked Recipes</h1>
                    </div>
                    <div id="rnkRecCard" class="flex flex-wrap gap-4 justify-center"></div>
                </section>

            </main>
            <c:import url="/html/include/footer.html" />
        </body>
        <!-- javascript code after body -->
        <script src="js/UI.js"></script>
        <script src="js/home.js?v=2"></script>

        </html>