package com.unbosque.wcsc.misestructuras;

import com.unbosque.wcsc.entities.UserSheets;

public class Nodo {
	
	private UserSheets element;
	
	private Nodo siguiente;
	
	public Nodo(UserSheets pElement)
	{
		element=pElement;
		siguiente=null;
	}
	
	public int getNumber()
	{
		return element.getNumberSheets();
	}

	public UserSheets getElement() {
		return element;
	}

	public void setElement(UserSheets element) {
		this.element = element;
	}

	public Nodo getSiguiente() {
		return siguiente;
	}

	public void setSiguiente(Nodo siguiente) {
		this.siguiente = siguiente;
	}
	
	

}
