/**
 * @author Daniele Grignani (dgr)
 * @date Aug 18, 2015
 */

package com.dgr.rat.storage.provider;

public abstract class AbstractStorageProvider {
	private IStorage _storage = null;
	
	public AbstractStorageProvider() {
		// TODO Auto-generated constructor stub
	}
	
	public IStorage getStorage() throws Exception{
		if(_storage == null){
			_storage = this.create();
		}
		
		return _storage;
	}
	
	public IStorage create() throws Exception{
		if(_storage == null){
			_storage = this.createStorage();
		}
		
		return _storage;
	}
	
	public abstract IStorage createStorage() throws Exception;
}
