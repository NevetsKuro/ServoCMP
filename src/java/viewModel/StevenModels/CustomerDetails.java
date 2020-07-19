package viewModel.StevenModels;

/**
 *
 * @author Steven
 */
public class CustomerDetails {

    String empcode;
    String empname;
    String empemail;
    String empCrcode;
    String empCrname;
    String empCremail;

    public CustomerDetails(String empcode, String empname, String empemail, String empCrcode, String empCrname, String empCremail) {
        this.empcode = empcode;
        this.empname = empname;
        this.empemail = empemail;
        this.empCrcode = empCrcode;
        this.empCrname = empCrname;
        this.empCremail = empCremail;
    }
    
    public CustomerDetails(){
    
    }

    public String getEmpcode() {
        return empcode;
    }

    public void setEmpcode(String empcode) {
        this.empcode = empcode;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getEmpemail() {
        return empemail;
    }

    public void setEmpemail(String empemail) {
        this.empemail = empemail;
    }

    public String getEmpCrcode() {
        return empCrcode;
    }

    public void setEmpCrcode(String empCrcode) {
        this.empCrcode = empCrcode;
    }

    public String getEmpCrname() {
        return empCrname;
    }

    public void setEmpCrname(String empCrname) {
        this.empCrname = empCrname;
    }

    public String getEmpCremail() {
        return empCremail;
    }

    public void setEmpCremail(String empCremail) {
        this.empCremail = empCremail;
    }
    
}
