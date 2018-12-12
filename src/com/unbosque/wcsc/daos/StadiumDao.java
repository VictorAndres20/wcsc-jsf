package com.unbosque.wcsc.daos;

import java.util.List;

import com.unbosque.wcsc.entities.Stadium;

public interface StadiumDao {
	
	public boolean save(Stadium object);

	public Stadium getObject(long id);
	
	public Stadium getStadiumByPhoto(String photo);

	public List<Stadium> list();

	public boolean remove(Stadium object);

	public boolean update(Stadium object);
	
	

}
