package com.unbosque.wcsc.util;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import com.unbosque.wcsc.entities.UserSheets;

public class Utility{
	
	// algoritmos
	public static String MD5 = "MD5";

	/***
	 * Convierte un arreglo de bytes a String usando valores hexadecimales
	 * 
	 * @param digest
	 *            arreglo de bytes a convertir
	 * @return String creado a partir de <code>digest</code>
	 */
	private static String toHexadecimal(byte[] digest) {
		String hash = "";
		for (byte aux : digest) {
			int b = aux & 0xff;
			if (Integer.toHexString(b).length() == 1)
				hash += "0";
			hash += Integer.toHexString(b);
		}
		return hash;
	}

	/***
	 * Encripta un mensaje de texto mediante algoritmo de resumen de mensaje.
	 * 
	 * @param message
	 *            texto a encriptar
	 * @param algorithm
	 *            algoritmo de encriptacion, puede ser: MD2, MD5, SHA-1,
	 *            SHA-256, SHA-384, SHA-512
	 * @return mensaje encriptado
	 */
	public static String getStringMessageDigest(String message, String algorithm) {
		byte[] digest = null;
		byte[] buffer = message.getBytes();
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			messageDigest.reset();
			messageDigest.update(buffer);
			digest = messageDigest.digest();
		} catch (NoSuchAlgorithmException ex) {
			System.out.println("Error creando Digest");
		}
		return toHexadecimal(digest);
	}

	public static long ahora() {
		return Calendar.getInstance().getTimeInMillis();
	}
	
	public static String randomStr(String str)
	{
		String s="";
		char[] array=str.toCharArray();
		int num1=(int)Math.floor(Math.random()*2000+1);
		int num2=(int)Math.floor(Math.random()*200+1);
		s=String.valueOf(num1)+String.valueOf(array[0]).toUpperCase()+String.valueOf(num2);
		
		return s;
	}
	
	public String getRandomString()
	{
		return UUID.randomUUID().toString().substring(0, 8);
	}
	
	public static boolean isNumeric(String cadena) {

        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }
	
	public static boolean isLongNumeric(String cadena) {

        boolean resultado;

        try {
            Long.parseLong(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }
	
	public static long getDiferenceBtwnTimeStamp(java.sql.Timestamp t1,java.sql.Timestamp t2)
	{
		return (t1.getTime()-t2.getTime())/86400000;
	}
	
	public static String getClientIp()
	{
		String ipAddress="";
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
		    ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}
	
	public static double getDifferenceInSeconds(java.sql.Timestamp time1, java.sql.Timestamp time2)
	{
		return ((time1.getTime()) - (time2.getTime()))/1000;
	}
	
}
