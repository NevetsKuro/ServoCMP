/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlets;

import DashBoards.LabAdminDashBoard;
import DashBoards.LabDashBoard;
import DashBoards.SharedDashBoard;
import DashBoards.TseAdminDashBoard;
import DashBoards.TseDashBoard;
import Exceptions.MyLogger;
import config.snConfigVars;
import globals.User;
import initializeSesion.initializeValues;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.LABoperations;
import operations.TSEoperations;

public class initailizeByAdmin extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userid = "";
        userid = request.getParameter("userCode");
        System.out.println("initializing a user"+userid);
        HttpSession session = request.getSession();
        User user1 = (User) session.getAttribute("sUser");
        
        RequestDispatcher rd = null;
        if (!userid.equals("")&& user1.getRole_id().equals("5")) {
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Expires", "0");
            response.setDateHeader("Expires", -1);
            String pwd = "";
            long authId = 0;
            snConfigVars V = new snConfigVars();

            authId = Long.parseLong(userid);

            try {
                if (true) {
                    globals.User user = null;
                    if (true) {
                        try {
                            while (userid.length() < 8) {
                                userid = "0" + userid;
                            }
                            // inline edit for testing
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
                            boolean isValidUser = true;
                            user.initialiseCemUser(Long.toString(authId), getServletContext().getInitParameter("cemDBPath"));
                            boolean hasPermission = user.getUserRoleId(user.getsFUNC_AREA_CODE(), "3000", Long.toString(authId));
                            if (hasPermission && isValidUser) {
                                session.setAttribute("sUser", user);
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
                                        request.setAttribute("upcomingData", TseDashBoard.getUpcomingData(user.getsEMP_CODE()));
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
        }else{
            request.setAttribute("errorMsg", "You do not have Access to this website. Kindly Go Back.");
            rd = request.getRequestDispatcher("errorPages/error-401.jsp");
            rd.forward(request, response);
        }
    }

}
