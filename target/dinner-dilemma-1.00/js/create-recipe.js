document.addEventListener("DOMContentLoaded", () => {

    const recipeForm = document.getElementById("recipeForm");

    recipeForm.addEventListener("submit", (e) => {
        e.preventDefault();

        addHidden(recipeForm, 'usr_src_ing', document.getElementById('ingredientsContainer'));
        addHidden(recipeForm, 'usr_src_tag', document.getElementById('tagsContainer'));

        if (confirm('Are you sure?')) {
            recipeForm.submit();
        }
    });

    /**
     * Initializes the search bar element and adds an event listener for handling search.
     * @param {string} inputId - The ID of the search bar input element.
     * @param {string} listId - The ID of the list element containing options.
     * @param {Function} addFunction - The function to be called when an option is selected.
     * @param {string} containerId - The ID of the container element where selected options will be added.
     */
    function initializeSearchBar(inputId, listId, addFunction, containerId) {
        const searchBar = document.getElementById(inputId);
        const options = document.getElementById(listId).getElementsByTagName("option");

        searchBar.addEventListener("change", () => {
            handleSearch(searchBar, options, addFunction, containerId);
        });
    }

    initializeSearchBar("usr_src_tag", "tags_list", addTag, "tagsContainer");
    initializeSearchBar("usr_src_ing", "ings_list", addIng, "ingredientsContainer");

    /**
     * Handles the search event and performs the necessary actions based on the search input.
     * @param {HTMLInputElement} searchBar - The search bar input element.
     * @param {NodeList} options - The list of options to search through.
     * @param {Function} addFunction - The function to call when a matching option is found.
     * @param {string} containerId - The ID of the container element to add the matching option to.
     */
    function handleSearch(searchBar, options, addFunction, containerId) {
        const text = searchBar.value.trim();
        let matchingOption = "";
        let matchingOptionId = "";

        for (let option of options) {
            if (option.textContent.toLowerCase() === text.toLowerCase()) {
                matchingOption = option.textContent;
                matchingOptionId = option.getAttribute("data-id");

                if (matchingOptionId == null)
                    matchingOptionId = option.getAttribute("id");

                break;
            }
        }

        if (matchingOption !== "") {
            addFunction(matchingOptionId, matchingOption, containerId);
            searchBar.value = ""; // Clear the search bar
        } else {
            alert("Ingrediente o tag non esistente");
        }
    }

    function addIng(selectedOptionId, selectedOption, containerId) {
        addTagOrIng(selectedOptionId, selectedOption, containerId, false);
    }

    function addTag(selectedOptionId, selectedOption, containerId) {
        addTagOrIng(selectedOptionId, selectedOption, containerId, true);
    }

    /**
     * Adds a tag or ingredient element to the specified container.
     * @param {string} selectedOptionId - The ID of the selected option.
     * @param {string} selectedOption - The selected option.
     * @param {string} containerId - The ID of the container element.
     */
    function addTagOrIng(selectedOptionId, selectedOption, containerId, isTag) {
        console.log("ADDING TAG OR INGREDIENT: " + selectedOptionId);
        const container = document.getElementById(containerId);

        const tagElement = document.createElement("div");
        tagElement.classList.add("tag", "m-1", "hover:scale-105");
        tagElement.style.cursor = "pointer"; // Ensures the cursor indicates clickability for the whole tag

        const iconElement = document.createElement("i");
        iconElement.classList.add("fa", "fa-times-circle");
        iconElement.style.marginRight = "0.5rem"; // Adds spacing between the icon and text

        const textElement = document.createElement("span");
        textElement.id = selectedOptionId;
        textElement.textContent = selectedOption;

        tagElement.appendChild(iconElement);
        tagElement.appendChild(textElement);
        container.appendChild(tagElement);

        var list;
        if (isTag)
            list = document.getElementById("tags_list");
        else
            list = document.getElementById("ings_list");

        for (var i = 0; i < list.children.length; i++) {
            if (list.children[i].value == selectedOption) {
                list.children[i].remove();
            }
        }

        tagElement.addEventListener("click", () => {
            container.removeChild(tagElement);

            var option = document.createElement("option");
            option.innerHTML = selectedOption;
            option.id = selectedOptionId;

            var list;
            if (isTag)
                list = document.getElementById("tags_list");
            else
                list = document.getElementById("ings_list");

            list.insertBefore(option, list.firstChild);
        });
    }
});

function addHidden(theForm, key, container) {
    var spans = container.querySelectorAll('span');

    let values = "";
    for (let i = 0; i < spans.length; i++) {
        values = values.concat(spans[i].id, ",");
    }

    var input = document.createElement('input');

    input.type = 'hidden';
    input.name = key;
    input.value = values;

    theForm.appendChild(input);
}