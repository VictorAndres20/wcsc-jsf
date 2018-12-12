package com.unbosque.wcsc.beans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.unbosque.wcsc.util.Constantes;

@ManagedBean
@ViewScoped
public class UserConstansBean {
	
	List<String> userTypes;
	
	public UserConstansBean()
	{
		userTypes=new ArrayList<>();
		userTypes.add(Constantes.USER_TYPE_ADMIN);
		userTypes.add(Constantes.USER_TYPE_NEW);
		userTypes.add(Constantes.USER_TYPE_NORMAL);
				
	}

	public List<String> getUserTypes() {
		return userTypes;
	}

	public void setUserTypes(List<String> userTypes) {
		this.userTypes = userTypes;
	}
	
}
