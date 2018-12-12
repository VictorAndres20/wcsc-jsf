package com.unbosque.wcsc.daos;

import java.util.List;

import com.unbosque.wcsc.entities.User;

public interface UserDao {
	
	/**
	 * Registra un nuevo usuario
	 * @param object - Objeto de tipo User
	 * @return - true o false
	 */
	public boolean save(User object);

	/**
	 * Obtiene un usuario por id
	 * @param id - id de usuario
	 * @return - User
	 */
	public User getObject(int id);
	
	/**
	 * Obtiene un usuario por correo
	 * @param email - correo de usuario
	 * @return - User
	 */
	public User getUsuarioByEmail(String email);
	
	/**
	 * Obtiene un usuario por nombre de usuario
	 * @param userName - nombre de usuario
	 * @return - User
	 */
	public User getUsuarioByUserName(String userName);
	
	/**
	 * Obtiene un usuario por nombre de usuario y correo
	 * @param email - Correo de usuario
	 * @param userName - Nombre de usuairo
	 * @return User
	 */
	public User getUsuarioByEmailAndUser(String email, String userName);

	/**
	 * Lista todos los usuarios de las base de datos
	 * @return List
	 */
	public List<User> list();

	/**
	 * Elimina un usuario de la base de datos
	 * @param object - Objeto de tipo User
	 * @return true o false
	 */
	public boolean remove(User object);

	/**
	 * Actualza un usuario en la bade de datos
	 * @param object - Objeto a actualizar
	 * @return - true o false
	 */
	public boolean update(User object);

}
