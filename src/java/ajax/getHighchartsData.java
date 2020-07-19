/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ajax;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import com.google.gson.Gson;
import globals.User;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.HighchartsSeries;

public class getHighchartsData extends HttpServlet {
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        
        HttpSession session = request.getSession();
        User emp = (User)session.getAttribute("sUser");
        String emp_code = emp.getsEMP_CODE();
        ArrayList<HighchartsSeries> seriesList2 = new ArrayList<HighchartsSeries>(); // for highcharts Series property
        Gson gson = new Gson();
        
        seriesList2 = getCustIndWise2(emp_code);
        gson.toJson(seriesList2, response.getWriter());
    }

    public static ArrayList<HighchartsSeries> getCustIndWise2(String empCode) {
        ArrayList<HighchartsSeries> seriesList = new ArrayList<HighchartsSeries>(); // for highcharts Series property
        
        
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT T3.IND_NAME, T2.CUST_NAME, COUNT(*) from MST_TANK T1 "
                        + "INNER JOIN MST_CUSTOMER T2 ON T1.CUST_ID = T2.CUST_ID INNER JOIN MST_INDUSTRY T3 ON T1.IND_ID = "
                        + "T3.IND_ID WHERE T2.EMP_CODE = ? GROUP BY ROLLUP(T3.IND_NAME,T2.CUST_NAME) ORDER BY T3.IND_NAME");) {
            pst.setString(1, empCode);
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    HighchartsSeries hs = new HighchartsSeries();
                    
                    if (res.getString(2) != null) {
                        if (res.getString(1) != null) {
                            hs.setName(res.getString(1));
                            hs.setY(res.getString(3));
                            hs.setCustName(res.getString(2));
                            seriesList.add(hs);
                        }
                    }
//                    if(res.getString(2)!=null&&res.getString(1)!=null){
//                        hsdrill.add(new String[]{res.getString(2),res.getString(3)});
//                    }
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return seriesList;
    }
    private static String method(String last10Data) {
        return last10Data.substring(0, last10Data.length() - 2);
    }
    
}
