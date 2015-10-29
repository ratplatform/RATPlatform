package com.dgr.utils;

import java.io.File;
import java.io.FileFilter;

public class FileTypeFilter implements FileFilter{
	public enum Type{
		AllFiles,
		OnlyDirectories,
		TypeOfFiles
	}
	private String _extension;
	private Type _type;
	
	public FileTypeFilter(String extension) {
		_type = Type.TypeOfFiles;
		String cleanString = extension.replaceAll("[^\\p{L}\\p{N}]", "");
		_extension = "." + cleanString;
	}
	
	public FileTypeFilter(Type type) {
		_type = type;
	}

	public boolean accept(File pathname) {
		boolean result = false;
		switch(_type){
		case AllFiles:
		    if (pathname.isFile()){
		    	result = true;
		    }
			break;
			
		case OnlyDirectories:
		    if (pathname.isDirectory()){
		    	result = true;
		    }
			break;
			
		case TypeOfFiles:
		    if (pathname.getName().endsWith(_extension)){
		    	result = true;
		    }
			break;
		}

	    return result;
	}
}
