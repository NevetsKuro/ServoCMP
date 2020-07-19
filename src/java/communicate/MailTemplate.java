package communicate;

public class MailTemplate {

    private String to, bcc, cc, subject, tseReportPath, labReportPath, tseName, tseEmail, hodName, hodEmail, empAuthorityName, empAuthorityEmail;
    private String from = "no-replyServocmp@indianoil.in";
    private String mailContent;
    private String[] attachments;
    private boolean canMail;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTseReportPath() {
        return tseReportPath;
    }

    public void setTseReportPath(String tseReportPath) {
        this.tseReportPath = tseReportPath;
    }

    public String getLabReportPath() {
        return labReportPath;
    }

    public void setLabReportPath(String labReportPath) {
        this.labReportPath = labReportPath;
    }

    public String getTseName() {
        return tseName;
    }

    public void setTseName(String tseName) {
        this.tseName = tseName;
    }

    public String getTseEmail() {
        return tseEmail;
    }

    public void setTseEmail(String tseEmail) {
        this.tseEmail = tseEmail;
    }

    public String getHodName() {
        return hodName;
    }

    public void setHodName(String hodName) {
        this.hodName = hodName;
    }

    public String getHodEmail() {
        return hodEmail;
    }

    public void setHodEmail(String hodEmail) {
        this.hodEmail = hodEmail;
    }

    public String getEmpAuthorityName() {
        return empAuthorityName;
    }

    public void setEmpAuthorityName(String empAuthorityName) {
        this.empAuthorityName = empAuthorityName;
    }

    public String getEmpAuthorityEmail() {
        return empAuthorityEmail;
    }

    public void setEmpAuthorityEmail(String empAuthorityEmail) {
        this.empAuthorityEmail = empAuthorityEmail;
    }

    public String getMailContent() {
        return mailContent;
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public String[] getAttachments() {
        return attachments;
    }

    public void setAttachments(String[] attachments) {
        this.attachments = attachments;
    }

    public boolean isCanMail() {
        return canMail;
    }

    public void setCanMail(boolean canMail) {
        this.canMail = canMail;
    }

}
