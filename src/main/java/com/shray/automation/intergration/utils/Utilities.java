package com.shray.automation.intergration.utils;

import java.io.FileInputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.search.SearchTerm;

public class Utilities {
	
	public String mode;
	
	public Utilities (String mode) {
		this.mode = mode;
	}
			
	public Properties getProperties (String load) {
		Properties prop = new Properties();
			try {
				prop.load(new FileInputStream(System.getProperty("user.dir")+"//config//"+load+".properties"));
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			return prop;
	}
	
	
			
	public Session getSession (Properties sProp, Authenticator sAuth) {
		Session session = Session.getDefaultInstance(sProp, sAuth);
		return session;
	}
	
	
	public HashMap<String, String> getAllHeaders (String emailSubject, Folder folder){
		
		final String subject = emailSubject;
		HashMap<String, String> messageHeaders = new HashMap<String, String>();
        SearchTerm searchCondition = new SearchTerm() {
			private static final long serialVersionUID = 1L;

			@Override
            public boolean match(Message message) {
                try {
                    if (message.getSubject().contains(subject)) {
                        return true;
                    }
                } catch (MessagingException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
        };
        
        try {
			Message[] foundMessages = folder.search(searchCondition);
			if(foundMessages.length==0) {
				System.out.println("No messages were found for the provided subject");
			}
			if(foundMessages.length>1) {
				System.out.println("More than one messages found with");
			}
			
			
			Enumeration allHeaders = foundMessages[0].getAllHeaders();
	        while (allHeaders.hasMoreElements()) {
	            Header header = (Header) allHeaders.nextElement();
	            String headerName = header.getName();
	            String headerVal = header.getValue();

	            messageHeaders.put(headerName, headerVal);
	            
	        }
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        return messageHeaders;
	}

}
