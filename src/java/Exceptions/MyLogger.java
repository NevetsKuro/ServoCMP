package Exceptions;

import initializeSesion.initializeValues;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import operations.ErrorLogger;
import viewModel.MessageDetails;

public class MyLogger {

    static Logger logger = Logger.getLogger(Logger.class.getName());

    public static MessageDetails logIt(Exception ex, String updatedBy) {
        MessageDetails md = new MessageDetails();
        try {
            md.setMsgClass("text-danger");
            System.out.println("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage());
            LogManager.getLogManager().readConfiguration(MyLogger.class.getResourceAsStream("log.properties"));
            logger.setLevel(Level.FINE);
            Handler fileHandler = new FileHandler(initializeValues.getServerLogsHost(), true);
            //Handler fileHandler = new FileHandler("D:/ServoCMP.log", true);
            fileHandler.setFormatter(new LogFormatter());
            logger.addHandler(fileHandler);
            logger.log(Level.INFO, "Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), initializeValues.getServerLogsHost());
            fileHandler.close();
            md.setModalMessage(ErrorLogger.getErrorCode("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), updatedBy));
        } catch (SecurityException | IOException exLog) {
            System.out.println("Exception While logging remotely " + exLog.getStackTrace()[0].getClassName() + "." + exLog.getStackTrace()[0].getMethodName() + " " + exLog.getMessage());
            logger.log(Level.INFO, "Exception While logging remotely " + exLog.getStackTrace()[0].getClassName() + "." + exLog.getStackTrace()[0].getMethodName() + " " + exLog.getMessage());
        }
        return md;
    }
    public static MessageDetails logIt2(String text, String updatedBy) {
        MessageDetails md = new MessageDetails();
        try {
            md.setMsgClass("text-danger");
//            System.out.println("Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage());
            LogManager.getLogManager().readConfiguration(MyLogger.class.getResourceAsStream("log.properties"));
            logger.setLevel(Level.FINE);
            Handler fileHandler = new FileHandler(initializeValues.getServerLogsHost(), true);
            //Handler fileHandler = new FileHandler("D:/ServoCMP.log", true);
            fileHandler.setFormatter(new LogFormatter());
            logger.addHandler(fileHandler);
//            logger.log(Level.INFO, "Exception in " + ex.getStackTrace()[0].getClassName() + "." + ex.getStackTrace()[0].getMethodName() + " " + ex.getMessage(), initializeValues.getServerLogsHost());
            logger.log(Level.INFO, text);
            fileHandler.close();
            md.setModalMessage(ErrorLogger.getErrorCode("Exception in","Me"));
        } catch (SecurityException | IOException exLog) {
            System.out.println("Exception While logging remotely " + exLog.getStackTrace()[0].getClassName() + "." + exLog.getStackTrace()[0].getMethodName() + " " + exLog.getMessage());
            logger.log(Level.INFO, "Exception While logging remotely " + exLog.getStackTrace()[0].getClassName() + "." + exLog.getStackTrace()[0].getMethodName() + " " + exLog.getMessage());
        }
        return md;
    }
}
