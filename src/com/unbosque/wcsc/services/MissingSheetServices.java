package com.unbosque.wcsc.services;

import com.unbosque.wcsc.entities.Missingsheet;

import java.util.List;

import com.unbosque.wcsc.daos.MissingSheetDao;
import com.unbosque.wcsc.daos.impl.MissingSheetDaoImpl;

public class MissingSheetServices implements MissingSheetDao{

	@Override
	public boolean save(Missingsheet object) {
		MissingSheetDaoImpl dao=new MissingSheetDaoImpl();
		return dao.save(object);
	}

	@Override
	public Missingsheet getObject(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Missingsheet> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Missingsheet object) {
		MissingSheetDaoImpl dao=new MissingSheetDaoImpl();
		return dao.remove(object);
	}

	@Override
	public boolean update(Missingsheet object) {
		MissingSheetDaoImpl dao=new MissingSheetDaoImpl();
		return dao.update(object);
	}

	@Override
	public List<Missingsheet> listById(int id) {
		MissingSheetDaoImpl dao=new MissingSheetDaoImpl();
		return dao.listById(id);
	}

	@Override
	public Missingsheet getSheetByNumber(int id) {
		MissingSheetDaoImpl dao=new MissingSheetDaoImpl();
		return dao.getSheetByNumber(id);
	}

	@Override
	public boolean saveAll(String sql) {
		MissingSheetDaoImpl dao=new MissingSheetDaoImpl();
		return dao.saveAll(sql);
	}

	@Override
	public boolean removeAll(int id) {
		MissingSheetDaoImpl dao=new MissingSheetDaoImpl();
		return dao.removeAll(id);
	}

	@Override
	public Missingsheet getSheetByNumberAndUser(int number, int userId) {
		MissingSheetDaoImpl dao=new MissingSheetDaoImpl();
		return dao.getSheetByNumberAndUser(number, userId);
	}
	
	

}
