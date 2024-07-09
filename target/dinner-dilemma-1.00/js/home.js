
//execute on start
onload();

function onload() {
    // cAlert("Js active");
    // setting up the listeners
    console.log("Adding event listeners");

    // listener for the search button 
    document.getElementById("searchButton").addEventListener("click", searchButtonListener);

    // setting the state of the animation 
    document.getElementById("searchAnimationContainer").style.display = "none";

    // hiding the results by default  
    document.getElementById("srcRec").style.display = "none";
    document.getElementById("suggRec").style.display = "none";

    // hiding the alrt
    document.getElementById("jsAlert").style.display = "none";

    updateRankedRecipe();
}

function updateSuggRecipeListener() {
    console.log("update suggested triggered");
    var ings = parseIngs();
    var tags = parseTags();
    var quelen = 8; // query result length

    if (ings + tags == "") {
        document.getElementById("suggRec").style.display = "none";
        return;
    }

    // Encode ingredient and tag values for URL
    const encodedIngs = encodeURIComponent(ings);
    const encodedTags = encodeURIComponent(tags);

    const suggestedUrl =
        "http://localhost:8080/dinner-dilemma-1.00/rest/recipe/suggested_recipe?" +
        `quelen=${quelen}&` +
        `usr_src_ing=${encodedIngs}&` +
        `usr_src_tag=${encodedTags}`;
    sendRequest(suggestedUrl, "GET", null, onRequestPerformedUpdateSuggRecipes);
}

function searchButtonListener() {

    var ings = parseIngs();
    var tags = parseTags();
    var quelen = 8; // query result length

    // Check if is empty, eventually it does not make the search 
    if (ings == "" && tags == "") {
        cAlert("You have to choose at least an ingredien or a tag");
        return
    }

    // Encode ingredient and tag values for URL
    const encodedIngs = encodeURIComponent(ings);
    const encodedTags = encodeURIComponent(tags);

    // Construct the URL with substituted values
    const searchUrl =
        "http://localhost:8080/dinner-dilemma-1.00/rest/recipe/search_recipe?" +
        `quelen=${quelen}&` +
        `usr_src_ing=${encodedIngs}&` +
        `usr_src_tag=${encodedTags}`;

    // deactivate the listener till we add or remove a new label 
    document.getElementById("searchButton").removeEventListener("click", searchButtonListener);
    setResultVisibility(false)
    setSearchAnimationVisibility(true)

    // Send the requests with the constructed URLs
    // console.log(suggestedUrl);
    sendRequest(searchUrl, "GET", null, onRequestPerformedUpdateSearchRecipes);
    // sendRequest(suggestedUrl, "GET", null, onRequestPerformedUpdateSuggRecipes);
}

// this may be turned into a standalone method
function onRequestPerformedUpdateSuggRecipes(xhr) {
    // not finished yet
    if (xhr.readyState !== XMLHttpRequest.DONE) {
        console.log(
            "Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]",
            xhr.readyState,
        );
        return;
    }
    //check if the request was successful
    if (xhr.status !== 200) {
        console.log("Request unsuccessful: HTTP status = %d.", xhr.status);
        console.log(xhr.response);

        div.appendChild(
            document.createTextNode("Unable to perform the AJAX request."),
        );
        return;
    }
    // parse the response as JSON and extract the resource-list array
    // const resourceList = JSON.parse(xhr.responseText)["resource-list"];
    resourceList = JSON.parse(xhr.responseText)["resource-list"];
    //UPDATE
    // UIupdateTableDiv(resourceList, "srcRec");	 
    UIupdateCardDiv(resourceList, "suggRecCard");
    setSuggVisibility(true);
}

function onRequestPerformedUpdateSearchRecipes(xhr) {
    // not finished yet
    if (xhr.readyState !== XMLHttpRequest.DONE) {
        console.log(
            "Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]",
            xhr.readyState,
        );
        return;
    }
    //check if the request was successful
    if (xhr.status !== 200) {
        console.log("Request unsuccessful: HTTP status = %d.", xhr.status);
        console.log(xhr.response);

        div.appendChild(
            document.createTextNode("Unable to perform the AJAX request."),
        );
        return;
    }

    resourceList = JSON.parse(xhr.responseText)["resource-list"];

    setSearchAnimationVisibility(false);

    if (resourceList.length > 0) {
        UIupdateTableDiv(resourceList, "srcRecCard");
        setResultVisibility(true);
    } else {
        // maybe make a better banner 
        cAlert("No recipes found, try using more ingredients")
    }
}

