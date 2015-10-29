package com.dgr.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class AppProperties {
	private static AppProperties _instance = null;
	private Properties _properties = null;
	
	private AppProperties() {
	}
	
	public static AppProperties getInstance(){
		if(_instance == null){
			_instance = new AppProperties();
		}
		
		return _instance;
	}
	
	public void init(String propFile) throws FileNotFoundException, IOException{
		if(_properties == null){
			_properties = new Properties();
		}
		
		_properties.load(new FileInputStream(propFile));
	}
	
	public void read(String propFile) throws FileNotFoundException, IOException{
		_properties = new Properties();
		_properties.load(new FileInputStream(propFile));
	}
	
	public void read(InputStream inputStream) throws IOException{
		_properties = new Properties();
		_properties.load(inputStream);
	}
	
  public void read(Properties properties){
    _properties = properties;
  }
	
	public String getStringProperty(String key, String defaultValue){
		String result = null;
		if(_properties != null){
			result = _properties.getProperty(key);
			if(result == null || result.length() < 1 && defaultValue != null){
				result = defaultValue;
			}
		}
		
		return result;
	}
	
	public String getStringProperty(String key){
		String result = null;
		if(_properties != null){
			result = _properties.getProperty(key);
		}
		
		return result;
	}
	
  public long getLongProperty(String key){
    long result = -1;
    if(_properties != null){
      String text = _properties.getProperty(key);
		try{
			result = StringUtils.parseToLong(text);
		}
		catch(Exception e){
			result = -1;
			e.printStackTrace();
		}
    }
    
    return result;
  }
	
	public int getIntProperty(String key){
		int result = -1;
		if(_properties != null){
			String text = _properties.getProperty(key);
			try{
				result = StringUtils.parse(text);
			}
			catch(Exception e){
				result = -1;
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public int getIntProperty(String key, int defaultValue){
		int result = -1;
		if(_properties != null){
			String text = _properties.getProperty(key);
			try{
				result = StringUtils.parse(text);
			}
			catch(Exception e){
				result = defaultValue;
				//e.printStackTrace();
			}
		}
		
		return result;
	}
}
