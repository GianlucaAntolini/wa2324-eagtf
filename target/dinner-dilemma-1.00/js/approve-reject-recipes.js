document.onclick = hideMenu;

const rows = document.querySelectorAll('#myTable tr');

for (let i = 0; i < rows.length; i++) {

	const cb = rows[i].querySelectorAll('input[type="checkbox"]');

	if (i == 0) {
		cb[0].onchange = function () { myCheckAll(this); };
		continue;
	}

	cb[0].onchange = function () { mySingleCheck(); };

	// Assign an "oncontextmenu" event to row i:
	rows[i].oncontextmenu = function (e) { if (!cb[0].checked) check(cb[0]); rightClick(e); };

	// Prevent default context menu:
	rows[i].addEventListener("contextmenu", (e) => { e.preventDefault() });
}

function hideMenu() {
	document.getElementById("contextMenu").style.display = "none";
}

function rightClick(e) {
	e.preventDefault();

	if (document.getElementById("contextMenu").style.display == "block")
		hideMenu();
	else {
		var menu = document.getElementById("contextMenu");

		menu.style.display = 'block';
		menu.style.left = e.pageX + "px";
		menu.style.top = e.pageY + "px";
	}
}

function check(cb_elem) {
	const tmp = document.querySelectorAll('input[type="checkbox"]');

	if (tmp.length == 2)
		tmp[0].checked = true;
	else
		for (let i = 0; i < tmp.length; i++)
			tmp[i].checked = false;

	cb_elem.checked = true;
}

function mySingleCheck() {

	const tmp = document.querySelectorAll('input[type="checkbox"][id="check-all"]');

	if (document.querySelectorAll('input[type="checkbox"]:not([id="check-all"]):checked').length
		== document.querySelectorAll('input[type="checkbox"]:not([id="check-all"])').length)
		tmp[0].checked = true;
	else
		tmp[0].checked = false;
}

function myCheckAll(cb_elem) {

	const tmp = document.querySelectorAll('input[type="checkbox"]:not([id="check-all"])');

	for (let i = 0; i < tmp.length; i++)
		if (cb_elem.checked)
			tmp[i].checked = true;
		else
			tmp[i].checked = false;

}

// set click action for the two buttons in the page
document.getElementById("approveButton").addEventListener("click", approveRecipeList);
document.getElementById("rejectButton").addEventListener("click", rejectRecipeList);

// set click action for the two buttons in the context menu
document.getElementById("cmApproveButton").addEventListener("click", approveRecipeList);
document.getElementById("cmRejectButton").addEventListener("click", rejectRecipeList);

// approve checked recipes
function approveRecipeList() {

	let msg = document.getElementById('confirmationMsg');
	if (msg != null)
		document.getElementById('confirmationMsg').remove();

	const recipeApprovalList = [];

	const tmp = document.querySelectorAll('input[type="checkbox"]:not([id="check-all"]):checked');

	for (let i = 0; i < tmp.length; i++) {
		let xhr = new XMLHttpRequest();

		if (!xhr) {
			console.log("Error while trying to create an XMLHttpRequest instance.")
			return;
		}

		xhr.onreadystatechange = function () {
			processResponse(this, tmp, recipeApprovalList);
		};

		xhr.open("POST", "http://localhost:8080/dinner-dilemma-1.00/rest/recipe/" + tmp[i].parentNode.parentNode.querySelectorAll('td')[0].innerHTML + "/approve", true);
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.send();
	}
}

// remove checked recipes
function rejectRecipeList() {

	let msg = document.getElementById('confirmationMsg');
	if (msg != null)
		document.getElementById('confirmationMsg').remove();

	const recipeRejectionList = [];

	const tmp = document.querySelectorAll('input[type="checkbox"]:not([id="check-all"]):checked');

	for (let i = 0; i < tmp.length; i++) {
		let xhr = new XMLHttpRequest();

		if (!xhr) {
			console.log("Error while trying to create an XMLHttpRequest instance.")
			return;
		}

		xhr.onreadystatechange = function () {
			processResponse(this, tmp, recipeRejectionList);
		};

		xhr.open("POST", "http://localhost:8080/dinner-dilemma-1.00/rest/recipe/" + tmp[i].parentNode.parentNode.querySelectorAll('td')[0].innerHTML + "/reject", true);
		xhr.setRequestHeader("Content-Type", "application/json");
		xhr.send();
	}
}

function processResponse(xhr, checkedList, confirmList) {
	if (xhr.readyState !== XMLHttpRequest.DONE) {
		//console.log("Request state: %d. [0=UNSENT; 1=OPENED; 2=HEADERS_RECEIVED; 3=LOADING]", xhr.readyState);
		return;
	}

	if (xhr.status !== 200) {
		//console.log("Request unsuccessful: status %d.", xhr.status);
		console.log(xhr.response);
		return;
	}

	const responseStatus = JSON.parse(xhr.responseText)["status"];
	const responseId = JSON.parse(xhr.responseText)["recipe_id"];

	// check coherence client-server
	if (responseStatus == "approved" || responseStatus == "rejected") {
		confirmList.push(responseId);

		for (let i = 0; i < checkedList.length; i++)
			if (responseId == checkedList[i].parentNode.parentNode.querySelectorAll('td')[0].innerText) {
				if (confirmList.length == checkedList.length)
					createConfirmMsg(confirmList, responseStatus);

				checkedList[i].parentNode.parentNode.remove();

				if (document.getElementById("check-all").checked && confirmList.length == checkedList.length) {
					document.getElementById("removable").remove();
					document.getElementById("hiddentxt").style.display = 'block';
				}

				break;
			}
	}
}

function createConfirmMsg(confirmList, responseStatus) {
	var input = document.createElement("div");

	input.classList.add("green-alerts");
	input.classList.add("fixed");
	input.id = "confirmationMsg";

	var div = document.createElement("div");

	"p-5 green-alert mb-4 text-sm text-green-900 rounded-lg bg-green-200 opacity-50 hover:opacity-100".split(" ").forEach(function (item) {
		div.classList.add(item);
	});
	div.role = "alert";
	div.onclick = function () { document.getElementById('confirmationMsg').remove(); };

	input.appendChild(div);

	var span1 = document.createElement('span');

	span1.class = "font-medium";
	span1.innerHTML = "Recipes successfully " + responseStatus + ": " + confirmList + ".";

	div.appendChild(span1);

	var span2 = document.createElement("span");

	"font-mono cursor-pointer float-right mr-5".split(" ").forEach(function (item) {
		span2.classList.add(item);
	});
	span2.innerHTML = "x";

	div.appendChild(span2);

	document.body.insertBefore(input, document.querySelector("main"));
}