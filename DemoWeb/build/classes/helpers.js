function isValidText(text){
	var letters = /^[0-9a-zA-Z_\s\-\.]+$/;
	if(text.match(letters)) {
		return true;
	}

	return false;
}

function addCombo(selectID, text, value) {
    var combo = document.getElementById(selectID);
     
    var option = document.createElement("option");
    option.text = text;
    option.value = value;
    try {
        combo.add(option, null);
    }catch(error) {
        combo.add(option); // IE only
    }
}
