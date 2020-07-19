package communicate;

import Exceptions.MyLogger;
import java.util.Date;
import java.util.Properties;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ServoMail {

    public static boolean MailForCust(String from, String to, String cc, String bcc, String subject, String mail_text) {
        boolean retVal = false;
        try {
            from = "no-replyServocmp@indianoil.in";
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "mkhoexht.ds.indianoil.in");  // Exchange
            properties.put("mail.smtp.from", from);
            Session session = Session.getInstance(properties, null);
            MimeMessage mimemessage = new MimeMessage(session);
            mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(to));
            mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(cc));
            mimemessage.setRecipients(javax.mail.Message.RecipientType.BCC, InternetAddress.parse(bcc));
            mimemessage.setSentDate(new Date());
            mimemessage.setSentDate(new Date());
            mimemessage.setSubject(subject);
            mimemessage.setContent(mail_text, "text/html");
            Transport.send(mimemessage); // blocked for stopping mail
            retVal = true;
        } catch (Exception ex) {
            MyLogger.logIt(ex, "ServoMail.MailForCust() ");
            retVal = false;
        }
        return retVal;
    }
}
