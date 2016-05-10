function showPopup(){
   $("#loginform").fadeIn();
   $("#loginform").css({"visibility":"visible","display":"block"});
}

function hidePopup(){
   $("#loginform").fadeOut();
   $("#loginform").css({"visibility":"hidden","display":"none"});
}

function logout(){
	var wsUrl = ratURL + "/logout?sessionid=" + loginResult.sessionID;
	callWsLogout(wsUrl, logoutCallBack, errorCallBack);
}
function logoutCallBack(data, textStatus, jqXHR){
	if(jqXHR.status == 200){
		location.reload(); 

		$("#login").prop("disabled", false);
		$("#logout").prop('disabled', true);
	}
	else{
		console.log("logoutCallBack status Error: " + jqXHR.status);
	}
}

function ratLogin(ratURL, login, password, errorCallBack){
	var json = {email:login, password:password};
	console.log("login: " + JSON.stringify(json));
	callWsSimple(ratURL + "/login", 'POST', JSON.stringify(json), loginCallBack, errorCallBack);
}

function loginCallBack(data, textStatus, jqXHR){
	try {
		loginResult.status = jqXHR.status;

		if(loginResult.status == 200){
			var json = JSON.parse(data);
			console.log("loginCallBack: " + data);
			loginResult.sessionID = json.sessionID;
			loginResult.userUUID = json.userUUID;
			loginResult.email = json.email;

			var wsUrl = ratURL + "/runquery?sessionid=" + loginResult.sessionID;
			var getAllUserDomains = getAllUserDomainsFunc("null", loginResult.userUUID);
			callWs(wsUrl, 'POST', getAllUserDomains, getAllUserDomainsCallBack, errorCallBack);
		}
		else{
			console.log("loginCallBack status Error: " + loginResult.status);
		}
	}
	catch (e) {
		loginResult.status = 500;
		console.log("loginCallBack Error: " + e);
	}
}

function getAllUserURLsCallBack(data, textStatus, jqXHR){
	if(jqXHR.status == 200){
		var json = JSON.parse(data);
		//console.log("getAllUserURLsCallBack: " + data);
		populateComboURLs(json);
	}
	else{
		console.log("getAllUserURLsCallBack status Error: " + jqXHR.status);
	}
}

function getAllUserDomainsCallBack(data, textStatus, jqXHR){
	if(jqXHR.status == 200){
		var json = JSON.parse(data);
		//console.log("getAllUserDomainsCallBack: " + data);
		alchemyConfig.dataSource = json;
		var alchemy = new Alchemy(alchemyConfig);
		$("#login").prop("disabled", true);
		$("#logout").prop('disabled', false);

		populateDomainTree(json);

		var wsUrl = ratURL + "/runquery?sessionid=" + loginResult.sessionID;
		var getAllUserURLs = getUserURLsFunc("null", loginResult.userUUID);
		//console.log("getAllUserDomainsCallBack getAllUserURLs: " + getAllUserURLs);
		callWs(wsUrl, 'POST', getAllUserURLs, getAllUserURLsCallBack, errorCallBack);
	}
	else{
		console.log("getAllUserDomainsCallBack status Error: " + jqXHR.status);
	}
}

