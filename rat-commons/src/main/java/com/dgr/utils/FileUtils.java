package com.dgr.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class FileUtils {
	
	
	public static void copyfile(String sourceFile, String destinationFile){
		try{
			File f1 = new File(sourceFile);
			File f2 = new File(destinationFile);
			InputStream in = new FileInputStream(f1);
			
			OutputStream out = new FileOutputStream(f2);
	
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0){
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
			System.out.println("File copied.");
		}
		catch(FileNotFoundException ex){
			System.out.println(ex.getMessage() + " in the specified directory.");
			System.exit(0);
		}
		catch(IOException e){
			System.out.println(e.getMessage());  
		}
	}
	
	public static File[] listingFiles(boolean recursive, String path, String extension){
		File dir = new File(path);
		File[] files = dir.listFiles(new FileTypeFilter(extension));
		List<File> list = new ArrayList<File>();
		
		if(recursive){
  		File[] directories = dir.listFiles(new FileTypeFilter(FileTypeFilter.Type.OnlyDirectories));
  		if (directories != null) {
  		    for (int i = 0; i < directories.length; i++) {
  		    	FileUtils.listingFiles(list, directories[i], extension);
  		    }
  		}
		}
		
		if (files != null) {
		    for (int i = 0; i < files.length; i++) {
		    	list.add(files[i]);
		    }
		}
		
		return list.toArray(new File[list.size()]);
	}
	
	private static void listingFiles(List<File> list, File dir, String extension){
		File[] files = dir.listFiles(new FileTypeFilter(extension));
		File[] directories = dir.listFiles(new FileTypeFilter(FileTypeFilter.Type.OnlyDirectories));
		
		if (directories != null) {
		    for (int i = 0; i < directories.length; i++) {
		    	FileUtils.listingFiles(list, directories[i], extension);
		    }
		}
		
		if (files != null) {
		    for (int i = 0; i < files.length; i++) {
		    	list.add(files[i]);
		    }
		}
	}

	public static boolean fileExists(String path){
		boolean exists = (new File(path)).exists();
		return exists;
	}
	
	static public boolean deleteDirectory(File path) {
		if(path.exists() ) {
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					files[i].delete();
				}
			}
		}
		
		return(path.delete());
	}
	
	static public boolean deleteDirectory(String path) {
		return FileUtils.deleteDirectory(new File(path));
	}

	
	public static boolean createDir(String path){
		boolean success = (new File(path)).mkdir();
		return success;
	}
	
	public static void write (String filename, String text, boolean append) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filename, append));
			bw.write(text);
			bw.newLine();
			bw.flush();
		} 
		catch (IOException ioe) {
			ioe.printStackTrace();
		} 
		finally {
			if (bw != null) try {
				bw.close();
			} 
			catch (IOException ioe2) {
			}
		}
	}
	
	public static int countFiles(String directoryPath){
		if(!FileUtils.fileExists(directoryPath)){
			return -1;
		}
		
		int result = 0;
		File dir = new File(directoryPath);
		String[] children = dir.list();
		if (children != null) {
		    result = children.length;
		}
		
		return result;
	}
	
	public static String fileRead(String path) throws Exception {
		if(!FileUtils.fileExists(path)){
			throw new Exception("File " + path + " does not exist!");
		}
		
		BufferedReader br = null;
		String result = "";
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(path));
			while ((sCurrentLine = br.readLine()) != null) {
				result += sCurrentLine;
			}
 
		} 
		catch (IOException e) {
			result = null;
			e.printStackTrace();
		} 
		finally {
			try {
				if (br != null)br.close();
			} 
			catch (IOException ex) {
				result = null;
				ex.printStackTrace();
			}
		}
		
		return result;
	}
	
	public static String fileRead(File file) throws Exception {
		if(!file.exists()){
			throw new Exception("File " + file.getAbsolutePath() + " does not exist!");
		}
		
		BufferedReader br = null;
		String result = "";
		try {
			String sCurrentLine;
			br = new BufferedReader(new FileReader(file));
			while ((sCurrentLine = br.readLine()) != null) {
				result += sCurrentLine;
			}
 
		} 
		catch (IOException e) {
			result = null;
			e.printStackTrace();
		} 
		finally {
			try {
				if (br != null)br.close();
			} 
			catch (IOException ex) {
				result = null;
				ex.printStackTrace();
			}
		}
		
		return result;
	}
}
