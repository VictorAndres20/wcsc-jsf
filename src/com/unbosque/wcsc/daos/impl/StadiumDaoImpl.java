package com.unbosque.wcsc.daos.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.unbosque.wcsc.daos.StadiumDao;
import com.unbosque.wcsc.entities.Stadium;
import com.unbosque.wcsc.util.HibernateUtil;

public class StadiumDaoImpl implements StadiumDao{

	@Override
	public boolean save(Stadium object) {
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
	public Stadium getObject(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Stadium> list() {
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		List list=session.createQuery("from Stadium").list();
		trans.commit();
		session.close();
		return list;
	}

	@Override
	public boolean remove(Stadium object) {
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
	public boolean update(Stadium object) {
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
	public Stadium getStadiumByPhoto(String photo) {
		Stadium st=null;
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		try
		{
			Query query=session.createQuery("from Stadium where photo = :photo");
			query.setParameter("photo", photo);
			List list=query.list();
			if(!list.isEmpty())
			{
				st=(Stadium)list.get(0);
			}
			trans.commit();
			session.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return st;
	}
	
	

}
