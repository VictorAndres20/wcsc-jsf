package com.unbosque.wcsc.entities;

public class UserSheets {
	
	/**
	 * Nombre del usuario
	 */
	private String userName;
	
	/**
	 * NUmero de laminas
	 */
	private int numberSheets;
	
	public UserSheets(String pUser, int quantity)
	{
		userName=pUser;
		numberSheets=quantity;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getNumberSheets() {
		return numberSheets;
	}

	public void setNumberSheets(int numberSheets) {
		this.numberSheets = numberSheets;
	}
	
	/**
	 * Meotod que retorna el objeto en forma de String
	 */
	public String toString()
	{
		return "["+userName+" , "+numberSheets+"]";
	}

}