//updates the status of the ranked_recipe
function updateRankedRecipe() {
    // const url = "http://localhost:8080/dinner-dilemma-1.00/rest/recipe/ranked_recipe"
    var quelen = 8;
    const url =
        "http://localhost:8080/dinner-dilemma-1.00/rest/recipe/ranked_recipe?" +
        `quelen=${quelen}`;

    sendRequest(url, "GET", null, onRequestPerformedUpdateRankedRecipes);

}

function onRequestPerformedUpdateRankedRecipes(xhr) {
    // not finished yet
    if (xhr.readyState !== XMLHttpRequest.DONE) {
        console.log(
            "Request state: %d. [0 = UNSENT; 1 = OPENED; 2 = HEADERS_RECEIVED; 3 = LOADING]",
            xhr.readyState,
        );
        return;
    }
    //check if the request was successful
    if (xhr.status !== 200) {
        console.log("Request unsuccessful: HTTP status = %d.", xhr.status);
        console.log(xhr.response);

        div.appendChild(
            document.createTextNode("Unable to perform the AJAX request."),
        );
        return;
    }

    // parse the response as JSON and extract the resource-list array
    resourceList = JSON.parse(xhr.responseText)["resource-list"];
    //UPDATE
    UIupdateCardDiv(resourceList, "rnkRecCard");

}

function sendRequest(url, method, params, onReqPerformed) {
    // the XMLHttpRequest object
    const xhr = new XMLHttpRequest();
    if (!xhr) {
        console.log("Cannot create an XMLHttpRequest instance.");
        cAlert("Giving up :( Cannot create an XMLHttpRequest instance");
        return false;
    }

    // Set up the callback for handling the request
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            onReqPerformed(this);
        }
    };

    // Handle parameters
    if (method === 'GET' && params) {
        // Convert params object to query string
        const queryString = new URLSearchParams(params).toString();
        url += '?' + queryString;
    }

    // Perform the request
    console.log("Performing the HTTP " + method + " request.");
    xhr.open(method, url, true);

    if (method === 'POST' && params) {
        // Set content type header for POST requests
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        // Convert params object to query string
        const queryString = new URLSearchParams(params).toString();
        xhr.send(queryString);
    } else {
        xhr.send();
    }

    console.log("HTTP " + method + " request sent.");
}

function parseIngs() {
    var list = document.getElementById("ingsContainer").getElementsByClassName("tag");
    var ing = "";
    // Loop through the list of elements
    for (let i of list) {
        // Extract the ID from the second child element's id attribute
        var tmp = i.children[1].id.split("_").pop();

        // Add the extracted ID to the ing array
        ing += "," + tmp
    }

    ing = ing.substring(1);
    return ing;
}

function parseTags() {
    var list = document.getElementById("tagsContainer").getElementsByClassName("tag");
    var tag = "";
    // Loop through the list of elements
    for (let i of list) {
        // Extract the ID from the second child element's id attribute
        var tmp = i.children[1].id.split("_").pop();

        // Add the extracted ID to the ing array
        tag += "," + tmp
    }
    tag = tag.substring(1);
    return tag;
}

// manage scrolling for logo animation
window.addEventListener("load", (event) => updateLogoPosition());
addEventListener("scroll", (event) => updateScroll());

document.getElementById('logo').addEventListener("click", (event) => {
    window.location.replace('home');
});

function updateScroll() {
    logo = document.getElementById('logo');

    // how much the page was scrolled (1 = not scrolled, 0 = fully scrolled)
    const maxScrolling = Math.max(document.body.scrollHeight - window.innerHeight, 11);
    const animationLimit = Math.min(maxScrolling - 10, 300);
    const scrolled = 1 - (Math.min(window.scrollY, animationLimit) / animationLimit);

    // minimum values
    const minTop = 3; // %
    const minLeft = 30; // px
    const minWidth = 4; // rem

    // maximum offsets wrt minimums (max - min)
    const topOffset = 12 - minTop;
    const widthOffset = 25 - minWidth;

    // update logo position based on how much the page was scrolled
    logo.style.top = (scrolled * topOffset + minTop) + '%';
    logo.style.width = (scrolled * widthOffset + minWidth) + 'rem';

    // left depends on logo width
    const leftOffset = ((document.body.clientWidth - logo.width) / 2) - minLeft;
    logo.style.left = (scrolled * leftOffset + minLeft) + 'px';
}

// if the window is resized, update the dimensions
addEventListener("resize", (event) => updateLogoPosition());

function updateLogoPosition() {
    console.log("BODY ONLOAD UPDATE LOGO POSITION");
    logo = document.getElementById('logo');
    logo.style.width = '25 rem';

    var logos = document.getElementsByClassName('logo-fixed');
    for (i = 0; i < logos.length; i++) { // there should be only one
        logos[i].style.display = 'none';
    }

    updateScroll();
};

