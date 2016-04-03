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
import com.dgr.rat.storage.orientdb.OrientDBService;
import com.dgr.rat.storage.orientdb.StorageInternalError;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.ODatabase;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Index;
import com.tinkerpop.blueprints.IndexableGraph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class OrientDBStorage implements IStorage{
	private IndexableGraph	_graph = null;
	//private IndexableGraph _orientGraph = null;
	
	public OrientDBStorage() {
    	OGlobalConfiguration.MEMORY_USE_UNSAFE.setValue(false);
    	OGlobalConfiguration.STORAGE_COMPRESSION_METHOD.setValue("gzip");
    	OGlobalConfiguration.JNA_DISABLE_USE_SYSTEM_LIBRARY.setValue(true);
	}
	
	@Override
	public synchronized void openConnection() throws StorageInternalError, Exception {
		System.out.println("_orientGraph.openConnection()");
		TransactionalGraph orientGraph = OrientDBService.getInstance().getConnection();
		_graph = (IndexableGraph) orientGraph;
	}
	
	private String escape(String text){
		return text;//text.replaceAll("-", "&#45;");
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#addVertex(java.lang.Object)
	 */
	@Override
	public synchronized Vertex addVertex(UUID vertexUUID) {
		// TODO: OrientParrebbe avere dei problemi con alcuni caratteri: per ora me la risolvo così, poi devo trovare una soluzione migliore in quanto potrebbe rallentare
		String escapedUUID = this.escape(vertexUUID.toString());
		
		Vertex orientVertex = this.addVertex();
		orientVertex.setProperty(RATConstants.VertexUUIDField, escapedUUID);
		
		return orientVertex;
	}
	
	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#addVertex()
	 */
	@Override
	public synchronized Vertex addVertex() {
		Vertex vertex = _graph.addVertex(null);
		return vertex;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#vertexExists(java.lang.String)
	 */
	@Override
	public synchronized boolean vertexExists(String label, String value) {
//		System.out.println(_orientGraph.countVertices());
		GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>(_graph.getVertices(label, value));
		List<Vertex> list = pipe.toList();
		return !list.isEmpty();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#vertexExists(java.lang.String)
	 */
	@Override
	public synchronized boolean vertexExists(UUID vertexUUID) {
		// TODO: Orient Parrebbe avere dei problemi con alcuni caratteri: per ora me la risolvo così, poi devo trovare una soluzione migliore in quanto potrebbe rallentare
		String escapedUUID = this.escape(vertexUUID.toString());
		
		return this.vertexExists(RATConstants.VertexUUIDField, escapedUUID);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#edgeExists(java.util.UUID)
	 */
	@Override
	public synchronized boolean edgeExists(UUID vertexUUID) {
		// TODO: Orient Parrebbe avere dei problemi con alcuni caratteri: per ora me la risolvo così, poi devo trovare una soluzione migliore in quanto potrebbe rallentare
		String escapedUUID = this.escape(vertexUUID.toString());
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#getVertex(java.util.UUID)
	 */
	@Override
	public synchronized Vertex getVertex(UUID vertexUUID) throws Exception {
		// TODO: Orient Parrebbe avere dei problemi con alcuni caratteri: per ora me la risolvo così, poi devo trovare una soluzione migliore in quanto potrebbe rallentare
		String escapedUUID = this.escape(vertexUUID.toString());
		
		return this.getVertex(RATConstants.VertexUUIDField, escapedUUID);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#getVertex(java.lang.String, java.lang.String)
	 */
	@Override
	public synchronized Vertex getVertex(String label, String value) throws Exception {
		Vertex vertex = null;
		try{
		// TODO: attenzion: per il problema del carattere "-" (cone le UUID), non posso fare chiamare vertexExists qui
		// perché non conosco il valore di label; pertanto se fosse per caso VertexUUIDField (controllare non è il massimo),
		// non facendo escape, il vertex non verrebbe trovato e la funzione fallirebbe
		Iterable<Vertex> iterable = _graph.getVertices(label, value);
		vertex = iterable.iterator().next();
		}
		catch(Exception e){
			e.printStackTrace();
			throw new Exception(e);
		}
		
		return vertex;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#getGraph()
	 */
	@Override
	public synchronized Graph getGraph() {
		// TODO Auto-generated method stub
		return _graph;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#getRootDomainUUID()
	 */
	// TODO: funzione davvero discutibile: se applicata ad un grafo complesso come rat, i rootVertex sarebbero molti, moltissimi e qui ne restituisco uno.
	// Da rivedere
	@Override
	public synchronized UUID getRootDomainUUID() throws Exception {
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
	// TODO: funzione davvero discutibile: idem come sopra
	@Override
	public synchronized Vertex getRootDomain() throws Exception {
		Vertex vertex = this.getVertex(RATConstants.VertexTypeField, RATConstants.VertexTypeValueRootDomain);
		
		return vertex;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#commit()
	 */
	@Override
	public synchronized void commit() {
		//_orientGraph.commit();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#rollBack()
	 */
	@Override
	public synchronized void rollBack() {
		//_orientGraph.rollback();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#shutDown()
	 */
	@Override
	public synchronized void shutDown() {
		System.out.println("_orientGraph.shutdown()");
		_graph.shutdown();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#close()
	 */
	@Override
	public synchronized void closeConnection() throws Exception {
		OrientDBService.getInstance().close();
	}

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
//	@Override
//	public Index<Vertex> getIndex(String indexName) {
////		String sql = "create index MyClass.my_field on MyClass (my_field) unique";
////		OCommandSQL createIndex = new OCommandSQL(sql);
////		Object done = db.command(createIndex).execute(new Object[0]);
//		
////		ODatabase odb	= ((OrientGraph)_orientGraph).getRawGraph();
////		OSchema schema = databaseDocumentTx.getMetadata().getSchema();
////		OClass oClass = schema.createClass("Foo");
////		oClass.createProperty("name", OType.STRING);
////		oClass.createIndex("City.name", "FULLTEXT", null, null, "LUCENE", new String[] { "name"});
////		IndexableGraph indexableGraph = (IndexableGraph) _orientGraph;
//		return null;
//	}
}
