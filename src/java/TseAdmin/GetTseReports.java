
package TseAdmin;

import com.google.gson.Gson;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import reports.TseReports;


public class GetTseReports extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rd = null;
        String fromDate, toDate;
        Gson gson = new Gson();
        
        switch (request.getServletPath()) {
            case "/TseAdmin/TsePerformanceReport":
                fromDate = request.getParameter("fromDate");
                toDate = request.getParameter("toDate");
                if (isAjax(request)) {
                    Boolean logIt = request.getParameter("logIt").equalsIgnoreCase("yes")?true:false;
                    gson.toJson(TseReports.getTseWisePerformance(fromDate, toDate,logIt), response.getWriter());
                } else {
                    Boolean logIt = false;
                    request.setAttribute("listRptDetails", TseReports.getTseWisePerformance(fromDate, toDate,logIt));
                    request.setAttribute("fromDate", fromDate);
                    request.setAttribute("toDate", toDate);
                    rd = request.getRequestDispatcher("/TseAdmin/reportTsePerformance.jsp");
                    rd.forward(request, response);
                }
                break;
            case "/TseAdmin/TseResponseTimeReport":
                fromDate = request.getParameter("fromDate");
                toDate = request.getParameter("toDate");
                if (isAjax(request)) {
                    gson.toJson(TseReports.getTseWiseResponseTime(fromDate, toDate), response.getWriter());
                } else {
                    request.setAttribute("listRptDetails", TseReports.getTseWiseResponseTime(fromDate, toDate));
                    request.setAttribute("fromDate", fromDate);
                    request.setAttribute("toDate", toDate);
                    rd = request.getRequestDispatcher("/TseAdmin/reportTseRespnseTime.jsp");
                    rd.forward(request, response);
                }
                break;
            case "/TseAdmin/LabCSLEfficiencyReport":
            case "/LabAdmin/LabCSLEfficiencyReport":
                fromDate = request.getParameter("fromDate");
                toDate = request.getParameter("toDate");
                if (isAjax(request)) {
                    gson.toJson(TseReports.getLabCSLEfficiencyReport(fromDate, toDate), response.getWriter());
                } else {
                    request.setAttribute("listRptDetails", TseReports.getLabCSLEfficiencyReport(fromDate, toDate));
                    request.setAttribute("fromDate", fromDate);
                    request.setAttribute("toDate", toDate);
                    rd = request.getRequestDispatcher("/TseAdmin/reportLabRespnseTime.jsp");
                    rd.forward(request, response);
                }
                break;
            case "/TseAdmin/CustomerServicesReport":
                fromDate = request.getParameter("fromDate");
                toDate = request.getParameter("toDate");
                if (isAjax(request)) {
                    gson.toJson(TseReports.getCustomerServices(fromDate, toDate), response.getWriter());
                } else {
                    request.setAttribute("listRptDetails", TseReports.getCustomerServices(fromDate, toDate));
                    request.setAttribute("fromDate", fromDate);
                    request.setAttribute("toDate", toDate);
                    rd = request.getRequestDispatcher("/TseAdmin/reportCustomerServices.jsp");
                    rd.forward(request, response);
                }
                break;
            default:
                response.sendError(404);
        }

    }

    /**
     *
     *
     *
       $.ajax({
      url:'/ServoCMP/TseAdmin/TsePerformanceReport?fromDate='+$("#fromDate").val()+'&toDate='+$("#tosDate").val()+'&logIt=yes',
      type:'POST',
      success:function(data){ console.log(data); },
      error:function(data){ console.log(data); } })
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest"
                .equals(request.getHeader("X-Requested-With"));
    }
}
