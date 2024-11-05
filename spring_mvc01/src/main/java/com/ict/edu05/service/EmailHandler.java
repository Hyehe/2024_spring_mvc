package com.ict.edu05.service;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

// Service에서 호출해서 사용할 클래스

public class EmailHandler {
	private JavaMailSender jacaMailSender;
	private MimeMessage message;
	private MimeMessageHelper messageHelper;
	
	public EmailHandler(JavaMailSender javaMailSender) throws Exception{
		this.jacaMailSender = javaMailSender;
		
		message = this.jacaMailSender.createMimeMessage();
		// true => 멀티파트 메세지를 사용 가능
		messageHelper = new MimeMessageHelper(message, true, "UTF-8");
		
		// 단순한 텍스트 메시지만 사용
		// messageHelper = new MimeMessageHelper(message, "UTF-8");
		
	}

	// 제목
	public void setSubject(String subject) throws Exception{
		messageHelper.setSubject(subject);
	}
	
	// 내용
	public void setText(String text) throws Exception{
		// true => 태그 사용가능
		messageHelper.setText(text, true);
	}
	// 보내는 사람의 이메일과 제목
	public void setForm(String email, String name) throws Exception{
		messageHelper.setFrom(email, name);
	}
	// 받는 이메일
	public void setTo(String email) throws Exception{
		messageHelper.setTo(email);
	}
	// 보내기
	public void send() {
        jacaMailSender.send(message);
    }
	
}
