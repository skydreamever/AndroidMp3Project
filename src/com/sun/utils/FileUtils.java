package com.sun.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtils {
	private String SDCardRoot;


	public FileUtils() {

		SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator;
	}

	
	public File creatFileInSDCard(String fileName,String dir) throws IOException {
		File file = new File(SDCardRoot + dir + File.separator+fileName);
		file.createNewFile();
		return file;
	}
	

	
	public File creatSDDir(String dir) {
		File dirFile = new File(SDCardRoot + dir+File.separator);
		dirFile.mkdirs();
		return dirFile;
	}

	
	public boolean isFileExist(String fileName,String path){
		File file = new File(SDCardRoot + path +File.separator+fileName);
		return file.exists();
	}
	

	public File write2SDFromInput(String path,String fileName,InputStream input){
		File file = null;
		OutputStream output = null;
		try{
			creatSDDir(path);
			file = creatFileInSDCard(fileName,path);
			output = new FileOutputStream(file);
			byte buffer [] = new byte[4 * 1024];
			int temp;
			while((temp=input.read(buffer)) != -1){
				
				output.write(buffer,0,temp);
			}
			output.flush(); 
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				output.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return file;
	}

}