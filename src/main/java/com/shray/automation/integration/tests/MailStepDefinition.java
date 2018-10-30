package com.shray.automation.integration.tests;

import java.util.HashMap;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.shray.automation.intergration.utils.Utilities;

public class MailStepDefinition {
	
	HashMap<String, String> categorySubjectMapping = new HashMap<String, String>();
	
	Utilities util = new Utilities("gsuite");
	Properties sessionProperties = util.getProperties("gsuite");
	Properties integrationProperties = util.getProperties("integration");
	Properties alertProperties = util.getProperties("alerts");
	String fromAddress = integrationProperties.getProperty(util.mode+".sender.email.id");
	String fromAddressPassword = integrationProperties.getProperty(util.mode+".sender.password");
	String fromName = integrationProperties.getProperty(util.mode+".sender.name");
	String toAddress = integrationProperties.getProperty(util.mode+".recepient.email.id");
	String toAddressPassword = integrationProperties.getProperty(util.mode+".recepient.email.id.password");
	
	
	Session sendEmailSession = util.getSession(sessionProperties, new Authenticator() {
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(fromAddress, fromAddressPassword);
		};
	});
	
	
	
	@DataProvider(parallel=true)
	public Object[][] getAlertsData(){
		
		String[] alerts = alertProperties.getProperty("alert.catergories").split(",");
		Object[][] data = new Object[alerts.length][2];
		int i = 0;
		for(String alert : alerts) {
			data[i][0] = alert.trim();
			data[i][1] = alertProperties.getProperty("test."+data[i][0]);
			i++;
		}
		return data;
	}
	
	
	
	@Test(dataProvider="getAlertsData",priority=1)
	public void send_mail (String category, String execute) {
		
		if (execute.equals("true")) {
			
			try {				
				MimeMessage message = new MimeMessage(sendEmailSession);
				message.setFrom(new InternetAddress(fromAddress, fromName));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toAddress));
				
				//message.setSubject(alertProperties.getProperty(category+".subject"));
				String randomSubject = "auto_"+RandomStringUtils.randomAlphanumeric(8);
				message.setSubject(randomSubject);
				
				message.setText(alertProperties.getProperty(category+".content"));
				
				System.out.println("The subject of: "+category+" is:"+randomSubject);
				
				Transport.send(message);
				System.out.println("Mail sent with Thread ID"+Thread.currentThread().getId());
				
				categorySubjectMapping.put(category, randomSubject);
				}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	
	
	@Test(dataProvider="getAlertsData", priority=2)
	public void verify_mail(String category, String execute) {
		
		if(execute.equals("true")) {
			
			Session receiveEmailSession = util.getSession(sessionProperties, new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(toAddress, toAddressPassword);
				};
			});
			
			try {
				Store store = receiveEmailSession.getStore("imap");
				store.connect(sessionProperties.getProperty("mail.imap.host"), toAddress, toAddressPassword);
				
				Folder folderInbox = store.getFolder("INBOX");
	            folderInbox.open(Folder.READ_ONLY);
			
	            HashMap<String, String> mailHeaders = util.getAllHeaders(categorySubjectMapping.get(category), folderInbox);
	            String message_id = mailHeaders.get("Message-ID");
	            System.out.println(categorySubjectMapping.get(category)+" : "+message_id);
	           
	            folderInbox.close(false);
	            store.close();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		
		
		
		
	}
	
}

	

