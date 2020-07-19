package Validators;

import globals.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import operations.ApplicationSQLDate;
import org.apache.commons.lang3.ArrayUtils;
import viewModel.MessageDetails;
import viewModel.SampleDetails;

public class ServoTSEValidator {

    public static boolean createSample(HttpServletRequest request, MessageDetails md) {
        HttpSession session;
        User user;
        SampleDetails sd;
        if (request == null) {
            md.setModalTitle("Error While Processing Request.");
            md.setModalMessage("Unable to Process Request at the Moment.");
            md.setMsgClass("text-danger");
            return false;
        } else {
            session = request.getSession();
            if (session == null) {
                md.setModalTitle("Session timeout.");
                md.setModalMessage("Kindly Logout and Try again.");
                md.setMsgClass("text-danger");
                return false;
            } else {
                if (request.getParameter("labType").equals("1")) {
                    if (request.getParameter("csl_labCode") == null||request.getParameter("csl_labCode").equals("")) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Please Select CSL Lab.");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("csl_labAuthority") == null||request.getParameter("csl_labAuthority").equals("")) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Unable to fetch CSL Lab Authority. Contact WRO(IS)");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("csl_priorityId") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Unable to fetch CSL Lab Priority. Contact WRO(IS).");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("csl_qtyDrawn") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Unable to fetch CSL Lab Quantity. Contact WRO(IS).");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("csl_testIds") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Select Test'(s) from Test for CSL Lab.");
                        md.setMsgClass("text-danger");
                        return false;
                    }
                } else if (request.getParameter("labType").equals("2")) {
                    if (request.getParameter("rnd_labCode") == null||request.getParameter("rnd_labCode").equals("")) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Please Select RND Lab.");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("rnd_labAuthority") == null||request.getParameter("rnd_labAuthority").equals("")) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Unable to fetch RND Lab Authority. Contact WRO(IS)");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("rnd_qtyDrawn") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Unable to fetch RND Lab Quantity. Contact WRO(IS).");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("rnd_testIds") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Select Test'(s) from Test for RND Lab.");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("csl_priorityId") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Unable to fetch CSL Lab Priority. Contact WRO(IS).");
                        md.setMsgClass("text-danger");
                        return false;
                    }
                } else if (request.getParameter("labType").equals("3")) {
                    if (request.getParameter("rnd_bth_labCode") == null||request.getParameter("rnd_bth_labCode").equals("")) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Please Select RND Lab.");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("rnd_bth_labAuthority") == null||request.getParameter("rnd_bth_labAuthority").equals("")) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Unable to fetch RND Lab Authority. Contact WRO(IS)");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("rnd_qtyDrawn1") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Unable to fetch RND Lab Quantity. Contact WRO(IS).");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("rnd_bth_testIds") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Select Test'(s) from Test for RND Lab.");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("csl_bth_labCode") == null||request.getParameter("csl_bth_labCode").equals("")) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Please Select CSL Lab.");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("csl_bth_labAuthority") == null||request.getParameter("csl_bth_labAuthority").equals("")) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Unable to fetch CSL Lab Authority. Contact WRO(IS)");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("csl_qtyDrawn1") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Unable to fetch CSL Lab Quantity. Contact WRO(IS).");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("csl_bth_testIds") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Select Test'(s) from Test.");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("csl_priorityId") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Unable to fetch CSL Lab Priority. Contact WRO(IS).");
                        md.setMsgClass("text-danger");
                        return false;
                    }
                }

                user = (User) session.getAttribute("sUser");
                if (session.getAttribute("sUser") == null) {
                    md.setModalTitle("Error While Identifying Request.");
                    md.setModalMessage("Unable to Process Request at the Moment.");
                    md.setMsgClass("text-danger");
                    return false;
                }
                if (user.getRole_id().equals("1")) {
                    if (session.getAttribute("cs") == null) {
                        md.setModalTitle("Error While fetching Sample.");
                        md.setModalMessage("Sample could not be Processed at the Moment.");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("qtyDrawn") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Please Enter Quantity Drawn.");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("qtyDrawnDate") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Please Enter Drawn Date.");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("topupQty") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Please Enter Top Up Quantity.");
                        md.setMsgClass("text-danger");
                        return false;
                    } else if (request.getParameter("runningHrs") == null) {
                        md.setModalTitle("Error while processing Sample.");
                        md.setModalMessage("Please Enter Oil Running Hours.");
                        md.setMsgClass("text-danger");
                        return false;
                    } else {
                        sd = (SampleDetails) session.getAttribute("cs");
                        sd.setSamplecreatedBy(user.getsEMP_CODE());
                        sd.setQtyDrawn(request.getParameter("qtyDrawn"));
                        sd.setTopupQty(request.getParameter("topupQty"));
                        sd.setQtydrawnDate(ApplicationSQLDate.convertStringtoUtilDate(request.getParameter("qtyDrawnDate")));
                        sd.setRunningHrs(request.getParameter("runningHrs"));
                        sd.setCsl_labCode(request.getParameter("csl_labCode"));
                        sd.setRnd_labCode(request.getParameter("rnd_labCode"));
                        sd.setCsl_labAuthority(request.getParameter("csl_labAuthority"));
                        sd.setRnd_labAuthority(request.getParameter("rnd_labAuthority"));
                        sd.setSamplepriorityId(request.getParameter("csl_priorityId"));
//                      if(request.getParameter("labType").equals("3")){
//                          sd.setSamplepriorityId(request.getParameter("csl_bth_priorityId"));
//                      }
                        switch (request.getParameter("labType")) {
                            case "1":
                                sd.setCsl_qtyDrawn(request.getParameter("csl_qtyDrawn"));
                                if (request.getParameterValues("csl_addtestIds") != null) {
                                    if (request.getParameterValues("csl_addtestIds").length > 0) {
                                        sd.setCsl_testIds(ArrayUtils.addAll(request.getParameterValues("csl_testIds"),
                                                request.getParameterValues("csl_addtestIds")));
                                    }
                                }
                                if (request.getParameter("csl_addtestIds") == null) {
                                    sd.setCsl_testIds(request.getParameterValues("csl_testIds"));
                                }
                                break;
                            case "2":
                                sd.setRnd_qtyDrawn(request.getParameter("rnd_qtyDrawn"));
                                if (request.getParameterValues("rnd_addtestIds") != null) {
                                    if (request.getParameterValues("rnd_addtestIds").length > 0) {
                                        sd.setRnd_testIds(ArrayUtils.addAll(request.getParameterValues("rnd_testIds"),
                                                request.getParameterValues("rnd_addtestIds")));
                                    }
                                }
                                if (request.getParameter("rnd_addtestIds") == null) {
                                    sd.setRnd_testIds(request.getParameterValues("rnd_testIds"));
                                }
                                break;
                            case "3":
                                sd.setCsl_qtyDrawn(request.getParameter("csl_qtyDrawn1"));
                                sd.setRnd_qtyDrawn(request.getParameter("rnd_qtyDrawn1"));
                                if (request.getParameterValues("rnd_bth_addtestIds") != null) {
                                    if (request.getParameterValues("rnd_bth_addtestIds").length > 0) {
                                        sd.setRnd_testIds(ArrayUtils.addAll(request.getParameterValues("rnd_bth_testIds"),
                                                request.getParameterValues("rnd_bth_addtestIds")));
                                    }
                                }
                                if (request.getParameter("rnd_bth_addtestIds") == null) {
                                    sd.setRnd_testIds(request.getParameterValues("rnd_bth_testIds"));
                                }
                                if (request.getParameterValues("csl_bth_addtestIds") != null) {
                                    if (request.getParameterValues("csl_bth_addtestIds").length > 0) {
                                        sd.setCsl_testIds(ArrayUtils.addAll(request.getParameterValues("csl_bth_testIds"),
                                                request.getParameterValues("csl_bth_addtestIds")));
                                    }
                                }
                                if (request.getParameter("csl_bth_addtestIds") == null) {
                                    sd.setCsl_testIds(request.getParameterValues("csl_bth_testIds"));
                                }
                                break;
                        }
                        sd.setSampledrawnRemarks(request.getParameter("drawnRemarks"));
                        sd.setSamplepriorityRemarks(request.getParameter("priorityRemarks"));
                        return true;
                    }
                } else {
                    md.setModalTitle("You are not Authorised to Access this Page.");
                    md.setModalMessage("Unable to Process Request at the Moment.");
                    md.setMsgClass("text-danger");
                    return false;
                }
            }
        }
    }
}
