package DashBoards;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import viewModel.NewsInfo;

public class SharedDashBoard {

    public static List<NewsInfo> getNews(String empCode, String roleId) {
        List<NewsInfo> nInfo = new ArrayList<>();
        if(roleId.equalsIgnoreCase("4")||roleId.equalsIgnoreCase("2")){
            roleId = "2";
        }else if(roleId.equalsIgnoreCase("3")||roleId.equalsIgnoreCase("1")){
            roleId = "1";
        }
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT * FROM USER_MSG_DETAILS WHERE ACTIVE = ? AND ROLE_ID = ? AND ACTIVE = ? ORDER BY UPDATED_DATETIME ASC");) {
            pst.setString(1, "1");
            pst.setString(2, roleId);
            pst.setString(3, "1");
            try (ResultSet res = pst.executeQuery();) {
                while (res.next()) {
                    NewsInfo newInfo = new NewsInfo();
                    newInfo.setMsgId(res.getString(1));
                    newInfo.setMsgTitle(res.getString(2));
                    newInfo.setMsgBody(res.getString(3));
                    newInfo.setUpdatedby(res.getString(5));
                    newInfo.setUpdatedDate(res.getString(6));
                    nInfo.add(newInfo);
                }
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return nInfo;
    }
    
    public static int addNews(String empCode, String roleId,String title,String msgBody) {
        int i = 0;
        roleId = (roleId.equalsIgnoreCase("4")?"2":"1");
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO USER_MSG_DETAILS VALUES ((SELECT count(USER_MSG_ID)+1 FROM USER_MSG_DETAILS),?,?,?,?,TO_CHAR(SYSDATE, 'DD-MM-YYYY'),?)");) {
            pst.setString(1, title);
            pst.setString(2, msgBody);
            pst.setString(3, roleId);
            pst.setString(4, empCode);
            pst.setString(5, "1");
            i = pst.executeUpdate();
        } catch (Exception ex) {
            MyLogger.logIt(ex, empCode);
        }
        return i;
    }
    
    public static int updateNews(String roleId,String newsId,String title,String msgBody) {
        int i = 0;
        roleId = (roleId.equalsIgnoreCase("4")?"2":"1");
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Update USER_MSG_DETAILS SET USER_MSG_TITLE = ?, USER_MSG_BODY = ? WHERE ROLE_ID = ? AND USER_MSG_ID = ?");) {
            pst.setString(1, title);
            pst.setString(2, msgBody);
            pst.setString(3, roleId);
            pst.setString(4, newsId);
            i = pst.executeUpdate();
        } catch (Exception ex) {
            MyLogger.logIt(ex, roleId);
        }
        return i;
    }
    
    
    public static int delNews(String roleId,String newsId) {
        int i = 0;
        roleId = (roleId.equalsIgnoreCase("4")?"2":"1");
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Update USER_MSG_DETAILS SET ACTIVE = 0 WHERE USER_MSG_ID = ? AND ROLE_ID = ?");) {
            pst.setString(1, newsId);
            pst.setString(2, roleId);
            i = pst.executeUpdate();
        } catch (Exception ex) {
            MyLogger.logIt(ex, roleId);
        }
        return i;
    }
}
