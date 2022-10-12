package net.anotheria.maf;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionForward;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.bean.FormBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestCustomAction implements Action {

    @Override
    public ActionForward execute(ActionMapping mapping,
								// @Form(TestBackingBean.class)
								 FormBean form,
								 HttpServletRequest req,
                                 HttpServletResponse res) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void postProcess(ActionMapping mapping, HttpServletRequest req,
                            HttpServletResponse res) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void preProcess(ActionMapping mapping, HttpServletRequest req,
                           HttpServletResponse res) throws Exception {
        // TODO Auto-generated method stub

    }

}