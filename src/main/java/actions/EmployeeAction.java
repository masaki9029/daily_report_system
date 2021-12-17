package actions;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import constants.AttributeConst;
import constants.ForwardConst;
import constants.JpaConst;
import constants.MessageConst;
import constants.PropertyConst;
import services.EmployeeService;


public class EmployeeAction extends ActionBase {

    private EmployeeService service;

    @Override
    public void process() throws ServletException, IOException {

        service = new EmployeeService();

        
        invoke();

        service.close();
    }

   
    public void index() throws ServletException, IOException {

        
        if (checkAdmin()) {

            int page = getPage();
            List<EmployeeView> employees = service.getPerPage(page);

           
            long employeeCount = service.countAll();

            putRequestScope(AttributeConst.EMPLOYEES, employees); 
            putRequestScope(AttributeConst.EMP_COUNT, employeeCount); 
            putRequestScope(AttributeConst.PAGE, page); 
            putRequestScope(AttributeConst.MAX_ROW, JpaConst.ROW_PER_PAGE);

          
            String flush = getSessionScope(AttributeConst.FLUSH);
            if (flush != null) {
                putRequestScope(AttributeConst.FLUSH, flush);
                removeSessionScope(AttributeConst.FLUSH);
            }

            
            forward(ForwardConst.FW_EMP_INDEX);

        }

    }

    
    public void entryNew() throws ServletException, IOException {

    
        if (checkAdmin()) { 
            putRequestScope(AttributeConst.TOKEN, getTokenId()); 
            putRequestScope(AttributeConst.EMPLOYEE, new EmployeeView()); 

            
            forward(ForwardConst.FW_EMP_NEW);
        } 
    }

  
    public void create() throws ServletException, IOException {

        
        if (checkAdmin() && checkToken()) { 

          
            EmployeeView ev = new EmployeeView(
                    null,
                    getRequestParam(AttributeConst.EMP_CODE),
                    getRequestParam(AttributeConst.EMP_NAME),
                    getRequestParam(AttributeConst.EMP_PASS),
                    toNumber(getRequestParam(AttributeConst.EMP_ADMIN_FLG)),
                    null,
                    null,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue());

            String pepper = getContextScope(PropertyConst.PEPPER);

          
            List<String> errors = service.create(ev, pepper);

            if (errors.size() > 0) {
               

                putRequestScope(AttributeConst.TOKEN, getTokenId()); 
                putRequestScope(AttributeConst.EMPLOYEE, ev); 
                putRequestScope(AttributeConst.ERR, errors); 

               
                forward(ForwardConst.FW_EMP_NEW);

            } else {
               
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_REGISTERED.getMessage());

               
                redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
            }

        }
    }

    
    public void show() throws ServletException, IOException {

      
        if (checkAdmin()) {

           
            EmployeeView ev = service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

            if (ev == null || ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {

            
                forward(ForwardConst.FW_ERR_UNKNOWN);
                return;
            }

            putRequestScope(AttributeConst.EMPLOYEE, ev); 

            forward(ForwardConst.FW_EMP_SHOW);
        }

    }

   
    public void edit() throws ServletException, IOException {

       
        if (checkAdmin()) { 

         
            EmployeeView ev = service.findOne(toNumber(getRequestParam(AttributeConst.EMP_ID)));

            if (ev == null || ev.getDeleteFlag() == AttributeConst.DEL_FLAG_TRUE.getIntegerValue()) {

                forward(ForwardConst.FW_ERR_UNKNOWN);
                return;
            }

            putRequestScope(AttributeConst.TOKEN, getTokenId()); 
            putRequestScope(AttributeConst.EMPLOYEE, ev); 

            
            forward(ForwardConst.FW_EMP_EDIT);

        } 
    }

    
    public void update() throws ServletException, IOException {

      
        if (checkAdmin() && checkToken()) { 
            EmployeeView ev = new EmployeeView(
                    toNumber(getRequestParam(AttributeConst.EMP_ID)),
                    getRequestParam(AttributeConst.EMP_CODE),
                    getRequestParam(AttributeConst.EMP_NAME),
                    getRequestParam(AttributeConst.EMP_PASS),
                    toNumber(getRequestParam(AttributeConst.EMP_ADMIN_FLG)),
                    null,
                    null,
                    AttributeConst.DEL_FLAG_FALSE.getIntegerValue());

            
            String pepper = getContextScope(PropertyConst.PEPPER);

           
            List<String> errors = service.update(ev, pepper);

            if (errors.size() > 0) {
              

                putRequestScope(AttributeConst.TOKEN, getTokenId()); 
                putRequestScope(AttributeConst.EMPLOYEE, ev); 
                putRequestScope(AttributeConst.ERR, errors); 

               
                forward(ForwardConst.FW_EMP_EDIT);
            } else {
                
                putSessionScope(AttributeConst.FLUSH, MessageConst.I_UPDATED.getMessage());

               
                redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
            }
        }
    }

   
    public void destroy() throws ServletException, IOException {

        
        if (checkAdmin() && checkToken()) { 
            service.destroy(toNumber(getRequestParam(AttributeConst.EMP_ID)));

            putSessionScope(AttributeConst.FLUSH, MessageConst.I_DELETED.getMessage());

            redirect(ForwardConst.ACT_EMP, ForwardConst.CMD_INDEX);
        }
    }

  
    private boolean checkAdmin() throws ServletException, IOException {

       
        EmployeeView ev = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        if (ev.getAdminFlag() != AttributeConst.ROLE_ADMIN.getIntegerValue()) {

            forward(ForwardConst.FW_ERR_UNKNOWN);
            return false;

        } else {

            return true;
        }

    }

}