package com.unbosque.wcsc.daos;

import java.util.List;

import com.unbosque.wcsc.entities.New;

public interface NewsDao {
	
	/**
	 * Crea un nuevo registro de New en la base de datos 
	 * @param object - objeto de tipo New
	 * @return - true o false
	 */
	public boolean save(New object);

	/**
	 * Obtiene un objeto de tipo New
	 * @param id - id del objeto
	 * @return - Objeto de tipo New
	 */
	public New getObject(long id);

	/**
	 * Lista todos los registros de la base de datos 
	 * @return - List
	 */
	public List<New> list();
	
	/**
	 * Lista todos los registros de la base de datos con un active en especifico
	 * @param active - estado de la noticia
	 * @return - Lista de noticias en el estado dado
	 */
	public List<New> listByActiveCiteria(String active);

	/**
	 * Elimina un registro de la base de datos
	 * @param object - Objeto a eliminar
	 * @return - true o false
	 */
	public boolean remove(New object);

	/**
	 * Actualiza un registro dentro de la base de datos
	 * @param object - Objeto a actualizar 
	 * @return true o false
	 */
	public boolean update(New object);

}
