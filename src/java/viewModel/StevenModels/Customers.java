package viewModel.StevenModels;

/**
 *
 * @author Steven
 */
public class Customers {

    String code;
    String name;
    String updated_by;
    String update_date;

    public Customers(String code, String name, String updated_by, String update_date) {
        this.code = code;
        this.name = name;
        this.updated_by = updated_by;
        this.update_date = update_date;
    }

    public Customers() {
    }
    

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUpdated_by() {
        return updated_by;
    }

    public void setUpdated_by(String updated_by) {
        this.updated_by = updated_by;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }
    
    
    
}
