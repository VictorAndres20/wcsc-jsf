package com.unbosque.wcsc.daos.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.unbosque.wcsc.daos.ParameterDao;
import com.unbosque.wcsc.entities.Parameter;
import com.unbosque.wcsc.util.HibernateUtil;

public class ParameterDaoImpl implements ParameterDao{

	@Override
	public boolean save(Parameter object) {
		boolean success=false;
		
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.save(object);
			trans.commit();
			session.close();
			success=true;
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return success;
	}

	@Override
	public Parameter getObject(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Parameter> list() {
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		List list=session.createQuery("from Parameter").list();
		trans.commit();
		session.close();
		return list;
	}

	@Override
	public boolean remove(Parameter object) {
		boolean success=false;
		
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.delete(object);
			trans.commit();
			session.close();
			success=true;
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return success;
	}

	@Override
	public boolean update(Parameter object) {
		boolean success=false;
		
		Session session=HibernateUtil.getSessionFactory().openSession();
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

	@Override
	public Parameter getParameterByCode(String code) {
		Parameter param=null;
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		try
		{
			Query query =session.createQuery("from Parameter where parameterCode = :code");
			query.setParameter("code", code);
			List list=query.list();
			trans.commit();
			session.close();
			if(!list.isEmpty())
			{
				param=(Parameter)list.get(0);
			}
		}
		catch (Exception e) {
			// TODO: handle exception
		}	
		return param;
	}
	
	

}
