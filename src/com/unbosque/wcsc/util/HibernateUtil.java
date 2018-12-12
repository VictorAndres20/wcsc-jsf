package com.unbosque.wcsc.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.unbosque.wcsc.entities.Audit;
import com.unbosque.wcsc.entities.Missingsheet;
import com.unbosque.wcsc.entities.New;
import com.unbosque.wcsc.entities.Parameter;
import com.unbosque.wcsc.entities.Repeatedsheet;
import com.unbosque.wcsc.entities.Stadium;
import com.unbosque.wcsc.entities.User;

public class HibernateUtil {

	private static SessionFactory sessionFactory;

	private HibernateUtil() {

	}

	@SuppressWarnings("deprecation")
	public static SessionFactory getSessionFactory() {

		if (sessionFactory == null) {
			try {
				// Create the SessionFactory from standard (hibernate.cfg.xml)
				// config file.
				@SuppressWarnings("deprecation")
				AnnotationConfiguration ac = new AnnotationConfiguration();
				ac.addAnnotatedClass(User.class);
				ac.addAnnotatedClass(Audit.class);
				ac.addAnnotatedClass(New.class);
				ac.addAnnotatedClass(Missingsheet.class);
				ac.addAnnotatedClass(Repeatedsheet.class);
				ac.addAnnotatedClass(Parameter.class);
				ac.addAnnotatedClass(Stadium.class);
				sessionFactory = ac.configure().buildSessionFactory();

			} catch (Throwable ex) {
				// Log the exception.
				System.err.println("Initial SessionFactory creation failed." + ex);
				throw new ExceptionInInitializerError(ex);
			}

			return sessionFactory;

		} else {
			return sessionFactory;
		}

	}

}