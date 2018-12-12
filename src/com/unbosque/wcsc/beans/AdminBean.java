package com.unbosque.wcsc.beans;

import java.awt.Container;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import javax.servlet.http.HttpServletRequest;
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
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Header;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.unbosque.wcsc.entities.Audit;
import com.unbosque.wcsc.entities.New;
import com.unbosque.wcsc.entities.Parameter;
import com.unbosque.wcsc.entities.Stadium;
import com.unbosque.wcsc.entities.User;
import com.unbosque.wcsc.services.AuditService;
import com.unbosque.wcsc.services.NewsService;
import com.unbosque.wcsc.services.ParameterServices;
import com.unbosque.wcsc.services.StadiumServices;
import com.unbosque.wcsc.services.UserServices;
import com.unbosque.wcsc.util.Constantes;
import com.unbosque.wcsc.util.Utility;

@ManagedBean
@ViewScoped
public class AdminBean {
	
	private static final Logger LOG= Logger.getLogger(AdminBean.class);
	
	private boolean parametersDefault;
	private Audit selectedAction;
	private List<Parameter> parameters; 
	private User user;
	private List<New> news;
	private List<User> users;
	private List<User> filteredUsers;	
	private List<Audit> actions;
	private List<Audit> filteredActions;
	private List<Stadium> stadiums;
	private Stadium selectedStadium;
	private Stadium stadium;
	private User selectedUser;
	private Parameter selectedParameter;
	private New selectedNew;
	private StreamedContent file;
	private New notice;
	private String userEmailSearch;
	private int id;
	
