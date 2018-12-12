package com.unbosque.wcsc.daos;

import java.util.List;

import com.unbosque.wcsc.entities.Parameter;

public interface ParameterDao {
	
	/**
	 * Registra un nuevo Parametro en la base de datos
	 * @param object - objeto a registrar
	 * @return - true o false
	 */
	public boolean save(Parameter object);

	/**
	 * Obtiene un parametro
	 * @param id - id de parametro
	 * @return - Objeto de tipo parametro
	 */
	public Parameter getObject(long id);
	
	/**
	 * Obtiene un parametro por codigo
	 * @param String - codigo de parametro
	 * @return - Objeto de tipo parametro
	 */
	public Parameter getParameterByCode(String code);

	/**
	 * Lista de parametros 
	 * @return - List
	 */
	public List<Parameter> list();

	/**
	 * Elimina un registro de la base de datos
	 * @param object - Objeto Parameter a eliminar
	 * @return - true o false
	 */
	public boolean remove(Parameter object);

	/**
	 * Actualiza un registro en la base de datos
	 * @param object - Objeto a actualizar
	 * @return - true o false
	 */
	public boolean update(Parameter object);

}
