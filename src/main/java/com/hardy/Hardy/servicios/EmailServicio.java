package com.hardy.Hardy.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServicio {
    
    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    private static final String SUBJECT = "Bienvenido a Hardy!";
    private static final String TEXT = "El registro en Hardy fue exitoso. Bienvenido!";

    public void enviarThread(String to) {
        new Thread(() -> {
            SimpleMailMessage message = new SimpleMailMessage(); 
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(SUBJECT);
            message.setText(TEXT);
            sender.send(message);
        }).start();
    }
}