	public AdminBean()
	{
		user=(User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
		
		parametersDefault=false;
		selectedAction = new Audit();
		selectedUser=new User();
		selectedNew=new New();
		notice=new New();
		stadium=new Stadium();
		//selectedStadium=new Stadium();
		
		news=new ArrayList<>();
		parameters=new ArrayList<>();
		users=new ArrayList<>();
		filteredUsers=new ArrayList<>();
		actions = new ArrayList<>();
		filteredActions=new ArrayList<>();
		stadiums=new ArrayList<>();
		
		updateNews();
		updateUsers();
		updateActions();
		updateStadiums();
		updateParameters();
			
		
		LOG.info("AdminBean creado correctamente");
		
	}	
	
	/**
	 * Metodo para validar sesion
	 * @param pUser - usuario logeado
	 */
	public void validateSession(User pUser)
	{
		user=pUser;
		if(user.getEmailAddress()==null || !user.getUserType().equals(Constantes.USER_TYPE_ADMIN))
		{
			LOG.warn("Intento de entrada sin autenticación por la ip "+Utility.getClientIp());
			
			ExternalContext context2 = FacesContext.getCurrentInstance().getExternalContext();
		    try
		    {
		        context2.redirect(context2.getRequestContextPath() +"/user/index.jsf");
		    }
		    catch (IOException e) {		           
		    	LOG.error("error: ",e);
		    } 
		}
		else
		{
			LOG.info("User creado correctamente");
		}		
	}
	
	/**
	 * Metodo para agregar un parametro
	 */
	public void addParameter()
	{
		ParameterServices ps=new ParameterServices();
		ParameterServices parameterS=new ParameterServices();
		Parameter param=ps.getParameterByCode(selectedParameter.getParameterCode());
		if(param==null)
		{
			if(parameterS.save(selectedParameter))
			{
				updateParameters();
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Parámetro agregado");
		        
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		}
		else
		{
			param.setNumberValue(selectedParameter.getNumberValue());
			if(parameterS.update(param))
			{
				updateParameters();
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Parámetro actualizado");
		        
		        PrimeFaces.current().dialog().showMessageDynamic(message);
			}
		}
		
				
		
	}
	
	/**
	 * Metodo para cambiar un parametro
	 */
	public void changeParameter()
	{
		ParameterServices parameterS=new ParameterServices();
		if(parameterS.update(selectedParameter))
		{
			updateParameters();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Parámetro actualizado");
	         
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		
		
	}
	
	/**
	 * Obtener y actualizar la lista de parametros
	 */
	public void updateParameters()
	{
		ParameterServices parameterS=new ParameterServices();
		parameters=parameterS.list();
		if(parameters.isEmpty())
		{
			createParametersByDefault();
			parametersDefault=true;
		}
		else if(parameters.size()==1)
		{
			parametersDefault=true;
			if(parameters.get(0).getParameterCode().equals(Constantes.PARAMETER_DAYS_PASS))
			{
				addParameterSecondsLoginDefault();
			}
			else if(parameters.get(0).getParameterCode().equals(Constantes.PARAMETER_SECONDS_LOGIN))
			{
				addParameterDaysPassDefault();
			}
		}
		else
		{
			parametersDefault=false;
		}
	}
	
	/**
	 * Genera parametros por defecto si en la base de datos no hay registros.
	 */
	public void createParametersByDefault()
	{
		
		Parameter param=new Parameter();
		Parameter param2=new Parameter();
		
		param.setDescriptionParameter("Días de cambio de contraseña obligatorio");
		param.setParameterCode(Constantes.PARAMETER_DAYS_PASS);		
		param.setNumberValue(60);
		param.setTextValue("Días C");
		param.setParameterType("U");	
		
		param2.setDescriptionParameter("Segundos máximos que tiene un nuevo usuario para ingresar al sistema después del registro");
		param2.setParameterCode(Constantes.PARAMETER_SECONDS_LOGIN);		
		param2.setNumberValue(120);
		param2.setTextValue("Log t");
		
		parameters.add(param);
		parameters.add(param2);
		
		
	}
	
	/**
	 * Metodo para agregar un parametro de dias de cambio de password por default
	 */
	public void addParameterDaysPassDefault()
	{
		Parameter param=new Parameter();
		param.setDescriptionParameter("Días de cambio de contraseña obligatorio");
		param.setParameterCode(Constantes.PARAMETER_DAYS_PASS);		
		param.setNumberValue(60);
		param.setTextValue("Días C");
		param.setParameterType("U");
		
		parameters.add(param);
	}
	
	/**
	 * Metodo para agregar un parametro de segundo de ingreso al sistema por default
	 */
	public void addParameterSecondsLoginDefault()
	{
		Parameter param2=new Parameter();
		param2.setDescriptionParameter("Segundos máximos que tiene un nuevo usuario para ingresar al sistema después del registro");
		param2.setParameterCode(Constantes.PARAMETER_SECONDS_LOGIN);		
		param2.setNumberValue(120);
		param2.setTextValue("Log t");
		param2.setParameterType("U");
		parameters.add(param2);
	}
	
	/**
	 * Metodo para bloequear un usuario
	 */
	public void loock()
	{
		if(selectedUser.getUserType().equals(Constantes.USER_TYPE_ADMIN))
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acción errónea", "No se puede bloquear a un administrador");
	         
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		else
		{
			UserServices userS=new UserServices();
			selectedUser.setActive(Constantes.USER_LOOCKED);
			userS.update(selectedUser);
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario "+selectedUser.getEmailAddress()+" ha sido bloqueado.");
	         
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		
		System.out.println("LOOCK");
		
	}
	
	/**
	 * Metodo para desbloquear un usuario
	 */
	public void unloock()
	{
		if(selectedUser.getUserType().equals(Constantes.USER_TYPE_ADMIN))
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Acción errónea", "");
	         
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		else
		{
			UserServices userS=new UserServices();
			selectedUser.setActive(Constantes.USER_ACTIVE);
			userS.update(selectedUser);
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Usuario "+selectedUser.getEmailAddress()+" ha sido activado.");
	         
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
			
		System.out.println("UNLOOCK");
		
	}
	
	/**
	 * Metodo que envia correo
	 * @param destinatario - Correo del destinatario
	 * @param asunto - Asunto del mensaje
	 * @param cuerpo - Cuerpo del mensaje
	 * @return - True si envio. False si no envio
	 */
	public boolean sendPassGmail(String destinatario, String asunto, String cuerpo)
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
	        me.printStackTrace();   //Si se produce un error
	    }
	    
		
		return success;
	}
	
	/**
	 * Metodo para enviar una nueva contraseña por correo a un usuario seleccionado
	 */
	public void send()
	{
		String newPass=Utility.randomStr(selectedUser.getEmailAddress());
		String asunto="WCSC Administración - envío de contraseña";
		String cuerpo="Estimado "+selectedUser.getFullName()+".\n";
		cuerpo+="Su nueva contraseña es "+newPass+"\n";
		
		if(sendPassGmail(selectedUser.getEmailAddress(), asunto, cuerpo))
		{
			UserServices userS=new UserServices();
			if(selectedUser.getUserType().equals(Constantes.USER_TYPE_NORMAL))
			{
				selectedUser.setUserType(Constantes.USER_TYPE_NEW);
			}			
			selectedUser.setPassword(Utility.getStringMessageDigest(newPass, Utility.MD5));
			selectedUser.setDateLastPassword(new Timestamp(Calendar.getInstance().getTime().getTime()));
			selectedUser.setIntents(3);
			selectedUser.setActive(Constantes.USER_ACTIVE);
			userS.update(selectedUser);
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Correo enviado", "Correo enviado satisfactoriamente a "+selectedUser.getEmailAddress());
	         
	        PrimeFaces.current().dialog().showMessageDynamic(message);
	        createAction(selectedUser.getId(), Constantes.OP_SEND_PASS, selectedUser.getId(), Constantes.TABLE_USER);
	        
	        LOG.info("Se envió nueva contraseña");
		}
		else
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Correo enviado no enviado", "Error al enviar correo a "+selectedUser.getEmailAddress());
	         
	        PrimeFaces.current().dialog().showMessageDynamic(message);
	        
		}
		
	}
	
	/**
	 * Metodo para buscar un usuario por el id
	 */
	public void searchUserById()
	{
		
		UserServices uService=new UserServices();
		User user=uService.getObject(id);
		users=new ArrayList<>();
		if(user!=null)
		{			
			users.add(user);
		}
	}
	
	/**
	 * Metodo para bloquear una noticia
	 */
	public void loockNew()
	{
		NewsService newS=new NewsService();
		selectedNew.setState(Constantes.USER_LOOCKED);
		newS.update(selectedNew);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bloqueo", "Noticia bloqueada");
        
        PrimeFaces.current().dialog().showMessageDynamic(message);
		

		
		System.out.println("Bloqueo");
	}
	
	/**
	 * Metodo para buscar un usuario
	 */
	public void searchUser()
	{
		UserServices uService=new UserServices();
		User user=uService.getUsuarioByEmail(userEmailSearch);
		users=new ArrayList<>();
		if(user!=null)
		{			
			users.add(user);
		}
	}
	
	/**
	 * Metodo para modificar un estadio
	 */
	public void modifyStadium()
	{
		StadiumServices ss=new StadiumServices();
		if(ss.update(selectedStadium))
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Se ha modificado el estadio");
	        
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}
	
	/**
	 * Metodo para actualizar la lista de estadios
	 */
	public void updateStadiums()
	{
		StadiumServices ss=new StadiumServices();
		stadiums=ss.list();
	}
	
	/**
	 * Metodo para actualizar las listas de acciones
	 */
	public void updateActions()
	{
		AuditService auditS=new AuditService();
		actions=auditS.list();
		filteredActions=actions;
		
	}
	
	/**
	 * Metodo para actualizar las listas de usuarios
	 */
	public void updateUsers()
	{
		UserServices userS=new UserServices();
		users=userS.list();
		filteredUsers=users;
		
	}
	
	/**
	 * Metodo para actualizar la lista de noticias
	 */
	public void updateNews()
	{
		NewsService newService=new NewsService();
		news=newService.list();
		
	}
	
	/**
	 * Metodo para desbloquear un usuario
	 */
	public void unloockNew()
	{
		NewsService newS=new NewsService();
		selectedNew.setState(Constantes.USER_ACTIVE);
		newS.update(selectedNew);
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Desbloqueo", "Noticia desbloqueada");
        
        PrimeFaces.current().dialog().showMessageDynamic(message);
		
		System.out.println("Desbloqueo");
	}
	
	/**
	 * Metodo para publicar una noticia
	 */
	public void publishNew()
	{
		if(createNew(user.getId(), notice.getShortDescription(), notice.getLargeDescription()))
		{
			updateNews();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Noticia agregada");
	        
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
		
		
	}
	
	/**
	 * Metodo para generar reprote PFD de los usuarios
	 */
	public void reportPDF()
	{
		
		Document document=new Document();
		String name="Usuarios";
		try {
			String path = new File(".").getCanonicalPath();
			path+="/pdf/"+name+".pdf";
			
			FileOutputStream fos=new FileOutputStream("/home/victorandres/Documentos/UniversidadELBosque/Semestre3/Programacion2/Proyecto/archivos/WCSC_Usuarios.pdf");
			//FileOutputStream fos=new FileOutputStream(path+"\\WCSC_Usuarios.pdf");
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
			p.add("WCSC - Usuarios registrados");
			
            
            f = new com.itextpdf.text.Font();
            f.setFamily(FontFamily.COURIER.name());
            f.setStyle(com.itextpdf.text.Font.NORMAL);
            f.setSize(12);
			
			Paragraph p1=new Paragraph();
			p1.setFont(f);
			p1.add("A continuación se dará un reporte de los usuarios registrados a la fecha \""+new Timestamp(Calendar.getInstance().getTime().getTime())+"\".");
			
			
			Paragraph p2=new Paragraph();
			p2.setFont(f);
			p2.add("En este reporte, se mostrará una tabla en la cual se especifican las caracteristicasde los usuarios dentro del sistema.");
			
			
			document.add(p);
			document.add(new Paragraph(" "));
			document.add(p1);
			document.add(new Paragraph(" "));
			document.add(p2);
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			
			PdfPTable table=new PdfPTable(9);
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 1, 1,5,3,7,4,3,3,5 });
			Paragraph c1=new Paragraph("n°");
			c1.getFont().setSize(9);
			c1.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c1);
			Paragraph c2=new Paragraph("id");
			c2.getFont().setSize(9);
			c2.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c2);
			Paragraph c3=new Paragraph("Nombre");
			c3.getFont().setSize(9);
			c3.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c3);
			Paragraph c4=new Paragraph("Usuario");
			c4.getFont().setSize(9);
			c4.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c4);
			Paragraph c5=new Paragraph("Correo");
			c5.getFont().setSize(9);
			c5.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c5);
			Paragraph c6=new Paragraph("Teléfono");
			c6.getFont().setSize(9);
			c6.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c6);
			Paragraph c7=new Paragraph("Tipo");
			c7.getFont().setSize(9);
			c7.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c7);
			Paragraph c8=new Paragraph("Estado");
			c8.getFont().setSize(9);
			c8.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c8);
			Paragraph c9=new Paragraph("Modificación contraseña");
			c9.getFont().setSize(8);
			c9.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c9);
			
			for (int i = 0; i < users.size(); i++)
			{
				
				Paragraph col1=new Paragraph(String.valueOf(i+1));
				col1.getFont().setSize(8);				
				table.addCell(col1);
				Paragraph col2=new Paragraph(String.valueOf(users.get(i).getId()));
				col2.getFont().setSize(8);				
				table.addCell(col2);
				Paragraph col3=new Paragraph(users.get(i).getFullName());
				col3.getFont().setSize(8);				
				table.addCell(col3);
				Paragraph col4=new Paragraph(users.get(i).getUserName());
				col4.getFont().setSize(8);				
				table.addCell(col4);
				Paragraph col5=new Paragraph(users.get(i).getEmailAddress());
				col5.getFont().setSize(8);				
				table.addCell(col5);
				Paragraph col6=new Paragraph(users.get(i).getPhoneNumber());
				col6.getFont().setSize(8);				
				table.addCell(col6);
				Paragraph col7=new Paragraph(users.get(i).getUserType());
				col7.getFont().setSize(8);				
				table.addCell(col7);
				Paragraph col8=new Paragraph(users.get(i).getActive());
				col8.getFont().setSize(8);			
				table.addCell(col8);
				Paragraph col9=new Paragraph(String.valueOf(users.get(i).getDateLastPassword()));
				col9.getFont().setSize(8);				
				table.addCell(col9);			
				
			}
			document.add(table);
			document.close();
			
			File file = new File("/home/victorandres/Documentos/UniversidadELBosque/Semestre3/Programacion2/Proyecto/archivos/WCSC_Usuarios.pdf");
			//File file = new File(path+"\\WCSC_Usuarios.pdf");
			java.awt.Desktop.getDesktop().open(file);
			
			FacesContext facesC=FacesContext.getCurrentInstance();
			
			HttpServletResponse response= (HttpServletResponse)facesC.getExternalContext().getResponse();
			response.reset();
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=WCSC_Usuarios.pdf");
			
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
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Descarga hecha", "Se ha descargado el archivo");
	        
	        PrimeFaces.current().dialog().showMessageDynamic(message);
	        
	        
			
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

		
		
		System.out.println("REPORTE");
		
	}
	
	/**
	 * Metodo para generar reprote Excel de los usuarios
	 */
	public void reportExcel()
	{
		
		
		String name="Usuarios_"+new Timestamp(Calendar.getInstance().getTime().getTime());
		 HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet();
	        workbook.setSheetName(0, "Hoja excel");

	        String[] headers = new String[]{
	            "n°",
	            "id",
	            "Nombre",
	            "Usuario",
	            "Correo",
	            "Teléfono",
	            "Tipo",
	            "Estado",
	            "Modificación contraseña"
	        };
	        
	        Object[][] data = new Object[users.size()][9];
	        for (int i = 0; i < data.length; i++) {
	        	data[i][0]=i+1;
	        	data[i][1]=users.get(i).getId();
	        	data[i][2]=users.get(i).getFullName();
	        	data[i][3]=users.get(i).getUserName();
	        	data[i][4]=users.get(i).getEmailAddress();
	        	data[i][5]=users.get(i).getPhoneNumber();
	        	data[i][6]=users.get(i).getUserType();
	        	data[i][7]=users.get(i).getActive();
	        	data[i][8]=users.get(i).getDateLastPassword();
				
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
	            dataRow.createCell(3).setCellValue(String.valueOf(data[i][3]));
	            dataRow.createCell(4).setCellValue(String.valueOf(data[i][4]));
	            dataRow.createCell(5).setCellValue(String.valueOf(data[i][5]));
	            dataRow.createCell(6).setCellValue(String.valueOf(data[i][6]));
	            dataRow.createCell(7).setCellValue(String.valueOf(data[i][7]));
	            dataRow.createCell(8).setCellValue(String.valueOf(data[i][8]));
	           
	        }

	        HSSFRow dataRow = sheet.createRow(1 + data.length);
	        HSSFCell total = dataRow.createCell(1);
	        total.setCellType(Cell.CELL_TYPE_FORMULA);
	        total.setCellStyle(style);
	        

	        FileOutputStream file;
			try {
				String path = new File(".").getCanonicalPath();
				
				file = new FileOutputStream("/home/victorandres/Documentos/UniversidadELBosque/Semestre3/Programacion2/Proyecto/archivos/WCSC_Usuarios.xls");
				//file = new FileOutputStream(path+"\\WCSC_Usuarios.xls");
				workbook.write(file);
				file.close();
				
				File file2 = new File("/home/victorandres/Documentos/UniversidadELBosque/Semestre3/Programacion2/Proyecto/archivos/WCSC_Usuarios.xls");
				//File file2 = new File(path+"\\WCSC_Usuarios.xls");
				
				FacesContext facesC=FacesContext.getCurrentInstance();
				
				HttpServletResponse response= (HttpServletResponse)facesC.getExternalContext().getResponse();
				response.reset();
				response.setHeader("Content-Type", "application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=WCSC_Usuarios.xls");
				
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
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Descarga hecha", "Se ha descargado el archivo");
		        
		        PrimeFaces.current().dialog().showMessageDynamic(message);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	}
	
	/**
	 * Metodo para generar reprote PDF de las acciones hechas por los uusairos
	 */
	public void reportAuditPDF()
	{
		
		Document document=new Document();
		String name="Auditoria_"+new Timestamp(Calendar.getInstance().getTime().getTime());
		try {
			String path = new File(".").getCanonicalPath();
			FileOutputStream fos=new FileOutputStream("/home/victorandres/Documentos/UniversidadELBosque/Semestre3/Programacion2/Proyecto/archivos/WCSC_Auditoría.pdf");
			//FileOutputStream fos=new FileOutputStream(path+"\\WCSC_Auditoría.pdf");
			
			PdfWriter.getInstance(document, fos).setInitialLeading(50);
			document.open();
			
			document.add(new Paragraph(" "));
			
			com.itextpdf.text.Font f = new com.itextpdf.text.Font();
            f.setFamily(FontFamily.COURIER.name());
            f.setStyle(com.itextpdf.text.Font.BOLD);
            f.setSize(20);
            
            Paragraph p=new Paragraph();
            p.setFont(f);
            p.setAlignment(Chunk.ALIGN_CENTER);
			p.add("WCSC - Auditoría");
			
            
            f = new com.itextpdf.text.Font();
            f.setFamily(FontFamily.COURIER.name());
            f.setStyle(com.itextpdf.text.Font.NORMAL);
            f.setSize(12);
			
			Paragraph p1=new Paragraph();
			p1.setFont(f);
			p1.add("A continuación se dará un reporte de las acciones realizadas por los usuarios a la fecha \""+new Timestamp(Calendar.getInstance().getTime().getTime())+"\".");
			
			
			Paragraph p2=new Paragraph();
			p2.setFont(f);
			p2.add("En este reporte, se mostrará una tabla en la cual se especifican todas las acciones de los usuarios dentro del sistema.");
			
			
			document.add(p);
			document.add(new Paragraph(" "));
			document.add(p1);
			document.add(new Paragraph(" "));
			document.add(p2);
			document.add(new Paragraph(" "));
			document.add(new Paragraph(" "));
			PdfPTable table=new PdfPTable(8);
			table.setWidthPercentage(100);
			table.setWidths(new float[] { 1, 1,3,3,3,3,5,3});
			table.setPaddingTop(20);
			Paragraph c1=new Paragraph("n°");
			c1.getFont().setSize(8);
			c1.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c1);
			Paragraph c2=new Paragraph("id");
			c2.getFont().setSize(8);
			c2.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c2);
			Paragraph c3=new Paragraph("Id usuario");
			c3.getFont().setSize(8);
			c3.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c3);
			Paragraph c4=new Paragraph("Id tabla");
			c4.getFont().setSize(8);
			c4.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c4);
			Paragraph c5=new Paragraph("Operación");
			c5.getFont().setSize(8);
			c5.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c5);
			Paragraph c6=new Paragraph("Tabla");
			c6.getFont().setSize(8);
			c6.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c6);
			Paragraph c7=new Paragraph("Fecha");
			c7.getFont().setSize(8);
			c7.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c7);
			Paragraph c8=new Paragraph("Dirección IP");
			c8.getFont().setSize(8);
			c8.getFont().setStyle(com.itextpdf.text.Font.BOLD);
			table.addCell(c8);
			
			for (int i = 0; i < actions.size(); i++)
			{
				
				Paragraph col1=new Paragraph(String.valueOf(i+1));
				col1.getFont().setSize(7);				
				table.addCell(col1);
				Paragraph col2=new Paragraph(String.valueOf(actions.get(i).getId()));
				col2.getFont().setSize(7);				
				table.addCell(col2);
				Paragraph col3=new Paragraph(String.valueOf(actions.get(i).getUserId()));
				col3.getFont().setSize(7);				
				table.addCell(col3);
				Paragraph col4=new Paragraph(String.valueOf(actions.get(i).getTableId()));
				col4.getFont().setSize(7);				
				table.addCell(col4);
				Paragraph col5=new Paragraph(actions.get(i).getOperation());
				col5.getFont().setSize(7);				
				table.addCell(col5);
				Paragraph col6=new Paragraph(actions.get(i).getTableName());
				col6.getFont().setSize(7);				
				table.addCell(col6);
				Paragraph col7=new Paragraph(String.valueOf(actions.get(i).getCreateDate()));
				col7.getFont().setSize(7);				
				table.addCell(col7);
				Paragraph col8=new Paragraph(String.valueOf(actions.get(i).getIpAddress()));
				col8.getFont().setSize(7);			
				table.addCell(col8);			
				
			}
			
			document.add(table);
			document.close();
			
			File file = new File("/home/victorandres/Documentos/UniversidadELBosque/Semestre3/Programacion2/Proyecto/archivos/WCSC_Auditoría.pdf");
			//File file = new File(path+"\\WCSC_Auditoría.pdf");
			java.awt.Desktop.getDesktop().open(file);
			FacesContext facesC=FacesContext.getCurrentInstance();
			
			HttpServletResponse response= (HttpServletResponse)facesC.getExternalContext().getResponse();
			response.reset();
			response.setHeader("Content-Type", "application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename=WCSC_Auditoría.pdf");
			
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
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Descarga hecha", "Se ha descargado el archivo");
	        
	        PrimeFaces.current().dialog().showMessageDynamic(message);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		
	}
	
	/**
	 * Metodo para generar reprote Excel de las acciones hechas por los uusairos
	 */
	public void reportAuditExcel()
	{
		String name="Auditoría_"+new Timestamp(Calendar.getInstance().getTime().getTime());
		 HSSFWorkbook workbook = new HSSFWorkbook();
	        HSSFSheet sheet = workbook.createSheet();
	        workbook.setSheetName(0, "Hoja excel");

	        String[] headers = new String[]{
	            "n°",
	            "id",
	            "Id usuario",
	            "Id tabla",
	            "Operación",
	            "Tabla",
	            "Fecha",
	            "Dirección ip"
	            
	        };
	        
	        Object[][] data = new Object[actions.size()][10];
	        for (int i = 0; i < data.length; i++) {
	        	data[i][0]=i+1;
	        	data[i][1]=actions.get(i).getId();
	        	data[i][2]=actions.get(i).getUserId();
	        	data[i][3]=actions.get(i).getTableId();
	        	data[i][4]=actions.get(i).getOperation();
	        	data[i][5]=actions.get(i).getTableName();
	        	data[i][6]=actions.get(i).getCreateDate();
	        	data[i][7]=actions.get(i).getIpAddress();
	        	
				
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
	            dataRow.createCell(3).setCellValue(String.valueOf(data[i][3]));
	            dataRow.createCell(4).setCellValue(String.valueOf(data[i][4]));
	            dataRow.createCell(5).setCellValue(String.valueOf(data[i][5]));
	            dataRow.createCell(6).setCellValue(String.valueOf(data[i][6]));	
	            dataRow.createCell(7).setCellValue(String.valueOf(data[i][7]));	
	           
	        }

	        HSSFRow dataRow = sheet.createRow(1 + data.length);
	        HSSFCell total = dataRow.createCell(1);
	        total.setCellType(Cell.CELL_TYPE_FORMULA);
	        total.setCellStyle(style);
	        

	        FileOutputStream file;
			try {
				String path = new File(".").getCanonicalPath();
				
				file = new FileOutputStream("/home/victorandres/Documentos/UniversidadELBosque/Semestre3/Programacion2/Proyecto/archivos/WCSC_Auditoría.xls");
				//file = new FileOutputStream(path+"\\WCSC_Auditoría.xls");
				workbook.write(file);
				file.close();
				
				File file2 = new File("/home/victorandres/Documentos/UniversidadELBosque/Semestre3/Programacion2/Proyecto/archivos/WCSC_Auditoría.xls");
				//File file2 = new File(path+"\\WCSC_Auditoría.xls");
				FacesContext facesC=FacesContext.getCurrentInstance();
				
				HttpServletResponse response= (HttpServletResponse)facesC.getExternalContext().getResponse();
				response.reset();
				response.setHeader("Content-Type", "application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=WCSC_Auditoría.xls");
				
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
				facesC.responseComplete();
				
				FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Descarga hecha", "Se ha descargado el archivo");
		        
		        PrimeFaces.current().dialog().showMessageDynamic(message);
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	 * Metodo para registrar una noticia hecha por el usuario
	 * @param idUser -id del usuario que genera la noticia
	 * @param shortDescrip - Descripcion corta de la noticia
	 * @param largedescrip - Descripcion larga de la noticia
	 * @return True registro con exito. False si no resgistro
	 */
	public boolean createNew(int idUser, String shortDescrip, String largedescrip)
	{
		boolean success=false;
		New notice = new New();
		notice=new New();
		notice.setIdUser(idUser);
		notice.setShortDescription(shortDescrip);
		notice.setLargeDescription(largedescrip);
		notice.setDateNews(new Timestamp(Calendar.getInstance().getTime().getTime()));
		notice.setState("A");
		
		NewsService newS=new NewsService();
		if(newS.save(notice))
		{
			success=true;
		}
		
		return success;
	}
	
	/**
	 * Metodo para modificar un usuario
	 */
	public void modifyUser()
	{
		UserServices us=new UserServices();
		if(us.update(selectedUser))
		{
			
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Cambio realizado para "+selectedUser.getUserName());
	        
	        PrimeFaces.current().dialog().showMessageDynamic(message);
			
			updateUsers();
			
		}
		
	}
	
	/**
	 * Metodo para agregar un estadio
	 */
	public void addStadium()
	{
		Stadium st=new StadiumServices().getStadiumByPhoto(stadium.getPhoto());
		if(st==null)
		{
			updateStadiums();
		}
		else
		{
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error", "Ya existe estadio con ese nombre");
	        
	        PrimeFaces.current().dialog().showMessageDynamic(message);
		}
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<Parameter> parameters) {
		this.parameters = parameters;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<New> getNews() {
		return news;
	}

	public void setNews(List<New> news) {
		this.news = news;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(User selectedUser) {
		this.selectedUser = selectedUser;
	}

	public Parameter getSelectedParameter() {
		return selectedParameter;
	}

	public void setSelectedParameter(Parameter selectedParameter) {
		this.selectedParameter = selectedParameter;
	}

	public List<Audit> getActions() {
		return actions;
	}

	public void setActions(List<Audit> actions) {
		this.actions = actions;
	}

	public Audit getSelectedAction() {
		return selectedAction;
	}

	public void setSelectedAction(Audit selectedAction) {
		this.selectedAction = selectedAction;
	}

	public New getSelectedNew() {
		return selectedNew;
	}

	public void setSelectedNew(New selectedNew) {
		this.selectedNew = selectedNew;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public New getNotice() {
		return notice;
	}

	public void setNotice(New notice) {
		this.notice = notice;
	}

	public String getUserEmailSearch() {
		return userEmailSearch;
	}

	public void setUserEmailSearch(String userEmailSearch) {
		this.userEmailSearch = userEmailSearch;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public List<User> getFilteredUsers() {
		return filteredUsers;
	}

	public void setFilteredUsers(List<User> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}

	public List<Audit> getFilteredActions() {
		return filteredActions;
	}

	public void setFilteredActions(List<Audit> filteredActions) {
		this.filteredActions = filteredActions;
	}

	public List<Stadium> getStadiums() {
		return stadiums;
	}

	public void setStadiums(List<Stadium> stadiums) {
		this.stadiums = stadiums;
	}



	public Stadium getSelectedStadium() {
		return selectedStadium;
	}



	public void setSelectedStadium(Stadium selectedStadium) {
		this.selectedStadium = selectedStadium;
	}

	public boolean isParametersDefault() {
		return parametersDefault;
	}

	public void setParametersDefault(boolean parametersDefault) {
		this.parametersDefault = parametersDefault;
	}

	public Stadium getStadium() {
		return stadium;
	}

	public void setStadium(Stadium stadium) {
		this.stadium = stadium;
	}
	
	
	
	
	

}
