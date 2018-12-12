package com.unbosque.wcsc.beans;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.primefaces.PrimeFaces;

import com.unbosque.wcsc.daos.impl.UserDaoImpl;
import com.unbosque.wcsc.entities.Audit;
import com.unbosque.wcsc.entities.Missingsheet;
import com.unbosque.wcsc.entities.New;
import com.unbosque.wcsc.entities.Parameter;
import com.unbosque.wcsc.entities.Repeatedsheet;
import com.unbosque.wcsc.entities.User;
import com.unbosque.wcsc.services.AuditService;
import com.unbosque.wcsc.services.MissingSheetServices;
import com.unbosque.wcsc.services.NewsService;
import com.unbosque.wcsc.services.ParameterServices;
import com.unbosque.wcsc.services.RepeatedSheetServices;
import com.unbosque.wcsc.services.UserServices;
import com.unbosque.wcsc.util.Constantes;
import com.unbosque.wcsc.util.Utility;

@ManagedBean
@SessionScoped
public class UserBean{
	
	private static final Logger LOG= Logger.getLogger(UserBean.class);
	
	/**
	 * Contraseña que digita en usuario al momento del reguistro
	 */
	private String pass;
	
	/**
	 * Contraseña actual 1 que digita el usuario al cambiar a contraseña
	 */
	private String pass1;
	
	/**
	 * Contraseña actual de confirmacion que digita el usuario al cambiar a contraseña
	 */
	private String pass2;
	
	/**
	 * Nueva contraseña
	 */
	private String newPass1;
	
	/**
	 * Confirmacion de ueva contraseña
	 */
	private String newPass2;
	
	private User userRegister,userLogin,userConfigure;
	
	public UserBean()
	{
		
		userRegister=new User();
		userLogin=new User();		
		userConfigure=new User();
		userConfigure.setEmailAddress("tuCorreo@unMail.com");
		LOG.info("UserBean creado correctamente");
		
	}

