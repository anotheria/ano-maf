package net.anotheria.maf.annotationsmapping;

import net.anotheria.maf.action.Action;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.annotation.ActionAnnotation;
import net.anotheria.maf.annotation.CommandForwardAnnotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Kirill Reviakin
 */
@ActionAnnotation(path = "/firstactionpath",
        forwards = {
                @CommandForwardAnnotation(name = "firstactionfirstforwardname", path = ""),
                @CommandForwardAnnotation(name = "firstactionsecondforwardname", path = "")
        })
public class AnnotationsMappingTestActionFirst implements Action {
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
