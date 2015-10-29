/**
 * @author Daniele Grignani (dgr)
 * @date Aug 18, 2015
 */

package com.dgr.rat.storage.provider;

import java.util.List;
import java.util.UUID;
import com.dgr.rat.commons.constants.RATConstants;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class TinkerGraphStorage implements IStorage{
	private Graph _graph = null;
	
	public TinkerGraphStorage() {
		_graph = new TinkerGraph();//TinkerGraphFactory.createTinkerGraph();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#addVertex(java.lang.Object)
	 */
	@Override
	public Vertex addVertex(UUID vertexUUID) {
//		for (Vertex vertex : _graph.getVertices()) {
//			System.out.println(vertex.getId());
//		}
		Vertex vertex = _graph.addVertex(null);
		vertex.setProperty(RATConstants.VertexUUIDField, vertexUUID.toString());
		return vertex;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#vertexExists(java.lang.String)
	 */
	@Override
	public boolean vertexExists(String label, String value) {
//		GremlinPipeline pipe = new GremlinPipeline();
//		Iterable<Vertex> vertex = _graph.getVertices(label, value);
		GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>(_graph.getVertices(label, value));
		List<Vertex> list = pipe.toList();
		return !list.isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#vertexExists(java.lang.String)
	 */
	@Override
	public Vertex getVertex(String label, String value) {
		Vertex vertex = null;
		
		if(this.vertexExists(label, value)){
			Iterable<Vertex> iterable = _graph.getVertices(label, value);
			vertex = iterable.iterator().next();
		}
		
		return vertex;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#vertexExists(java.lang.String)
	 */
	@Override
	public boolean vertexExists(UUID vertexUUID) {
		return this.vertexExists(RATConstants.VertexUUIDField, vertexUUID.toString());
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#edgeExists(java.util.UUID)
	 */
	@Override
	public boolean edgeExists(UUID edgeUUID) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#addVertex()
	 */
	@Override
	public Vertex addVertex() {
		Vertex vertex = _graph.addVertex(null);
		return vertex;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#getVertex(java.util.UUID)
	 */
	@Override
	public Vertex getVertex(UUID vertexUUID) {
		return this.getVertex(RATConstants.VertexUUIDField, vertexUUID.toString());
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#getGraph()
	 */
	@Override
	public Graph getGraph() {
		// TODO Auto-generated method stub
		return _graph;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#getRootDomainUUID()
	 */
	@Override
	public UUID getRootDomainUUID() {
		UUID result = null;
		Vertex vertex = this.getVertex(RATConstants.VertexTypeField, RATConstants.VertexTypeValueRootDomain);
		if(vertex != null){
			String uuid = vertex.getProperty(RATConstants.VertexUUIDField);
			result = UUID.fromString(uuid);
		}
		return result;
	}

//	private static final PipeFunction<Vertex, Boolean> isParam = new PipesFunction<Vertex, Boolean>(){
//		@Override
//		public Boolean compute(Vertex vertex){
//			boolean result = false;
//			String paramValue = vertex.getProperty(RATConstants.VertexInstructionParameterValueField);
//			
//			result = RATConstants.VertexContentUndefined.equals(paramValue) ? true : false;
//			return result;
//		}
//	};
//	GremlinPipeline<Vertex, Vertex>pipe = new GremlinPipeline<Vertex, Vertex>(_graph.getVertices());
//	List<Vertex> list = pipe.outE(RATConstants.EdgeInstruction).inV()
//			.outE(RATConstants.EdgeInstructionParameter).inV().filter(CreateJsonCommand.isParam).toList();
	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#getRootDomain()
	 */
	@Override
	public Vertex getRootDomain() {
		Vertex vertex = this.getVertex(RATConstants.VertexTypeField, RATConstants.VertexTypeValueRootDomain);
		
		return vertex;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#commit()
	 */
	@Override
	public void commit() {
//		_graph.shutdown();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#rollBack()
	 */
	@Override
	public void rollBack() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#addIndex()
	 */
	@Override
	public void addIndex() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#shutDown()
	 */
	@Override
	public void shutDown() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#close()
	 */
	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
