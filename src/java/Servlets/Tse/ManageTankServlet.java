package Servlets.Tse;

import DAOs.MstTankDAO;
import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import com.google.gson.Gson;
import globals.User;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import operations.ApplicationSQLDate;
import operations.GetMaster;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import viewModel.MessageDetails;
import viewModel.MstTank;

public class ManageTankServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Gson gson = new Gson();
        Map<String, Object> params = new HashMap<>();
        List<MstTank> result;
        boolean first = true;
        String path = request.getServletPath();
        String query = "SELECT T1.TANK_ID, T2.IND_NAME, T3.CUST_NAME, T4.DEPT_NAME, T5.APPL_NAME, T6.EQUIP_NAME, T7.PROD_NAME, T1.TANK_NO, T1.TANK_DESC, "
                + "T1.APPL_DESC, T1.CAPACITY, T1.SAMPLING_NO, T1.PREV_SAMPLE_DATE, T1.NEXT_SAMPLE_DATE, T1.UPDATED_BY, T1.UPDATED_DATETIME, T1.SAMPLE_FREQ, T1.LAST_OIL_CHANGED, T8.MAKE_NAME FROM MST_TANK "
                + "T1 INNER JOIN MST_INDUSTRY T2 ON T2.IND_ID = T1.IND_ID INNER JOIN MST_CUSTOMER T3 ON T3.CUST_ID = T1.CUST_ID INNER JOIN "
                + "MST_DEPARTMENT T4 ON T4.DEPT_ID = T1.DEPT_ID INNER JOIN MST_APPLICATION T5 ON T5.APPL_ID = T1.APPL_ID INNER JOIN MST_EQUIPMENT T6 "
                + "ON T6.EQUIP_ID = T1.EQUIP_ID INNER JOIN MST_PRODUCT T7 ON T7.PROD_ID = T1.PROD_ID LEFT JOIN MST_MAKE T8 on T8.MAKE_ID = T6.MAKE_ID";
        if(request.getParameter("tseOfficer")!=null){
            if(request.getParameter("tseOfficer").equals("101")){
                path = "/Tse/GetAllTse";
            }
        }
        
        switch (path) {
            case "/Tse/GetAllTse":
                List<MstTank> mstTank = getAllTseInfo(request.getParameter("tseOfficer"));
                String rootPath = getServletContext().getRealPath("/files/");
                convertAndCreateExcel(mstTank, rootPath,response);

//                if (!mstTank.isEmpty()) {
//                    response.getWriter().write(gson.toJson(mstTank));
//                } else {
//                    response.getWriter().write(gson.toJson("No Tank Found"));
//                }
                break;
            case "/Tse/GetTank":
            case "/TseAdmin/GetTank":
                boolean flag = false;
                String queryPlus = "";
                Boolean queryPlusBoo = false;
                if (!"".equals(request.getParameter("industry")) && request.getParameter("industry") != null) {
                    params.put("T1.IND_ID", request.getParameter("industry"));
                    if ("".equals(request.getParameter("customer")) || request.getParameter("customer") == null) {
                        queryPlus = "and t3.cust_id in (select cust_id from mst_customer where emp_code = "+request.getParameter("tseCode") +")";
                        queryPlusBoo = true;
                    }
                    flag = true;
                }
                if (!"".equals(request.getParameter("customer")) && request.getParameter("customer") != null) {
                    params.put("T1.CUST_ID", request.getParameter("customer"));
                    flag = true;
                }
                if (!"".equals(request.getParameter("department")) && request.getParameter("department") != null) {
                    params.put("T1.DEPT_ID", request.getParameter("department"));
                    flag = true;
                }
                if (!"".equals(request.getParameter("application")) && request.getParameter("application") != null) {
                    params.put("T1.APPL_ID", request.getParameter("application"));
                    flag = true;
                }
                if (!"".equals(request.getParameter("equipment")) && request.getParameter("equipment") != null) {
                    params.put("T1.EQUIP_ID", request.getParameter("equipment"));
                    flag = true;
                }
                if (!"".equals(request.getParameter("product")) && request.getParameter("product") != null) {
                    params.put("T1.PROD_ID", request.getParameter("product"));
                    flag = true;
                }
                
                for (String paramName : params.keySet()) {
                    Object paramVal = params.get(paramName);
                    if (paramVal != null) {
                        if (first) {
                            query += " WHERE " + paramName + " = ? ";
                            first = false;
                        } else {
                            query += "AND " + paramName + " = ? ";
                        }
                    }
                }
                if(queryPlusBoo){
                    query += queryPlus;
                }
                if(!flag){
                    String tseCode = request.getParameter("tseCode");
                    result = getTanksByTse(tseCode);
                }else{
                    result = MstTankDAO.getAllTanks(query, params);
                }
                if (!result.isEmpty()) {
                    response.getWriter().write(gson.toJson(result));
                } else {
                    response.getWriter().write(gson.toJson("No Tank Found"));
                }
                break;

            case "/Tse/ManageTank":
                if (request.getParameter("equipment") == null || "".equals(request.getParameter("equipment"))) {
                    List equipments = GetMaster.getEquipments();
                    if (equipments.size() != 0) {
                        response.getWriter().write(gson.toJson(equipments));
                    } else if (equipments.size() == 0) {
                        response.getWriter().write("No Equipments found");
                    }
                } else if (request.getParameter("equipment") != null || !"".equals(request.getParameter("equipment"))) {
                    List tanks = GetMaster.getTanks(request.getParameter("equipment").toString());
                    if (tanks.size() != 0) {
                        response.getWriter().write(gson.toJson(tanks));
                    } else if (tanks.size() == 0) {
                        response.getWriter().write(gson.toJson("No Tank with this Equipment. Kindly Add New"));
                    }
                }
                break;
            case "/TseAdmin/UpdateTank2":
                String tankid = request.getParameter("tankid");
                String freq = request.getParameter("freq");
                Date nexSampleDate = ApplicationSQLDate.convertStringtoUtilDate(request.getParameter("nexSampleDate"));
                String status = "..in progress";
                status = updateTank(tankid, freq, nexSampleDate);
                response.getWriter().write(gson.toJson(status));
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        MessageDetails md = new MessageDetails();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("sUser");
        MstTank mstTank = new MstTank();
        switch (request.getServletPath()) {
            case "/Tse/RemoveTank":
                mstTank.setTankId(request.getParameter("tank"));
                mstTank.setSamplingNo("0");
                md = MstTankDAO.deleteTank(mstTank);
                md.setModalTitle("Tank Deletion Status");
                break;
            case "/Tse/AddNewTank":
                mstTank.setIndId(request.getParameter("tankIndustry"));
                mstTank.setCustId(request.getParameter("tankCustomer"));
                mstTank.setDeptId(request.getParameter("tankDepartment"));
                mstTank.setApplId(request.getParameter("tankApplication"));
                mstTank.setEquipId(request.getParameter("tankEquipment"));
                mstTank.setProId(request.getParameter("tankProduct"));
                mstTank.setTankNo(request.getParameter("tankTankNo"));
                mstTank.setTankDesc(request.getParameter("tankTankDesc"));
                mstTank.setApplDesc(request.getParameter("tankAppDesc"));
                mstTank.setCapacity(request.getParameter("tankCapacity"));
                String check = request.getParameter("oneTimeCheckbox")==null?"0":request.getParameter("oneTimeCheckbox");
                if(check.equals("1")){
                    mstTank.setProductName(request.getParameter("prodNameOMC"));
                    mstTank.setProductGrade(request.getParameter("prodGradeOMC"));
                }else{
                    mstTank.setProductGrade("");
                    mstTank.setProductName("");
                }
                mstTank.setOneTimeCheckbox(check);
                mstTank.setSamplingNo("0");
                String freq = request.getParameter("tankFreq") == null?"0":request.getParameter("tankFreq");
                mstTank.setSampleFreq(freq);
                if(check.equals("1")){
                    mstTank.setPrevSampleDate(ApplicationSQLDate.convertStringtoUtilDate(request.getParameter("tankPreDate")));
                    mstTank.setNxtSampleDate(ApplicationSQLDate.convertStringtoUtilDate(request.getParameter("tankPreDate")));
                }else{
                    mstTank.setPrevSampleDate(ApplicationSQLDate.convertStringtoUtilDate(request.getParameter("tankPreDate")));
                    mstTank.setNxtSampleDate(ApplicationSQLDate.convertStringtoUtilDate(request.getParameter("tankNextDate")));
                }
                mstTank.setPostponeFlag("0");
                mstTank.setPostponeCount("0");
                mstTank.setLastOilChange(request.getParameter("tankLastOilChange"));
                mstTank.setUpdatedBy(user.getsEMP_CODE());
                mstTank.setActive("1");
                md = MstTankDAO.InsertTank(mstTank, request.getParameterValues("testIds"));
                md.setModalTitle("New Tank Status");
                break;
        }
        if (isAjax(request)) {
            Gson gson = new Gson();
            response.getWriter().write(gson.toJson(md));
        } else {
            request.setAttribute("messageDetails", md);
            RequestDispatcher rd = request.getRequestDispatcher("ManageSample");
            rd.forward(request, response);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest"
                .equals(request.getHeader("X-Requested-With"));
    }
    
    private List getTanksByTse(String tseCode) {
        List<MstTank> mstTank = new ArrayList<>();
        String sql = "SELECT T1.TANK_ID, T2.IND_NAME, T3.CUST_NAME, T4.DEPT_NAME, T5.APPL_NAME, T6.EQUIP_NAME, T7.PROD_NAME, T1.TANK_NO, T1.TANK_DESC, " +
                    "T1.APPL_DESC, T1.CAPACITY, T1.SAMPLING_NO, T1.PREV_SAMPLE_DATE, T1.NEXT_SAMPLE_DATE, T1.UPDATED_BY, T1.UPDATED_DATETIME, T1.SAMPLE_FREQ, T1.LAST_OIL_CHANGED, T8.MAKE_NAME FROM MST_TANK " +
                    "T1 INNER JOIN MST_INDUSTRY T2 ON T2.IND_ID = T1.IND_ID INNER JOIN MST_CUSTOMER T3 ON T3.CUST_ID = T1.CUST_ID INNER JOIN " +
                    "MST_DEPARTMENT T4 ON T4.DEPT_ID = T1.DEPT_ID INNER JOIN MST_APPLICATION T5 ON T5.APPL_ID = T1.APPL_ID INNER JOIN MST_EQUIPMENT T6 " +
                    "ON T6.EQUIP_ID = T1.EQUIP_ID INNER JOIN MST_PRODUCT T7 ON T7.PROD_ID = T1.PROD_ID LEFT JOIN MST_MAKE T8 on T8.MAKE_ID = T6.MAKE_ID where t3.cust_id in (select cust_id from mst_customer where emp_code = ?)";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(sql);) {
            pst.setString(1, tseCode);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTank mTank = new MstTank();
                mTank.setTankId(res.getString(1));
                mTank.setIndName(res.getString(2));
                mTank.setCustName(res.getString(3));
                mTank.setDeptName(res.getString(4));
                mTank.setApplName(res.getString(5));
                mTank.setEquipName(res.getString(6));
                mTank.setProName(res.getString(7));
                mTank.setTankNo(res.getString(8));
                mTank.setTankDesc(res.getString(9));
                mTank.setApplDesc(res.getString(10));
                mTank.setCapacity(res.getString(11));
                mTank.setSamplingNo(res.getString(12));
                mTank.setStrPrevDate(ApplicationSQLDate.convertUtilDatetoString(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(13))));
                mTank.setStrNxtDate(ApplicationSQLDate.convertUtilDatetoString(ApplicationSQLDate.convertSqltoUtilDate(res.getDate(14))));
                mTank.setUpdatedBy(res.getString(15));
                mTank.setUpdatedDateTime(res.getString(16));
                mTank.setSampleFreq(res.getString(17));
                mTank.setLastOilChange(res.getString(18));
                mTank.getMstEquipment().getMstmake().setMakeName(res.getString(19));
                mstTank.add(mTank);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, tseCode);
        }
        return mstTank;
    }

