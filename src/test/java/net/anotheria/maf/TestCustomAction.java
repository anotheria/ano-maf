package net.anotheria.maf;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestCustomAction implements Action {

    @Override
    public ActionCommand execute(ActionMapping mapping,
                                 // @Form(TestBackingBean.class)
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