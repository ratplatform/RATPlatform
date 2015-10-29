/**
 * @author Daniele Grignani (dgr)
 * @date Aug 18, 2015
 */

package com.dgr.rat.storage.provider;

public class TinkerGraphProvider extends AbstractStorageProvider{
	
	public TinkerGraphProvider() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.dgr.rat.storage.provider.AbstractStorageProvider#createStorage()
	 */
	@Override
	public IStorage createStorage() {
		IStorage result = new TinkerGraphStorage();
		
		return result;
	}

}
