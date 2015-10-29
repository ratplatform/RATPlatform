package com.dgr.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {
	private static ConfigurationManager _instance;
	private Properties _properties;
	
	public static ConfigurationManager getInstance(){
		if(_instance == null){
			_instance = new ConfigurationManager();
		}
		
		return _instance;
	}
	
    public int getIntProperty(String property){
    	int result = -1;
        if (_properties == null){
            return result;
        }

        String stringValue = _properties.getProperty(property);

        if (stringValue != null){
            try{
            	result = Integer.parseInt(stringValue);
            }
            catch (NumberFormatException e){
            	e.printStackTrace();
            	System.out.println("Warning: Number format error in property: " + property);
            }
        }

        return result;
    }
    
    public boolean getBoolProperty(String property){
    	boolean result = false;
        if (_properties == null){
            return result;
        }

        String stringValue = _properties.getProperty(property);

        if (stringValue != null){
            try{
            	result = Boolean.parseBoolean(stringValue);
            }
            catch (NumberFormatException e){
            	e.printStackTrace();
            	System.out.println("Warning: Boolean format error in property: " + property);
            }
        }

        return result;
    }
    
    public String getStringProperty(String property){
    	String result = null;
        if (_properties == null){
            return result;
        }

        result = _properties.getProperty(property);

        return result;
    }
	
	public void init(String configFile) throws FileNotFoundException, IOException{
		_properties = new Properties();
		_properties.load(new FileInputStream(configFile));
	}
}
