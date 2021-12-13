package actions;

import java.io.IOException;

import javax.servlet.ServletException;

import constants.ForwardConst;

public class UnknownAction extends ActionBase {
    @Override
    public void process()throws ServletException,IOException{
        forward(ForwardConst.FW_ERR_UNKNOWN);
    }


    }


