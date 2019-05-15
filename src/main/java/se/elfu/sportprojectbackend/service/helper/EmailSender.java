package se.elfu.sportprojectbackend.service.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import se.elfu.sportprojectbackend.repository.model.PasswordResetToken;
import se.elfu.sportprojectbackend.repository.model.Request;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
@Slf4j
public class EmailSender {

    private final static String RESET_LINK ="http://localhost:4200/password/reset/";
    private final static String RESET_SUBJECT ="Sport Meet - Återställ lösenord";
    private final static String LOGIN_LINK ="http://localhost:4200/logga-in";
    private final static String REQUEST_SUBJECT ="Sport Meet - Någon vill vara med i ditt evenemang!";

    @Value("${email.default.adress}")
    private String deafultEmail;

    @Autowired
    private JavaMailSender emailSender;

    public void sendResetPasswordMail(PasswordResetToken passwordResetToken) {

        MimeMessage mimeMessage = emailSender.createMimeMessage();
        String resetLink = RESET_LINK + passwordResetToken.getToken();
        String htmlMsg = "<h3>Hej!</h3> <br>" +
                "<div>Du kan återställa ditt lösenord på Sport meet genom att följa denna " +
                "<a href=" + resetLink + ">länk</a>" +
                "</div> <br>" +
                "<div> Länken är giltig i 24 timmar. </div>";

        createEmail(mimeMessage, RESET_SUBJECT, htmlMsg, passwordResetToken.getAccount().getEmail());
    }

    public void sendNewRequestNotification(Request request) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        String loginLink = LOGIN_LINK;
        String htmlMsg = "<h3>Hej! " +request.getReceiver().getUsername() +"</h3> <br>" +
                "<div>" + request.getSender().getUsername() + "vill vara med i ditt evenemang " + request.getEvent().getName() + "<br>" +
                "<a href=" + loginLink + ">Logga in</a>" + " och kollar vad hen har skrivit: " +
                "</div>";

        createEmail(mimeMessage, REQUEST_SUBJECT, htmlMsg, request.getReceiver().getAccount().getEmail());
    }

    private void createEmail(MimeMessage mimeMessage, String subject, String htmlMsg, String email) {
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessage.setContent(htmlMsg, "text/html");
            helper.setTo(deafultEmail); //email
            helper.setSubject(subject);
            helper.setFrom("no-reply@sportmeet.se");
        } catch (MessagingException ex){
            log.error("Could not create mail", ex);
            throw new RuntimeException("Could not send email");
        }
        sendEmail(mimeMessage);
    }

    private void sendEmail(MimeMessage msg) {
        try {
            this.emailSender.send(msg);
        } catch (MailException ex) {
            log.error("Could not send email", ex);
            throw new RuntimeException("Could not send emaill");
        }
    }
}
