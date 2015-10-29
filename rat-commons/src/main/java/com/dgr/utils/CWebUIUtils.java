/**
 * Document   : CWebUIUtils
 * Created on : Sep 7, 2009, 12:30:42 AM
 * Author     : Daniele Grignani
 */

package com.dgr.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CWebUIUtils {
    public static void memoryDump(){
        long mem = Runtime.getRuntime().totalMemory();
        System.out.println("Actual memory: " + mem);
    }

    public static String getRandomNumber(){
        Random rand = new Random();
        int randomNum = rand.nextInt();

        String result = String.valueOf(randomNum);
        int pos = result.indexOf("-");
        if(pos > -1){
            result = result.substring(pos + 1);
        }

        return result;
    }
    
    public static int getIntegerRandomNumber(){
        Random rand = new Random();
        int randomNum = rand.nextInt();

        return randomNum;
    }


//    public static Date getTime(String time, String format){
//        DateFormat formatter = new SimpleDateFormat(format);
//        Date dateOut = null;
//        try {
//            dateOut = (Date) formatter.parse(date);
//        }
//        catch (ParseException ex) {
////            Logger.getLogger(CWebUIUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        finally{
//            return dateOut;
//        }
//    }

    // Only for test
//    public static String getType(String typeURI){
//        String result = "";
//        if(null != typeURI){
//            int pos = typeURI.indexOf("#");
//            if(pos > -1){
//                result = typeURI.substring(pos + 1);
//            }
//        }
//        return result;
//    }

//    public static String getDate(String text, String separator){
//        String result = text;
//        int pos = text.lastIndexOf(separator);
//        if(pos > 0){
//            result = ((String)text).substring(0, pos);
//        }
//
//        return result;
//    }

//    public static String getTime(String text, String separator){
//        String result = text;
//        int pos = text.lastIndexOf(separator);
//        if(pos > 0){
//            result = ((String)text).substring(pos + 1, ((String)text).length());
//        }
//        pos = result.indexOf("+");
//        if(pos > 0){
//            result = result.substring(0, pos);
//        }
//
//        return result;
//    }

    // Only for test
    public static void getSystemProperties() {
        Properties systemP = System.getProperties();
        Enumeration e = systemP.keys();
        while (e.hasMoreElements()) {
            Object name = e.nextElement();
            String value = systemP.get(name).toString();
            System.out.println("Property name: " + name.toString());
            System.out.println("Property value: " + value);
        }
    }

    public static String getMessage(String key){
        String message = key;
        if(resourceBundleContainsKey(key)){
            ResourceBundle bundle = ResourceBundle.getBundle("Messages");
            message = bundle.getString(key);
        }

        return message;
    }

    public static String getPropertyMessage(String attributeName){
        String message = attributeName;
        String key = "property." + attributeName;
        if(resourceBundleContainsKey(key)){
            ResourceBundle bundle = ResourceBundle.getBundle("Messages");
            message = bundle.getString(key);
        }

        return message;
    }

    private static boolean resourceBundleContainsKey(String key){
        boolean result = false;
        ResourceBundle bundle = ResourceBundle.getBundle("Messages");
        Enumeration enumeration = bundle.getKeys();
        while (enumeration.hasMoreElements()){
            String propName = (String)enumeration.nextElement();
            if(0 == propName.compareTo(key)){
                result = true;
                break;
            }
        }

        return result;
    }

    // In alcune circostanze la URL definita nel repository non 
    // è quella corretta e il sito effettua una redirect; in questi casi
    // libManentParser.so si pianta. Per avere la URL corretta:
    @SuppressWarnings("finally")
	public static String getRedirectedUrl(String theURL) {
    	String result = null;
        try {
            URL url = new URL(theURL);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            for (int i=0; ; i++) {
                String headerValue = connection.getHeaderField(i);
                if(headerValue.contains("200 OK")){
                    result = connection.getURL().toString();
                    break;
                }
                if (headerValue == null) {
                    break;
                }
            }
        }

        catch (MalformedURLException ex) {
            Logger.getLogger(CWebUIUtils.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (IOException ex) {
            Logger.getLogger(CWebUIUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            return result;
        }
    }

    public static boolean isUrl(String theURL){
    	if(theURL.startsWith("http://")){
    		return true;
    	}
    	else if(theURL.startsWith("https://")){
    		return true;
    	}
    	
    	return false;
    }
    
    @SuppressWarnings("finally")
	public static boolean urlExist(String theURL) {
        boolean result = false;
        try {
            URL url = new URL(theURL);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            for (int i=0; ; i++) {
                String headerValue = connection.getHeaderField(i);
                System.out.println(connection.getURL());
                // In alcune circostanze la URL definita nel repository non 
                // è quella corretta e il sito effettua una redirect; in questi casi
                // libManentParser.so si pianta. Per avere la URL corretta:
                theURL = connection.getURL().toString();
                if(headerValue.contains("200 OK")){
                    result = true;
                    break;
                }
                if (headerValue == null) {
                    break;
                }
            }
        }

        catch (MalformedURLException ex) {
            Logger.getLogger(CWebUIUtils.class.getName()).log(Level.SEVERE, null, ex);
        }        catch (IOException ex) {
            Logger.getLogger(CWebUIUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            return result;
        }
    }
    
    public static String getHostFromURL(String url){
    	HashMap<String, String> tempUri = CWebUIUtils.parseURL(url);
    	String result = null;
    	
    	if(tempUri.containsKey("host")){
    		result = tempUri.get("host");
    	}
    	
    	return result;
    }
    
    public static HashMap<String, String> parseURL(String url) {
    	HashMap<String, String> tempUri = new HashMap<String, String>(14);
    	String[] parts = {"source","protocol","authority","userInfo","user","password","host","port","relative","path","directory","file","query","ref"};
    	
    	boolean strictMode = false;
    	Pattern pattern;
    	if(strictMode) {
    		pattern = Pattern.compile("^(?:([^:/?#]+):)?(?://((?:(([^:@]*):?([^:@]*))?@)?([^:/?#]*)(?::(\\d*))?))?((((?:[^?#/]*/)*)([^?#]*))(?:\\?([^#]*))?(?:#(.*))?)");
    	} else {
    		pattern = Pattern.compile("^(?:(?![^:@]+:[^:@/]*@)([^:/?#.]+):)?(?://)?((?:(([^:@]*):?([^:@]*))?@)?([^:/?#]*)(?::(\\d*))?)(((/(?:[^?#](?![^?#/]*\\.[^?#/.]+(?:[?#]|$)))*/?)?([^?#/]*))(?:\\?([^#]*))?(?:#(.*))?)");
    	}
    	Matcher matcher = pattern.matcher(url);
    	String match;
    	if(matcher.find()) {
    		for(int i=0;i<14;i++) {
    			try {
    				match = matcher.group(i);
    			} catch(Exception ex) {
    				match = "*";
    			}
    			tempUri.put(parts[i],  match == null ? "*" : match);
    		}
    	}
//    	System.out.println(tempUri.get("protocol"));
//    	System.out.println(tempUri.get("user"));
//    	System.out.println(tempUri.get("password"));
//    	System.out.println(tempUri.get("host"));
//    	System.out.println(tempUri.get("directory"));
//    	System.out.println(tempUri.get("file"));
//    	System.out.println(tempUri.get("query"));
//    	System.out.println(tempUri.get("ref"));
    	
    	return tempUri;
    }
    
    public static String downloadURL(String address) throws Exception {
      String result = null;
      HttpURLConnection connection = null;
      BufferedReader bufferedReader = null;
      
      try{
        StringBuilder text = new StringBuilder();
        URL url = new URL(address);
        connection = (HttpURLConnection)url.openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setRequestMethod("GET");
        connection.connect();
        
        bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream())); 
        String line = null;
        while((line = bufferedReader.readLine()) != null){
            text.append(line);
        }
        System.out.println(url + "=> "+ connection.getResponseCode());
        result = text.toString();
        
        connection.disconnect();
        connection = null;
        bufferedReader = null;
      }
      catch(Exception e){
        String msg = "";
        if(connection != null){
          msg = "Connection response " + connection.getResponseCode();
          connection.disconnect();
          connection = null;
          bufferedReader = null;
        }
        
        throw new Exception(msg, e);
      }
      
      return result;
    }
    
//    public static String downloadURL(String address) throws Exception {
//    	String result = "";
//    	BufferedReader bufferedReader = null;
//    	
//	    try {
//		    URL url = new URL(address);
//		
//		    URLConnection urlConnection = url.openConnection();
//		    bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//		    String inputLine = null;
//	        while ((inputLine = bufferedReader.readLine()) != null){
//	            //System.out.println(inputLine);
//	        	result += inputLine;
//	        }
//	        bufferedReader.close();
//		    System.out.println("Downloaded Successfully.");
//	    }
//	    catch (Exception e) {
//	    	//e.printStackTrace();
//	    	throw new Exception();
//	    }
//	    
//	    finally {
//		    try {
//		    	if(bufferedReader != null){
//		    		bufferedReader.close();
//		    		bufferedReader = null;
//		    	}
//		    }
//		    catch (IOException e) {
//		    	//e.printStackTrace();
//		    	throw new Exception();
//		    }
//	    }
//	    
//	    return result;
//    }
    
    public static void downloadURL(String address, String destinationFile) {
	    OutputStream outStream = null;
	    URLConnection  uCon = null;
	    InputStream is = null;
	    
	    try {
		    byte[] buf;
		    int ByteRead,ByteWritten=0;
		    URL url = new URL(address);
		    outStream = new BufferedOutputStream(new FileOutputStream(destinationFile));
		
		    uCon = url.openConnection();
		    is = uCon.getInputStream();
		    buf = new byte[1024];
		    while ((ByteRead = is.read(buf)) != -1) {
			    outStream.write(buf, 0, ByteRead);
			    ByteWritten += ByteRead;
		    }
		    System.out.println("Downloaded Successfully.");
		    System.out.println("File name:\""+destinationFile+ "\"\nNo ofbytes :" + ByteWritten);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	    finally {
		    try {
			    is.close();
			    outStream.close();
		    }
		    catch (IOException e) {
		    	e.printStackTrace();
		    }
	    }
    }

//    public static void downloadUrl (String url) {
//
//        //-----------------------------------------------------//
//        //  Step 1:  Start creating a few objects we'll need.
//        //-----------------------------------------------------//
//
//        URL u;
//        InputStream is = null;
//        DataInputStream dis;
//        String s;
//
//        try {
//
//           //------------------------------------------------------------//
//           // Step 2:  Create the URL.                                   //
//           //------------------------------------------------------------//
//           // Note: Put your real URL here, or better yet, read it as a  //
//           // command-line arg, or read it from a file.                  //
//           //------------------------------------------------------------//
//
//           u = new URL(url);
//
//           //----------------------------------------------//
//           // Step 3:  Open an input stream from the url.  //
//           //----------------------------------------------//
//
//           is = u.openStream();         // throws an IOException
//
//           //-------------------------------------------------------------//
//           // Step 4:                                                     //
//           //-------------------------------------------------------------//
//           // Convert the InputStream to a buffered DataInputStream.      //
//           // Buffering the stream makes the reading faster; the          //
//           // readLine() method of the DataInputStream makes the reading  //
//           // easier.                                                     //
//           //-------------------------------------------------------------//
//
//           dis = new DataInputStream(new BufferedInputStream(is));
//
//           //------------------------------------------------------------//
//           // Step 5:                                                    //
//           //------------------------------------------------------------//
//           // Now just read each record of the input stream, and print   //
//           // it out.  Note that it's assumed that this problem is run   //
//           // from a command-line, not from an application or applet.    //
//           //------------------------------------------------------------//
//
//           while ((s = dis.readLine()) != null) {
//              System.out.println(s);
//           }
//
//        } catch (MalformedURLException mue) {
//
//           System.out.println("Ouch - a MalformedURLException happened.");
//           mue.printStackTrace();
//           System.exit(1);
//
//        } catch (IOException ioe) {
//
//           System.out.println("Oops- an IOException happened.");
//           ioe.printStackTrace();
//           System.exit(1);
//
//        } finally {
//
//           //---------------------------------//
//           // Step 6:  Close the InputStream  //
//           //---------------------------------//
//
//           try {
//              is.close();
//           } catch (IOException ioe) {
//              // just going to ignore this one
//           }
//
//        } // end of 'finally' clause
//
//     }  // end of main
    
    public static String getIPV4() throws UnknownHostException, SocketException{
    	String result = null;
    	
    	Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
    	for (NetworkInterface iface : Collections.list(ifaces)) {
//	    	Enumeration<NetworkInterface> virtualIfaces = iface.getSubInterfaces();
//	    	for (NetworkInterface viface : Collections.list(virtualIfaces)) {
//		    	System.out.println(iface.getDisplayName() + " VIRT " + viface.getDisplayName());
//		    	Enumeration<InetAddress> vaddrs = viface.getInetAddresses();
//		    	for (InetAddress vaddr : Collections.list(vaddrs)) {
//		    		System.out.println("\t" + vaddr.toString());
//		    	}
//	    	}
	    	
	    	System.out.println("Real iface addresses: " + iface.getDisplayName());
	    	Enumeration<InetAddress> raddrs = iface.getInetAddresses();
	    	for (InetAddress raddr : Collections.list(raddrs)) {
	    		
	    		if (!raddr.isLoopbackAddress() && raddr.isSiteLocalAddress() && !(raddr instanceof Inet6Address)) {
	    			result = raddr.getHostAddress();
//	    			System.out.println("\t" + raddr.toString());
	    			break;
	    		}
	    	}
    	}
    	
    	return result;
    }

}