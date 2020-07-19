package TseAdmin;

import DAOs.MstTestDAO;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import viewModel.MessageDetails;
import viewModel.MstTest;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;

public class TestSequenceServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<MstTest> mTest = MstTestDAO.listAllTest("SELECT TEST_ID, TEST_NAME, UNIT, TEST_METHOD, SAMPLE_QTY, DISP_SEQ_NO, UPDATED_BY, UPDATED_DATETIME FROM MST_TEST WHERE ACTIVE = 1 ORDER BY DISP_SEQ_NO");
        request.setAttribute("mstTest", mTest);
        
        RequestDispatcher rd = request.getRequestDispatcher("/TseAdmin/testSequence.jsp");
        rd.forward(request, response);
        
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson = new Gson();
        MessageDetails md;
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> testPos = gson.fromJson(request.getParameter("dispPos"), type);
        md = MstTestDAO.saveSequence(testPos);
        md.setModalTitle("Test Sequence Status");
        request.setAttribute("messageDetails", md);
        if (isAjax(request)) {
            response.getWriter().write(gson.toJson(md));
        } else {
            doGet(request, response);
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    
    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest"
                .equals(request.getHeader("X-Requested-With"));
    }
    
}
