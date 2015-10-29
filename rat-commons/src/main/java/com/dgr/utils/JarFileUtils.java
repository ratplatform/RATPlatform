package com.dgr.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class JarFileUtils {
  private static final Class<?>[] parameters = new Class[]{URL.class};
  
	public static String getCurrentJarPath(Class<?> cls) throws UnsupportedEncodingException{
		String decodedPath = null;
		String path = cls.getProtectionDomain().getCodeSource().getLocation().getPath();
		decodedPath = URLDecoder.decode(path, "UTF-8");
		 
		return decodedPath;
	}
	
  @SuppressWarnings("deprecation")
  public static void openLibs(String path, ArrayList<URL> classPath) throws Exception{
    File[] files = FileUtils.listingFiles(true, path, ".jar");
    for (int i = 0; i < files.length; i++) {
      classPath.add(files[i].toURL());
      JarFileUtils.addURL(files[i].toURL());
    }
    
    File dir = new File(path);
    File[] directories = dir.listFiles(new FileTypeFilter(FileTypeFilter.Type.OnlyDirectories));
    for (int i = 0; i < directories.length; i++) {
      JarFileUtils.openLibs(directories[i].getAbsolutePath(), classPath);
    }
  }
  
  public static void addURL(URL u) throws IOException {
    URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
    Class<?> sysclass = URLClassLoader.class;
    try {
        Method method = sysclass.getDeclaredMethod("addURL",parameters);
        method.setAccessible(true);
        method.invoke(sysloader, new Object[]{ u });
    } 
    catch (Throwable t) {
        t.printStackTrace();
        throw new IOException("Error, could not add URL to system classloader");
    }      
  }
	
  public static void unJar(File jarFile, File toDir) throws IOException {
    JarFile jar = new JarFile(jarFile);
    try {

      @SuppressWarnings("rawtypes")
      Enumeration entries = jar.entries();
      while (entries.hasMoreElements()) {
        JarEntry entry = (JarEntry)entries.nextElement();
        if (!entry.isDirectory()) {
          InputStream in = jar.getInputStream(entry);
          try {
            File file = new File(toDir, entry.getName());
            if (!file.getParentFile().mkdirs()) {
              if (!file.getParentFile().isDirectory()) {
                throw new IOException("Mkdirs failed to create " + 
                                      file.getParentFile().toString());
              }
            }
            OutputStream out = new FileOutputStream(file);
            try {
              byte[] buffer = new byte[8192];
              int i;
              while ((i = in.read(buffer)) != -1) {
                out.write(buffer, 0, i);
              }
            } finally {
              out.close();
            }
          } finally {
            in.close();
          }
        }
      }
    } finally {
      jar.close();
    }
  }
	
	public static void addFileToClassPath(File file) throws Exception {
		Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[]{URL.class});
		method.setAccessible(true);
		method.invoke(ClassLoader.getSystemClassLoader(), new Object[]{file.toURI().toURL()});
	}
	
	public static String getStringFile(String jarFilePathName, String filePathNameToFind) throws Exception{
	  StringBuilder sb = null;
	  BufferedReader br = null;
	  try{
  	  InputStream is = JarFileUtils.findFile(jarFilePathName, filePathNameToFind);
  	  sb = new StringBuilder();
      br = new BufferedReader(new InputStreamReader(is));
      String line;
      while ((line = br.readLine()) != null) {
        sb.append(line + "\n");
      } 
      
      br.close();
	  }
    catch(Exception e){
      if(br != null){
        try {
          br.close();
        } catch (IOException e1) {
          // TODO Auto-generated catch block
          e1.printStackTrace();
          throw new Exception(e1);
        }
      }
    }
    
    return sb.toString();
  }
	
	public static InputStream findFile(String jarFilePathName, String filePathNameToFind) throws Exception{
		InputStream result = null;
		JarFile jarFile = null;
		try{
			File file = new File(jarFilePathName);
			jarFile = new JarFile(file);
			for(Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) { 
				String entryName = entries.nextElement().toString();
				//System.out.println(entryName);
				if(entryName.endsWith(filePathNameToFind)){
					ZipEntry entry = jarFile.getEntry(entryName);
					InputStream inStream = jarFile.getInputStream(entry);
					result = inStream;
					break;
				}
			}
			// Se chiamo jarFile.close() chiudo anche inStream
			//jarFile.close();
		}
		catch (Exception e){
			if(jarFile != null){
				jarFile.close();
			}
			
			throw new Exception(e);
		}
		
		return result;
	}
}
