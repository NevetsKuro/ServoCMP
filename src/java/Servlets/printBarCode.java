package Servlets;

import Exceptions.MyLogger;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfWriter;

public class printBarCode extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Document document = new Document(new Rectangle(PageSize.A4));
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("d:/BarCode_128.pdf"));
            document.open();
            document.add(new Paragraph("Sample Details"));
            Barcode128 code128 = new Barcode128();
            code128.setGenerateChecksum(true);
            code128.setCode("1234554321-1234");
            document.add(code128.createImageWithBarcode(writer.getDirectContent(), null, null));
            document.close();
        } catch (Exception ex) {
            MyLogger.logIt(ex, "printBarCode.processRequest() ");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
