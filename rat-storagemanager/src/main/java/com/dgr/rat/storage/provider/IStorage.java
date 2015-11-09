/**
 * @author Daniele Grignani (dgr)
 * @date Aug 18, 2015
 */

package com.dgr.rat.storage.provider;

import java.util.UUID;

import com.dgr.rat.storage.orientdb.StorageInternalError;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

// COMMENT: le funzioni qui restituiscono dei Vertex perché in ogni caso, ossia qualunque sia il DB utilizzato, utilizzo le Tinkerpop
public interface IStorage {
	// TODO: addVertex mette in vertice in un indice
	public Vertex addVertex(UUID vertexUUID);
	// TODO: getVertex cerca in un indice
	public Vertex getVertex(UUID vertexUUID)throws Exception;
	public Vertex addVertex();
	public boolean vertexExists(String label, String value);
	public boolean vertexExists(UUID vertexUUID);
	public boolean edgeExists(UUID vertexUUID);
	public Vertex getVertex(String label, String value)throws Exception;
	public Graph getGraph();
	public UUID getRootDomainUUID()throws Exception;
	public Vertex getRootDomain()throws Exception;
	public void commit();
	public void rollBack();
	public void addIndex();
	public void shutDown();
	public void closeConnection() throws Exception;
	public void openConnection() throws StorageInternalError, Exception;
}
