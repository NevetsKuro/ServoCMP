/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package viewModel;

/**
 *
 * @author wrtrg2
 */
public class HighchartsSeries {

    String name;
    String y;
    String custName;

    public HighchartsSeries(){
    
    }

    public HighchartsSeries(String name, String y, String custName) {
        this.name = name;
        this.y = y;
        this.custName = custName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }
}
