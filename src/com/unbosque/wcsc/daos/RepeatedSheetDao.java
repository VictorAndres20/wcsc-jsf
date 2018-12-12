package com.unbosque.wcsc.daos;

import java.util.List;

import com.unbosque.wcsc.entities.Missingsheet;
import com.unbosque.wcsc.entities.Repeatedsheet;

public interface RepeatedSheetDao {
	
	/**
	 * Agregar una lamina repetida a la base de datos
	 * @param object - lamina repetida
	 * @return True si agrega correctamente. False si agrega correctamente
	 */
	public boolean save(Repeatedsheet object);

	/**
	 * Obtener Lamina repetida por id de esta
	 * @param id - id de lamina repetida
	 * @return - lamina repetida
	 */
	public Repeatedsheet getObject(long id);
	
	/**
	 * Obtener Lamina repetida por numero de esta
	 * @param id - numero
	 * @return - lamina repetida
	 */
	public Repeatedsheet getSheetByNumber(int id);
	
	/**
	 * Obtener Lamina repetida por numero y usuario que la registro
	 * @param number - numero
	 * @param userId - id de usaurio
	 * @return - Lamina repetida
	 */
	public Repeatedsheet getSheetByNumber(int number, int userId);

	/**
	 * Lista todas las laminas de la base d edatos
	 * @return - List
	 */
	public List<Repeatedsheet> list();
	
	/**
	 * Lista todas las laminas de la base d edatos por un id en especifico
	 * @param id - id de lamina
	 * @return - List
	 */
	public List<Repeatedsheet> listById(int id);
	
	/**
	 * Lista todas las laminas de la base d edatos por un numero en especifico
	 * @param id - numero de lamina
	 * @return - List
	 */
	public List<Repeatedsheet> listByNumber(int number);	
	
	/**
	 * Lista de laminas repetidas que se cargan con base en una lista de laminas
	 * faltantes
	 * @param missingList - lista de laminas faltantes
	 * @return - List
	 */
	public List<Repeatedsheet> recomededList(List<Missingsheet> missingList);

	/**
	 * Elimina un registro de la base de datos
	 * @param object - Objeto de tipo Repeatedsheet a eliminar
	 * @return - true o false
	 */
	public boolean remove(Repeatedsheet object);

	/**
	 * Actualiza un registro de la base de datos
	 * @param object - Objeto de tipo Repeatedsheet a actualizar
	 * @return - true o false
	 */
	public boolean update(Repeatedsheet object);

}
