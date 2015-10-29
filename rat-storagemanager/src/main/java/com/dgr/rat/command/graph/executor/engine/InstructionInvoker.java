/**
 * @author Daniele Grignani (dgr)
 * @date Sep 13, 2015
 */

package com.dgr.rat.command.graph.executor.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.validator.routines.UrlValidator;

import com.dgr.rat.command.graph.executor.engine.result.CommandResponse;
import com.dgr.rat.command.graph.executor.engine.result.InstructionResultContainer;
import com.dgr.rat.command.graph.executor.engine.result.IInstructionResult;
import com.dgr.rat.command.graph.executor.engine.result.instructions.InstructionResultInfo;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.errors.ErrorType;
import com.dgr.rat.commons.errors.ResourceException;
import com.dgr.rat.json.utils.ReturnType;
import com.dgr.rat.storage.provider.IStorage;
import com.dgr.utils.StringUtils;
import com.dgr.utils.Utils;

// COMMENT In questa classe unisco tutti i parametri provenienti dal comando remoto, ossia quello inviato dal client
// all'instruction contenuta nel DB e rappresentata da IInvokable. I parametri remoti sono dentro RemoteCommandsContainer,
// mentre quelli noti a IInvokable sono contenuti in quest'ultima. Detto in altri termini, questa classe 
// prende i parametri in IInvokable, verifica che non siano anche in RemoteCommandsContainer; se ci sono, allora 
// valorizza i parametri di IInvokable con quelli corrispondenti in RemoteCommandsContainer
public class InstructionInvoker implements IInstructionInvoker{
	private RemoteCommandsContainer _remoteCommandsContainer = null;
	private IStorage _storage = null;
	private Map<String, String> _parameters = new HashMap<String, String>();
	private Map<String, InstructionResultContainer> _instructionResults = new HashMap<String, InstructionResultContainer>();
	private IInstructionBuilder _instructionBuilder = null;
	private CommandResponse _commandResult = null;
	private String _currentInstruction = null;
	
	// COMMENT: il _resultStack serve per passare valori tra una instruction e l'altra.
	// TODO: per ora _resultStack è manuale, ossia devo chiamare pop, tuttavia
	// devo pensare ad un meccanismo di reference counting che permetta la rimozione dei  parametri in modo automatico. Quando entro in un nodo, 
	// attivo il reference counting in base al suo grado, ed ogni volta lo riduco di 1 quando rientro dal suo child. Non appena
	// il refcount è a 0, rimuovo il valore dallo stack (tranne se fa riferimento ad un nodo root, però in questo caso è come se non fosse più
	// disponibile in quanto a 0; ciò mi permetterebbe di rimuovere anche _responses e di gestire meglio i ICommandResponse). In questo modo
	// il valore nello stack è a disposizione di tutti i suoi child
//	private Stack<InstructionResult>_resultStack = new Stack<InstructionResult>();
	
	public InstructionInvoker(IStorage storage, RemoteCommandsContainer instructionsContainer, IInstructionBuilder instructionBuilder) {
		_storage = storage;
		_remoteCommandsContainer = instructionsContainer;
		_instructionBuilder = instructionBuilder;
	}
	
	public IStorage getStorage(){
		return _storage;
	}
	
	public Iterator<String>getParameterNameIterator(){
		return _parameters.keySet().iterator();
	}
	
	public String getParamValue(String paramName){
		String result = null;
		if(_parameters.containsKey(paramName)){
			result = _parameters.get(paramName);
		}
		
		return result;
	}
	
