function showPopup(){
   $("#loginform").fadeIn();
   $("#loginform").css({"visibility":"visible","display":"block"});
}

function hidePopup(){
   $("#loginform").fadeOut();
   $("#loginform").css({"visibility":"hidden","display":"none"});
}

function ratLogin(ratURL, login, password, loginResultCallBack, errorCallBack){
	var json = {email:login, password:password};
	console.log("login: " + JSON.stringify(json));
	callWs(ratURL + "/login", 'POST', JSON.stringify(json), loginResultCallBack, errorCallBack);
}

function loginResultCallBack(data, textStatus, jqXHR){
	try {
		loginResult.status = jqXHR.status;

		if(loginResult.status == 200){
			var json = JSON.parse(data);
			loginResult.sessionID = json.sessionID;
			loginResult.userUUID = json.userUUID;
			loginResult.email = json.email;

			GetAllUserDomainsSet(null, "Domain", loginResult.userUUID);
			var getAllUserDomains = JSON.stringify(GetAllUserDomains);
			console.log("loginResultCallBack: " + JSON.stringify(getAllUserDomains));
			var wsUrl = ratURL + "/runquery?sessionid=" + loginResult.sessionID;
			callWs(wsUrl, 'POST', getAllUserDomains, getAllUserDomainsResultCallBack, errorCallBack);

			//loginResult.userDomains = json.userDomains;
			//$("#login").prop("disabled", true);
			//$("#logout").prop('disabled', false);
			//console.log("loginResultCallBack: " + data);
			//populateDomainTree(loginResult.userDomains);
		}
		else{
			console.log("loginResultCallBack status Error: " + loginResult.status);
		}
	}
	catch (e) {
		loginResult.status = 500;
		console.log("loginResultCallBack Error: " + e);
	}
}

function getAllUserDomainsResultCallBack(data, textStatus, jqXHR){
	console.log("getAllUserDomainsResultCallBack: " + data);
	var json = JSON.parse(data);
	alchemyConfig.dataSource = json;//'test.json';
	var alchemy = new Alchemy(alchemyConfig);
	$("#login").prop("disabled", true);
	$("#logout").prop('disabled', false);

	populateDomainTree(json);
}
