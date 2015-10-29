/*
 * @author Daniele Grignani
 * Mar 30, 2015
*/

package com.dgr.rat.json.factory;

import java.util.UUID;

import com.dgr.rat.command.graph.executor.engine.CommandData;
import com.dgr.rat.command.graph.executor.engine.CommandTemplateInvoker;
import com.dgr.rat.command.graph.executor.engine.ICommandGraphVisitableFactory;
import com.dgr.rat.command.graph.executor.engine.ICommandTemplateInvoker;
import com.dgr.rat.command.graph.executor.engine.IInstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.InstructionBuilder;
import com.dgr.rat.command.graph.executor.engine.InstructionInvoker;
import com.dgr.rat.command.graph.executor.engine.RemoteCommandsContainer;
import com.dgr.rat.command.graph.executor.engine.commands.CommandProxyNodeFactory;
import com.dgr.rat.command.graph.executor.engine.queries.QueryProxyNodeFactory;
import com.dgr.rat.command.graph.executor.engine.result.CommandResponse;
import com.dgr.rat.command.graph.executor.engine.result.instructions.CommandResultDriller;
import com.dgr.rat.command.graph.executor.engine.result.queries.QueryResultDriller;
import com.dgr.rat.commons.constants.JSONType;
import com.dgr.rat.commons.constants.MessageType;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.constants.StatusCode;
import com.dgr.rat.commons.errors.ErrorType;
import com.dgr.rat.commons.errors.ResourceException;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.storage.provider.IStorage;
import com.dgr.rat.storage.provider.StorageBridge;
import com.dgr.utils.AppProperties;
import com.dgr.utils.Utils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// TODO: da rivedere tutte le exception: al momento restituisce solo l'errore 500 e i codici delle exception 
// interne non sono impostati (mentre le exception sono comunque tutte gestite, anche se nessuna di esse emette un condice particolare)
public class CommandSink {
	public CommandSink() {
		// TODO Auto-generated constructor stub
	}
	
	public Response doCommand(String json){
		String placeHolder = AppProperties.getInstance().getStringProperty(RATConstants.DomainPlaceholder);
		String applicationName = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationName);
		String applicationVersion = AppProperties.getInstance().getStringProperty(RATConstants.ApplicationVersionField);
		
		Response response = new Response();
		JsonHeader header = new JsonHeader();
		
		header.setApplicationName(applicationName);
		header.setApplicationVersion(applicationVersion);
		header.setDomainName(placeHolder);
		header.setMessageType(MessageType.Response);
		
		IStorage storage = null;
		try {
			storage = StorageBridge.getInstance().getStorage();
		} 
		catch (Exception e1) {
			// TODO log
			header.setStatusCode(StatusCode.InternalServerError);
			e1.printStackTrace();
		}
		
		try{
			RATJsonObject ratJsonObject = RATHelpers.getRATJsonObject(json);
			CommandResponse commandResponse = this.buildCommand(ratJsonObject, header, storage);
			response.setCommandResponse(commandResponse);
			
			header.setStatusCode(commandResponse.getStatusCode());
		} 
		catch (JsonParseException | JsonMappingException e) {
			e.printStackTrace();
			// TODO log
			header.setStatusCode(StatusCode.InternalServerError);
		}
		catch(Exception e){
        	e.printStackTrace();
        	// TODO log
        	header.setStatusCode(StatusCode.InternalServerError);
		}
		finally{
			if(storage != null){
				storage.shutDown();
			}
			response.setHeader(header);
		}

