package com.unbosque.wcsc.daos.impl;

import java.util.List;

import javax.transaction.Synchronization;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.engine.transaction.spi.LocalStatus;

import com.unbosque.wcsc.daos.AuditDao;
import com.unbosque.wcsc.daos.UserDao;
import com.unbosque.wcsc.entities.Audit;
import com.unbosque.wcsc.util.HibernateUtil;

public class AuditDaoImpl implements AuditDao{

	@Override
	public boolean save(Audit object) {
		boolean success=false;
		
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		
		try
		{
			session.save(object);
			trans.commit();
			session.close();
			success=true;
		}
		catch (Exception e) {
			
		}
		
		return success;
	}

	@Override
	public Audit getObject(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Audit> list() {
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		List list=session.createQuery("from Audit where id >0 order by id desc").list();
		trans.commit();
		session.close();
		return list;
	}

	@Override
	public boolean remove(Audit object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(Audit object) {
		boolean success=false;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		
		try
		{
			session.update(object);
			trans.commit();
			session.close();
			success=true;
		}
		catch (Exception e) {
			// TODO: handle exception
		}
			
		
		return success;
	}
	
	

}
