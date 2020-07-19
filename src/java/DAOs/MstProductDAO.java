package DAOs;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import operations.ErrorLogger;
import viewModel.MessageDetails;
import viewModel.MstProduct;
import viewModel.ReportDetails;

public class MstProductDAO {

    public static List<MstProduct> listAllProducts(String query) {
        List<MstProduct> mstProduct = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);) {
            while (res.next()) {
                MstProduct mPro = new MstProduct();
                mPro.setProId(res.getString(1));
                mPro.setProName(res.getString(2));
                mPro.setActive(res.getString(3));
                mPro.setUpdatedBy(res.getString(4));
                mPro.setUpdatedDate(res.getString(5));
                mstProduct.add(mPro);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstProductDAO.listAllProducts()");
        }
        return mstProduct;
    }
    
    public static List<MstProduct> listAllProductsCat(String query) {
        List<MstProduct> mstProduct = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(query);) {
            while (res.next()) {
                MstProduct mPro = new MstProduct();
                mPro.setProId(res.getString(1));
                mPro.setProName(res.getString(2));
                if(res.getString(3)!=null){
                    mPro.setProdCat(res.getString(3));
                }
                mPro.setActive(res.getString(4));
                mPro.setUpdatedBy(res.getString(5));
                mPro.setUpdatedDate(res.getString(6));
                mstProduct.add(mPro);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "MstProductDAO.listAllProducts()");
        }
        return mstProduct;
    }

    public static MessageDetails AddProduct(MstProduct mstProduct) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("INSERT INTO MST_PRODUCT (PROD_ID, PROD_NAME, ACTIVE, UPDATED_BY, UPDATED_DATETIME) "
                        + "VALUES ((SELECT MAX(TO_NUMBER(REPLACE(PROD_ID, 'SYS', ''))) + 1 FROM MST_PRODUCT), ?, ?, ?, "
                        + "TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM'))");) {
            md.setModalMessage("");
            for (String proName : mstProduct.getProNames()) {
                try {
                    pst.setString(1, proName.toUpperCase());
                    pst.setString(2, mstProduct.getActive());
                    pst.setString(3, mstProduct.getUpdatedBy());
                    if (pst.executeUpdate() > 0) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-success'> Product '" + proName.toUpperCase() + "' Added Successfully to Masters.</span><br/><br/>");
                    } else {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error occured while adding Product '" + proName.toUpperCase() + "'. No changes were made.</span><br/><br/>");
                    }
                } catch (Exception ex) {
                    md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Error While adding Product '" + proName.toUpperCase() + "'. (" + ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), mstProduct.getUpdatedBy()) + ") </span> <br/>");
                    if (ex.getMessage().contains("unique")) {
                        md.setModalMessage(md.getModalMessage() + "<span class='text-danger'> Product '" + proName.toUpperCase() + "' Already exist in Master. </span><br/><br/>");
                    }
                }
            }
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstProduct.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFilemsgClass("text-danger");
                md.setFileMsg("Product '" + mstProduct.getProName() + "' Already exist in Master");
            }
        }
        return md;
    }

    public static MessageDetails markInactive(MstProduct mstProduct) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_PRODUCT SET ACTIVE = ?, UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE PROD_ID = ?");) {
            pst.setString(1, mstProduct.getActive());
            pst.setString(2, mstProduct.getUpdatedBy());
            pst.setString(3, mstProduct.getProId());
            md.setStatus(pst.executeUpdate() > 0);
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstProduct.getUpdatedBy());
        }
        return md;
    }

    public static MessageDetails UpdateProduct(MstProduct mstProduct) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("UPDATE MST_PRODUCT SET PROD_NAME = ?, CATEGORY = ? , UPDATED_BY = ?, UPDATED_DATETIME = TO_CHAR(SYSDATE, 'DD-MON-YYYY HH12:MI:SS AM') WHERE PROD_ID = ?");) {
            pst.setString(1, mstProduct.getProName().toUpperCase());
            pst.setInt(2, Integer.parseInt(mstProduct.getProdCat()));
            pst.setString(3, mstProduct.getUpdatedBy());
            pst.setString(4, mstProduct.getProId());
            md.setStatus(pst.executeUpdate() > 0);
        } catch (Exception ex) {
            md = MyLogger.logIt(ex, mstProduct.getUpdatedBy());
            if (ex.getMessage().contains("unique")) {
                md.setFileMsg("Product " + mstProduct.getProName() + " Already Exist");
                md.setFilemsgClass("text-danger");
            }
        }
        return md;
    }

    public static List<ReportDetails> getCategoryByProduct(String productId) {
        List<ReportDetails> listRptDetails = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("select c1.cat_ids,c1.test_ids,t1.test_name,t1.sample_qty from product_category c1 inner join mst_test t1 on t1.test_id = c1.test_ids where c1.cat_ids = (select category from mst_product where prod_id = ?)");) {
            pst.setString(1, productId);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
               ReportDetails rptDetails = new ReportDetails();
               rptDetails.setCol1(rs.getString(1));
               rptDetails.setCol2(rs.getString(2));
               rptDetails.setCol3(rs.getString(3));
               rptDetails.setCol4(rs.getString(4));
               listRptDetails.add(rptDetails);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, productId);
        }
        return listRptDetails;
    }

    public static List<MstProduct> getProductByOMCTag(String tankId, String productId) {
        List<MstProduct> listproduct = new ArrayList<>();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("select p1.prod_id, p1.prod_name from mst_tank t1 inner join mst_product p1 on t1.similar_product = p1.prod_id where t1.tank_id = ? and t1.prod_id = ? ");) {
            pst.setString(1, tankId);
            pst.setString(2, productId);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
               MstProduct prod = new MstProduct();
               prod.setProId(rs.getString(1));
               prod.setProName(rs.getString(2));
               listproduct.add(prod);
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, productId);
        }
        return listproduct;
    }

}
