package com.unbosque.wcsc.services;

import java.util.List;

import com.unbosque.wcsc.daos.RepeatedSheetDao;
import com.unbosque.wcsc.daos.UserDao;
import com.unbosque.wcsc.daos.impl.RepeatedSheetDaoImpl;
import com.unbosque.wcsc.entities.Missingsheet;
import com.unbosque.wcsc.entities.Repeatedsheet;

public class RepeatedSheetServices implements RepeatedSheetDao{

	@Override
	public boolean save(Repeatedsheet object) {
		RepeatedSheetDaoImpl dao=new RepeatedSheetDaoImpl();
		return dao.save(object);
	}

	@Override
	public Repeatedsheet getObject(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Repeatedsheet> list() {
		RepeatedSheetDaoImpl dao=new RepeatedSheetDaoImpl();
		return dao.list();
	}

	@Override
	public boolean remove(Repeatedsheet object) {
		RepeatedSheetDaoImpl dao=new RepeatedSheetDaoImpl();
		return dao.remove(object);
	}

	@Override
	public boolean update(Repeatedsheet object) {
		RepeatedSheetDaoImpl dao=new RepeatedSheetDaoImpl();
		return dao.update(object);
	}

	@Override
	public List<Repeatedsheet> listById(int id) {
		RepeatedSheetDaoImpl dao=new RepeatedSheetDaoImpl();
		return dao.listById(id);
	}

	@Override
	public List<Repeatedsheet> recomededList(List<Missingsheet> missingList) {
		RepeatedSheetDaoImpl dao=new RepeatedSheetDaoImpl();
		return dao.recomededList(missingList);
	}

	@Override
	public List<Repeatedsheet> listByNumber(int number) {
		RepeatedSheetDaoImpl dao=new RepeatedSheetDaoImpl();
		return dao.listByNumber(number);
	}

	@Override
	public Repeatedsheet getSheetByNumber(int id) {
		RepeatedSheetDaoImpl dao=new RepeatedSheetDaoImpl();
		return dao.getSheetByNumber(id);
	}

	@Override
	public Repeatedsheet getSheetByNumber(int number, int userId) {
		RepeatedSheetDaoImpl dao=new RepeatedSheetDaoImpl();
		return dao.getSheetByNumber(number, userId);
	}
	
	

}
