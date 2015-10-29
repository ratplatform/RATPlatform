/**
 * @author Daniele Grignani (dgr)
 * @date Aug 18, 2015
 */

package com.dgr.rat.storage.provider;

import com.dgr.rat.storage.orientdb.StorageInternalError;

public class OrientDBProvider extends AbstractStorageProvider{
	
	public OrientDBProvider() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.AbstractStorageProvider#createStorage()
	 */
	@Override
	public IStorage createStorage() throws Exception {
		IStorage result = new OrientDBStorage();
		
		return result;
	}

}
