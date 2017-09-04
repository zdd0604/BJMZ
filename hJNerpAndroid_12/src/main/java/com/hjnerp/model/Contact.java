package com.hjnerp.model;

import java.io.Serializable;
import java.util.Arrays;

public class Contact implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String disPlayName;
	private String phoneNumber;
	private byte[] photos;
	private String contactId;

	public String getDisPlayName() {
		return disPlayName;
	}

	public void setDisPlayName(String disPlayName) {
		this.disPlayName = disPlayName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public byte[] getPhotos() {
		return photos;
	}

	public void setPhotos(byte[] photos) {
		this.photos = photos;
	}

	@Override
	public String toString() {
		return "Contact [disPlayName=" + disPlayName + ", phoneNumber="
				+ phoneNumber + ", photos=" + Arrays.toString(photos)
				+ ", contactId=" + contactId + "]";
	}

}
