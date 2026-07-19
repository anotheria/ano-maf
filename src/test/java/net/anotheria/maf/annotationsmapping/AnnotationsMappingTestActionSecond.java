package net.anotheria.maf.annotationsmapping;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.annotation.ActionAnnotation;
import net.anotheria.maf.annotation.ActionsAnnotation;
import net.anotheria.maf.annotation.CommandForwardAnnotation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Kirill Reviakin
 */
@ActionsAnnotation(maps = {
        @ActionAnnotation(path = "/secondactionfirstpath", forwards = {@CommandForwardAnnotation(name = "secondactionfirstforwardname", path = "")}),
        @ActionAnnotation(path = "/secondactionsecondpath", forwards = {@CommandForwardAnnotation(name = "secondactionsecondforwardname", path = "")})
})
public class AnnotationsMappingTestActionSecond implements Action {
    @Override
    public void preProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
    }

    @Override
    public ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
        return null;
    }

    @Override
    public void postProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception {
    }
}
