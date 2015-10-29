/**
 * @author Daniele Grignani (dgr)
 * @date Aug 18, 2015
 */

package com.dgr.rat.storage.provider;

public class StorageBridge {
	private static StorageBridge _instance = null;
	private AbstractStorageProvider _storageProvider = null;
	
	private StorageBridge() {
		// TODO Auto-generated constructor stub
	}
	
	public static StorageBridge getInstance() throws Exception{
		if(_instance == null){
			_instance = new StorageBridge();
		}
		
		return _instance;
	}
	
	public IStorage getStorage() throws Exception{
		if(_storageProvider == null){
			// TODO da gestire
			throw new Exception();
		}
		
		IStorage result = _storageProvider.getStorage();
		
		return result;
	}
	
	public void init(StorageType storageType) throws Exception{
		if(_storageProvider == null){
			switch(storageType){
			case OrientDB:
				_storageProvider = new OrientDBProvider();
				break;
			case TinkerGraph:
				_storageProvider = new TinkerGraphProvider();
				break;
			case Unknown:
				break;
			default:
				// TODO: add exception
				break;
			
			}
			
			_storageProvider.create();
		}
	}

}
