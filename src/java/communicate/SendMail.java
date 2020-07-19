package communicate;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import globals.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import viewModel.MessageDetails;
import viewModel.MstUser;

public class SendMail {

    public static void sentMailTseToLab(String strEmpCode, String strSampleId, String strSendUserName) {
        MailTemplate mt = new MailTemplate();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT USER_NAME, EMP_EMAIL FROM MST_USER WHERE EMP_CODE= ? ");) {
            pst.setString(1, strEmpCode);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                mt.setTseName(res.getString(1));
                mt.setTseEmail(res.getString(2));
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "sendMail.sentMailTseToLab() ");
        }
    }

    public static void sentMailLabToCustomer(String sampleId, String custId, String sampleInfoId, String labLocCode, String userId) {
        MailTemplate mt = getMailPeople(custId, sampleInfoId);
        mt.setSubject("Status of the Sample Id : " + sampleId);
        mt.setMailContent("Dear " + mt.getHodName() + ", <br/> Sample Id: '" + sampleId + "' has been Tested and sent to TSE (" + mt.getTseName() + "), you will receive final test report shortly. This is for your Information <br/> <br/> *** This is a system generated email. Please do not reply to this Email. ***");
        mt.setTo(mt.getHodEmail());
        mt.setCc(mt.getTseEmail() + ", " + mt.getEmpAuthorityEmail());
        if (!"".equals(mt.getTo()) || mt.getTo().equals(null)) {
            try {
                if (sendMailText(mt)) {
                    mt.setSubject(mt.getSubject() + " :: LAB has entered and submitted the test result");
                    insertSentMailSummary(sampleId, mt, userId, "LAB");
                }
            } catch (Exception ex) {
                MyLogger.logIt(ex, "SendMail.sentMailLabToCustomer() ");
            }
        }
    }

    public static MailTemplate getMailPeople(String custId, String sampleInfoId) {
        MailTemplate mt = new MailTemplate();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement stAuthority = con.prepareStatement("SELECT T2.EMP_NAME, T2.EMP_EMAIL, T2.CNTRLG_EMP_NAME, T2.CNTRLG_EMP_EMAIL FROM MST_CUSTOMER T1 INNER JOIN MST_USER T2 ON  T1.EMP_CODE=T2.EMP_CODE WHERE T1.CUST_ID= ? ");
                PreparedStatement stHod = con.prepareStatement("SELECT HOD_NAME, HOD_EMAIL FROM MST_DEPARTMENT WHERE DEPT_ID IN (SELECT DEPT_ID FROM MST_TANK WHERE TANK_ID = ? )");) {
                stAuthority.setString(1, custId);
                stHod.setString(1, sampleInfoId);
                ResultSet resAuthority = stAuthority.executeQuery();
                ResultSet resHod = stHod.executeQuery();
            if (resAuthority.next() && resHod.next()) {
                mt.setTseName(resAuthority.getString(1));
                mt.setTseEmail(resAuthority.getString(2));
                mt.setEmpAuthorityName(resAuthority.getString(3));
                mt.setEmpAuthorityEmail(resAuthority.getString(4));
                mt.setHodName(resHod.getString(1));
                mt.setHodEmail(resHod.getString(2));
                mt.setCanMail(true);
            } else {
                mt.setCanMail(false);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SendMail.getMailPeople() ");
        }
        return mt;
    }

    public static MessageDetails sentMailTSEToCustomer(String sampleId, String custId, String sampleInfoId, String tempPath, boolean labDocSelectedFlag, String userid, MessageDetails md, HashMap<String, String> hm) throws SQLException {
        MailTemplate mt = getMailPeople(custId, sampleInfoId);
        if (mt.isCanMail()) {
            try {
                mt.setTo("");
                mt.setCc("");
                mt.setBcc("");
                mt.setTseReportPath("");
                mt.setLabReportPath("");
                if (hm.get("To").equals("")) {
                    mt.setTo(mt.getHodEmail());
                } else {
                    mt.setTo(hm.get("To"));
                }
                if (hm.get("CC").equals("")) {
                    mt.setCc(mt.getTseEmail() + "," + mt.getEmpAuthorityEmail());
                } else {
                    mt.setCc(hm.get("CC"));
                }
                if (hm.get("subject").equals("")) {
                    mt.setSubject("Status of Sample Id: " + sampleId);
                } else {
                    mt.setSubject(hm.get("subject"));
                }
                mt.setMailContent("Dear " + mt.getHodName() + ", <br/>" + hm.get("body") + "<br/> Please find Test Report Attached. <br/> ***This is a System Generated Email. Please do not Reply to this Email.***");
//                System.err.println("Sending Mail to.... \n To: "+mt.getTo()+"; \n Cc: "+ mt.getCc()+ "; \n Subject: "+ mt.getSubject() + "; \n Body "+ mt.getMailContent());
                mt.setTseReportPath(getTestReportPath(sampleId, tempPath, "TSE"));
                if (labDocSelectedFlag) {
                    mt.setLabReportPath(getTestReportPath(sampleId, tempPath, "LAB"));
                }
                if (!"".equals(mt.getTseReportPath()) || !mt.getTseReportPath().equals(null)) {
                    if (sendMailWithAttachment(mt)) {
                        System.out.println("Mail Sent Successfully to \n To: "+mt.getTo()+" \n Cc: "+ mt.getCc()+ " \n Subject: "+ mt.getSubject() + " \n Body "+ mt.getMailContent());
                        md.setMailStatus(true);
                        md.setMailMsg("Mail Sent Successfully");
                        md.setMailMsgClass("text-success");
                        if (labDocSelectedFlag) {
                            mt.setSubject(mt.getSubject() + " :: Both reports have been sent as attachments");
                        } else {
                            mt.setSubject(mt.getSubject() + " :: Only one report has been sent as attachment");
                        }
                        insertSentMailSummary(sampleId, mt, userid, "TSE");
                    } else {
                        md.setMailStatus(false);
                        md.setMailMsg("Error occured while Sending Mail.");
                        md.setMailMsgClass("text-danger");
                    }
                }
            } catch (Exception ex) {
                md.setMailStatus(false);
                md.setMailMsg("Error occured while Sending Mail.");
                md.setMailMsgClass("text-danger");
                MyLogger.logIt(ex, "SendMail.sentMailTSEToCustomer() ");
            } finally {
                if (!mt.getTseReportPath().equals("") || !mt.getTseReportPath().equals(null) || !mt.getLabReportPath().equals("") || !mt.getLabReportPath().equals(null)) {
                    File fileTse = new File(mt.getTseReportPath());
                    fileTse.delete();
                    File fileLab = new File(mt.getLabReportPath());
                    fileLab.delete();
                }
            }
        } else {
            md.setMailStatus(false);
            md.setMailMsg("No Mail was Sent. Reason: No recepients were found to send Mail.");
            md.setMailMsgClass("text-danger");
        }
        return md;
    }
    
    public static MessageDetails sendEditedTSE(HashMap<String,String> TSEinfo,String CurrentRole, String PreviousRole, String TseOfficer){
        MailTemplate mt = new MailTemplate();
        MessageDetails md = new MessageDetails();
        if(!CurrentRole.equals("")){
            try {
                mt = new MailTemplate();
                mt.setTo(TSEinfo.get("To"));
                mt.setCc(TSEinfo.get("Cc"));
                mt.setBcc(TSEinfo.get("Bcc"));
                mt.setSubject("Role Change");
                mt.setMailContent("<br/> Your "+PreviousRole+" has been changed to "+CurrentRole+" <br/> by TSE Officer"+ TseOfficer); //body
                if(sendMailText(mt)){
                    md.setModalTitle("Send Mail");
                    md.setModalMessage("Successful sent mail to Edited Tse Officer");
                }else{
                    md.setModalTitle("Send Mail");
                    md.setModalMessage("Failed to sent mail to Edited Tse Officer");
                }
            }catch(Exception ex){
                MyLogger.logIt(ex, "sendEditedTSE()");
            }
        }
        return md;
    }

    public static MessageDetails sendMailForRejectedSample(String sampleId, MstUser user1, User user2, String reason){
        MailTemplate mt = new MailTemplate();
        MessageDetails md = new MessageDetails();
        if(!sampleId.equals("")){
            try {
                mt = new MailTemplate();
                mt.setTo(user1.getEmpEmail());
                mt.setCc(user2.getsEMAIL_ID());
                mt.setBcc("ISHELPDESKWRO@INDIANOIL.IN");
                mt.setSubject("Sample Rejected");
                mt.setMailContent("<br/> Sample Id :-"+sampleId+" has been rejected due "+reason+" <br/> by Lab Incharge "+ user2.getsEMP_NAME()+ " of Lab Code "+ user2.getsLOC_CODE()); 
                if(sendMailText(mt)){
                    md.setModalTitle("Send Mail");
                    md.setModalMessage("Successful sent mail to Tse Officer \n");
                }else{
                    md.setModalTitle("Send Mail");
                    md.setModalMessage("Failed to sent mail to Tse Officer \n");
                }
            }catch(Exception ex){
                MyLogger.logIt(ex, "sendMailForRejectedSample()");
            }
        }
        return md;
    } 
    
    public static String getTestReportPath(String sampleId, String tempFolderPath, String TseorLab) {
        String Query = null;
        switch (TseorLab) {
            case "TSE":
                Query = "SELECT TEST_REPORT FROM CMP_TEST_REPORT_INFO where SAMPLE_ID= ? ";
                break;
            case "LAB":
                Query = "SELECT LAB_RESULT_DOC FROM LAB_TEST_RESULT_DOC_INFO where SAMPLE_ID= ? ";
                break;
        }
        InputStream is = null;
        File file = new File(tempFolderPath + File.separator + sampleId + "_Rpt" + TseorLab + ".pdf");
        FileOutputStream fos = null;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(Query);) {
            pst.setString(1, sampleId);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                is = res.getBinaryStream(1);
                fos = new FileOutputStream(file);
                byte[] buffer = new byte[1];
                while (is.read(buffer) > 0) {
                    fos.write(buffer);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SendMail.getTestReportPath() ");
        } finally {
            try {
                fos.close();
                is.close();
            } catch (IOException ex) {
                Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return tempFolderPath + File.separator + sampleId + "_Rpt" + TseorLab + ".pdf";
    }

    public static boolean sendMailWithAttachment(MailTemplate mt) {
        try {
            Properties properties = new Properties();
//            System.out.println("Sending mail to "+mt.getTo());
//            System.out.println("in CC "+ mt.getCc()+"---"+ mt.getBcc());
//            System.out.println("with subject" + mt.getSubject());
//            System.out.println("with body" + mt.getMailContent());
            properties.put("mail.smtp.host", "mkhoexht.ds.indianoil.in");  // Exchange
            properties.put("mail.smtp.from", mt.getFrom());
            Session session1 = Session.getInstance(properties, null);
            MimeMessage mimemessage = new MimeMessage(session1);
            mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(mt.getTo()));
            mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(mt.getCc()));
            mimemessage.setRecipients(javax.mail.Message.RecipientType.BCC, InternetAddress.parse(mt.getBcc()));
            mimemessage.setSentDate(new Date());
            mimemessage.setSubject(mt.getSubject());
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(mt.getMailContent(), "text/html");
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            if (!"".equals(mt.getTseReportPath())) {
                MimeBodyPart attachmentTse = new MimeBodyPart();
                attachmentTse.attachFile(mt.getTseReportPath());
                multipart.addBodyPart(attachmentTse);
            }
            if (!"".equals(mt.getLabReportPath())) {
                MimeBodyPart attachmentLab = new MimeBodyPart();
                attachmentLab.attachFile(mt.getLabReportPath());
                multipart.addBodyPart(attachmentLab);
            }
            mimemessage.setContent(multipart);
            Transport.send(mimemessage);
            return true;
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SendMail.sendMailWithAttachment() ");
            return false;
        }
    }

    public static boolean sendMailText(MailTemplate mt) {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "mkhoexht.ds.indianoil.in");  // Exchange
            properties.put("mail.smtp.from", mt.getFrom());
            Session session1 = Session.getInstance(properties, null);
            MimeMessage mimemessage = new MimeMessage(session1);
            mimemessage.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(mt.getTo()));
            mimemessage.setRecipients(javax.mail.Message.RecipientType.CC, InternetAddress.parse(mt.getCc()));
            mimemessage.setRecipients(javax.mail.Message.RecipientType.BCC, InternetAddress.parse(mt.getBcc()));
            mimemessage.setSentDate(new Date());
            mimemessage.setSentDate(new Date());
            mimemessage.setSubject(mt.getSubject());
            mimemessage.setContent(mt.getMailContent(), "text/html");
            Transport.send(mimemessage);
            properties.clear();
            return true;
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SendMail.sendMailText() ");
            return false;
        }
    }

    public static boolean insertSentMailSummary(String smplId, MailTemplate mt, String userId, String userType) {
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pstmt = con.prepareStatement("INSERT INTO SENT_MAIL_DETAILS(SAMPLE_ID,SENT_FROM,SENT_TO,SENT_CC,SENT_BY,USER_TYPE,MAIL_SUBJECT,SENT_DATETIME) VALUES(?,?,?,?,?,?,?,?)");) {
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            pstmt.setString(1, smplId);
            pstmt.setString(2, mt.getFrom());
            pstmt.setString(3, mt.getTo());
            pstmt.setString(4, mt.getCc());
            pstmt.setString(5, userId);
            pstmt.setString(6, userType);
            pstmt.setString(7, mt.getSubject());
            pstmt.setString(8, format.format(new Date()));
            pstmt.execute();
            return true;
        } catch (Exception ex) {
            MyLogger.logIt(ex, "SendMail.insertSentMailSummary() ");
            return false;
        }
    }
}
