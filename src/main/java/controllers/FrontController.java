package controllers;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import actions.ActionBase;
import actions.UnknownAction;
import constants.ForwardConst;

@WebServlet("/")
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FrontController() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ActionBase action=getAction(request,response);
        action.init(getServletContext(),request,response);
        action.process();

    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        doGet(request, response);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" }) //コンパイラ警告を抑制
    private ActionBase getAction(HttpServletRequest request, HttpServletResponse response) {
        Class type = null;
        ActionBase action = null;
        try {

            //リクエストからパラメータ"action"の値を取得 (例:"Employee"、"Report")
            String actionString = request.getParameter(ForwardConst.ACT.getValue());

            //該当するActionオブジェクトを作成 (例:リクエストからパラメータ action=Employee の場合、actions.EmployeeActionオブジェクト)
            type = Class.forName(String.format("actions.%sAction", actionString));

            //ActionBaseのオブジェクトにキャスト(例:actions.EmployeeActionオブジェクト→actions.ActionBaseオブジェクト)
            action = (ActionBase) (type.asSubclass(ActionBase.class)
                    .getDeclaredConstructor()
                    .newInstance());

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SecurityException
                | IllegalArgumentException | InvocationTargetException| NoSuchMethodException e) {

            //リクエストパラメータに設定されている"action"の値が不正の場合(例:action=xxxxx 等、該当するActionクラスがない場合)
            //エラー処理を行うActionオブジェクトを作成
            action = new UnknownAction();
        }
        return action;
    }


}
