package com.unbosque.wcsc.services;

import java.util.List;

import com.unbosque.wcsc.daos.NewsDao;
import com.unbosque.wcsc.daos.UserDao;
import com.unbosque.wcsc.daos.impl.NewsDaoImpl;
import com.unbosque.wcsc.entities.New;

public class NewsService implements NewsDao{

	@Override
	public boolean save(New object) {
		NewsDaoImpl dao=new NewsDaoImpl();
		return dao.save(object);
	}

	@Override
	public New getObject(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<New> list() {
		NewsDaoImpl dao=new NewsDaoImpl();
		
		return dao.list();
	}

	@Override
	public boolean remove(New object) {
		NewsDaoImpl dao=new NewsDaoImpl();
		return dao.remove(object);
	}

	@Override
	public boolean update(New object) {
		NewsDaoImpl dao=new NewsDaoImpl();
		return dao.update(object);
	}

	@Override
	public List<New> listByActiveCiteria(String active) {
		NewsDaoImpl dao=new NewsDaoImpl();
		return dao.listByActiveCiteria(active);
	}
	
	

}
