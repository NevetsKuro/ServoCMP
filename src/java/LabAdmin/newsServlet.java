/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package LabAdmin;

import DashBoards.SharedDashBoard;
import com.google.gson.Gson;
import globals.User;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import viewModel.NewsInfo;

public class newsServlet extends HttpServlet {
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doPost(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        
        String url = request.getParameter("url");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        Gson gson = new Gson();
        
        switch(url){
            case "getNewsList":
                List<NewsInfo> newsInfo = SharedDashBoard.getNews(user.getsEMP_CODE(), user.getRole_id());
                gson.toJson(newsInfo, response.getWriter());
                break;
            case "addNews":
                String title = request.getParameter("title");
                String msgBody = request.getParameter("body");
                int a = SharedDashBoard.addNews(user.getsEMP_CODE(), user.getRole_id(), title, msgBody);
                String status3 = String.valueOf(a);
                gson.toJson(status3, response.getWriter());
                break;
            case "updateNews":
                String newsId2 = request.getParameter("newsId");
                String title2 = request.getParameter("title");
                String msgBody2 = request.getParameter("body");
                int u =SharedDashBoard.updateNews( user.getRole_id() ,newsId2,  title2, msgBody2);
                String status2 = (u==1?"Success":"Failure");
                gson.toJson(status2, response.getWriter());
                break;
            case "delNews":
                String newsId = request.getParameter("newsId");
                int i = SharedDashBoard.delNews(user.getRole_id(), newsId);
                String status = (i==1?"Success":"Failure");
                gson.toJson(status, response.getWriter());
                break;
        }  
    }
}
