var loginButton = document.getElementById("login");
var closeButton = document.getElementById("close");

var urlObj = document.getElementById('url');
var userEmailObj = document.getElementById('userEmail');
var userPasswordObj = document.getElementById('userPassword');

function textIsValid(text){
	if (!text){
		return false;
	}

	var tmp = text.trim();
	var n = tmp.length;
	if (n == 0){
		return false;
	}

	return true;
}

loginButton.addEventListener("click", function(){
	var url = urlObj.value;
	if (!textIsValid(url)){
		alert("Please enter a valid RAT url");
		return;
	}
	url = url.trim();

	var userEmail = userEmailObj.value;
	if (!textIsValid(userEmail)){
		alert("Please enter a valid user email");
		return;
	}
	userEmail = userEmail.trim();

	var userPassword = userPasswordObj.value;
	if (!textIsValid(userPassword)){
		alert("Please enter a valid user password");
		return;
	}
	userPassword = userPassword.trim();

	var loginData = {
		url: url,
		userEmail: userEmail,
		userPwd: userPassword
	};

	self.port.emit("login", loginData);

	urlObj.value = '';
	userEmailObj.value = '';
	userPasswordObj.value = '';

	self.port.emit("hide");
});

closeButton.addEventListener("click", function(){
	urlObj.value = '';
	userEmailObj.value = '';
	userPasswordObj.value = '';

	self.port.emit("hide");
});

self.port.on("loginData", function(loginData) {
	urlObj.value = loginData.url;
	userEmailObj.value = loginData.userEmail;
	userPasswordObj.value = loginData.userPwd;
});

self.port.on("show", function onShow() {
	urlObj.focus();
});
