/**
 * @author Daniele Grignani (dgr)
 * @date Aug 27, 2015
 */

package com.dgr.rat.tests;

import java.nio.file.FileSystems;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.dgr.rat.commons.constants.RATConstants;
import com.dgr.rat.commons.utils.RATUtils;
import com.dgr.rat.json.toolkit.RATHelpers;
import com.dgr.rat.main.SystemCommandsInitializer;
import com.dgr.rat.storage.provider.IStorage;
import com.dgr.rat.storage.provider.StorageBridge;

public class LoadCommandsTest {
	private IStorage _storage = null;
	
	@Test
	public void test() {
		SystemCommandsInitializer systemCommandsInitializer = TestHelpers.getSystemCommandsInitializer();
		try {
			systemCommandsInitializer.addCommandTemplates();
			_storage = StorageBridge.getInstance().getStorage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Before
	public void init(){
		try {
			RATUtils.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + "unittest.properties");
			RATUtils.initProperties(RATConstants.ConfigurationFolder + FileSystems.getDefault().getSeparator() + RATConstants.PropertyFileName);
			
//			StorageType storageType = StorageType.TinkerGraph;
//			StorageBridge.getInstance().init(storageType);
//			_storage = StorageBridge.getInstance().getStorage();
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@After
	public void verify(){
		// Anche in caso di exception voglio che scriva lo stesso i risultati per vederli
		try {
			@SuppressWarnings("unused")
			String resultFilename = this.getClass().getSimpleName() + "Result";
//			TestHelpers.writeGraphToHTML(resultFilename);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
