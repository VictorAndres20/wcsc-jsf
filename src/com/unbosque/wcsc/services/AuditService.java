package com.unbosque.wcsc.services;

import java.util.List;

import com.unbosque.wcsc.daos.AuditDao;
import com.unbosque.wcsc.daos.impl.AuditDaoImpl;
import com.unbosque.wcsc.entities.Audit;

public class AuditService implements AuditDao{

	@Override
	public boolean save(Audit object) {
		AuditDaoImpl dao=new AuditDaoImpl();
		return dao.save(object);
	}

	@Override
	public Audit getObject(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Audit> list() {
		AuditDaoImpl dao=new AuditDaoImpl();
		return dao.list();
	}

	@Override
	public boolean remove(Audit object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Audit object) {
		AuditDaoImpl dao=new AuditDaoImpl();
		return dao.update(object);
	}
	
	
	
	

}
