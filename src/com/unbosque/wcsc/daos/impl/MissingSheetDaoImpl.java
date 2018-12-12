package com.unbosque.wcsc.daos.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.unbosque.wcsc.daos.MissingSheetDao;
import com.unbosque.wcsc.daos.UserDao;
import com.unbosque.wcsc.entities.Missingsheet;
import com.unbosque.wcsc.util.HibernateUtil;

public class MissingSheetDaoImpl implements MissingSheetDao{

	@Override
	public boolean save(Missingsheet object) {
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
			// TODO: handle exception
		}
		return success;
	}

	@Override
	public Missingsheet getObject(long id) {
		Missingsheet sheet=null;
		List list =new ArrayList<>();
		Session session =HibernateUtil.getSessionFactory().openSession();
		Transaction trans =session.beginTransaction();
		
		try
		{
			Query query=session.createQuery("from Missingsheet where userId = :id");
			query.setParameter("id", id);
			list=query.list();
			if(!list.isEmpty())
			{
				sheet=(Missingsheet)list.get(0);
			}
			trans.commit();
			session.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return sheet;
	}

	@Override
	public List<Missingsheet> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(Missingsheet object) {
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
	public boolean update(Missingsheet object) {
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
	public List<Missingsheet> listById(int id) {
		List list =new ArrayList<>();
		Session session =HibernateUtil.getSessionFactory().openSession();
		Transaction trans =session.beginTransaction();
		
		try
		{
			Query query=session.createQuery("from Missingsheet where userId = :id");
			query.setParameter("id", id);
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
	public Missingsheet getSheetByNumber(int id) {
		Missingsheet sheet=null;
		List list =new ArrayList<>();
		Session session =HibernateUtil.getSessionFactory().openSession();
		Transaction trans =session.beginTransaction();
		
		try
		{
			Query query=session.createQuery("from Missingsheet where numberSheets = :id");
			query.setParameter("id", id);
			list=query.list();
			if(!list.isEmpty())
			{
				sheet=(Missingsheet)list.get(0);
			}
			trans.commit();
			session.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return sheet;
	}

	@Override
	public boolean saveAll(String sqlValues) {
		boolean success=false;
		
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		try
		{
			Query query=session.createSQLQuery("INSERT INTO missingsheets(userId,numberSheets,countSheets) VALUES"+sqlValues);
			query.executeUpdate();
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
	public boolean removeAll(int id){
		boolean success=false;
		
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		try
		{
			Query query=session.createSQLQuery("DELETE FROM missingsheets WHERE userId = "+id);
			query.executeUpdate();
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
	public Missingsheet getSheetByNumberAndUser(int number, int userId) {
		Missingsheet sheet=null;
		List list =new ArrayList<>();
		Session session =HibernateUtil.getSessionFactory().openSession();
		Transaction trans =session.beginTransaction();
		
		try
		{
			Query query=session.createQuery("from Missingsheet where numberSheets = :number and userId = :userId");
			query.setParameter("number", number);
			query.setParameter("userId", userId);
			list=query.list();
			if(!list.isEmpty())
			{
				sheet=(Missingsheet)list.get(0);
			}
			trans.commit();
			session.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return sheet;
	}

}
