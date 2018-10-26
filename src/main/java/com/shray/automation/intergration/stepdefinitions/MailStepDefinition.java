package com.shray.automation.intergration.stepdefinitions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.shray.automation.intergration.utils.Utilities;

public class MailStepDefinition {
		
	public MailStepDefinition() {
		this.properties = new Properties();
		try {
			properties.load(new FileInputStream(System.getProperty("user.dir")+"//config//integration.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	Properties properties = null;
	Utilities util = new Utilities("gsuite");
	Properties sessionProperties = util.getSessionProperties();
	String fromAddress = properties.getProperty(util.mode+".sender.email.id");
	String fromAddressPassword = properties.getProperty(util.mode+".sender.password");
	String fromName = properties.getProperty(util.mode+".sender.name");
	String toAddress = properties.getProperty(util.mode+".recepient.email.id");
	
	
	Session currentSession = util.getSession(sessionProperties, new Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(fromAddress, senderPassword);
		};
	});
	
	
	
	//@Given
	public void i_sent_a_mail () {
		// TODO Auto-generated method stub
		
		try {
			
			MimeMessage message = new MimeMessage(mail.currentSession);
			message.setFrom(new InternetAddress(mail.fromAddress, mail.fromName));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.toAddress));
			message.setSubject("Test Subject");
			message.setText("This is the body");
			
			Transport.send(message);
			System.out.println("Mail sent");
			}
		catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	}

	

