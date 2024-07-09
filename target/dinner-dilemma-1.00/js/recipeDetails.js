document.getElementById("likeButton").addEventListener("click", putLike);
var waitingResponse = false;

// toggle like on current recipe
function putLike() {
	// change like status
	like = !like;

	// get number of likes and like button html objects
	const nLikesText = document.getElementById("likesNumber");
	const likeText = document.getElementById("likeButton");

	// update html like status
	if (like) {
		likeText.style.color = "blue";
		nLikes += 1;
		nLikesText.innerHTML = "Likes: " + nLikes;
	} else {
		likeText.style.color = "black";
		nLikes -= 1;
		nLikesText.innerHTML = "Likes: " + nLikes;
	}

	// send like toggle request
	if (!waitingResponse) {
		waitingResponse = true;
		sendLikeRequest();
	}
}

function sendLikeRequest() {
	const xhr = new XMLHttpRequest();

	if (!xhr) {
		//console.log("Error while trying to create an XMLHttpRequest instance.")
		return;
	}

	xhr.onreadystatechange = function () {
		processPutLikeResponse(this);
	};

	xhr.open("POST", "http://localhost:8080/dinner-dilemma-1.00/rest/like/" + recipe, true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function processPutLikeResponse(xhr) {
	if (xhr.readyState !== XMLHttpRequest.DONE) {
		//console.log("Request state: %d. [0=UNSENT; 1=OPENED; 2=HEADERS_RECEIVED; 3=LOADING]", xhr.readyState);
		return;
	}

	if (xhr.status !== 200) {
		//console.log("Request unsuccessful: status %d.", xhr.status);
		console.log(xhr.response);
		return;
	}

	const likeText = document.getElementById("likeButton");
	const resourceResponse = JSON.parse(xhr.responseText)["like"];

	// check coherence client-server
	if ((resourceResponse == "yes" && !like) || (resourceResponse == "no" && like)) {
		sendLikeRequest();
	} else {
		waitingResponse = false;
	}
}
