/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package LabAdmin;

import DAOs.MstTestDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import viewModel.MstTest;

public class OtherValuesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        Gson gson = new Gson();
        switch (action) {
            case "getSummary":
                String query = "select test_name,oval,t2.test_id from (select test_id, listagg(OTHER_VAL,',') within group( order by test_id ) AS oval from MST_OTHER_TEST_VALUE group by test_id) t1, mst_test t2 where t1.test_id=t2.test_id";
                List<MstTest> tests = new ArrayList<MstTest>();
                tests = MstTestDAO.listOfOthTestsWithVals(query);
                new Gson().toJson(tests, response.getWriter());
                break;
            case "getTests":
                List<MstTest> allTests = MstTestDAO.gettestMaster();
                new Gson().toJson(allTests, response.getWriter());
                break;
            case "addOValues":
                String testId2 = request.getParameter("testId");
                String json = request.getParameter("json");

                String[] Ovalues2 = gson.fromJson(json, String[].class);
                String status2 = MstTestDAO.addOVtest(testId2, Ovalues2);
                status2 += " is inserted";
                new Gson().toJson(status2, response.getWriter());
                break;
            case "updateOVTests":
                String json1 = request.getParameter("json");
                String[] Ovalues = gson.fromJson(json1, String[].class);
                String testId = request.getParameter("testId");
                String status = "";
                status = MstTestDAO.updateOVtest(testId, Ovalues);
                new Gson().toJson(status, response.getWriter());
                break;
            case "getUniqueOValues":
                List<MstTest> ovalues = MstTestDAO.getExistingOValues();
                new Gson().toJson(ovalues, response.getWriter());
                break;
        }

    }

}
