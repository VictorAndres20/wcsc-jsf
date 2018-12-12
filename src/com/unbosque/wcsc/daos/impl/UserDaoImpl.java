package com.unbosque.wcsc.daos.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.unbosque.wcsc.daos.UserDao;
import com.unbosque.wcsc.entities.User;
import com.unbosque.wcsc.util.HibernateUtil;

public class UserDaoImpl implements UserDao{

	@Override
	public boolean save(User object) {
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
			
		}
		return success;
		
		
	}

	@Override
	public User getObject(int id) {
		User user=null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List lista= new ArrayList();
		Query query = session.createQuery("from User where id = :id");
		query.setParameter("id", id);
		lista=query.list();
		t.commit();
		session.close();
		if(!lista.isEmpty())
		{
			user=(User)lista.get(0);
		}
		
		
		return user;
	}

	@Override
	public User getUsuarioByEmail(String email) {
		User user=null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List lista= new ArrayList();
		Query query = session.createQuery("from User where emailAddress = :email");
		query.setParameter("email", email);
		lista=query.list();
		t.commit();
		session.close();
		if(!lista.isEmpty())
		{
			user=(User)lista.get(0);
		}
		
		return user;
		
	}
	
	@Override
	public User getUsuarioByUserName(String userName) {
		User user=null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List lista= new ArrayList();
		Query query = session.createQuery("from User where userName = :user");
		query.setParameter("user", userName);
		lista=query.list();
		t.commit();
		session.close();
		if(!lista.isEmpty())
		{
			user=(User)lista.get(0);
		}
		
		return user;
		
	}

	@Override
	public List<User> list() {
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		List list=session.createQuery("from User").list();
		trans.commit();
		session.close();
		return list;
	}

	@Override
	public boolean remove(User object) {
		boolean success=false;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trans=session.beginTransaction();
		try
		{
			session.delete(object);
			trans.commit();
			session.close();
			success=true;
		}
		catch (Exception e) {
			
		}
		return success;
	}

	@Override
	public boolean update(User object) {
		boolean success=false;
		
		Session session=HibernateUtil.getSessionFactory().openSession();
		Transaction trans = session.beginTransaction();
		try
		{
			session.update(object);
			trans.commit();
			session.close();
			success=true;
		}
		catch(Exception e)
		{
			
		}	
		
		return success;
		
	}

	@Override
	public User getUsuarioByEmailAndUser(String email, String userName) {
		User user=null;
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		List lista= new ArrayList();
		Query query = session.createQuery("from User where userName = :user and emailAddress = :email");
		query.setParameter("user", userName);
		query.setParameter("email", email);		
		lista=query.list();
		t.commit();
		session.close();
		if(!lista.isEmpty())
		{
			user=(User)lista.get(0);
		}
		
		return user;
	}
	
	

}
