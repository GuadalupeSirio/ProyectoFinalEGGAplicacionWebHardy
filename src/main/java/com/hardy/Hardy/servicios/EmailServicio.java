package com.hardy.Hardy.servicios;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServicio {

    @Autowired
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${my.property}")
    private String directory;
    
    private static final String SUBJECT = "Bienvenido a Hardy!";

    public void enviarThread(String to) {
        new Thread(() -> {
            try {
                MimeMessage message = sender.createMimeMessage();

                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setTo(to);
                helper.setSubject(SUBJECT);
                //helper.setText("<html><body> <h1>Bienvenido a Hardy!</h1> <h3>El registro en Hardy fue exitoso. Esperamos que disfrutes mucho nuestra aplicación!</h3> <br> <img src='cid:identifier1234'> <br> <h4>Equipo de Hardy®</h4> </body></html>", true);
                helper.setText("<html><body><img src='cid:identifier1234'></body></html>", true);
                FileSystemResource res = new FileSystemResource(new File(directory + "\\card.png"));
                helper.addInline("identifier1234", res);
                sender.send(message);
            } catch (MessagingException ex) {
                Logger.getLogger(EmailServicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
}
