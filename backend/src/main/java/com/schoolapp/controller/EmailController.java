package com.schoolapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.schoolapp.entity.EmailForm;

@RestController
@RequestMapping("/email")

public class EmailController {

	@Autowired
	private JavaMailSender emailSender;

	@PostMapping("/send-email")
	public String sendEmail(@RequestBody EmailForm emailForm) {
		if (emailForm == null || emailForm.getTo_email() == null || emailForm.getSubject() == null
				|| emailForm.getBody() == null) {
			return "Invalid email data. Please provide 'to', 'subject', and 'body' fields.";
		}

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(emailForm.getTo_email());
			message.setSubject(emailForm.getSubject());
			message.setText(emailForm.getBody());

			emailSender.send(message);
			System.out.println("sent successfully");
			return "Email sent successfully!";

		} catch (MailException e) {
			e.printStackTrace();
			return "Failed to send email. Please try again later.";
		}
	}
}
