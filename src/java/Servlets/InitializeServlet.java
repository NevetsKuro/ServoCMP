package Servlets;

import DAOs.MstUserDAO;
import DashBoards.LabAdminDashBoard;
import DashBoards.LabDashBoard;
import DashBoards.SharedDashBoard;
import DashBoards.TseAdminDashBoard;
import DashBoards.TseDashBoard;
import Encryption.TrippleDes;
import Exceptions.MyLogger;
import initializeSesion.initializeValues;
import config.snConfigVars;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.LABoperations;
import operations.TSEoperations;
import org.apache.commons.codec.binary.Base64;
import sessions.snAuthentications;
import viewModel.MstUser;

public class InitializeServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        System.out.println("ServoCMP Deploying in progress.. dated:26-06-2019 11.38");

        if (true) {
//        if (session != null) {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Expires", "0");
            response.setDateHeader("Expires", -1);
            String userid = "0";
            String pwd = "";
            long authId = 0;
            snConfigVars V = new snConfigVars();
            RequestDispatcher rd = null;
            System.out.println("ServoCMP Session Created..");
            if (true) {
//            if (V.isCASenabled()) {
                pwd = null;
//                userid = "" + snAuthentications.getAuthenticationId_Numeric(request);
//                userid = "" + "507469";//snAuthentications.getAuthenticationId_Numeric(request);
//                userid = "00700892"; for testing change id required
//                userid ="700892";//  //lab
//                userid = "505721";
                userid = "505724";
//                userid = 21905; // rajech nambiar
//                userid = "29857"; // samrat das  //Production TSE Admin
//                userid = "225696"; //Rajiv Srivastava  //Production // TSE
//                userid = "505721"; //ABHISHEK KUMAR VERMA //Production
                authId = Long.parseLong(userid);
            } else {
                authId = Long.parseLong(userid);
                pwd = request.getParameter("passwd");
            }
            try {
//                if (true) {
                if (authId > 0) {
                    System.out.println("ServoCMP User authenticated.." + getServletContext().getInitParameter("cemDBPath"));
                    globals.User user = null;
                    if (userid.length() > 0) {
                        try {
                            // inline edit for testing
                            while (userid.length() < 8) {
                                userid = "0" + userid;
                            }
                            user = new globals.User();
                            globals.DBService dbservice = new globals.DBService();
                            user.setIsloggedin(true);

                            user.setDbCon(dbservice.getConnection());
                            if (dbservice.isconnectionvalid == false || user.getDbCon() == null) {
                                request.setAttribute("errorMsg", "Error Occured while connecting to Servo database. Kindly contact WRO IS Department. " + dbservice.sErrorMsg);
                                rd = request.getRequestDispatcher("errorPages/error-DB.jsp");
                                rd.forward(request, response);
                                return;
                            }
                            System.out.println("DB Connected!!!");
                            boolean isValidUser = true;
                            user.setsEMP_CODE("505721");
                            user.setsEMP_NAME("SuperUser");
                            user.setsFUNC_AREA_CODE("3000");
                            user.setsEMAIL_ID("Example@gmail.com");
                            user.setIsvaliduser(true);
//                            user.initialiseCemUser(Long.toString(authId), getServletContext().getInitParameter("cemDBPath"));
                            boolean hasPermission = user.getUserRoleId(user.getsFUNC_AREA_CODE(), "3000", Long.toString(authId));
                            user.setRole_id("1");//Role ID
                            if (hasPermission && isValidUser) {
                                session.setAttribute("sUser", user);
                                //Token Generation
                                Date date = new Date();
                                String genToken = userid + date.getDate() + date.getMinutes() + date.getTime();
                                String csrfToken = new TrippleDes().encrypt(genToken).replaceAll("\\+", "A");;
                                session.setAttribute("csrfToken", csrfToken);
                                System.out.println("csrfToken at initialize servlet::" + csrfToken);
                                initializeValues.setSessionControlTable(request);

                                if (user.getRole_id().equals("1")) {
                                    boolean hasTsePermission = true;// user.initialiseTseUser(Long.toString(authId));
                                    if (hasTsePermission) {
                                        List<String> statuses = TseDashBoard.getStatuses(user.getsEMP_CODE(), (String) session.getAttribute("TseDashDaysLimit"));
                                        List<String> custInd = TseDashBoard.getCustIndWise(user.getsEMP_CODE());
                                        request.setAttribute("stat0", statuses.get(0));
                                        request.setAttribute("stat1", statuses.get(1));
                                        request.setAttribute("stat2", statuses.get(2));
                                        request.setAttribute("stat3", statuses.get(3));
                                        request.setAttribute("stat4", statuses.get(4));
                                        request.setAttribute("last10Data", TseDashBoard.getLast10Data(user.getsEMP_CODE()));
//                                        request.setAttribute("upcomingData", TseDashBoard.getUpcomingData(user.getsEMP_CODE()));
//                                        request.setAttribute("upcomingData", TseDashBoard.getDueSample(user.getsEMP_CODE(),session.getAttribute("NotifyDaysLimit").toString()));
                                        request.setAttribute("recActivity", TseDashBoard.getRecentActivity(user.getsEMP_CODE(), (String) session.getAttribute("TseDashDaysLimit")));
                                        session.setAttribute("notidetails", TSEoperations.getindustryTse(user.getsEMP_CODE(), session.getAttribute("NotifyDaysLimit").toString(), request));
                                        request.setAttribute("news", SharedDashBoard.getNews(user.getsEMP_CODE(), user.getRole_id()));
                                        request.setAttribute("IndustryGraph", custInd.get(0));
                                        request.setAttribute("indCust", custInd.get(1));
                                    } else {
                                        request.setAttribute("errorMsg", "You do not have Access to ServoCMP portal as a TSE user OR no Customer is assigned to You.");
                                        rd = request.getRequestDispatcher("errorPages/error-401.jsp");
                                        rd.forward(request, response);
                                        return;
                                    }
                                } else if (user.getRole_id().equals("2")) {
                                    boolean hasLabPermission = user.initialiseLabUser(Long.toString(authId));
                                    if (hasLabPermission) {
                                        List<String> statuses = LabDashBoard.getStatuses(user.getsEMP_CODE(), (String) session.getAttribute("LabDashDaysLimit"));
                                        List<String> custInd = TseDashBoard.getCustIndWise(user.getsEMP_CODE());
                                        request.setAttribute("stat1", statuses.get(0));
                                        request.setAttribute("stat2", statuses.get(1));
                                        request.setAttribute("stat3", statuses.get(2));
                                        request.setAttribute("last10Data", LabDashBoard.getLast10Data(user.getsEMP_CODE()));
                                        request.setAttribute("upcomingData", LabDashBoard.getUpcomingData(user.getsEMP_CODE()));
                                        request.setAttribute("recActivity", LabDashBoard.getRecentActivity(user.getsEMP_CODE(), (String) session.getAttribute("TseDashDaysLimit")));
                                        session.setAttribute("notidetails", LABoperations.getIndustryWiseLABSummary(user.getsEMP_CODE(), request));
                                        request.setAttribute("news", SharedDashBoard.getNews(user.getsEMP_CODE(), user.getRole_id()));
                                        request.setAttribute("IndustryGraph", custInd.get(0));
                                        request.setAttribute("indCust", custInd.get(1));
                                    } else {
                                        request.setAttribute("errorMsg", "You do not have Access to ServoCMP portal as LAB user OR no LAB is assigned to You.");
                                        rd = request.getRequestDispatcher("errorPages/error-401.jsp");
                                        rd.forward(request, response);
                                        return;
                                    }
                                } else if (user.getRole_id().equals("3")) {
                                    List<String> statuses = TseAdminDashBoard.getStatuses(user.getsEMP_CODE(), (String) session.getAttribute("TseDashDaysLimit"));
                                    List<String> custInd = TseAdminDashBoard.getCustIndWise(user.getsEMP_CODE());
                                    request.setAttribute("stat0", statuses.get(0));
                                    request.setAttribute("stat1", statuses.get(1));
                                    request.setAttribute("stat2", statuses.get(2));
                                    request.setAttribute("stat3", statuses.get(3));
                                    request.setAttribute("stat4", statuses.get(4));
                                    request.setAttribute("last10Data", TseAdminDashBoard.getLast10Data(user.getsEMP_CODE()));
                                    request.setAttribute("upcomingData", TseAdminDashBoard.getUpcomingData(user.getsEMP_CODE()));
                                    request.setAttribute("recActivity", TseAdminDashBoard.getRecentActivity(user.getsEMP_CODE(), (String) session.getAttribute("TseDashDaysLimit")));
                                    //session.setAttribute("notidetails", TSEoperations.getindustryTse(user.getsEMP_CODE(), session.getAttribute("NotifyDaysLimit").toString(), request));
                                    request.setAttribute("news", SharedDashBoard.getNews(user.getsEMP_CODE(), "1" /*user.getRole_id()*/));
                                    request.setAttribute("IndustryGraph", custInd.get(0));
                                    request.setAttribute("indCust", custInd.get(1));
                                } else if (user.getRole_id().equals("4")) {
                                    List<String> statuses = LabAdminDashBoard.getStatuses(user.getsEMP_CODE(), (String) session.getAttribute("TseDashDaysLimit"));
                                    List<String> custInd = LabAdminDashBoard.getCustIndWise(user.getsEMP_CODE());
                                    request.setAttribute("stat0", statuses.get(0));
                                    request.setAttribute("stat1", statuses.get(1));
                                    request.setAttribute("stat2", statuses.get(2));
                                    request.setAttribute("stat3", statuses.get(3));
                                    request.setAttribute("stat4", statuses.get(4));
                                    request.setAttribute("last10Data", LabAdminDashBoard.getLast10Data(user.getsEMP_CODE()));
                                    request.setAttribute("upcomingData", LabAdminDashBoard.getUpcomingData(user.getsEMP_CODE()));
                                    request.setAttribute("recActivity", LabAdminDashBoard.getRecentActivity(user.getsEMP_CODE(), (String) session.getAttribute("TseDashDaysLimit")));
                                    //session.setAttribute("notidetails", TSEoperations.getindustryTse(user.getsEMP_CODE(), session.getAttribute("NotifyDaysLimit").toString(), request));
                                    request.setAttribute("news", SharedDashBoard.getNews(user.getsEMP_CODE(), "2" /* user.getRole_id()*/));
                                    request.setAttribute("IndustryGraph", custInd.get(0));
                                    request.setAttribute("indCust", custInd.get(1));
                                } else if (user.getRole_id().equals("5")) {
                                    String ur = "SELECT MST_USER.EMP_CODE, MST_USER.EMP_NAME, MST_USER.EMP_EMAIL, MST_USER.CNTRLG_EMP_CODE, MST_USER.CNTRLG_EMP_NAME, MST_USER.CNTRLG_EMP_EMAIL, MST_USER.ROLE_ID, MST_USER.UPDATED_BY, MST_USER.UPDATED_DATETIME, MST_ROLE.ROLE_NAME, MST_USER.ACTIVE FROM MST_USER INNER JOIN MST_ROLE ON MST_USER.ROLE_ID = MST_ROLE.ROLE_ID WHERE MST_USER.ACTIVE = 1";
                                    List<MstUser> lst = MstUserDAO.listAllUser(ur);
                                    request.setAttribute("userList", lst);
                                    System.out.println("to universalLogged page");
                                }
                                if (session.getAttribute("SystemMaintenanceFlag").toString().equals("0")) {
                                    rd = request.getRequestDispatcher("index.jsp");
                                    rd.forward(request, response);
                                } else {
                                    response.sendRedirect("systemMaintenance.jsp");
                                }
                            } else {
                                user = null;
                                dbservice = null;
                                request.setAttribute("errorMsg", "You do not have Access to ServoCMP portal as a TSE of LAB user.");
                                rd = request.getRequestDispatcher("errorPages/error-401.jsp");
                                rd.forward(request, response);
                                return;
                            }
                            user.getDbCon().close();
                        } catch (Exception ex) {
                            user.getDbCon().close();
                            userid = "0L";
                            MyLogger.logIt(ex, "InitializeServlet.processRequest()");
                        }
                    } else {
                        response.sendRedirect("/initializeServlet");
                    }
                } else {
                    response.sendRedirect("initializeServlet");
                }
            } catch (Exception ex) {
                userid = "0L";
                MyLogger.logIt(ex, "InitializeServlet.processRequest()");
            }
        } else {
            response.sendRedirect("/logout");
        }

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
