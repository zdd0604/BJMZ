package com.hjnerp.util;

 
  
public class ImageFileHelper
{
	private static ImageFileHelper imageFileHelper;
	private ImageFileHelper() { 
	}
	public static ImageFileHelper getInstance() {
		if (imageFileHelper == null) {
			imageFileHelper = new ImageFileHelper();
		}
		return imageFileHelper;
	}
	String fileName;
	
	public String getFileName()
	{
		return fileName;
	}
	public void setFileName(String file)
	{
		fileName = file;
	}
}
