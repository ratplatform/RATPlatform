/*
 * @author Daniele Grignani
 * Apr 2, 2015
*/

package com.dgr.rat.storage.orientdb;

import org.apache.log4j.Logger;

import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.errors.InternalServerErrorException;
import com.dgr.utils.AppProperties;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory;

// TODO: togliere i synchronized e sostituirli con dei reentrantlock
public class OrientDBService{
	protected final static Logger _logger = Logger.getLogger(OrientDBService.class.getName());
    private static final int DEFAULT_POOL_MIN_SIZE = 5;
    private static final int DEFAULT_POOL_MAX_SIZE = 20;
	private static OrientDBService _instance = null;
//	private ODatabaseDocumentPool _pool = null;
	private OrientGraphFactory _factory = null;
    private String _dbURL = null;
    private String _user = null;
    private String _password = null;
    private int _poolMinSize = 1;
    private int _poolMaxSize = 1;
	
	private OrientDBService() {
		// TODO Auto-generated constructor stub
	}
	
    private synchronized void init() throws Exception {
	        try {
	            _dbURL = AppProperties.getInstance().getStringProperty("orientdb.url");
	            _logger.info(String.format("Use DB at dbURL: %s", _dbURL));
	
	            _user = AppProperties.getInstance().getStringProperty("orientdb.default.admin", "admin");
	            _password = AppProperties.getInstance().getStringProperty("orientdb.default.admin.pwd", "admin");
	            _poolMinSize = AppProperties.getInstance().getIntProperty("orientdb.pool.minimum.size", OrientDBService.DEFAULT_POOL_MIN_SIZE);
	            _poolMaxSize = AppProperties.getInstance().getIntProperty("orientdb.pool.max.size", OrientDBService.DEFAULT_POOL_MAX_SIZE);

	        	_factory = new OrientGraphFactory(_dbURL);
	        	_factory.setupPool(_poolMinSize, _poolMaxSize);
	        } 
	        catch (Exception ex) {
	            _logger.error("Invalid configuration!", ex);
	            throw ex;
	        }
	
//	        try {
//
//	        	_pool = OrientDBHelper.getPool(_dbURL, _user, _password, _poolMinSize, _poolMaxSize, true);
//	        	_logger.debug(String.format("Obtained pool name: %s; pool id: %d", _pool.getName(), _pool.getId()));
//	        } 
//	        catch (InternalServerErrorException ex) {
//	            _logger.error("Initializing database failed", ex);
//	            throw ex;
//	        }
    }
	
	public static synchronized OrientDBService getInstance() throws Exception{
		if(_instance == null){
			_instance = new OrientDBService();
			_instance.init();
		}
		
		return _instance;
	}
	
    public void cleanup() {
    	OrientDBHelper.closePools();
    }
    
    public synchronized OrientGraph getConnection() throws StorageInternalError {
    	return _factory.getTx();
    }
    
    public void close(){
    	_factory.close();
    }

//    public synchronized ODatabaseDocumentTx getConnection() throws StorageInternalError {
//        ODatabaseDocumentTx db = null;
//        int maxRetry = 100;
//        int retryCount = 0;
//        
//        // COMMENT: tento almeno maxRetry connessioni
//        while (db == null && retryCount < maxRetry) {
//            retryCount++;
//            try {
//                db = _pool.acquire(_dbURL, _user, _password);
//                if (retryCount > 1) {
//                    _logger.info("Succeeded in acquiring connection in retry attempt " + retryCount);
//                }
//                retryCount = maxRetry;
//            } 
//            catch (com.orientechnologies.orient.core.exception.ORecordNotFoundException ex) {
//                if (retryCount == maxRetry) {
//                    _logger.error(String.format("Failure in acquiring connection, retried %d times.", retryCount), ex);
//                    throw new StorageInternalError(
//                    		String.format("Failure in acquiring connection from connection's pool, retried %d times: %s", retryCount, ex.getMessage()), ex);
//                } 
//                else {
//                	// COMMENT: ci riprovo...
//                    _logger.info(String.format("Pool acquire failure, retrying - attempt %d", retryCount));
//                    _logger.trace("Pool acquire failure detail ", ex);
//                    try {
//                    	// COMMENT: aspetto prima di ritentare (non si sa mai....)
//                        Thread.sleep(100);
//                    } 
//                    catch (InterruptedException iex) {
//                        // TODO: log?
//                    }
//                }
//            }
//        }
//        return db;
//    }
}
