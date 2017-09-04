package com.hjnerp.model;

import java.io.File;
import java.io.Serializable;


public class FileItem implements Serializable {
	private boolean isSelect;
	private File file;
	public boolean isSelect() {
		return isSelect;
	}
	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public FileItem(boolean isSelect, File file) {
		super();
		this.isSelect = isSelect;
		this.file = file;
	}
	public FileItem() {
		super();
	}
	
}
