/**
 * @author Daniele Grignani (dgr)
 * @date Aug 18, 2015
 */

package com.dgr.rat.storage.provider;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.storage.orientdb.StorageInternalError;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.IndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class TinkerGraphStorage implements IStorage{
	//private Graph _graph = null;
	private IndexableGraph _graph = null;
	//private Map<String, Vertex> _index = new HashMap<String, Vertex>();
	
	public TinkerGraphStorage() {
		//_graph = new TinkerGraph();//TinkerGraphFactory.createTinkerGraph();
		_graph = (IndexableGraph) new TinkerGraph();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#addVertex(java.lang.Object)
	 */
	@Override
	public Vertex addVertex(UUID vertexUUID) {
		Vertex vertex = _graph.addVertex(null);
		vertex.setProperty(RATConstants.VertexUUIDField, vertexUUID.toString());
		return vertex;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#vertexExists(java.lang.String)
	 */
	@Override
	public boolean vertexExists(String label, String value) {
		GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>(_graph.getVertices(label, value));
		List<Vertex> list = pipe.toList();
		return !list.isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#vertexExists(java.lang.String)
	 */
	@Override
	public Vertex getVertex(String label, String value) throws Exception{
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
	public Vertex getVertex(UUID vertexUUID) throws Exception{
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
	public UUID getRootDomainUUID() throws Exception{
		UUID result = null;
		Vertex vertex = this.getVertex(RATConstants.VertexTypeField, RATConstants.VertexTypeValueRootDomain);
		if(vertex != null){
			String uuid = vertex.getProperty(RATConstants.VertexUUIDField);
			result = UUID.fromString(uuid);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#getRootDomain()
	 */
	@Override
	public Vertex getRootDomain() throws Exception{
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
	public void addToIndex(String indexName, Vertex vertex, String key, Object value) {
		Index<Vertex> index = this.getIndex(indexName);
		index.put(key, value, vertex);
	}
	
	@Override
	public List<Vertex> getVertices(String indexName, String key, Object value){
		List<Vertex> result = new ArrayList<Vertex>();
		Index<Vertex> index = this.getIndex(indexName);
		if(index != null){
			Iterator<Vertex> it = index.get(key, value).iterator();
			while(it.hasNext()){
				Vertex v = it.next();
				result.add(v);
			}
		}
		
		return result;
	}
	
	@Override
	public boolean vertexExists(String indexName, String key, Object value){
		Index<Vertex> index = this.getIndex(indexName);
		long result = index.count(key, value);
		
		return result > 0 ? true : false;
	}
	
	@Override
	public Index<Vertex> getIndex(String indexName){
		Index<Vertex> index = _graph.getIndex(indexName, Vertex.class);
		if(index == null){
			index = _graph.createIndex(indexName, Vertex.class);
		}

		 return index;
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
	public void closeConnection() throws Exception {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#openConnection()
	 */
	@Override
	public void openConnection() throws StorageInternalError, Exception{
		// TODO Auto-generated method stub
		
	}
}
