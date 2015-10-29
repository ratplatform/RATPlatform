/*
 * @author Daniele Grignani
 * Apr 11, 2015
*/

package com.dgr.rat.storage.orientdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.dgr.rat.commons.errors.InternalServerErrorException;
import com.orientechnologies.orient.core.config.OGlobalConfiguration;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.metadata.security.ORole;
import com.orientechnologies.orient.core.metadata.security.OSecurity;
import com.orientechnologies.orient.core.metadata.security.OUser;

public class OrientDBHelper {
	final static Logger _logger = Logger.getLogger(OrientDBHelper.class.getName());
	private static Map<String, ODatabaseDocumentPool> _pools = new HashMap<String, ODatabaseDocumentPool>();
	
    public synchronized static void closePools() {
        _logger.debug("Close DB pools");
        for (ODatabaseDocumentPool pool : _pools.values()) {
            try {
                pool.close();
                _logger.trace("Closed DB pool name " + pool.getName() + "; pool id: " + pool.getId());
            } 
            catch (Exception ex) {
                _logger.info("Failure reported in closing pool " + pool.getName() + "; pool id: " + pool.getId(), ex);
            }
        }
        _pools = new HashMap<String, ODatabaseDocumentPool>();
    }
	
    public synchronized static void closePool(String dbUrl, ODatabaseDocumentPool pool) {
        _logger.debug("Close DB pool for " + dbUrl + "; pool name " + pool.getName() + "; pool id: " + pool.getId());
        try {
            _pools.remove(dbUrl);
            pool.close();
            _logger.debug("Closed DB pool for " + dbUrl + "; pool name " + pool.getName() + "; pool id: " + pool.getId());
        } 
        catch (Exception ex) {
            _logger.error("Failure reported in closing pool " + dbUrl + "; pool name " + pool.getName() + "; pool id: " + pool.getId(), ex);
        }
    }
	
    public static ODatabaseDocumentPool getPool(String dbURL, String user, String password, int minSize, int maxSize, boolean setupDB) throws InternalServerErrorException {
        ODatabaseDocumentTx setupDbConn = null;
        ODatabaseDocumentPool pool = null;
        try {       
            if (setupDB) {
            	_logger.debug("Check if DB exists in correct state for pool " + dbURL);
                setupDbConn = OrientDBHelper.checkDB(dbURL, user, password);
            }
            _logger.debug("Getting pool " + dbURL);
            pool = _pools.get(dbURL);
            if (pool == null) {
                pool = OrientDBHelper.initPool(dbURL, user, password, minSize, maxSize);
                _pools.put(dbURL, pool);
            }
        } 
        catch (Exception e){
        	// TODO: gestire e log
        	e.printStackTrace();
        }
        finally {
            if (setupDbConn != null) {
                setupDbConn.close();
            }
        }
        
        return pool;
    }
    
    private static ODatabaseDocumentTx checkDB(String dbURL, String user, String password) throws InternalServerErrorException {
        ODatabaseDocumentTx db = new ODatabaseDocumentTx(dbURL);
        try{
	        if (isLocalDB(dbURL) || isMemoryDB(dbURL)) {
	            if (db.exists()) {
	                _logger.info("Using DB at " + dbURL);
	                db.open(user, password); 
	            } 
	            else { 
	                _logger.info("DB does not exist: creating in " + dbURL);
	                try{
	                	db.create();
	                }
	                catch (Exception e){
	                	// TODO: gestire e log
	                	e.printStackTrace();
	                }
	                // COMMENT rimuovo l'admin di default
	                OSecurity security = db.getMetadata().getSecurity();
	                security.dropUser(OUser.ADMIN);
	                // COMMENT: new admin user con nuova username and password
	                security.createUser(user, password, security.getRole(ORole.ADMIN));
	            } 

	        } 
	        else {
	            _logger.info("Using DB at " + dbURL);
	        }
        }
        catch (Exception e){
        	// TODO: gestire e log
        	e.printStackTrace();
        }
        
        return db;
    }
    
    private static ODatabaseDocumentPool initPool(String dbURL, String user, String password, int minSize, int maxSize) throws InternalServerErrorException {
        _logger.trace("Initializing DB Pool " + dbURL);
        
        // COMMENT transaction log
        OGlobalConfiguration.TX_USE_LOG.setValue(true);
        // COMMENT disk sync per il commit 
        OGlobalConfiguration.TX_COMMIT_SYNCH.setValue(true);
        OGlobalConfiguration.STORAGE_KEEP_OPEN.setValue(false);
        
        ODatabaseDocumentPool pool = new ODatabaseDocumentPool();
        pool.setup(minSize, maxSize);
        OrientDBHelper.warmUpPool(pool, dbURL, user, password, 1);
        
        _logger.debug("Pool " + pool + " is OK");

        return pool;
    }
    
    private static void warmUpPool(ODatabaseDocumentPool pool, String dbURL, String user, String password, int minSize) {
        _logger.trace("Warming up pool up to minSize " + Integer.valueOf(minSize));
        
        List<ODatabaseDocumentTx> list = new ArrayList<ODatabaseDocumentTx>();
        for (int count = 0; count < minSize; count++) {
            _logger.trace("Warming up entry " + Integer.valueOf(count));
            try {
                list.add(pool.acquire(dbURL, user, password));
            } 
            catch (Exception ex) {
                // TODO: log
            }
        }
        for (ODatabaseDocumentTx entry : list) {
            try {
                if (entry != null) {
                    entry.close();
                }
            } 
            catch (Exception ex) {
                _logger.warn("Problems in connection close during warming up db pool, entry " + entry, ex);
            }
        }
    }
    
    private static boolean isLocalDB(String dbURL) throws InternalServerErrorException {
        if (dbURL == null) {
            throw new InternalServerErrorException("dbURL is not set");
        }
        return dbURL.startsWith("local:") || dbURL.startsWith("plocal");
    }
    
    private static boolean isMemoryDB(String dbURL) throws InternalServerErrorException {
    	if (dbURL == null) {
    		throw new InternalServerErrorException("dbURL is not set");
    	}
    	return dbURL.startsWith("memory:");
    }
}
