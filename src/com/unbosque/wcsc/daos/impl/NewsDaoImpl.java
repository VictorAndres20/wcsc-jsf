package com.unbosque.wcsc.daos.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.unbosque.wcsc.daos.NewsDao;
import com.unbosque.wcsc.daos.UserDao;
import com.unbosque.wcsc.entities.New;
import com.unbosque.wcsc.util.HibernateUtil;

public class NewsDaoImpl implements NewsDao{

	@Override
	public boolean save(New object) {
		boolean success=false;
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
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
	public New getObject(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<New> list() {
		List list=new ArrayList<>();
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		try
		{
			Query query=session.createQuery("from New where id >0 order by id desc");
			list=query.list();
			trans.commit();
			session.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return list;
	}

	@Override
	public boolean remove(New object) {
		boolean success=false;
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		
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
	public boolean update(New object) {
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
	public List<New> listByActiveCiteria(String active) {
		List list=new ArrayList<>();
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		try
		{
			Query query=session.createQuery("from New where state = :active order by id desc");
			query.setParameter("active", active);
			list=query.list();
			trans.commit();
			session.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return list;
	}
	
	

}
