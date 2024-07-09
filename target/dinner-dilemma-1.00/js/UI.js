async function UIupdateCardDiv(resourceList, targetElementId) {

	// add  a title or somenthing
	// Fetch the HTML template
	const response = await fetch("html/include/card.html");
	if (!response.ok) {
		throw new Error('Network response was not ok');
	}
	const htmlText = await response.text();

	// Create a temporary container to parse the HTML
	const tempDiv = document.createElement('div');
	tempDiv.innerHTML = htmlText;

	// Get the target element by ID
	const targetElement = document.getElementById(targetElementId);
	targetElement.innerHTML = ''; // Clear the content of the target element

	// Add Tailwind CSS classes to the target element
    targetElement.classList.add('rounded-3xl', 'flex', 'flex-wrap', 'justify-center', 'gap-4', 'p-4');

	for (let i = 0; i < resourceList.length; i++) {
		const resource = resourceList[i];

		// Clone the template content for each resource
		const cardDiv = tempDiv.firstElementChild.cloneNode(true);

		// Modify the card content with resource data
		cardDiv.querySelector('.card-link').href = `http://localhost:8080/dinner-dilemma-1.00/recipeDetails?recipeID=${resource.id}`;
		cardDiv.querySelector('.card-image').src = resource.image_url;
		cardDiv.querySelector('.card-image').alt = resource.name;
		cardDiv.querySelector('.card-title-link').href = `http://localhost:8080/dinner-dilemma-1.00/recipeDetails?recipeID=${resource.id}`;
		cardDiv.querySelector('.card-title').textContent = resource.name;
		cardDiv.querySelector('.card-time').textContent = `${resource.time_minutes} minutes`;

		if (targetElementId == "rnkRecCard")
			cardDiv.querySelector('.card-likes').textContent = resource.nLikes;
		else
			cardDiv.querySelector('.card-likes-container').style.display = 'none';

		// Capitalize the first letter of the difficulty
		const difficulty = resource.difficulty.charAt(0).toUpperCase() + resource.difficulty.slice(1);
		cardDiv.querySelector('.card-difficulty').textContent = difficulty;

		// Append the modified card to the target element
		targetElement.appendChild(cardDiv);
	}

	console.log("HTTP GET request successfully performed and processed.");
}

async function UIupdateTableDiv(resourceList, targetElementId) {

	// add  a title or somenthing
	// Fetch the HTML template
	const response = await fetch("html/include/table.html");
	if (!response.ok) {
		throw new Error('Network response was not ok');
	}
	const htmlText = await response.text();

	// Create a temporary container to parse the HTML
	const tempDiv = document.createElement('div');
	tempDiv.innerHTML = htmlText;

	// Get the target element by ID
	const targetElement = document.getElementById(targetElementId);
	targetElement.innerHTML = ''; // Clear the content of the target element

	// Add Tailwind CSS classes to the target element
    targetElement.classList.add('flex', 'flex-wrap', 'justify-center', 'gap-4', 'p-4');

	for (let i = 0; i < resourceList.length; i++) {
		const resource = resourceList[i];

		// Clone the template content for each resource
		const cardDiv = tempDiv.firstElementChild.cloneNode(true);

		// Modify the card content with resource data
		cardDiv.querySelector('.card-link').href = `http://localhost:8080/dinner-dilemma-1.00/recipeDetails?recipeID=${resource.id}`;
		cardDiv.querySelector('.card-image').src = resource.image_url;
		cardDiv.querySelector('.card-image').alt = resource.name;
		cardDiv.querySelector('.card-title-link').href = `http://localhost:8080/dinner-dilemma-1.00/recipeDetails?recipeID=${resource.id}`;
		cardDiv.querySelector('.card-title').textContent = resource.name;
		cardDiv.querySelector('.card-time').textContent = `${resource.time_minutes} minutes`;

		// Capitalize the first letter of the difficulty
		const difficulty = resource.difficulty.charAt(0).toUpperCase() + resource.difficulty.slice(1);
		cardDiv.querySelector('.card-difficulty').textContent = difficulty;

		// Set description HTML content
		const descriptionElement = cardDiv.querySelector('.card-description');
		var truncatedDescription = resource.description.length > 60 ? resource.description.substring(0, 57) + '...' : resource.description;
		// Split truncated description on <br> and keep only last element
		truncatedDescription = truncatedDescription.split('<br>').pop();
		descriptionElement.innerHTML = truncatedDescription; // Set truncated description

		// Append the modified card to the target element
		targetElement.appendChild(cardDiv);
	}
	console.log("HTTP GET request successfully performed and processed.");
}

// Toggle the visibility of the search animation container
function toggleSearchAnimation() {
	var searchAnimationContainer = document.getElementById("searchAnimationContainer");
	if (searchAnimationContainer.style.display === "none" || searchAnimationContainer.style.display === "") {
		searchAnimationContainer.style.display = "block";
	} else {
		searchAnimationContainer.style.display = "none";
	}
}



function cAlert(string) {


  //set the text  
  document.getElementById("jsAlertText").textContent = string; 
  // display the text
  document.getElementById("jsAlert").style.display = "block"; 

  // listener for the pop ups
  document.getElementById('jsAlert').addEventListener("click", (event) => {
      // hide the stuff 
      document.getElementById("jsAlert").style.display = "none"; 
  });

}



// Set the visibility of the search animation container based on a boolean
function setSearchAnimationVisibility(bool) {
	var searchAnimationContainer = document.getElementById("searchAnimationContainer");
	if (bool) {
		searchAnimationContainer.style.display = "block";
	} else {
		searchAnimationContainer.style.display = "none";
	}
}

// Toggle the visibility of the search results
function toggleResultVisibility() {
	var srcRec = document.getElementById("srcRec");
	if (srcRec.style.display === "none" || srcRec.style.display === "") {
		srcRec.style.display = "block";
	} else {
		srcRec.style.display = "none";
	}
}

// Set the visibility of the search results based on a boolean
function setResultVisibility(bool) {
	var srcRec = document.getElementById("srcRec");
	if (bool) {
		srcRec.style.display = "block";
	} else {
		srcRec.style.display = "none";
	}
}

// Toggle the visibility of the suggestions
function toggleSuggVisibility() {
	var suggRec = document.getElementById("suggRec");
	if (suggRec.style.display === "none" || suggRec.style.display === "") {
		suggRec.style.display = "block";
	} else {
		suggRec.style.display = "none";
	}
}

// Set the visibility of the suggestions based on a boolean
function setSuggVisibility(bool) {
	var suggRec = document.getElementById("suggRec");
	if (bool) {
		suggRec.style.display = "block";
	} else {
		suggRec.style.display = "none";
	}
}

