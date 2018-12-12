package com.unbosque.wcsc.services;

import java.util.List;

import com.unbosque.wcsc.daos.ParameterDao;
import com.unbosque.wcsc.daos.impl.ParameterDaoImpl;
import com.unbosque.wcsc.entities.Parameter;

public class ParameterServices implements ParameterDao{

	@Override
	public boolean save(Parameter object) {
		ParameterDaoImpl dao=new ParameterDaoImpl();
		return dao.save(object);
	}

	@Override
	public Parameter getObject(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Parameter> list() {
		ParameterDaoImpl dao=new ParameterDaoImpl();
		return dao.list();
	}

	@Override
	public boolean remove(Parameter object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Parameter object) {
		ParameterDaoImpl dao=new ParameterDaoImpl();
		return dao.update(object);
	}

	@Override
	public Parameter getParameterByCode(String code) {
		ParameterDaoImpl dao=new ParameterDaoImpl();
		return dao.getParameterByCode(code);
	}
	
	

}
