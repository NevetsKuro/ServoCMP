package operations;

import Exceptions.MyLogger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ApplicationSQLDate {

    public static String subtractDate(java.util.Date nxtsampleDate, int daystoSubtract) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nxtsampleDate);
        calendar.add(calendar.DATE, daystoSubtract);
        return convertUtilDatetoString(calendar.getTime());
    }

    public static String addDate(java.util.Date nxtsampleDate, int daystoAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nxtsampleDate);
        calendar.add(calendar.DATE, daystoAdd);
        return convertUtilDatetoString(calendar.getTime());
    }

    public static String convertUtilDatetoString(java.util.Date utilDate) {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
        return dateformat.format(utilDate);
    }

    public static java.util.Date convertSqltoUtilDate(java.sql.Date sqlDate) {
        java.util.Date utilDate = new Date(sqlDate.getTime());
        return utilDate;
    }

    public static java.sql.Date getnextSampleSQLDate(Date oldNxtdate, String sampleFreq) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(oldNxtdate);
        calendar.add(calendar.MONTH, Integer.parseInt(sampleFreq));
        return new java.sql.Date(calendar.getTimeInMillis());
    }

    public static String getfutureDate(String daystoExtend, Date nxtSampleDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(nxtSampleDate);
        calendar.add(calendar.DATE, Integer.parseInt(daystoExtend));
        return convertUtilDatetoString(calendar.getTime());
    }

    public static java.sql.Date convertUtiltoSqlDate(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

    public static java.util.Date convertStringtoUtilDate(String datetoConvert) {
        Date convertedDate = null;
        try {
            convertedDate = new SimpleDateFormat("dd-MM-yyyy").parse(datetoConvert);
        } catch (ParseException ex) {
            MyLogger.logIt(ex, "convertStringtoUtilDate()");
            MyLogger.logIt(ex, "ApplicationSQLDate.convertStringtoUtilDate() ");
        }
        return convertedDate;
    }

    public static java.sql.Date getcurrentSQLDate() {
        return new java.sql.Date(Calendar.getInstance().getTimeInMillis());
    }
}
