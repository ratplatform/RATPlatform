/**
 * @author Daniele Grignani (dgr)
 * @date Aug 18, 2015
 */

package com.dgr.rat.storage.provider;

import java.util.List;
import java.util.UUID;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.storage.orientdb.OrientDBService;
import com.dgr.rat.storage.orientdb.StorageInternalError;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.TransactionalGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.gremlin.java.GremlinPipeline;

public class OrientDBStorage implements IStorage{
	private TransactionalGraph	_orientGraph = null;
	
	public OrientDBStorage() {
    	OGlobalConfiguration.MEMORY_USE_UNSAFE.setValue(false);
    	OGlobalConfiguration.STORAGE_COMPRESSION_METHOD.setValue("gzip");
    	OGlobalConfiguration.JNA_DISABLE_USE_SYSTEM_LIBRARY.setValue(true);
	}
	
	@Override
	public void openConnection() throws StorageInternalError, Exception {
		_orientGraph = OrientDBService.getInstance().getConnection();
	}
	
	private String escape(String text){
		return text.replaceAll("-", "&#45;");
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#addVertex(java.lang.Object)
	 */
	@Override
	public Vertex addVertex(UUID vertexUUID) {
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
	public Vertex addVertex() {
		Vertex vertex = _orientGraph.addVertex(null);
		return vertex;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#vertexExists(java.lang.String)
	 */
	@Override
	public boolean vertexExists(String label, String value) {
//		System.out.println(_orientGraph.countVertices());
		GremlinPipeline<Vertex, Vertex> pipe = new GremlinPipeline<Vertex, Vertex>(_orientGraph.getVertices(label, value));
		List<Vertex> list = pipe.toList();
		return !list.isEmpty();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#vertexExists(java.lang.String)
	 */
	@Override
	public boolean vertexExists(UUID vertexUUID) {
		// TODO: Orient Parrebbe avere dei problemi con alcuni caratteri: per ora me la risolvo così, poi devo trovare una soluzione migliore in quanto potrebbe rallentare
		String escapedUUID = this.escape(vertexUUID.toString());
		
		return this.vertexExists(RATConstants.VertexUUIDField, escapedUUID);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#edgeExists(java.util.UUID)
	 */
	@Override
	public boolean edgeExists(UUID vertexUUID) {
		// TODO: Orient Parrebbe avere dei problemi con alcuni caratteri: per ora me la risolvo così, poi devo trovare una soluzione migliore in quanto potrebbe rallentare
		String escapedUUID = this.escape(vertexUUID.toString());
		
		return false;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#getVertex(java.util.UUID)
	 */
	@Override
	public Vertex getVertex(UUID vertexUUID) throws Exception {
		// TODO: Orient Parrebbe avere dei problemi con alcuni caratteri: per ora me la risolvo così, poi devo trovare una soluzione migliore in quanto potrebbe rallentare
		String escapedUUID = this.escape(vertexUUID.toString());
		
		return this.getVertex(RATConstants.VertexUUIDField, escapedUUID);
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#getVertex(java.lang.String, java.lang.String)
	 */
	@Override
	public Vertex getVertex(String label, String value) throws Exception {
		Vertex vertex = null;
		try{
		// TODO: attenzion: per il problema del carattere "-" (cone le UUID), non posso fare chiamare vertexExists qui
		// perché non conosco il valore di label; pertanto se fosse per caso VertexUUIDField (controllare non è il massimo),
		// non facendo escape, il vertex non verrebbe trovato e la funzione fallirebbe
		Iterable<Vertex> iterable = _orientGraph.getVertices(label, value);
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
	public Graph getGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#getRootDomainUUID()
	 */
	// TODO: funzione davvero discutibile: se applicata ad un grafo complesso come rat, i rootVertex sarebbero molti, moltissimi e qui ne restituisco uno.
	// Da rivedere
	@Override
	public UUID getRootDomainUUID() throws Exception {
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
	public Vertex getRootDomain() throws Exception {
		Vertex vertex = this.getVertex(RATConstants.VertexTypeField, RATConstants.VertexTypeValueRootDomain);
		
		return vertex;
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#commit()
	 */
	@Override
	public void commit() {
		_orientGraph.commit();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#rollBack()
	 */
	@Override
	public void rollBack() {
		_orientGraph.rollback();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#addIndex()
	 */
	@Override
	public void addIndex() {
//		_orientGraph.createIndex(indexName, indexClass, indexParameters)
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#shutDown()
	 */
	@Override
	public void shutDown() {
		_orientGraph.shutdown();
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.IStorage#close()
	 */
	@Override
	public void closeConnection() throws Exception {
		OrientDBService.getInstance().close();
	}
}
