package com.unbosque.wcsc.beans;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.primefaces.PrimeFaces;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.unbosque.wcsc.entities.Audit;
import com.unbosque.wcsc.entities.Missingsheet;
import com.unbosque.wcsc.entities.New;
import com.unbosque.wcsc.entities.Parameter;
import com.unbosque.wcsc.entities.Repeatedsheet;
import com.unbosque.wcsc.entities.Stadium;
import com.unbosque.wcsc.entities.User;
import com.unbosque.wcsc.entities.UserSheets;
import com.unbosque.wcsc.misestructuras.ColaPrioridadMayores;
import com.unbosque.wcsc.misestructuras.ColaPrioridadMenores;
import com.unbosque.wcsc.services.AuditService;
import com.unbosque.wcsc.services.MissingSheetServices;
import com.unbosque.wcsc.services.NewsService;
import com.unbosque.wcsc.services.ParameterServices;
import com.unbosque.wcsc.services.RepeatedSheetServices;
import com.unbosque.wcsc.services.StadiumServices;
import com.unbosque.wcsc.services.UserServices;
import com.unbosque.wcsc.util.Constantes;
import com.unbosque.wcsc.util.Utility;

@ManagedBean
@ViewScoped
public class StickerBean{	
	
	private static final Logger LOG= Logger.getLogger(StickerBean.class);
	
	private Audit action;
	private User user;
	private String searchNumber;
	private Repeatedsheet repeated;
	private Missingsheet missing;
	private Missingsheet missingSelectedSheet;
	private Repeatedsheet selectedSheet;
	private List<New> news;
	private New notice;
	private List<Missingsheet> missingList,filteredMissing;
	private List<Repeatedsheet> repeatedList,filteredRepeted;
	private List<Repeatedsheet> searchSeheets,recomendedRepeatedList,filteredRecomList;
	private List<Stadium> stadiums;
	private String asuntoContact, bodyContact;
	private List<UserSheets> userTopMax, userTopMin,userFullAlbum;
	
