package com.unbosque.wcsc.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class ImageBean{
	
	/**
	 * Lista de nombre de las imagenes de la pagina principal
	 */
	private List<String> images;
	
	/**
	 * Post constructor, inicializa la lista de las imagenes y añade cada uno de los nombres
	 */
	@PostConstruct
    public void init() {
        images = new ArrayList<String>();
        for (int i = 0; i <= 5; i++) {
            images.add("/images/index" + i + ".png");
        }
    }
 
	/**
	 * Obtiene la lista de los nombes de las imagenes
	 * @return
	 */
    public List<String> getImages() {
        return images;
    }

}