window.onload = function () { updateLogoPosition() };

// on change of the search bar get the inserted ingredient or tag
document.getElementById('srcBar').addEventListener("change", (event) => searchBarManager());

function searchBarManager() {

    const srcBar = document.getElementById("srcBar");
    const text = srcBar.value.trim();
    const datalistOptions = document.getElementById("ings_tags").getElementsByTagName("option");
    var matchingOption = "";
    var matchingOptionId = "";

    // check if it's a valid ingredient or tag
    for (let option of datalistOptions) {
        if (option.textContent.toLowerCase() == text.toLowerCase()) {
            matchingOption = option.textContent;
            matchingOptionId = option.getAttribute("data-id");

            if (matchingOptionId == null)
                matchingOptionId = option.getAttribute("id");

            break;
        }
    }

    // add to the list
    if (matchingOption != "") {
        if (matchingOptionId.split('_')[0] == "ing") {

            addIng(matchingOptionId, matchingOption);

        } else {
            addTag(matchingOptionId, matchingOption);
        }

        event.preventDefault();
    } else {
        cAlert("The choosen ingredient or tag does not exist");
    }
    // the bar is anyway empty at the end 
    srcBar.value = "";

    // enable search if we added a new tag or label 
    document.getElementById("searchButton").addEventListener("click", searchButtonListener);
    //update the suggeggested list recipe
    updateSuggRecipeListener()

}

// function to add an ingredient or a tag under the search bar
function addIng(selectedOptionId, selectedOption) {
    const tagElement = document.createElement("div");
    tagElement.classList.add("tag", "whitespace-nowrap", "gap-1");

    const iconElement = document.createElement("i");
    const textElement = document.createElement("span");

    //setting the attributes of the object
    iconElement.classList.add("fa", "fa-times-circle", "mr-1");
    textElement.setAttribute("id", selectedOptionId);
    textElement.textContent = selectedOption;

    // appending elements
    tagElement.appendChild(iconElement);
    tagElement.appendChild(textElement);

    tagElement.addEventListener("click", () => {
        tagElement.remove();

        var option = document.createElement("option");
        option.innerHTML = selectedOption;
        option.id = selectedOptionId;
        var list = document.getElementById("ings_tags");
        list.insertBefore(option, list.firstChild);

        // activate search listener if remove a ing
        document.getElementById("searchButton").addEventListener("click", searchButtonListener);

        // update the recipe 
        updateSuggRecipeListener()
    });

    document.getElementById("ingsContainer").appendChild(tagElement);
    // document.getElementById("srcBar").value = "";

    ingsList = document.getElementById("ings_tags").children;
    for (var i = 0; i < ingsList.length; i++) {
        if (ingsList[i].value == selectedOption) {
            ingsList[i].remove();
        }
    }
}

// function to add an ingredient or a tag under the search bar
function addTag(selectedOptionId, selectedOption) {
    const tagElement = document.createElement("div");
    tagElement.classList.add("tag", "whitespace-nowrap");

    const iconElement = document.createElement("i");
    const textElement = document.createElement("span");

    //setting the attributes of the object
    iconElement.classList.add("fa", "fa-times-circle", "mr-1");
    textElement.setAttribute("id", selectedOptionId);
    textElement.textContent = selectedOption;

    // appending elements
    tagElement.appendChild(iconElement);
    tagElement.appendChild(textElement);

    tagElement.addEventListener("click", () => {
        tagElement.remove();

        var option = document.createElement("option");
        option.innerHTML = selectedOption;
        option.id = selectedOptionId;
        var list = document.getElementById("ings_tags");
        list.insertBefore(option, list.firstChild);

        // activate search listener if remove a tag 
        document.getElementById("searchButton").addEventListener("click", searchButtonListener);

        // update the recipe 
        updateSuggRecipeListener()
    });

    document.getElementById("tagsContainer").appendChild(tagElement);
    // document.getElementById("srcBar").value = "";

    ingsList = document.getElementById("ings_tags").children;
    for (var i = 0; i < ingsList.length; i++) {
        if (ingsList[i].value == selectedOption) {
            ingsList[i].remove();
        }
    }
}

// hide alerts on click
if (document.getElementById('verificationError1') != null) {
    document.getElementById('verificationError1').addEventListener("click", (event) => {
        document.getElementById('verificationError1').remove();
    });
}

if (document.getElementById('verificationError2') != null) {
    document.getElementById('verificationError2').addEventListener("click", (event) => {
        document.getElementById('verificationError2').remove();
    });
}

if (document.getElementById('changePasswordError1') != null) {
    document.getElementById('changePasswordError1').addEventListener("click", (event) => {
        document.getElementById('changePasswordError1').remove();
    });

}
