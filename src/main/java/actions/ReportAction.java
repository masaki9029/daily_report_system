package actions;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.ReportView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import services.ReportService;


public class ReportAction extends ActionBase {

    private ReportService service;


    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService();


        invoke();
        service.close();
    }


    public void index() throws ServletException, IOException {


        int page = getPage();
        List<ReportView> reports = service.getAllPerPage(page);


        long reportsCount = service.countAll();

        putRequestScope(AttributeConst.REPORTS, reports);
        putRequestScope(AttributeConst.REP_COUNT, reportsCount);
        putRequestScope(AttributeConst.PAGE, page);
        putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);

        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }


        forward(ForwardConst.FW_REP_INDEX);
    }
    public void entryNew() throws ServletException, IOException {

        putRequestScope(AttributeConst.TOKEN, getTokenId());


        ReportView rv = new ReportView();
        rv.setReportDate(LocalDate.now());
        putRequestScope(AttributeConst.REPORT, rv);

        forward(ForwardConst.FW_REP_NEW);

    }
    public void create() throws ServletException, IOException {

        if (checkToken()) {

            LocalDate day = null;
            if (getRequestParam(AttributeConst.REP_DATE) == null
                    || getRequestParam(AttributeConst.REP_DATE).equals("")) {
                day = LocalDate.now();
            } else {
                day = LocalDate.parse(getRequestParam(AttributeConst.REP_DATE));
            }

            EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

            ReportView rv = new ReportView(
                    null,
                    ev,
                    day,
                    getRequestParam(AttributeConst.REP_TITLE),
                    getRequestParam(AttributeConst.REP_CONTENT),
                    null,
                    null);


            List<String> errors = service.create(rv);

            if (errors.size() > 0) {


                putRequestScope(AttributeConst.TOKEN, getTokenId());
                putRequestScope(AttributeConst.REPORT, rv);
                putRequestScope(AttributeConst.ERR, errors);


                forward(ForwardConst.FW_REP_NEW);

            } else {

                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());


                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);
            }
        }
    }

    public void show() throws ServletException, IOException {


        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

        if (rv == null) {
            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.REPORT, rv);

            forward(ForwardConst.FW_REP_SHOW);
        }
    }

    public void edit() throws ServletException, IOException {

        ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));


        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (rv == null || ev.getId() != rv.getEmployee().getId()) {

            forward(ForwardConst.FW_ERR_UNKNOWN);

        } else {

            putRequestScope(AttributeConst.TOKEN, getTokenId());
            putRequestScope(AttributeConst.REPORT, rv);

            forward(ForwardConst.FW_REP_EDIT);
        }

    }
    public void update() throws ServletException, IOException {


        if (checkToken()) {


            ReportView rv = service.findOne(toNumber(getRequestParam(AttributeConst.REP_ID)));

            rv.setReportDate(toLocalDate(getRequestParam(AttributeConst.REP_DATE)));
            rv.setTitle(getRequestParam(AttributeConst.REP_TITLE));
            rv.setContent(getRequestParam(AttributeConst.REP_CONTENT));

            List<String> errors = service.update(rv);

            if (errors.size() > 0) {


                putRequestScope(AttributeConst.TOKEN, getTokenId());
                putRequestScope(AttributeConst.REPORT, rv);
                putRequestScope(AttributeConst.ERR, errors);

                forward(ForwardConst.FW_REP_EDIT);
            } else {

                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

                redirect(ForwardConst.ACT_REP, ForwardConst.CMD_INDEX);

            }
        }
    }


}