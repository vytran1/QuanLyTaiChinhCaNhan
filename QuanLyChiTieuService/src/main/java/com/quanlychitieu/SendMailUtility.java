package com.quanlychitieu;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;



public class SendMailUtility {
    
	public static final String MAILFROM = "vy.tn171003@gmail.com";
	public static final String MAILHOST = "smtp.gmail.com";
	public static final String MAILPASSWORD = "bsyx joyp muhz lngp";
	public static final String MAILPORT = "587";
	public static final String MAILSENDERNAME = "QuanLyTaiChinh_Corporation";
	public static final String MAILUSERNAME = "vy.tn171003@gmail.com";
	public static final boolean SMTP_AUTH  = true;
	public static final boolean SMTP_SECURED  = true;
    
	
	public static JavaMailSenderImpl preJavaMailSenderImpl() {
		   JavaMailSenderImpl mailSender =  new JavaMailSenderImpl();
		   
		   mailSender.setHost(MAILHOST);
		   mailSender.setPort(Integer.valueOf(MAILPORT));
		   mailSender.setUsername(MAILUSERNAME);
		   mailSender.setPassword(MAILPASSWORD);
		   
		   Properties mailProperties = new Properties();
		   mailProperties.setProperty("mail.smtp.auth",String.valueOf(SMTP_AUTH));
		   mailProperties.setProperty("mail.smtp.starttls.enable",String.valueOf(SMTP_SECURED));
		   
		   mailSender.setJavaMailProperties(mailProperties);
		   return mailSender;
	}
			
	
}
