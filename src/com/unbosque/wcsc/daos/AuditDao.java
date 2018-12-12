package com.unbosque.wcsc.daos;

import java.util.List;

import com.unbosque.wcsc.entities.Audit;

public interface AuditDao {
	
	/**
	 * Registra un nuevo Audit en la base de datos
	 * @param object - audit
	 * @return - true o false
	 */
	public boolean save(Audit object);

	/**
	 * Obtiene un objeto de tipo Audit
	 * @param id - id del objeto de tipo Audit
	 * @return - Objeto de tipo Audit
	 */
	public Audit getObject(long id);

	/**
	 * Da una lista de todos los objetos de tipo Audit
	 * @return - List
	 */
	public List<Audit> list();

	/**
	 * Elimina un registro de la base d edatos
	 * @param object - Audit
	 * @return - true o flase
	 */
	public boolean remove(Audit object);

	/**
	 * Actualiza un registro de la base de datos
	 * @param object - Objeto de tipo Audit
	 * @return - true o false
	 */
	public boolean update(Audit object);

}
