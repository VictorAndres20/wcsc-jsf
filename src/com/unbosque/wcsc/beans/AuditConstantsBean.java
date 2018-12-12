package com.unbosque.wcsc.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.unbosque.wcsc.util.Constantes;

@ManagedBean
@ViewScoped
public class AuditConstantsBean {

	/**
	 * Lista de nombres de las tablas
	 */
	List<String> auditTableNames;
	
	/**
	 * Constructor que inicializa la lista de nombres de las tablas y agrega los nombres
	 */
	public AuditConstantsBean()
	{
	
		auditTableNames=new ArrayList<>();		
		auditTableNames.add(Constantes.TABLE_USER);
		auditTableNames.add(Constantes.TABLE_MISSIN_SHEET);
		auditTableNames.add(Constantes.TABLE_REPEATED_SHEET);
		auditTableNames.add(Constantes.TABLE_NEWS);
		
	}

	/**
	 * Obtiene lista de los nombres de las tablas
	 * @return - Lista de los nombres de las Tablas
	 */
	public List<String> getAuditTableNames() {
		return auditTableNames;
	}

	/**
	 * Cambia la lista los nombres de las tablas
	 * @param auditTableNames - Nueva lista 
	 */
	public void setAuditTableNames(List<String> auditTableNames) {
		this.auditTableNames = auditTableNames;
	}
}
