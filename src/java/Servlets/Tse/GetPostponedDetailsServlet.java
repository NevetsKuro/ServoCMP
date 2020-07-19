package Servlets.Tse;

import Exceptions.MyLogger;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import operations.SharedOperations;
import viewModel.SamplePostponedHistory;
import org.json.JSONArray;
import org.json.JSONObject;

public class GetPostponedDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SamplePostponedHistory samplepostponed = new SamplePostponedHistory();
        samplepostponed.setCustsampleinfoId(request.getParameter("custInfoId"));
        samplepostponed.setPrevsampleDate(request.getParameter("prevsampledate"));
        List samplePostponedList = SharedOperations.getPostponedSampleDetails(samplepostponed);
        JSONObject ObjectList = new JSONObject();
        JSONArray listObj = new JSONArray();
        try {
            for (int i = 0; i < samplePostponedList.size(); i++) {
                JSONObject eachObj = new JSONObject();
                SamplePostponedHistory spObj = (SamplePostponedHistory) samplePostponedList.get(i);
                String preDate = spObj.getPrevsampleDate();
                String nxtDate = spObj.getNxtsampleDate();
                String postponedDate = spObj.getPostponedDate();
                String rmks = spObj.getRemarks();
                if (rmks == null) {
                    rmks = "No Remarks";
                }
                eachObj.put("PREV_DATE", preDate);
                eachObj.put("NEXT_DATE", nxtDate);
                eachObj.put("POSTPONED_DATE", postponedDate);
                eachObj.put("REMARKS", rmks);
                listObj.put(eachObj);
            }
            ObjectList.put("DATA", listObj);
        } catch (Exception ex) {
            MyLogger.logIt(ex, "GetPostponedDetailsServlet.doGet() ");
        }
        response.setContentType("application/json");
        response.getWriter().write("{\"data\":" + listObj.toString() + "}");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