private List getAllTseInfo(String tseCode) {
        List<MstTank> mstTank = new ArrayList<>();
        String query = "SELECT T2.IND_NAME, t5.appl_name, T3.CUST_NAME, t8.sap_code, T6.EQUIP_NAME, t9.make_name, T7.PROD_NAME, T1.TANK_NO, " +
            "T1.CAPACITY, T1.PREV_SAMPLE_DATE, T1.SAMPLE_FREQ, T10.EMP_NAME, T1.UPDATED_BY,T1.UPDATED_DATETIME FROM MST_TANK  " +
            "T1 INNER JOIN MST_INDUSTRY T2 ON T2.IND_ID = T1.IND_ID INNER JOIN MST_CUSTOMER T3 ON T3.CUST_ID = T1.CUST_ID INNER JOIN  " +
            "MST_DEPARTMENT T4 ON T4.DEPT_ID = T1.DEPT_ID INNER JOIN MST_APPLICATION T5 ON T5.APPL_ID = T1.APPL_ID INNER JOIN MST_EQUIPMENT T6  " +
            "ON T6.EQUIP_ID = T1.EQUIP_ID INNER JOIN MST_PRODUCT T7 ON T7.PROD_ID = T1.PROD_ID left join mst_map_cust_sapcode T8 " +
            "on T8.cust_id = t1.cust_id LEFT JOIN MST_MAKE T9 ON T6.MAKE_ID = t9.MAKE_ID INNER JOIN MST_USER T10 on T3.emp_code =  T10.emp_code " +
            "where t3.cust_id in (select cust_id from mst_customer)";
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement(query);) {
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                MstTank mTank = new MstTank();
                mTank.setIndName(res.getString(1));
                mTank.setApplName(res.getString(2));
                mTank.setCustName(res.getString(3));
                mTank.setSapcodes(res.getString(4));
                mTank.setEquipName(res.getString(5));
                mTank.getMstEquipment().getMstmake().setMakeName(res.getString(6));
                mTank.setProName(res.getString(7));
                mTank.setTankNo(res.getString(8));
                mTank.setCapacity(res.getString(9));
                mTank.setStrPrevDate(res.getString(10));
                mTank.setSampleFreq(res.getString(11));
                mTank.setActive(res.getString(12));
                mTank.setUpdatedBy(res.getString(13));
                mTank.setUpdatedDateTime(res.getString(14));
                mstTank.add(mTank);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, tseCode);
        }
        return mstTank;
    }

    private void convertAndCreateExcel(List<MstTank> mstTank, String rootPath,HttpServletResponse response) throws FileNotFoundException, IOException {
        Workbook workbook = new HSSFWorkbook(); 
        
//        OutputStream fileOut = new FileOutputStream("Geeks.xlsx");
//        FileOutputStream fileOut = new FileOutputStream(new File(rootPath+"/Geekz.xls")); 
        ByteArrayOutputStream fileOut = new ByteArrayOutputStream();
        Sheet sheet1 = workbook.createSheet("Tse Details"); 
        
        Map<String, Object[]> data = new TreeMap<String, Object[]>(); 
        data.put("1", new Object[]{ "INDUSTRY", "Oil Application Type ", "CUSTOMER NAME", "CUSTOMER SAP ", "EQUIPMENT NAME", 
            "OEM / Make", "Oil Grade Name","Tank Number","Capacity of Tank/Sump (ltrs)","Previous Sample Drawn Date","Frequency of Sampling (in months) ", "TSE RESPONSIBLE",
            "UPDATED BY(EMP CODE)","DATE OF UPDATION" }); 
            int count = 1;
        for (MstTank tank : mstTank) {
            count++;
            data.put(String.valueOf(count), new Object[]{ 
                tank.getIndName(),
                tank.getApplName(),
                tank.getCustName(),
                tank.getSapcodes(),
                tank.getEquipName(),
                tank.getMstEquipment().getMstmake().getMakeName(),
                tank.getProName(),
                tank.getTankNo(),
                tank.getCapacity(),
                tank.getStrPrevDate(),
                tank.getSampleFreq(),
                tank.getActive(),
                tank.getUpdatedBy(),
                tank.getUpdatedDateTime()
            }); 
        }
        
  
        // Iterate over data and write to sheet 
        Set<String> keyset = data.keySet(); 
        int rownum = 0; 
        for (String key : keyset) { 
            // this creates a new row in the sheet 
            Row row = sheet1.createRow(rownum++); 
            Object[] objArr = data.get(key); 
            int cellnum = 0; 
            for (Object obj : objArr) { 
                // this line creates a cell in the next column of that row 
                Cell cell = row.createCell(cellnum++); 
                if (obj instanceof String) 
                    cell.setCellValue((String)obj); 
                else if (obj instanceof Integer) 
                    cell.setCellValue((Integer)obj); 
            } 
        } 
        
        workbook.write(fileOut);

        byte[] outputByte = fileOut.toByteArray();  // new byte[4096];
        //copy binary contect to output stream
        File file = new File(rootPath+"/Geekz.xls");
        response.setContentType("application/ms-excel");
        response.setContentLength(outputByte.length);
        response.setHeader("Expires:", "0"); // eliminates browser caching
        response.setHeader("Content-Disposition", "attachment; filename=All_MIS_System_Master.xls");//Set name of the file
//        ServletOutputStream out = response.getOutputStream();
        OutputStream out = response.getOutputStream();
//        FileInputStream fileIn = new FileInputStream(file);
//        while(fileIn.read(outputByte, 0, 4096) != -1)
//        {
//                out.write(outputByte, 0, 4096);
//        }
        out.write(outputByte);
        out.flush();
        out.close();
//        fileIn.close();
        fileOut.close();
    }

    private String updateTank(String tankid,String freq, Date date) {
        int count = 0;
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Update mst_tank set SAMPLE_FREQ = ?, NEXT_SAMPLE_DATE = ?, OLD_NEXT_SAMPLE_DATE = ? where tank_id = ? ");) {
            pst.setString(1, freq);
            pst.setDate(2, ApplicationSQLDate.convertUtiltoSqlDate(date));
            pst.setDate(3, ApplicationSQLDate.convertUtiltoSqlDate(date));
            pst.setString(4, tankid);
            count = pst.executeUpdate();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        if(count==1){
            return "Successfully Updated";
        }else{
            return "Failed to update.";
        }
    }
}