	public StickerBean()
	{
		user=(User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		
        validateSession(user);
		repeated=new Repeatedsheet();
		selectedSheet=new Repeatedsheet();
		missingSelectedSheet=new Missingsheet();
		missing=new Missingsheet();
		missingList=new ArrayList<>();
		filteredMissing=new ArrayList<>();
		recomendedRepeatedList=new ArrayList<>();
		stadiums=new ArrayList<>();
		searchSeheets=new ArrayList<>();
		filteredRepeted=new ArrayList<>();
		repeatedList=new ArrayList<>();
		news=new ArrayList<>();
		notice = new New();	
		
		updateListTopMax();
		updateListTopMin();
		updateListFullAlbum();
					
		updateNews();
		updateStadiums();
		
		
		LOG.info("StickerBean creado correctamente ");
		
	}
	
	/**
	 * Metodo para registrar una accion 
	 * @param idUser - id del usuario
	 * @param op - operacion realizada
	 * @param idTable - id de la tabla
	 * @param tableName - nombre de la tabla
	 * @return True si registro correctamente. False si no registro
	 */
	public boolean createAction(int idUser,String op,int idTable,String tableName)
	{
		boolean success=false;
		
		action=new Audit();
		action.setCreateDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
		action.setOperation(op);
		action.setTableId(idTable);
		action.setTableName(tableName);
		action.setUserId(idUser);
		action.setIpAddress(Utility.getClientIp());
		
		AuditService auditS=new AuditService();
		if(auditS.save(action))
		{
			success=true;
		}
		
		return success;
	}
	
	/**
	 * Metodo para validar la sesion
	 * @param pUser - usuario logeado
	 */
	public void validateSession(User pUser)
	{
		user=pUser;
		if(user.getEmailAddress()==null)
		{
			LOG.warn("Intento de ingreso sin autenticación por la ip "+Utility.getClientIp());
			ExternalContext context2 = FacesContext.getCurrentInstance().getExternalContext();
		    try
		    {
		        context2.redirect(context2.getRequestContextPath() +"/user/index.jsf");
		    }
		    catch (IOException e) {		           
		    	LOG.error("ERROR: ",e);
		    } 
		}
		else
		{
			LOG.info("Usuario creado correctamente "+user.getId());
		}		
	}
	
	/**
	 * Metodo para añadir una lamina repetida
	 */
	public void addRepeated()
	{
		LOG.info("Se ha presionado AÑADIR REPETIDA");
		
		try
		{
			if(repeated.getNumberSheets()>=Constantes.TOTAL_STICKERS || repeated.getNumberSheets()<0)
			{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Por favor, digite número de lámina válido");
		         
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
			else
			{
				RepeatedSheetServices rs=new RepeatedSheetServices();
				RepeatedSheetServices rService=new RepeatedSheetServices();
				Repeatedsheet rep=rs.getSheetByNumber(repeated.getNumberSheets(),user.getId());
				if(rep==null)
				{
					repeated.setUserId(user.getId());
					
					if(rService.save(repeated))
					{
						createAction(user.getId(), Constantes.OP_ADD_REPETED, user.getId(), Constantes.TABLE_REPEATED_SHEET);
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Agrega", "lámina "+repeated.getNumberSheets()+", cantidad "+repeated.getCountSheets());
				         
				        PrimeFaces.current().dialog().showMessageDynamic(message);
					}
					else
					{
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agragra", "Por favor intente más tarde");
				         
				        PrimeFaces.current().dialog().showMessageDynamic(message);
					}
				}
				else if(rep!=null)
				{
					rep.setCountSheets(repeated.getCountSheets()+rep.getCountSheets());
					if(rService.update(rep))
					{
						createAction(user.getId(), Constantes.OP_ADD_REPETED, user.getId(), Constantes.TABLE_REPEATED_SHEET);
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nuevas láminas agregadas", "lámina "+rep.getNumberSheets()+", cantidad "+rep.getCountSheets());
				         
				        PrimeFaces.current().dialog().showMessageDynamic(message);
					}
					else
					{
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar", "Por favor intente más tarde");
				         
				        PrimeFaces.current().dialog().showMessageDynamic(message);
					}
				}
				
				
				
			}
		}
		catch (Exception e) {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar", "");
	         
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		
	}
	
	/**
	 * Metodo para añadir una lamina faltante
	 */
	public void addMissing()
	{
		LOG.info("Se ha presionado AÑADIR FALTANTE");
		
		if(missing.getNumberSheets()>=Constantes.TOTAL_STICKERS || missing.getNumberSheets()<0)
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Por favor, digite número de lámina válido");
	         
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		else
		{
			MissingSheetServices ms=new MissingSheetServices();
			Missingsheet miss=ms.getSheetByNumberAndUser(missing.getNumberSheets(),user.getId());
			MissingSheetServices mService=new MissingSheetServices();
			
			if(miss==null)
			{
				missing.setUserId(user.getId());
				
				if(mService.save(missing))
				{
					createAction(user.getId(), Constantes.OP_ADD_MISSIN, user.getId(), Constantes.TABLE_MISSIN_SHEET);
					
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Agrega", "lámina "+missing.getNumberSheets()+", cantidad "+missing.getCountSheets());
			         
			        PrimeFaces.current().dialog().showMessageDynamic(message);
				}
				else
				{
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar", "Por favor intente más tarde");
			         
			        PrimeFaces.current().dialog().showMessageDynamic(message);
				}
			}
			else if(miss!=null)
			{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error al agregar", "Lámina ya existe");
		         
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
			
			
			
		}
	}
	
	/**
	 * Metodo para añadir todas las laminas como faltantes
	 */
	public void addAllMissing()
	{
		MissingSheetServices ms=new MissingSheetServices();
		ms.removeAll(user.getId());
		
		String sqlValues="";
		
		for (int i = 0; i < Constantes.TOTAL_STICKERS; i++)
		{
			sqlValues+="("+user.getId()+","+i+",1)";
			if(i<Constantes.TOTAL_STICKERS-1)
			{
				sqlValues+=",";
			}
			
		}
		
		MissingSheetServices ms2=new MissingSheetServices();
		if(ms2.saveAll(sqlValues))
		{
			updateMissingList(user.getId());
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Nuevas láminas agregadas", "Todas las láminas han sido agregadas");
	         
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}
	
	public void hola()
	{
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "PERRORS", "No ha ingresado número de lámina a buscar");
        
        PrimeFaces.current().dialog().showMessageDynamic(message);
	}
	
	/**
	 * Metodo que busca una lamina en especial por el numero de etsa
	 */
	public void search()
	{
		LOG.info("Se ha presionado BUSCAR");
		if(searchNumber.equals(""))
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Campos vacíos", "No ha ingresado número de lámina a buscar");
	         
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		else
		{
			if(!Utility.isNumeric(searchNumber))
			{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Por favor, digite número de lámina válido");
		         
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
			else
			{
				int n=Integer.parseInt(searchNumber);
				if(n>Constantes.TOTAL_STICKERS || n<0)
				{
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Por favor, digite número de lámina válido");
			         
			        PrimeFaces.current().dialog().showMessageDynamic(message);
				}
				else
				{
					createAction(user.getId(), Constantes.OP_SEARCH_REPEATED, user.getId(), Constantes.TABLE_REPEATED_SHEET);
					List<Repeatedsheet> repeatedListSearch=new ArrayList<>();
					RepeatedSheetServices rs=new RepeatedSheetServices();
					repeatedListSearch=rs.listByNumber(n);
					if(!repeatedListSearch.isEmpty())
					{
						searchSeheets=repeatedListSearch;
						LOG.info("Se ha listado");			
					}
					else
					{
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No hay láminas", "No hay láminas repetidas con el número "+n);
				         
				        PrimeFaces.current().dialog().showMessageDynamic(message);
					}
					
					
				}
				
			}
			
		}
	}
	
	/**
	 * Metodo que reduce en 1 la cantidad una lamina repetida
	 */
	public void deleteOneRepeated()
	{
		RepeatedSheetServices rs=new RepeatedSheetServices();
		selectedSheet.setCountSheets(selectedSheet.getCountSheets()-1);
		if(selectedSheet.getCountSheets()<=0)
		{
			if(rs.remove(selectedSheet))
			{
				createAction(user.getId(), Constantes.OP_DELETE_REPETED, user.getId(), Constantes.TABLE_REPEATED_SHEET);
				
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lámina eliminada", "lamina id"+selectedSheet.getNumberSheets());
		        
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
			else
			{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR en eliminar", "Por favor, intente más tarde");
		        
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		}
		else if(selectedSheet.getCountSheets()>0)
		{
			if(rs.update(selectedSheet))
			{
				createAction(user.getId(), Constantes.OP_DELETE_REPETED, user.getId(), Constantes.TABLE_REPEATED_SHEET);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Una lámina eliminada", "lamina id"+selectedSheet.getNumberSheets());
		        
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
			else
			{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR en eliminar", "Por favor, intente más tarde");
		        
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		}
		
	}
	
	/**
	 * Metodo que elimina una lamina repetida en su totalidad
	 */
	public void deleteRepeated()
	{
		RepeatedSheetServices rs=new RepeatedSheetServices();
		if(rs.remove(selectedSheet))
		{
			createAction(user.getId(), Constantes.OP_DELETE_MISSIN, user.getId(), Constantes.TABLE_MISSIN_SHEET);
			
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lámina eliminada", "lamina id"+selectedSheet.getNumberSheets());
	        
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		else
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR en eliminar", "Por favor, intente más tarde");
	        
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}	
		
	}
	
	/**
	 * Metodo que elimina una lamina faltante en su totalidad
	 */
	public void deleteMissing() 
	{
		MissingSheetServices ms=new MissingSheetServices();
		if(ms.remove(missingSelectedSheet))
		{
			createAction(user.getId(), Constantes.OP_DELETE_REPETED, user.getId(), Constantes.TABLE_REPEATED_SHEET);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lámina eliminada", "lamina id"+missingSelectedSheet.getNumberSheets());
	        
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		else
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR en eliminar", "Por favor, intente más tarde");
	        
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}	
	}
	
	/**
	 * Metodo que reduce en 1 la cantidad de una lamina faltante en su totalidad
	 */
	public void deleteOneMissing()
	{
		MissingSheetServices ms=new MissingSheetServices();
		missingSelectedSheet.setCountSheets(missingSelectedSheet.getCountSheets()-1);
		if(missingSelectedSheet.getCountSheets()<=0)
		{
			if(ms.remove(missingSelectedSheet))
			{
				createAction(user.getId(), Constantes.OP_DELETE_MISSIN, user.getId(), Constantes.TABLE_MISSIN_SHEET);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Lámina eliminada", "lamina id"+missingSelectedSheet.getNumberSheets());
		        
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
			else
			{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR en eliminar", "Por favor, intente más tarde");
		        
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		}
		else if(selectedSheet.getCountSheets()>0)
		{
			if(ms.update(missingSelectedSheet))
			{
				createAction(user.getId(), Constantes.OP_DELETE_MISSIN, user.getId(), Constantes.TABLE_MISSIN_SHEET);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Una lámina eliminada", "lamina id"+missingSelectedSheet.getNumberSheets());
		        
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
			else
			{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR en eliminar", "Por favor, intente más tarde");
		        
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		}
	}
	
	/**
	 * Metodo para contactar al usuario para intercambio de lamina
	 */
	public void contactUser()
	{
		asuntoContact="Integrante interesado en una lámina";
		bodyContact="Hola "+getUserName(selectedSheet.getUserId())+".\n\nNuestro integrante "+user.getEmailAddress()+" desea una lámina tuya.\n\nEsta lámina es la número "+selectedSheet.getNumberSheets()+".\n\nContáctalo para realizar una negociación.\n\nSaludos, administración WCSC.";
		
		if(enviarGmail(getUserEmail(selectedSheet.getUserId()), asuntoContact, bodyContact))
		{
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Se ha enviado correo satisfactoriamente a "+selectedSheet.getNumberSheets());
	        
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		else
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Se ha producido una error en el envpio a "+getUserEmail(selectedSheet.getUserId()));
	        
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		
	}	
	
	/**
	 * Metodo para actualizar la lista de noticias
	 */
	public void updateNews()
	{
		NewsService newService=new NewsService();
		news=newService.listByActiveCiteria(Constantes.USER_ACTIVE);	
	}
	
	/**
	 * Actualiza la lsta de laminas recomendadas
	 * @param list - lista de laminas repetidas recomendadas
	 */
	public void updateRecomendedSheets(List<Missingsheet> list)
	{
		RepeatedSheetServices rs=new RepeatedSheetServices();		
		recomendedRepeatedList=rs.recomededList(list);		
		filteredRecomList=recomendedRepeatedList;
		
	}
	
	/**
	 * Actualiza los estadios para que el usuario pueda visualizar
	 */
	public void updateStadiums()
	{
		StadiumServices ss=new StadiumServices();
		stadiums=ss.list();
	}
	
	/**
	 * Actualiza la lista de mis repetidas
	 * @param id - id del usuario
	 */
	public void updateRepeatedList(int id)
	{
		RepeatedSheetServices rs=new RepeatedSheetServices();
		repeatedList=rs.listById(id);
		filteredRepeted=repeatedList;
	}
	
	/**
	 * Actualiza la lista de mis repetidas
 	 */
	public void updateRepeatedList()
	{
		RepeatedSheetServices rs=new RepeatedSheetServices();
		repeatedList=rs.listById(user.getId());
		filteredRepeted=repeatedList;
	}
	
	/**
	 * Actualiza la lista de mis faltantes
	 * @param id - id del usuario
	 */
	public void updateMissingList(int id)
	{
		MissingSheetServices ms=new MissingSheetServices();
		missingList=ms.listById(id);
		filteredMissing=missingList;
		updateRecomendedSheets(missingList);
		
	}
	
	/**
	 *Actualiza la lista de mis faltantes 
	 */
	public void updateMissingList()
	{
		MissingSheetServices ms=new MissingSheetServices();
		missingList=ms.listById(user.getId());
		filteredMissing=missingList;
		updateRecomendedSheets(missingList);
	}
	
	/**
	 * Metodo para enviar correo electronico
	 * @param destinatario - correo del destinatario
	 * @param asunto - asusto del mensaje
	 * @param cuerpo - cuerpo del mensaje
	 * @return - True si envia con exito, False si hay algun fallo
	 */
	public boolean enviarGmail(String destinatario, String asunto, String cuerpo)
	{
		boolean success=false;
		
		//Correo desde donde se envía
		String username = "awcsc1706@gmail.com";  
		
		String password="adminWCSC14321432";
		
		Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
		
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
	    
	    try {
	    	Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(cuerpo);
 
            Transport.send(message);
	        success=true;
	    }
	    catch (MessagingException me) {
//	        me.printStackTrace();   //Si se produce un error
	    	LOG.error("ERROR: ",me);
	    }
	    
		
		return success;
	}
	
	/**
	 * Obtiene el userName de un usuario
	 * @param id - id del usuario na retornar
	 * @return nombre de usuario
	 */
	public String getUserName(int id)
	{
		UserServices us=new UserServices();
		return us.getObject(id).getUserName();
	}
	
	/**
	 * Obtiene el Email de un usuario
	 * @param id - id del usuario na retornar
	 * @return email de usuario
	 */
	public String getUserEmail(int id)
	{
		UserServices us=new UserServices();
		return us.getObject(id).getEmailAddress();
	}
	
	/**
	 * Retorna el numero de dias en que se debe hacer cambio de password obligatorio.
	 * Si el parametro no se encentra en la base de datos, por defecto arrojara 60
	 * @return - Entero, numero de dias del cambio de password
	 */
	public int getParameterDaysValueNum()
	{
		int value=60;
		ParameterServices ps=new ParameterServices();
		Parameter param=ps.getParameterByCode(Constantes.PARAMETER_DAYS_PASS);
		if(param!=null)
		{
			value=param.getNumberValue();
		}
		
		return value;
	}
	
	/**
	 * Retorna los segundos que dispone un usuario (al recibir un password aleatorio al correo)
	 * para ingresar al sistema.
	 * Si el parametro no se encentra en la base de datos, por defecto arrojara 100
	 * @return - Entero, numero de segundos para ingresar al sistema.
	 */
	public int getParameterSecondsValueNum()
	{
		int value=60;
		ParameterServices ps=new ParameterServices();
		Parameter param=ps.getParameterByCode(Constantes.PARAMETER_SECONDS_LOGIN);
		if(param!=null)
		{
			value=param.getNumberValue();
		}
		
		return value;
	}
	
	/**
	 * Metodo para generar reprote PFD de las láminas recomendadas
	 */
	public void reportPDF()
	{		
		Document document=new Document();
		String name="Laminas";
		try {
			String path = new File(".").getCanonicalPath();
			path+="/pdf/"+name+".pdf";
			
			FileOutputStream fos=new FileOutputStream("/home/victorandres/Documentos/UniversidadELBosque/Semestre3/Programacion2/Proyecto/archivos/WCSC_Laminas.pdf");
			//FileOutputStream fos=new FileOutputStream(path+"\\WCSC_Laminas.pdf");
			PdfWriter.getInstance(document, fos);		
			
			document.open();		
			
			document.add(new Paragraph(" "));
			
			com.itextpdf.text.Font f = new com.itextpdf.text.Font();
            f.setFamily(FontFamily.COURIER.name());
            f.setStyle(com.itextpdf.text.Font.BOLD);
            f.setSize(20);
            
            Paragraph p=new Paragraph();
            p.setFont(f);
            p.setAlignment(Chunk.ALIGN_CENTER);
			p.add("WCSC - Láminas recomendadas");
			
            
            f = new com.itextpdf.text.Font();
            f.setFamily(FontFamily.COURIER.name());
            f.setStyle(com.itextpdf.text.Font.NORMAL);
            f.setSize(12);
			
			Paragraph p1=new Paragraph();
			p1.setFont(f);
			p1.add("A continuación se dará un reporte de las láminas recomendadas a la fecha \""+new Timestamp(Calendar.getInstance().getTime().getTime())+"\".");
			
			
			Paragraph p2=new Paragraph();
			p2.setFont(f);
			p2.add("En este reporte, dará un listado de las láminas que le faltan y los demás usuarios tienen.");
			
			
			document.add(p);
			document.add(new Paragraph(" "));
			document.add(p1);
			document.add(new Paragraph(" "));
			document.add(p2);
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			
			PdfPTable table=new PdfPTable(3);
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 1, 3, 5});
			Paragraph c1=new Paragraph("n°");
			c1.getFont().setSize(9);
			c1.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c1);
			Paragraph c2=new Paragraph("Lámina");
			c2.getFont().setSize(9);
			c2.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c2);
			Paragraph c3=new Paragraph("Usuario");
			c3.getFont().setSize(9);
			c3.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c3);
			
			
			for (int i = 0; i < recomendedRepeatedList.size(); i++)
			{
				
				Paragraph col1=new Paragraph(String.valueOf(i+1));
				col1.getFont().setSize(8);				
				table.addCell(col1);
				Paragraph col2=new Paragraph(String.valueOf(recomendedRepeatedList.get(i).getNumberSheets()));
				col2.getFont().setSize(8);				
				table.addCell(col2);
				Paragraph col3=new Paragraph(getUserName(recomendedRepeatedList.get(i).getUserId()));
				col3.getFont().setSize(8);				
				table.addCell(col3);
							
				
			}
			document.add(table);
			document.close();
			
			
				File file = new File("/home/victorandres/Documentos/UniversidadELBosque/Semestre3/Programacion2/Proyecto/archivos/WCSC_Laminas.pdf");
				//File file = new File(path+"\\WCSC_Laminas.pdf");
				java.awt.Desktop.getDesktop().open(file);
				
				FacesContext facesC=FacesContext.getCurrentInstance();
				
				HttpServletResponse response= (HttpServletResponse)facesC.getExternalContext().getResponse();
				response.reset();
				response.setHeader("Content-Type", "application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=WCSC_Laminas.pdf");
				
				OutputStream out=response.getOutputStream();
				
				InputStream is=new FileInputStream(file);
				byte[] bytes=new byte[2048];
				int reader;
				while((reader=is.read(bytes))>0)
				{
					out.write(bytes,0,reader);
				}
				
				out.flush();
				is.close();
				out.close();
				facesC.responseComplete();
		      
	        
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
	}
	
	/**
	 * Metodo para generar reprote Excel de los usuarios
	 */
	public void reportExcel()
	{
		String name="Laminas_"+new Timestamp(Calendar.getInstance().getTime().getTime());
		 HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet();
	        workbook.setSheetName(0, "Hoja excel");

	        String[] headers = new String[]{
	            "n°",
	            "Lámina",
	            "Usuario"	           
	        };
	        
	        Object[][] data = new Object[recomendedRepeatedList.size()][9];
	        for (int i = 0; i < data.length; i++) {
	        	data[i][0]=i+1;
	        	data[i][1]=recomendedRepeatedList.get(i).getId();
	        	data[i][2]=getUserName(recomendedRepeatedList.get(i).getUserId());
	        	
				
			}

	        CellStyle headerStyle = workbook.createCellStyle();
	        Font font = workbook.createFont();
	        font.setBold(true);
	        headerStyle.setFont(font);

	        CellStyle style = workbook.createCellStyle();
	        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
	        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	        HSSFRow headerRow = sheet.createRow(0);
	        for (int i = 0; i < headers.length; ++i) {
	            String header = headers[i];
	            HSSFCell cell = headerRow.createCell(i);
	            cell.setCellStyle(headerStyle);
	            cell.setCellValue(header);
	        }

	        for (int i = 0; i < data.length; ++i) {
	            HSSFRow dataRow = sheet.createRow(i + 1);

	            dataRow.createCell(0).setCellValue(String.valueOf(data[i][0]));
	            dataRow.createCell(1).setCellValue(String.valueOf(data[i][1]));
	            dataRow.createCell(2).setCellValue(String.valueOf(data[i][2]));
	            
	           
	        }

	        HSSFRow dataRow = sheet.createRow(1 + data.length);
	        HSSFCell total = dataRow.createCell(1);
	        total.setCellType(Cell.CELL_TYPE_FORMULA);
	        total.setCellStyle(style);
	        

	        FileOutputStream file;
			try {
				String path = new File(".").getCanonicalPath();
				
				file = new FileOutputStream("/home/victorandres/Documentos/UniversidadELBosque/Semestre3/Programacion2/Proyecto/archivos/WCSC_Laminas.xls");
				//file = new FileOutputStream(path+"\\WCSC_Laminas.xls");
				workbook.write(file);
				file.close();
				
				File file2 = new File("/home/victorandres/Documentos/UniversidadELBosque/Semestre3/Programacion2/Proyecto/archivos/WCSC_Laminas.xls");
				//File file2 = new File(path+"\\WCSC_Laminas.xls");
				
				FacesContext facesC=FacesContext.getCurrentInstance();
				
				HttpServletResponse response= (HttpServletResponse)facesC.getExternalContext().getResponse();
				response.reset();
				response.setHeader("Content-Type", "application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=WCSC_Laminas.xls");
				
				OutputStream out=response.getOutputStream();
				
				InputStream is=new FileInputStream(file2);
				byte[] bytes=new byte[2048];
				int reader;
				while((reader=is.read(bytes))>0)
				{
					out.write(bytes,0,reader);
				}
				
				out.flush();
				is.close();
				out.close();
				
								
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	}
	
	/**
	 * Actualiza lista de usuarios con mas laminas faltantes
	 */
	public void updateListTopMax()
	{
		userTopMax=new ArrayList<>();
		ColaPrioridadMayores cola=new ColaPrioridadMayores();
		UserServices us=new UserServices();	
		List<User> users=us.list();
		for (int i = 0; i < users.size(); i++)
		{
			
			MissingSheetServices ms=new MissingSheetServices();
			List<Missingsheet> listMissing=ms.listById(users.get(i).getId());
			
			if(!users.get(i).getUserType().equals(Constantes.USER_TYPE_ADMIN))
			{
				cola.addNodo(new UserSheets(users.get(i).getUserName(), listMissing.size()));
				
			}
			
		}
		
		userTopMax=cola.getAsList();
	}
	
	/**
	 * Actualiza lista de usuarios con menos laminas faltantes
	 */
	public void updateListTopMin()
	{
		userTopMin=new ArrayList<>();
		ColaPrioridadMenores cola=new ColaPrioridadMenores();
		UserServices us=new UserServices();		
		List<User> users=us.list();
		for (int i = 0; i < users.size(); i++)
		{		
			
			MissingSheetServices ms=new MissingSheetServices();
			List<Missingsheet> listMissing=ms.listById(users.get(i).getId());
			if(!users.get(i).getUserType().equals(Constantes.USER_TYPE_ADMIN))
			{
				cola.addNodo(new UserSheets(users.get(i).getUserName(), listMissing.size()));
				
			}
			
			
		}
		userTopMin=cola.getAsList();
	}
	
	/**
	 * Actualiza lista de usuarios sin laminas faltantes
	 */
	public void updateListFullAlbum()
	{
		userFullAlbum=new ArrayList<>();
		UserServices us=new UserServices();		
		List<User> users=us.list();
		int cont=0;
		for (int i = 0; i < users.size(); i++)
		{
			
			
			MissingSheetServices ms=new MissingSheetServices();
			List<Missingsheet> listMissing=ms.listById(users.get(i).getId());
			if(listMissing.size()==0 && !users.get(i).getUserType().equals(Constantes.USER_TYPE_ADMIN))
			{
				userFullAlbum.add(new UserSheets(users.get(i).getUserName(), listMissing.size()));
				cont++;
			}
			
			if(cont==4)
			{
				i= users.size();
			}
			
			
			
		}
		
	}

	public Repeatedsheet getRepeated() {
		return repeated;
	}

	public void setRepeated(Repeatedsheet repeated) {
		this.repeated = repeated;
	}

	public Missingsheet getMissing() {
		return missing;
	}

	public void setMissing(Missingsheet missing) {
		this.missing = missing;
	}

	public List<Repeatedsheet> getRepeatedList() {
		return repeatedList;
	}

	public void setRepeatedList(List<Repeatedsheet> repeatedList) {
		this.repeatedList = repeatedList;
	}

	public String getSearchNumber() {
		return searchNumber;
	}

	public void setSearchNumber(String searchNumber) {
		this.searchNumber = searchNumber;
	}

	public Repeatedsheet getSelectedSheet() {
		return selectedSheet;
	}

	public void setSelectedSheet(Repeatedsheet selectedSheet) {
		this.selectedSheet = selectedSheet;
	}

	public Missingsheet getMissingSelectedSheet() {
		return missingSelectedSheet;
	}

	public void setMissingSelectedSheet(Missingsheet missingSelectedSheet) {
		this.missingSelectedSheet = missingSelectedSheet;
	}

	public List<New> getNews() {
		return news;
	}

	public void setNews(List<New> news) {
		this.news = news;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Audit getAction() {
		return action;
	}

	public void setAction(Audit action) {
		this.action = action;
	}

	public New getNotice() {
		return notice;
	}

	public void setNotice(New notice) {
		this.notice = notice;
	}

	public String getAsuntoContact() {
		return asuntoContact;
	}

	public void setAsuntoContact(String asuntoContact) {
		this.asuntoContact = asuntoContact;
	}

	public String getBodyContact() {
		return bodyContact;
	}

	public void setBodyContact(String bodyContact) {
		this.bodyContact = bodyContact;
	}

	public List<Missingsheet> getMissingList() {
		return missingList;
	}

	public void setMissingList(List<Missingsheet> missingList) {
		this.missingList = missingList;
	}

	public List<Missingsheet> getFilteredMissing() {
		return filteredMissing;
	}

	public void setFilteredMissing(List<Missingsheet> filteredMissing) {
		this.filteredMissing = filteredMissing;
	}

	public List<Repeatedsheet> getFilteredRepeted() {
		return filteredRepeted;
	}

	public void setFilteredRepeted(List<Repeatedsheet> filteredRepeted) {
		this.filteredRepeted = filteredRepeted;
	}

	public List<Repeatedsheet> getSearchSeheets() {
		return searchSeheets;
	}

	public void setSearchSeheets(List<Repeatedsheet> searchSeheets) {
		this.searchSeheets = searchSeheets;
	}

	public List<Repeatedsheet> getRecomendedRepeatedList() {
		return recomendedRepeatedList;
	}

	public void setRecomendedRepeatedList(List<Repeatedsheet> recomendedRepeatedList) {
		this.recomendedRepeatedList = recomendedRepeatedList;
	}

	public List<Repeatedsheet> getFilteredRecomList() {
		return filteredRecomList;
	}

	public void setFilteredRecomList(List<Repeatedsheet> filteredRecomList) {
		this.filteredRecomList = filteredRecomList;
	}

	public List<Stadium> getStadiums() {
		return stadiums;
	}

	public void setStadiums(List<Stadium> stadiums) {
		this.stadiums = stadiums;
	}


	public List<UserSheets> getUserTopMax() {
		return userTopMax;
	}


	public void setUserTopMax(List<UserSheets> userTopMax) {
		this.userTopMax = userTopMax;
	}


	public List<UserSheets> getUserTopMin() {
		return userTopMin;
	}


	public void setUserTopMin(List<UserSheets> userTopMin) {
		this.userTopMin = userTopMin;
	}


	public List<UserSheets> getUserFullAlbum() {
		return userFullAlbum;
	}


	public void setUserFullAlbum(List<UserSheets> userFullAlbum) {
		this.userFullAlbum = userFullAlbum;
	}

}
