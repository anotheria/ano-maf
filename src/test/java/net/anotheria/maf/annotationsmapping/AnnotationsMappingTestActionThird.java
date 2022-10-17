package net.anotheria.maf.annotationsmapping;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.annotation.ActionAnnotation;
import net.anotheria.maf.annotation.ActionsAnnotation;
import net.anotheria.maf.annotation.CommandForwardAnnotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kirill Reviakin
 */
@ActionsAnnotation(maps = {
        @ActionAnnotation(context = "/test", path = "/thirdactionfirstpath", forwards = {@CommandForwardAnnotation(name = "thirdactionfirstforwardname", path = "")}),
        @ActionAnnotation(context = "/test", path = "/thirdactionsecondpath", forwards = {@CommandForwardAnnotation(name = "thirdactionsecondforwardname", path = "")})
})
public class AnnotationsMappingTestActionThird implements Action {
    @Override
    public void preProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
    }

    @Override
    public ActionCommand execute(ActionMapping mapping,  HttpServletRequest req, HttpServletResponse res) throws Exception {
        return null;
    }

    @Override
    public void postProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
    }
}
