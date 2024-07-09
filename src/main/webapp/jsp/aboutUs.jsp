<%@ page contentType="text/html;charset=utf-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

        <!DOCTYPE html>
        <html>
        <c:import url="/html/include/header.html" />

        <body class="bg-2 flex flex-col">
            <c:import url="/jsp/include/log-banner.jsp" />
            <div class="flex-grow overflow-auto pt-24 px-6">
                <!-- HERE -->
                <section id="what-is-it" class="mb-12">
                    <h2 class="text-4xl font-bold mb-4 text-black">What is Dinner Dilemma?</h2>
                    <p class="text-lg leading-relaxed text-black">
                        Dinner Dilemma is a web application designed to help users make the most of the ingredients they
                        have at home.
                        If you find yourself staring at your open fridge or pantry, unsure of what to make for dinner,
                        Dinner Dilemma has got you covered.
                        By simply entering the ingredients you have on hand, the app provides a list of suggested
                        recipes that use those exact ingredients.
                        It's the perfect solution for reducing food waste and discovering new meals without the hassle
                        of extensive meal planning.
                    </p>
                </section>

                <section id="how-does-it-work" class="mb-12">
                    <h2 class="text-4xl font-bold mb-4 text-black">How does Dinner Dilemma work?</h2>
                    <p class="text-lg leading-relaxed text-black mb-4">
                        Dinner Dilemma revolves around the creation and visualization of recipes, offering several key
                        functionalities:
                    </p>
                    <ul class="list-disc list-inside space-y-2 text-lg text-black">
                        <li><strong>Ingredient-Based Recipe Search:</strong> Users can search for recipes by inputting
                            ingredients they have available. The app then generates a list of recipes that incorporate
                            those ingredients.</li>
                        <li><strong>Recipe Recommendations:</strong> Along with the searched recipes, the app provides a
                            list of similar recipes, allowing users to explore various options.</li>
                        <li><strong>Random Recipe Generator:</strong> For those feeling adventurous, there is a button
                            to search for a random recipe, offering a fun way to discover new dishes.</li>
                        <li><strong>Top Liked Recipes:</strong> Users can view a list of the most liked recipes, making
                            it easy to find popular and highly rated meals.</li>
                        <li><strong>User Accounts:</strong> By registering, users can manage their information, create
                            and save recipes, and like other recipes to keep them easily accessible.</li>

                    </ul>
                    <p class="text-lg leading-relaxed text-black mt-4">
                        Dinner Dilemma streamlines the process of finding and sharing recipes, making meal preparation
                        both simple and enjoyable.
                    </p>
                </section>

                <section id="creators" class="mb-12">
                    <h2 class="text-4xl font-bold mb-4 text-black">Creators of Dinner Dilemma</h2>
                    <p class="text-lg leading-relaxed text-black mb-4">
                        Dinner Dilemma was developed by a talented group of Computer Engineering students from the
                        University of Padova:
                    </p>
                    <ul class="list-disc list-inside space-y-2 text-lg text-black">
                        <li>Gianluca Antolini</li>
                        <li>Tommaso Bergamasco</li>
                        <li>Andrea Felline</li>
                        <li>Francesco Frigato</li>
                        <li>Emilio Risi</li>
                    </ul>
                </section>
            </div>
            <c:import url="/html/include/footer.html" />
        </body>

        </html>