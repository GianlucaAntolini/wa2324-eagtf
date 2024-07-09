
// execute periodically
function onrefresh() {
	// console.log("Refreshing status listeners"); 
	//updateRankedRecipe();
}

//executed on load of the query 
function onload() {
	// alert("Js active");
	// setting up the listeners
	console.log("Adding event listeners");	
  // listener for the search button 
  document.getElementById("searchButton").addEventListener("click", searchButtonListener);	
  // setting the state of the animation 
  document.getElementById("searchAnimationContainer").style.display = "none";
  // hiding the results by default  
  document.getElementById("srcRec").style.display = "none";
  document.getElementById("suggRec").style.display = "none";

  updateRankedRecipe(); 
}

function updateSuggRecipeListener() {
    console.log("update suggested triggered");
    var ings = parseIngs();
    var tags = parseTags();
    var quelen = 6; // Aggiungi una variabile per 'quelen'

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
    var quelen = 6; // Aggiungi una variabile per 'quelen'


      
    if(ings=="" && tags==""){
      //TODO make a better alert box with a div 
      alert("devi selezionare almeno un ingrediente o un tag");
      return
    }



    // need to check if is empty so it does not  make the search 

    // Encode ingredient and tag values for URL
    const encodedIngs = encodeURIComponent(ings);
    const encodedTags = encodeURIComponent(tags);

    


    // Construct the URL with substituted values
    const searchUrl =
        "http://localhost:8080/dinner-dilemma-1.00/rest/recipe/search_recipe?" +
        `quelen=${quelen}&` +
        `usr_src_ing=${encodedIngs}&` +
        `usr_src_tag=${encodedTags}`;



    setResultVisibility(false)
    setSearchAnimationVisibility(true)

    // Send the requests with the constructed URLs
    // console.log(suggestedUrl);
    sendRequest(searchUrl, "GET", null, onRequestPerformedUpdateSearchRecipes);
    // sendRequest(suggestedUrl, "GET", null, onRequestPerformedUpdateSuggRecipes);

}



// this may be turned into a standalone method
function  onRequestPerformedUpdateSuggRecipes(xhr) {
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



function  onRequestPerformedUpdateSearchRecipes(xhr) {
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
	UIupdateTableDiv(resourceList, "srcRecCard");	

  setSearchAnimationVisibility(false)
  setResultVisibility(true)
}



//updates the status of the ranked_recipe
function updateRankedRecipe() {
	// const url = "http://localhost:8080/dinner-dilemma-1.00/rest/recipe/ranked_recipe"
  var quelen = 6; 
  const url=
        "http://localhost:8080/dinner-dilemma-1.00/rest/recipe/ranked_recipe?" +
        `quelen=${quelen}`;

	sendRequest(url, "GET",null, onRequestPerformedUpdateRankedRecipes);

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
		alert("Giving up :( Cannot create an XMLHttpRequest instance");
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
        ing +=","+tmp
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
        tag +=","+tmp
    }
    tag = tag.substring(1);
    return tag;
}


var resourceList;
//execute on start
onload();

//execute periodically every 5 sec 
window.setInterval(onrefresh, 5 * 10 ** 3);


