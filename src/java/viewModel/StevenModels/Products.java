/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package viewModel.StevenModels;

/**
 *
 * @author wrtrg2
 */
public class Products {

    String code;
    String name;
    String prodcat;

    public Products(){
    
    }
    
    public String getProdcat() {
        return prodcat;
    }

    public void setProdcat(String prodcat) {
        this.prodcat = prodcat;
    }

    public Products(String code, String name) {
        this.code = code;
        this.name = name;
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
    
    
}