	private boolean verifyParam(ReturnType returnType, String paramValue) {
		boolean result = false;
		switch(returnType){
			case string:
				result = true;
				break;
				
			case integer:
				result = StringUtils.isParsableToInt(paramValue) ? true : false;
				break;
				
			case uuid:
				result = Utils.isUUID(paramValue) ? true : false;
				break;
				
			case url:
				if(paramValue.isEmpty()){
					result = true;
				}
				else{
					UrlValidator defaultValidator = new UrlValidator();
					result = defaultValidator.isValid(paramValue) ? true : false;
				}
				break;
				
			case systemKey:
			case StringArray:
				result = true; //?
				break;
				
			default:
				result = false;
		}
		
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.instruction.IInvoker#invoke(java.lang.Object)
	 */
	@Override
	public void invoke(IInstructionNodeWrapper invokable) throws Exception {
		_currentInstruction = invokable.getInstructionName();
		
		int maxNum = invokable.getMaxNumParameters();
		IInstruction executable = this.getInstruction(_currentInstruction);
		_parameters.clear();
		
		int num = invokable.getNumberOfInstructionParameters();
		if(num != maxNum){
			throw new Exception();
			// TODO log
		}
		
		// COMMENT: creo la mappa coi parametri previsti dal grafo
		for(int i = 0; i < num; i++){
			IInstructionParam instructionParameter = invokable.getInstructionParameter(i);
			String paramValue = instructionParameter.getInstructionsParameterValueField();
			String paramName = instructionParameter.getInstructionsParameterNameField();
			ReturnType returnType = instructionParameter.getInstructionsParameterReturnTypeField();
			
			if(paramValue.equalsIgnoreCase(RATConstants.VertexContentUndefined)){
				paramValue = _remoteCommandsContainer.getParameter(instructionParameter);
			}
			
			if(_parameters.containsKey(paramName)){
				throw new Exception();
				// TODO log
			}
			// TODO: creare un wrapper per i parametri che sia una template, cossì posso gestire 
			// tutti i parametri che voglio, anche oggetti; la classe deve essere creata in this.verifyParam
			if(!this.verifyParam(returnType, paramValue)){
				throw ResourceException.getException(ErrorType.BAD_REQUEST);
				// TODO log; inoltre da capire se è meglio lanciare l'eccezione oppure continuare con quello successivo
			}
			_parameters.put(paramName, paramValue);
		}
		
		// COMMENT: verifico che ci sia coerenza tra i parametri previsti dal grafo e quelli trovati 
		if(_parameters.isEmpty() || _parameters.size() != maxNum){
			throw new Exception();
			// TODO log
		}
		
		IInstructionResult instructionResult = executable.execute(this, invokable.getCallerNode());
		
		if(instructionResult != null){
			this.addInstructionResult(invokable.getCallerNode(), instructionResult, _currentInstruction);
		}
	}
	
	public void addCommandResponse(ICommandNodeVisitable visited, IInstructionResult instructionResult) throws Exception{
		if(_commandResult != null){
			throw new Exception();
			// TODO log
		}
		
		UUID uuid = instructionResult.getInMemoryOwnerNodeUUID();
		
		_commandResult = new CommandResponse();
		
		InstructionResultInfo info = new InstructionResultInfo(uuid);
		info.setStoredNodeUUID(visited.getStoredNodeUUID());
		info.setStoredNodeID(visited.getId());
		info.setInstructionName(_currentInstruction);
		
		_commandResult.setInstructionResult(info, instructionResult);
	}
	
	private void addInstructionResult(ICommandNodeVisitable visited, IInstructionResult instructionResult, String instructionName){
		UUID uuid = instructionResult.getInMemoryOwnerNodeUUID();
		
		InstructionResultContainer result = null;
		if(_instructionResults.containsKey(uuid.toString())){
			result = _instructionResults.get(uuid.toString());
		}
		else{
			result = new InstructionResultContainer();
			_instructionResults.put(uuid.toString(), result);
		}
		
		InstructionResultInfo info = new InstructionResultInfo(uuid);
		info.setStoredNodeUUID(visited.getStoredNodeUUID());
		info.setStoredNodeID(visited.getId());
		info.setInstructionName(instructionName);
		
		result.setInstructionResult(info, instructionResult);
	}
	
	private IInstruction getInstruction(String instructionName) throws Exception{
		IInstruction action = _instructionBuilder.buildInstruction(instructionName);
		return action;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.command.graph.executor.engine.IInstructionInvoker#getCommandResponse()
	 */
	@Override
	public InstructionResultContainer getInstructionResult(UUID nodeUUID) {
		return _instructionResults.get(nodeUUID.toString());
	}
	
	@Override
	public CommandResponse getCommandResponse() {
		return _commandResult;
	}
}
