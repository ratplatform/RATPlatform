package com.dgr.utils;

import java.util.regex.*;

public class StringUtils {
	public static String replace(CharSequence inputStr, String patternStr, String replacementStr){
        Pattern pattern = Pattern.compile(patternStr);
       
        Matcher matcher = pattern.matcher(inputStr);
        String output = matcher.replaceAll(replacementStr);
        
        return output;
	}
	
  public static boolean isParsableToLong(String i){
    try
    {
      Long.parseLong(i);
      return true;
    }
    catch(NumberFormatException nfe)
    {
      return false;
    }
  }
  
  public static long parseToLong(String value){
    long result = -1;
    if(StringUtils.isParsableToLong(value)){
      result = Long.parseLong(value);
    }
    return result;
  }
	
	public static boolean isParsableToInt(String i){
		try
		{
			Integer.parseInt(i);
			return true;
		}
		catch(NumberFormatException nfe)
		{
			return false;
		}
	}
	
	public static int parse(String value){
		int result = -1;
		if(StringUtils.isParsableToInt(value)){
			result = Integer.parseInt(value);
		}
		return result;
	}
}
