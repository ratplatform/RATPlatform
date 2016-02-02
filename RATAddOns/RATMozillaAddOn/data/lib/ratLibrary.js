
function executeMenuCommand(command, commandData){
	switch(command){
		case 'reply':
		addon.port.emit("message", "opencomment-dlg", commandData.currentDomainName, commandData.currentDomainUUID, commandData);
		break;
	}
}

