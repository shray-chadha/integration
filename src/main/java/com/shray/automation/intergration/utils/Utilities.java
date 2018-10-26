package com.shray.automation.intergration.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

public class Utilities {
	
	public String mode;
	public Properties properties;
	
	public Utilities (String mode) {
		this.mode = mode;
		this.properties = new Properties();
		try {
			properties.load(new FileInputStream(System.getProperty("user.dir")+"//config//integration.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public Properties getSessionProperties () {
		Properties sProperties = null;
		if(mode.equals("gsuite")) {
			sProperties = new Properties();
			try {
				sProperties.load(new FileInputStream(System.getProperty("user.dir")+"//config//gsuite.properties"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return sProperties;
		}
		return sProperties;
	}
	
	
	public Authenticator getAuthenticator () {
		Authenticator sessionAuth = null;
		if(mode.equals("gsuite")) {
			sessionAuth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(properties.getProperty("gsuite.sender.email.id"), properties.getProperty("gsuite.sender.password"));
				}
			};
			return sessionAuth;
		}
		return sessionAuth;
	}
	
	
	public Session getSession (Properties sProp, Authenticator sAuth) {
		Session session = Session.getDefaultInstance(sProp, sAuth);
		return session;
	}
	

}
