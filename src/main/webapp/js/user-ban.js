document.querySelectorAll("#myTable button").forEach(function (button) {
	button.addEventListener("click", myBan);
});

function myBan() {
	const btn = this;

	let xhr = new XMLHttpRequest();

	if (!xhr) {
		console.log("Error while trying to create an XMLHttpRequest instance.")
		return;
	}

	xhr.onreadystatechange = function () {
		processResponse(this, btn);
	};

	xhr.open("POST", "http://localhost:8080/dinner-dilemma-1.00/rest/user/" + btn.parentNode.parentNode.querySelector('th').innerText + "/" + btn.innerText.toLowerCase(), true);
	xhr.setRequestHeader("Content-Type", "application/json");
	xhr.send();
}

function processResponse(xhr, btn) {
	if (xhr.readyState !== XMLHttpRequest.DONE) {
		//console.log("Request state: %d. [0=UNSENT; 1=OPENED; 2=HEADERS_RECEIVED; 3=LOADING]", xhr.readyState);
		return;
	}

	if (xhr.status !== 200) {
		//console.log("Request unsuccessful: status %d.", xhr.status);
		console.log(xhr.response);
		return;
	}

	const responseId = JSON.parse(xhr.responseText)["role_id"];

	// check coherence client-server
	if (responseId == 1) {
		btn.innerText = "Ban";

		const spans = btn.parentNode.parentNode.querySelectorAll("td")[4].querySelectorAll("span");

		spans[0].classList.add("text-yellow-900");
		spans[1].classList.add("bg-yellow-200");

		spans[0].classList.remove("text-red-900");
		spans[1].classList.remove("bg-red-200");

		spans[2].innerText = "User";
	}
	else if (responseId == 2) {
		btn.innerText = "Unban";

		const spans = btn.parentNode.parentNode.querySelectorAll("td")[4].querySelectorAll("span");

		spans[0].classList.add("text-red-900");
		spans[1].classList.add("bg-red-200");

		spans[0].classList.remove("text-yellow-900");
		spans[1].classList.remove("bg-yellow-200");

		spans[2].innerText = "Banned";
	}
}