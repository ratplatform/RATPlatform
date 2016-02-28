function setUrlCookie(value){
	var c = $.cookie('url');
	$.removeCookie('url');
	var index = -1;
	if(c){
		index = c.indexOf(value);
	}
	if(index < 0){
		if(c && c.length > 0){
			c = c + "@@" + value;
		}
		else{
			c = value;
		}
	}

	$.cookie('url', c, { expires: 7, path: '/' });
}

function getUrlCookieValues(){
	var result = [];

	var c = $.cookie('url');
	if(c && c.indexOf("@@") > -1){
		result = c.split("@@");
	}
	else{
		result[0] = c;
	}

	return result;
}
