package com.unbosque.wcsc.daos;

import java.util.List;

import com.unbosque.wcsc.entities.Missingsheet;

public interface MissingSheetDao {
	
	/**
	 * Registra un nuevo Audit en la base de datos
	 * @param object
	 * @return
	 */
	public boolean save(Missingsheet object);
	
	/**
	 * Registra 679 Missingsheet
	 * @param sql - inyeccion SQL con los INSERT correspondientes
	 * @return - true o false
	 */
	public boolean saveAll(String sql);

	/**
	 * Obteiene un objeto de tipo Missingsheet
	 * @param id - id del objeto de tipo Missingsheet
	 * @return - Objeto de tipo Missingsheet
	 */
	public Missingsheet getObject(long id);
	
	/**
	 * Obtiene una Missingsheet filtrado por un numero en especifico
	 * @param id - numero a buscar
	 * @return - objeto de tipo Missingsheet
	 */
	public Missingsheet getSheetByNumber(int id);
	
	/**
	 * Obtiene una Missingsheet filtrado por un numero en especifico y id de usuario que
	 * la registro
	 * @param number - numero de la lamina
	 * @param userId - id del usuario que la registro
	 * @return - objeto de tipo Missingsheet
	 */
	public Missingsheet getSheetByNumberAndUser(int number,int userId);

	/**
	 * Lista todos los registros de la base de datos
	 * @return List
	 */
	public List<Missingsheet> list();
	
	/**
	 * Obtiene una lista de laminas registradas por un usuario
	 * @param id - id del usuario que regidtro las laminas
	 * @return - List
	 */
	public List<Missingsheet> listById(int id);

	/**
	 * Elimina un registro de Missingsheet
	 * @param object - Objeto Missingsheet a eliminar
	 * @return - true o false
	 */
	public boolean remove(Missingsheet object);
	
	/**
	 * Elimina todas las laminas registradas por un usuario
	 * @param id - id del usario 
	 * @return - true o false
	 */
	public boolean removeAll(int id);

	/**
	 * Actualiza un registro
	 * @param object - Objeto a actualizar
	 * @return - true o false
	 */
	public boolean update(Missingsheet object);

}
