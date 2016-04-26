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

function getComboValue(selectID){
	var combo = document.getElementById(selectID);
	var str = combo.options[combo.selectedIndex].text;
	return str;
}

function clearAllOptionsFromCombo(selectID){
	var select = document.getElementById(selectID);
	var length = select.options.length;
	for (i = 0; i < length; i++) {
		select.options[i] = null;
	}
}
