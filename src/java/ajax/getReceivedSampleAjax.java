package ajax;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.LABoperations;
import viewModel.MessageDetails;
import viewModel.SampleDetails;

public class getReceivedSampleAjax extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            String pid = request.getParameter("pid");
        String cid = request.getParameter("cid");
        String did = request.getParameter("did");
        String aid = request.getParameter("aid");
        HttpSession session = request.getSession();
        MessageDetails md = (MessageDetails) request.getAttribute("messsge");
        globals.User user = new globals.User();
        user = (globals.User) session.getAttribute("sUser");
        List<SampleDetails> listpndSmpls = LABoperations.getLabSampleDetails(user.getsEMP_CODE(), request.getParameter("status"), pid, cid, did, aid);

        Gson gson = new Gson();
        gson.toJson( listpndSmpls , response.getWriter());
    }

}