		return response;
	}
	
	private CommandResponse buildCommand(final RATJsonObject ratJsonObject, JsonHeader headerOut, IStorage storage) throws Exception {
		String errorMessage = "Error retrieving the %s from JSON";
		JSONType commandType = JSONType.fromString(ratJsonObject.getHeaderProperty(RATConstants.CommandType));
		if(commandType == null || commandType == JSONType.Unknown){
			errorMessage = String.format(errorMessage, RATConstants.CommandType);
			throw ResourceException.getException(ErrorType.JSON_PARSE_ERROR, errorMessage);
		}
		headerOut.setCommandType(commandType);
		
		String commandName = ratJsonObject.getHeaderProperty(RATConstants.CommandName);
		if(commandName == null){
			errorMessage = String.format(errorMessage, RATConstants.CommandName);
			throw ResourceException.getException(ErrorType.JSON_PARSE_ERROR, errorMessage);
		}
		headerOut.setCommandName(commandName);
		
		String commandVersion = ratJsonObject.getHeaderProperty(RATConstants.CommandVersion);
		if(commandVersion == null){
			errorMessage = String.format(errorMessage, RATConstants.CommandVersion);
			throw ResourceException.getException(ErrorType.JSON_PARSE_ERROR, errorMessage);
		}
		headerOut.setCommandVersion(commandVersion);
		
		String commandUUID = ratJsonObject.getHeaderProperty(RATConstants.CommandGraphUUID);
		if(!Utils.isUUID(commandUUID)){
			errorMessage = String.format(errorMessage, RATConstants.CommandGraphUUID);
			throw ResourceException.getException(ErrorType.JSON_PARSE_ERROR, errorMessage);
		}
		headerOut.setCommandGraphUUID(UUID.fromString(commandUUID));
		
		ICommandTemplateInvoker commandTemplateInvoker = null;
		ICommandGraphVisitableFactory visitableFactory = null;
		RemoteCommandsContainer instructionsContainer = null;
		String instructionPackageName = null;
		
		instructionsContainer = new RemoteCommandsContainer();
		ObjectMapper mapper = new ObjectMapper();
		String output = mapper.writeValueAsString(ratJsonObject.getSettings());
		instructionsContainer.deserialize(output);
		CommandData data = new CommandData(ratJsonObject, instructionsContainer, storage);
		commandName = ratJsonObject.getHeaderProperty(RATConstants.CommandName);
		IInstructionInvoker invoker = null;
		
		switch (commandType){
		case LoadCommands:{
			// TODO da fare: sarebbe quello caricato in SystemCommandsInitializer.loadCommandTemplates 
			}
			break;
		
		case SystemCommands:{
				// TODO: passo il package dei comandi come stringa, ma è pericoloso nel caso in cui 
				// lo modificassi. Valutare l'ipotesi di metterlo nel file delle proprietà o trovare soluzione alternativa
				instructionPackageName = "com.dgr.rat.command.graph.executor.engine.commands.instructions";
				invoker = new InstructionInvoker(storage, instructionsContainer, new InstructionBuilder(instructionPackageName));
				visitableFactory = new CommandProxyNodeFactory();
				CommandResultDriller driller = new CommandResultDriller();
				commandTemplateInvoker = new CommandTemplateInvoker(data, driller);
			}
			break;
			
		case Query:{
				// TODO: passo il package dei comandi come stringa, ma è pericoloso nel caso in cui 
				// lo modificassi. Valutare l'ipotesi di metterlo nel file delle proprietà o trovare soluzione alternativa
				instructionPackageName = "com.dgr.rat.command.graph.executor.engine.queries.instructions";
				invoker = new InstructionInvoker(storage, instructionsContainer, new InstructionBuilder(instructionPackageName));
				visitableFactory = new QueryProxyNodeFactory();
				QueryResultDriller driller = new QueryResultDriller();
				commandTemplateInvoker = new CommandTemplateInvoker(data, driller);
			}
			break;
			
		case ConfigurationCommands:{
			}
			break;
			
		case UserCommands:{
			}
			break;
			
		case UserCommandTemplates:{
			}
			break;
			
			default:
				// TODO: log
				throw ResourceException.getException(ErrorType.INTERNAL_ERROR);
		}
		
		commandTemplateInvoker.setCommandName(commandName);
		commandTemplateInvoker.setCommandType(commandType);
		commandTemplateInvoker.setCommandVersion(commandVersion);
		commandTemplateInvoker.setCommandUUID(UUID.fromString(commandUUID));

		CommandResponse commandResponse = commandTemplateInvoker.execute(visitableFactory, invoker);
		
		return commandResponse;
	}
}
