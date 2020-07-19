package operations;

import Exceptions.MyLogger;
import com.DatabaseConnectionFactory;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.Part;
import viewModel.FileDetails;
import viewModel.MessageDetails;
import viewModel.SampleDetails;

public class FileOperations {

    public static MessageDetails checkandUploadDoc(FileDetails file,SampleDetails sd) {
        MessageDetails md = new MessageDetails();
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("SELECT SAMPLE_ID FROM LAB_TEST_RESULT_DOC_INFO WHERE SAMPLE_ID=? AND LAB_CODE = ?");
                PreparedStatement updateSt = con.prepareStatement("UPDATE LAB_TEST_RESULT_DOC_INFO SET LAB_RESULT_DOC=?, DOC_TYPE=?, "
                        + "UPLOADED_DATETIME=?, UPLOADED_BY=? where SAMPLE_ID=? AND LAB_CODE = ?");
                PreparedStatement insertSt = con.prepareStatement("INSERT INTO LAB_TEST_RESULT_DOC_INFO(SAMPLE_ID, LAB_CODE, LAB_RESULT_DOC, DOC_TYPE,"
                        + "UPLOADED_DATETIME, UPLOADED_BY) VALUES(?,?,?,?,?,?)");
                InputStream is = file.getFile().getInputStream();) { 
            pst.setString(1, sd.getSampleId());
            pst.setString(2, sd.getMstLab().getLabCode());
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                updateSt.setBinaryStream(1, is, is.available());
                updateSt.setString(2, file.getFileExt());
                updateSt.setString(3, file.getUploadTime());
                updateSt.setString(4, file.getUploadedBy());
                updateSt.setString(5, file.getFileName());
                updateSt.setString(6, sd.getMstLab().getLabCode());
                updateSt.execute();
                file.setSaveStatus("1");
            } else {
                insertSt.setString(1, file.getFileName());
                insertSt.setString(2, sd.getMstLab().getLabCode());
                insertSt.setBinaryStream(3, is, is.available());
                insertSt.setString(4, file.getFileExt());
                insertSt.setString(5, file.getUploadTime());
                insertSt.setString(6, file.getUploadedBy());
                insertSt.execute();
                file.setSaveStatus("1");
            }
            if (file.getSaveStatus().equals("1")) {
                md.setFileMsg("File Uploaded Successfully.");
                md.setFilemsgClass("text-success");
            }

        } catch (Exception ex) {
            md.setFileMsg("Failed to Upload File. Try Again Later.");
            md.setMsgClass("text-danger");
            MyLogger.logIt(ex, file.getUploadedBy());
        }
        return md;
    }

    public static boolean saveFile(Part file, String filePath, String fileName, String ext) {
        File createDirectory = new File(filePath);
        File fileFound = new File(filePath + fileName + ext);
        try (InputStream is = file.getInputStream();
                FileOutputStream fos = new FileOutputStream(filePath + fileName + ext);) {
            if (!createDirectory.exists()) {
                createDirectory.mkdir();
            }
            if (fileFound.exists()) {
                return true;
            }
            if (file.getSize() > 0) {
                int Readfile = 0;
                final byte[] readbyt = new byte[1024];
                while ((Readfile = is.read(readbyt)) != -1) {
                    fos.write(readbyt, 0, Readfile);
                }
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
            MyLogger.logIt(ex, "FileOperations.saveFile() ");
            return false;
        }

    }

    public static boolean deleteFile(String filePath, String fileName, String ext) {
        File filefound = new File(filePath + fileName + ext);
        try {
            if (filefound.exists()) {
                filefound.delete(); //delete file if already exist
                return true;
            } else {
                return false; // for empty file no need to upload the file
            }
        } catch (Exception ex) {
            MyLogger.logIt(ex, "FileOperations.deleteFile() ");
            return false;
        }
    }

    public static boolean insertUploadedTestDoc(String sampleId, Part file, String fileName, String ext, String userid) {
        boolean status;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("Select SAMPLE_ID from LAB_TEST_RESULT_DOC_INFO where SAMPLE_ID=?");
                InputStream is = file.getInputStream();) {
            pst.setString(1, sampleId);
            ResultSet res = pst.executeQuery();
            if (res.next()) {
                if (file.getSize() <= 0) {
                    status = true;
                } else {
                    try (PreparedStatement upstmt = con.prepareCall("UPDATE LAB_TEST_RESULT_DOC_INFO SET LAB_RESULT_DOC=?, DOC_TYPE=?, UPLOADED_DATETIME=?, UPLOADED_BY=? where SAMPLE_ID='?'");) {
                        upstmt.setBinaryStream(1, is, is.available());
                        upstmt.setString(2, ext);
                        upstmt.setString(3, format.format(new Date()));
                        upstmt.setString(4, userid);
                        upstmt.setString(5, sampleId);
                        upstmt.execute();
                    } catch (Exception ex) {
                        MyLogger.logIt(ex, "FileOperations.insertUploadedTestDoc()1 ");
                    }
                    status = true;
                }
            } else {
                if (file.getSize() <= 0) {
                    status = false; // for empty file no need to upload the file
                } else {
                    try (PreparedStatement ipstmt = con.prepareCall("INSERT INTO LAB_TEST_RESULT_DOC_INFO (SAMPLE_ID,LAB_RESULT_DOC,DOC_TYPE,UPLOADED_DATETIME,UPLOADED_BY) VALUES(?,?,?,?,? )");) {
                        ipstmt.setString(1, sampleId);
                        ipstmt.setBinaryStream(2, is, is.available());
                        ipstmt.setString(3, ext);
                        ipstmt.setString(4, format.format(new Date()));
                        ipstmt.setString(5, userid);
                        ipstmt.execute();
                    } catch (Exception ex) {
                        MyLogger.logIt(ex, "FileOperations.insertUploadedTestDoc()2 ");
                    }
                    status = true;
                }
            }
        } catch (Exception ex) {
            status = false;
            MyLogger.logIt(ex, "FileOperations.insertUploadedTestDoc()3 ");
        }
        return status;
    }

    public static MessageDetails deleteUploadedTestDoc(String smplId, String labCode) {
        MessageDetails md = new MessageDetails();
        md.setModalTitle("Sample and File Deletion Status.");
        try (Connection con = DatabaseConnectionFactory.createConnection();
                PreparedStatement pst = con.prepareStatement("DELETE FROM LAB_TEST_RESULT_DOC_INFO WHERE SAMPLE_ID=? AND LAB_CODE = ?");) {
            pst.setString(1, smplId);
            pst.setString(2, labCode);
            int i = pst.executeUpdate();
            if (i > 0) {
                md.setFilemsgClass("text-success");
                md.setFileMsg("File Removed Successfully");
                md.setStatus(true);
            }
        } catch (Exception ex) {
            md.setFilemsgClass("text-danger");
            md.setFileMsg("Error While removing File");
            md.setStatus(false);
            MyLogger.logIt(ex, "FileOperations.deleteUploadedTestDoc() ");
        }
        return md;
    }
    
    public static Boolean isSafe(Part file) {
        Boolean state = false;
        if(file.getSize() == 0){
            state = true;
        }else if(file.getContentType().equalsIgnoreCase("application/pdf")){
            try {
                PdfReader reader = new PdfReader(file.getInputStream());
                // Check 1:
                // Detect if the document contains any JavaScript code
                String jsCode = reader.getJavaScript();
                if (jsCode == null) {
                    // OK no JS code then when pass to check 2:
                    // Detect if the document has any embedded files
                    PdfDictionary root = reader.getCatalog();
                    PdfDictionary names = root.getAsDict(PdfName.NAMES);
                    PdfArray namesArray = null;
                    if (names != null) {
                        PdfDictionary embeddedFiles = names.getAsDict(PdfName.EMBEDDEDFILES);
                        namesArray = embeddedFiles.getAsArray(PdfName.NAMES);
                    }
                    state = ((namesArray == null) || namesArray.isEmpty());
                    // Get safe state from number of embedded files
                }
            }catch (Exception e) {
                MyLogger.logIt(e, "isSafe()");
            }
        }
        return state;
    }
}
