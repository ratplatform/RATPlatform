function isValidText(text){
	var letters = /^[0-9a-zA-Z_\s\-\.]+$/;
	if(text.match(letters)) {
		return true;
	}

	return false;
}
