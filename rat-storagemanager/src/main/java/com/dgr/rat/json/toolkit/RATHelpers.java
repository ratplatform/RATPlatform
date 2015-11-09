/**
 * @author Daniele Grignani (dgr)
 * @date Aug 23, 2015
 */

package com.dgr.rat.json.toolkit;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IInstructionNodeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeEdgeFrame;
import com.dgr.rat.command.graph.executor.engine.ratvertexframes.IRATNodeFrame;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.json.RATJsonObject;
import com.dgr.utils.AppProperties;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReader;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONWriter;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.javahandler.JavaHandlerModule;

public class RATHelpers {

	public static Graph fromRatJsonToGraph(String ratJson) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		RATJsonObject jsonHeader = (RATJsonObject) mapper.readValue(ratJson, RATJsonObject.class);
		String output = mapper.writeValueAsString(jsonHeader.getSettings());
		JsonNode actualObj = mapper.readTree(output);
		Graph graph = new TinkerGraph();
		InputStream inputStream = new ByteArrayInputStream(actualObj.toString().getBytes());
		GraphSONReader.inputGraph(graph, inputStream);
		
		return graph;
	}
	
	public static void initProperties(String propertiesFile) throws Exception{
		Path path = FileSystems.getDefault().getPath(propertiesFile);
		boolean fileExists = Files.exists(path);
		if(fileExists){
			AppProperties.getInstance().init(propertiesFile);
		}
		else{
			// TODO: da gestire i messaggi con ResourceBundle
			throw new Exception("File " + propertiesFile + " not found");
		}
	}
	
	public static String fromGraphToJson(Graph graph) throws IOException{
	    OutputStream output = new OutputStream(){
	        private StringBuilder string = new StringBuilder();
	        
	        @Override
	        public void write(int b) throws IOException {
	            this.string.append((char) b );
	        }
	        public String toString(){
	            return this.string.toString();
	        }
	    };
	    
		GraphSONWriter.outputGraph(graph, output);
		String json = output.toString();
		
		return json;
	}
	
	public static IRATNodeEdgeFrame getRATEdgeFrameFromEdge(Edge edge) throws Exception{
		Graph graph = new TinkerGraph();
	    FramedGraph<Graph> framedGraph = new FramedGraphFactory(new JavaHandlerModule()).create(graph);
	    IRATNodeEdgeFrame frameEdge = framedGraph.frame(edge, IRATNodeEdgeFrame.class);
		if(frameEdge == null){
			throw new Exception();
		}
	    return frameEdge;
	}
	
	public static IRATNodeFrame getRATNodeFrameFromVertex(Vertex vertex) throws Exception{
		Graph graph = new TinkerGraph();
	    FramedGraph<Graph> framedGraph = new FramedGraphFactory(new JavaHandlerModule()).create(graph);
	    IRATNodeFrame frameVertex = framedGraph.frame(vertex, IRATNodeFrame.class);
		if(frameVertex == null){
			throw new Exception();
		}
	    return frameVertex;
	}
	
	public static IRATNodeFrame getRATNodeFrameFromVertex(Graph graph, Vertex vertex) throws Exception{
	    FramedGraph<Graph> framedGraph = new FramedGraphFactory(new JavaHandlerModule()).create(graph);
	    IRATNodeFrame frameVertex = framedGraph.frame(vertex, IRATNodeFrame.class);
		if(frameVertex == null){
			throw new Exception();
		}
	    return frameVertex;
	}
	
	public static IInstructionNodeFrame getInstructionNodeFrameFromVertex(Vertex vertex) throws Exception{
		Graph graph = new TinkerGraph();
	    FramedGraph<Graph> framedGraph = new FramedGraphFactory(new JavaHandlerModule()).create(graph);
	    IInstructionNodeFrame farmeVertex = framedGraph.frame(vertex, IInstructionNodeFrame.class);
		if(farmeVertex == null){
			throw new Exception();
		}
	    return farmeVertex;
	}
	
	public static String getCommandsPath(String folderName){
		StringBuffer pathBuffer = new StringBuffer();
		pathBuffer.append(RATConstants.ConfigurationFolder);
		pathBuffer.append(FileSystems.getDefault().getSeparator());
		String systemCommandsFolder = AppProperties.getInstance().getStringProperty(folderName);
		pathBuffer.append(systemCommandsFolder);
		pathBuffer.append(FileSystems.getDefault().getSeparator());
		
		return pathBuffer.toString();
	}
	
	public static void duplicateVertex(final Vertex vertex, final boolean createNewUUID, Vertex newVertexOut){
		Set<String> keys = vertex.getPropertyKeys();
		for(String key : keys){
			if(!key.equalsIgnoreCase(RATConstants.VertexUUIDField)){
				Object value = vertex.getProperty(key);
				newVertexOut.setProperty(key, value);
			}
		}
		
		if(createNewUUID){
			newVertexOut.setProperty(RATConstants.VertexUUIDField, UUID.randomUUID());
		}
		else{
			newVertexOut.setProperty(RATConstants.VertexUUIDField, vertex.getProperty(RATConstants.VertexUUIDField));
		}
	}
}