	/**
	 * Metodo para confirmar el ingreso del suario
	 * @return nombre de la pagina del usuario
	 */
	public String login()
	{
		
		
		String page="";	
		LOG.info("Se ha presionado Login");
		
			//OBTENER USUSARIO
			UserServices uService=new UserServices();
			
			userLogin=uService.getUsuarioByUserName(userLogin.getUserName());
			if(userLogin!=null)
			{
				if(userLogin.getIntents()>=1)
				{
					String password=Utility.getStringMessageDigest(pass, Utility.MD5);
					if(password.equals(userLogin.getPassword()))
					{
											
						UserServices uService2=new UserServices();
						userLogin.setIntents(3);
						uService2.update(userLogin);						
						userConfigure=userLogin;
						if(userLogin.getUserType().equals(Constantes.USER_TYPE_NEW))
						{
							
							java.sql.Timestamp time1=new java.sql.Timestamp(Calendar.getInstance().getTime().getTime());
														
							if(Utility.getDifferenceInSeconds(time1,userLogin.getDateLastPassword())>getParameterSecondsValueNum())
							{
								
								FacesMessage message1 = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Demora en el ingreso", "Lo sentimos,se ha demorado más del tiempo establecido, por favor vuelva a generar una contraseña dando click en ¿Olvidaste tu contraseña?");
							         
							    PrimeFaces.current().dialog().showMessageDynamic(message1);
							
								
							}
							else
							{
								FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", userLogin);
								
								page="edit";
								
							}						
						}
						else if(userLogin.getUserType().equals(Constantes.USER_TYPE_NORMAL))
						{
							if(userLogin.getActive().equals(Constantes.USER_ACTIVE))
							{
								
								int days=getParameterDaysValueNum();
								Timestamp dateUser = userLogin.getDateLastPassword();
								Timestamp date=new Timestamp(Calendar.getInstance().getTime().getTime());
								if(days<=Utility.getDiferenceBtwnTimeStamp(date, dateUser))
								{
									
									FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", userLogin);
									
									page="edit";
									
								}
								else
								{
									FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", userLogin);
									
									createAction(userLogin.getId(), Constantes.OP_LOGIN, userLogin.getId(), Constantes.TABLE_USER);
									
									
									
									page="sticker";
									
								}
								
								
							}
							else if(userLogin.getActive().equals(Constantes.USER_LOOCKED))
							{
								FacesMessage message3 = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario bloqueado", "Envía un correo a awcsc1706@gmail.com manifestando el bloqueo");
						         
						        PrimeFaces.current().dialog().showMessageDynamic(message3);
							}
							
						}
						else if(userLogin.getUserType().equals(Constantes.USER_TYPE_ADMIN))
						{
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", userLogin);
							
							createAction(userLogin.getId(), Constantes.OP_LOGIN, userLogin.getId(), Constantes.TABLE_USER);
							
														
							page="admin";
							
							
						}
						
						
					}
					else
					{
						if(!userLogin.getUserType().equals(Constantes.USER_TYPE_ADMIN))
						{
							UserServices uService2=new UserServices();
							userLogin.setIntents(userLogin.getIntents()-1);
							if(userLogin.getIntents()<1)
							{
								userLogin.setActive(Constantes.USER_LOOCKED);
								if(uService2.update(userLogin))
								{
									FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contraseña incorrecta", "Contraseña incorrecta! "+ (userLogin.getEmailAddress()) +" bloqueado, genere una nueva contraseña en \" ¿Olvidaste tu contraseña? \", o comúniquese con awcsc1706@gmail.com");
							         
							        PrimeFaces.current().dialog().showMessageDynamic(message);
								}
								
							}
							else
							{
								if(uService2.update(userLogin))
								{
									FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Contraseña incorrecta", "Contraseña incorrecta! Te quedan "+ (userLogin.getIntents()) +" intentos");
							         
							        PrimeFaces.current().dialog().showMessageDynamic(message);
								}
								
							}						
							
							
						}
					}					
				}
				else
				{
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario bloqueado", "Intentos de ingreaso acabados");
			         
			        PrimeFaces.current().dialog().showMessageDynamic(message);
				}
				
				
				
			}
			else
			{
				userLogin=new User();				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "No hay registros", "Usuario no se encuntra registrado. Registrate ya!");
		         
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
			
			
		
		
		return page;
		
	}

	/**
	 * Metodo para registrar el usuario
	 */
	public void register()
	{
		LOG.info("Se ha presionado Registrar");
		
		if(!Utility.isLongNumeric(userRegister.getPhoneNumber()) && !userRegister.getPhoneNumber().equals(""))
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Número telefónico inválido");
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
		else
		{
			UserServices uService1=new UserServices();
			User userExist=uService1.getUsuarioByUserName(userRegister.getUserName());
			User userExist2=uService1.getUsuarioByEmail(userRegister.getEmailAddress());
			if(userExist!=null || userExist2!=null)
			{

				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Usuario ya está registrado, por favor intente con otro");
		        FacesContext.getCurrentInstance().addMessage(null, message);
			}
			else
			{	
				String pass=Utility.randomStr(userRegister.getEmailAddress());
				
				String passC=Utility.getStringMessageDigest(pass, Utility.MD5);
				
				userRegister.setActive(Constantes.USER_ACTIVE);
				userRegister.setDateLastPassword(new Timestamp(Calendar.getInstance().getTime().getTime()));
				userRegister.setUserType(Constantes.USER_TYPE_NEW);
				userRegister.setPassword(passC);
				userRegister.setIntents(3);
				
				UserServices uService=new UserServices();
				if(uService.save(userRegister))
				{
					
					createAction(userRegister.getId(), Constantes.OP_REGISTER, userRegister.getId(),Constantes.TABLE_USER );
					if(enviarGmail(userRegister.getEmailAddress(),"Bienvenido a World Cup Sticker Changer.", "Bienvenido(a), "+userRegister.getFullName()+".\n\nIngresa ya con los siguientes datos:\n\nUsuario: "+userRegister.getUserName()+"\nContraseña: "+pass+"\n\nNO OLVIDES CAMBIAR TU CONTRASEÑA POR UNA MÁS SEGURA.\n\nSaludos, administración WCSC"))
					{					
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "¡BIENVENIDO!", "Te hemos enviado tu contraseña al correo "+this.userRegister.getEmailAddress()+". Tienes "+ getParameterSecondsValueNum() +" segundos para ingresar, de lo contrario el registro será cancelado. Ingresa ya para comenzar a encontrar tus láminas.");
				        FacesContext.getCurrentInstance().addMessage(null, message);
						
					}
					else
					{
						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Correo "+this.userRegister.getEmailAddress()+" no disponible, por favor revíselo o intente con otro");
				        FacesContext.getCurrentInstance().addMessage(null, message);
					}
				}
			}
			
			
			
		}
		
		
	}
	
	/**
	 * Metodo para salir del sistema, cerrando sesion
	 * @return
	 */
	public String exit()
	{
		String page="";
		
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		
		createAction(userLogin.getId(), Constantes.OP_LOGOUT, userLogin.getId(), Constantes.TABLE_USER);
		userLogin=new User();
		userConfigure=new User();
		userConfigure.setEmailAddress("tuCorreo@unMail.com");
		page="index";
		
		LOG.info("Se ha presionado EXIT");
		
		return page;
	}
	
	/**
	 * Metodo para cambiar datos del usuario
	 */
	public void edit()
	{
		LOG.info("Se ha presionado Editar perfil");
		
		UserServices uService=new UserServices();
		userLogin.setDateLastPassword(new Timestamp(Calendar.getInstance().getTime().getTime()));
		if(uService.update(userLogin))
		{
			createAction(userLogin.getId(), Constantes.OP_UPDATE_DATA, userLogin.getId(), Constantes.TABLE_USER);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "¡Cambio exitoso!", "Tus datos han sido cambiados satisfactoriamente");
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
	}
	
	/**
	 * Metodo para cambiar el password del usuario
	 */
	public String editPass()
	{
		LOG.info("Se ha presionado Editar password");
		
		String page="";
		if(!pass1.equals(pass2))
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Error de validación en contraseña actual");
	        FacesContext.getCurrentInstance().addMessage(null, message);
		}
		else
		{
			pass1=Utility.getStringMessageDigest(pass1, Utility.MD5);
			if(!pass1.equals(userLogin.getPassword()))
			{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Contraseña incorrecta");
		        FacesContext.getCurrentInstance().addMessage(null, message);
			}
			else
			{
				if(!newPass1.equals(newPass2))
				{
					FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Error de validación en nueva contraseña, no coinciden");
			        FacesContext.getCurrentInstance().addMessage(null, message);
				}
				else
				{
					UserServices uService=new UserServices();
					
					userConfigure=userLogin;
					String userType=userConfigure.getUserType();
					userConfigure.setPassword(Utility.getStringMessageDigest(newPass1, Utility.MD5));
					if(userType.equals(Constantes.USER_TYPE_NEW))
					{
						userConfigure.setUserType(Constantes.USER_TYPE_NORMAL);
					}					
					userConfigure.setDateLastPassword(new Timestamp(Calendar.getInstance().getTime().getTime()));
					if(uService.update(userConfigure))
					{
						createAction(userConfigure.getId(), Constantes.OP_CHANGE_PASS, userConfigure.getId(), Constantes.TABLE_USER);
						userLogin=userConfigure;

						FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "¡Cambio exitoso!", "Contraseña cambiada satisfactoriamente");
				        FacesContext.getCurrentInstance().addMessage(null, message);
				        if(userType.equals(Constantes.USER_TYPE_NEW))
						{				        	
				        	page="sticker";
						}
					}		
					
					
				}
				
				
			}
			
		}
		return page;
		
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
	 * Metodo para generar y enviar nueva contraseña
	 */
	public void sendPass()
	{
		UserServices us=new UserServices();
		userConfigure=us.getUsuarioByEmail(userConfigure.getEmailAddress());
		if(userConfigure==null)
		{
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Correo no enviado", "No se encuentra registrado");
	         
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		else
		{
			userLogin=userConfigure;			
			String newPass=Utility.randomStr(userLogin.getEmailAddress());
			String asunto="WCSC Administración - envío de contraseña";
			String cuerpo="Estimado "+userLogin.getFullName()+".\n";
			cuerpo+="Su nueva contraseña es "+newPass;
			if(userLogin.getUserType().equals(Constantes.USER_TYPE_NORMAL))
			{
				
				cuerpo+="\n\nPor favor, recuerde que tiene "+getParameterSecondsValueNum()+" segundos para ingresar al sistema, de lo contrario, deberá generar una nueva contraseña.";
			}
			cuerpo+="\n\nSaludos. Administración WCSC";
			
			
			if(enviarGmail(userLogin.getEmailAddress(), asunto, cuerpo))
			{
				UserServices userS=new UserServices();
				if(userLogin.getUserType().equals(Constantes.USER_TYPE_NORMAL))
				{
					userLogin.setUserType(Constantes.USER_TYPE_NEW);
				}			
				userLogin.setPassword(Utility.getStringMessageDigest(newPass, Utility.MD5));
				userLogin.setDateLastPassword(new Timestamp(Calendar.getInstance().getTime().getTime()));
				userLogin.setIntents(3);
				userLogin.setActive(Constantes.USER_ACTIVE);
				userS.update(userLogin);
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correo enviado", "Correo enviado satisfactoriamente a "+userLogin.getEmailAddress()+". Recuerde que tiene unos cuantos segundo para ingresar al sistema. ");
		         
		        PrimeFaces.current().dialog().showMessageDynamic(message);
		        createAction(userLogin.getId(), Constantes.OP_SEND_PASS, userLogin.getId(), Constantes.TABLE_USER);
		        
		        LOG.info("Se envió nueva contraseña");
			}
			else
			{
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Correo enviado no enviado", "Error al enviar correo a "+userLogin.getEmailAddress());
		         
		        PrimeFaces.current().dialog().showMessageDynamic(message);
		        
			}
			
			
			
			
		}
		userConfigure=new User();
		userConfigure.setEmailAddress("tuCorreo@unMail.com");
		
		
	}
	
	/**
	 * Metodo de envio de correo
	 * @param destinatario - Correo destino
	 * @param asunto - Asunto del correo
	 * @param cuerpo - Cuerpo del correo
	 * @return True si envio con exito. False si no envio
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
	 * Metodo para registrar una accion hecha por el usuario
	 * @param idUser - id del usuario que realizo la accion
	 * @param op - operacion que realizo
	 * @param idTable - id de la tabla afectada
	 * @param tableName - nombre de la tabla afectada
	 * @return - True registro con exito. False si no resgistro
	 */
	public boolean createAction(int idUser,String op,int idTable,String tableName)
	{
		boolean success=false;
		Audit action=new Audit();
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
	
	

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public User getUserRegister() {
		return userRegister;
	}

	public void setUserRegister(User userRegister) {
		this.userRegister = userRegister;
	}

	public User getUserLogin() {
		return userLogin;
	}

	public void setUserLogin(User userLogin) {
		this.userLogin = userLogin;
	}

	public String getPass1() {
		return pass1;
	}

	public void setPass1(String pass1) {
		this.pass1 = pass1;
	}

	public String getPass2() {
		return pass2;
	}

	public void setPass2(String pass2) {
		this.pass2 = pass2;
	}

	public String getNewPass1() {
		return newPass1;
	}

	public void setNewPass1(String newPass1) {
		this.newPass1 = newPass1;
	}

	public String getNewPass2() {
		return newPass2;
	}

	public void setNewPass2(String newPass2) {
		this.newPass2 = newPass2;
	}

	public User getUserConfigure() {
		return userConfigure;
	}

	public void setUserConfigure(User userConfigure) {
		this.userConfigure = userConfigure;
	}
	

}
