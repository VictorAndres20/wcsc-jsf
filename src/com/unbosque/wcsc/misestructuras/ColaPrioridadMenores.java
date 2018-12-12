package com.unbosque.wcsc.misestructuras;

import java.util.ArrayList;
import java.util.List;

import com.unbosque.wcsc.entities.UserSheets;

public class ColaPrioridadMenores {
	
	private Nodo primero;
	
	public ColaPrioridadMenores()
	{
		primero=null;
	}
	
	public boolean addNodo(UserSheets pUser)
	{
		boolean success=false;
		Nodo nodo=primero;
		
		if(nodo==null)
		{
			primero=new Nodo(pUser);
		}
		else
		{
			
			while(nodo!=null)
			{
				if(nodo.getNumber()>=pUser.getNumberSheets())
				{
					if(nodo.equals(primero))
					{
						Nodo aux=primero;						
						primero=new Nodo(pUser);
						primero.setSiguiente(aux);
						primero.getSiguiente().setSiguiente(aux.getSiguiente());
						break;
						
					}
					else
					{
						Nodo aux=nodo;						
						nodo=new Nodo(pUser);
						nodo.setSiguiente(aux);
						nodo.getSiguiente().setSiguiente(aux.getSiguiente());
						break;
					}
				}
				
				nodo=nodo.getSiguiente();
			}
		}
		
		
		
		return success;
	}
	
	public UserSheets searchNodo(UserSheets pUser)
	{
		UserSheets user=null;
		Nodo nodo=primero;
		while(nodo!=null)
		{
			if(nodo.getElement().equals(pUser))
			{
				user=nodo.getElement();
				break;
			}
			nodo=nodo.getSiguiente();
		}
		
		return user;
	}

	public Nodo getPrimero() {
		return primero;
	}

	public void setPrimero(Nodo primero) {
		this.primero = primero;
	}
	
	public List<UserSheets> getAsList()
	{
		
		List<UserSheets> list=new ArrayList<>();
		Nodo nodo=primero;
		int cont =0;
		while(nodo!=null && cont<5)
		{
			list.add(nodo.getElement());
			nodo=nodo.getSiguiente();
			cont++;
		}		
		
		return list;
	}

}
