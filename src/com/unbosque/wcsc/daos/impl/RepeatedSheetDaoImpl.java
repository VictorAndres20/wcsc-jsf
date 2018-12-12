package com.unbosque.wcsc.daos.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.unbosque.wcsc.daos.RepeatedSheetDao;
import com.unbosque.wcsc.entities.Missingsheet;
import com.unbosque.wcsc.entities.Repeatedsheet;
import com.unbosque.wcsc.util.HibernateUtil;

public class RepeatedSheetDaoImpl implements RepeatedSheetDao{

	@Override
	public boolean save(Repeatedsheet object) {
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
	public Repeatedsheet getObject(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Repeatedsheet> list() {
		List list=new ArrayList<>();
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		try
		{
			Query query=session.createQuery("from repeatedsheets");
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
	public boolean remove(Repeatedsheet object) {
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
	public boolean update(Repeatedsheet object) {
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
	public List<Repeatedsheet> recomededList(List<Missingsheet> missingList) {
		List list=new ArrayList<>();
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		try
		{
			Query query=session.createQuery("from Repeatedsheet where numberSheets = :number");
			for (int i = 0; i < missingList.size(); i++) 
			{
				List listAux=new ArrayList<>();				
				query.setParameter("number", missingList.get(i).getNumberSheets());
				listAux=query.list();
				list.addAll(listAux);
			}
			
			trans.commit();
			session.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}		
		return list;
	}

	@Override
	public List<Repeatedsheet> listById(int id) {
		List list=new ArrayList<>();
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		try
		{
			Query query=session.createQuery("from Repeatedsheet where userId = :id");
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
	public List<Repeatedsheet> listByNumber(int number) {
		List list=new ArrayList<>();
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		try
		{
			Query query=session.createQuery("from Repeatedsheet where numberSheets = :number");
			query.setParameter("number", number);
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
	public Repeatedsheet getSheetByNumber(int id) {
		Repeatedsheet sheet=null;
		List list=new ArrayList<>();
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		try
		{
			Query query=session.createQuery("from Repeatedsheet where numberSheets = :number");
			query.setParameter("number", id);
			list=query.list();
			if(!list.isEmpty())
			{
				sheet=(Repeatedsheet)list.get(0);
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
	public Repeatedsheet getSheetByNumber(int number, int userId) {
		Repeatedsheet sheet=null;
		List list=new ArrayList<>();
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		try
		{
			Query query=session.createQuery("from Repeatedsheet where numberSheets = :number and userId = :userId");
			query.setParameter("number", number);
			query.setParameter("userId", userId);
			list=query.list();
			if(!list.isEmpty())
			{
				sheet=(Repeatedsheet)list.get(0);
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
