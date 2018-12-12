package com.unbosque.wcsc.services;

import java.util.List;

import org.primefaces.component.dashboard.Dashboard;

import com.unbosque.wcsc.daos.UserDao;
import com.unbosque.wcsc.daos.impl.UserDaoImpl;
import com.unbosque.wcsc.entities.User;

public class UserServices implements UserDao{

	@Override
	public boolean save(User object) {
		UserDaoImpl uDao=new UserDaoImpl();
		return uDao.save(object);
		
	}

	@Override
	public User getObject(int id) {
		UserDaoImpl dao=new UserDaoImpl();
		return dao.getObject(id);
	}
	
	public User getUsuarioByEmail(String email) {
		UserDaoImpl uDao=new UserDaoImpl();
		return uDao.getUsuarioByEmail(email);
	}

	@Override
	public List<User> list() {
		UserDaoImpl dao=new UserDaoImpl();
		return dao.list();
	}

	@Override
	public boolean remove(User object) {
		UserDaoImpl dao=new UserDaoImpl();
		return dao.remove(object);
		
	}

	@Override
	public boolean update(User object) {
		UserDaoImpl dao=new UserDaoImpl();
		return dao.update(object);
		
	}

	@Override
	public User getUsuarioByUserName(String userName) {
		UserDaoImpl dao=new UserDaoImpl();
		return dao.getUsuarioByUserName(userName);
	}

	@Override
	public User getUsuarioByEmailAndUser(String email, String userName) {
		UserDaoImpl dao=new UserDaoImpl();
		return dao.getUsuarioByEmailAndUser(email, userName);
	}
	
	

}
