package com.unbosque.wcsc.services;

import java.util.List;

import com.unbosque.wcsc.daos.StadiumDao;
import com.unbosque.wcsc.daos.impl.StadiumDaoImpl;
import com.unbosque.wcsc.entities.Stadium;

public class StadiumServices implements StadiumDao{

	@Override
	public boolean save(Stadium object) {
		StadiumDao dao=new StadiumDaoImpl();
		return dao.save(object);
	}

	@Override
	public Stadium getObject(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Stadium> list() {
		StadiumDao dao=new StadiumDaoImpl();
		return dao.list();
	}

	@Override
	public boolean remove(Stadium object) {
		StadiumDao dao=new StadiumDaoImpl();
		return dao.remove(object);
	}

	@Override
	public boolean update(Stadium object) {
		StadiumDao dao=new StadiumDaoImpl();
		return dao.update(object);
	}

	@Override
	public Stadium getStadiumByPhoto(String photo) {
		StadiumDao dao=new StadiumDaoImpl();
		return dao.getStadiumByPhoto(photo);
	}
	
	

}
