package net.anotheria.maf.builtin;

import net.anotheria.maf.MAFExecutionContext;
import net.anotheria.maf.action.AbstractAction;
import net.anotheria.maf.action.ActionCommand;
import net.anotheria.maf.action.ActionMapping;
import net.anotheria.maf.action.ActionMappings;
import net.anotheria.util.xml.XMLAttribute;
import net.anotheria.util.xml.XMLNode;
import net.anotheria.util.xml.XMLTree;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Show configured mappings in this ano-maf instance.
 */
public class ShowMappingsAction extends AbstractAction{

	@Override
	public ActionCommand execute(ActionMapping mapping,
			HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		res.setContentType("text/xml");
		OutputStreamWriter out = new OutputStreamWriter(res.getOutputStream(), Charset.forName("UTF-8"));
		
		XMLTree tree = new XMLTree();
		
		XMLNode root = new XMLNode("mappings");
		XMLNode aliases = new XMLNode("aliases");
		ActionMappings actionMappings = MAFExecutionContext.currentExecutionContext().getMappings();
		Map<String,String> aliasesMap = actionMappings.getAliases();
		for (Map.Entry<String, String> e : aliasesMap.entrySet()){
			XMLNode a = new XMLNode("alias");
			a.addAttribute(new XMLAttribute("source", e.getKey()));
			a.addAttribute(new XMLAttribute("target", e.getValue()));
			aliases.addChildNode(a);
		}
		
		XMLNode mappings = new XMLNode("actions");
		Map<String,ActionMapping> mappingsMap = actionMappings.getMappings();
		for (Map.Entry<String, ActionMapping> e : mappingsMap.entrySet()){
			XMLNode m = new XMLNode("action");
			m.addAttribute(new XMLAttribute("path", e.getValue().getPath()));
			m.addAttribute(new XMLAttribute("type", e.getValue().getType()));
			Map<String, ActionCommand> commands = e.getValue().getCommands();
			for (Map.Entry<String,ActionCommand> c : commands.entrySet()){
				XMLNode command = new XMLNode("command");
				command.addAttribute(new XMLAttribute("type", c.getValue().getClass().getSimpleName()));
				command.setContent(c.getValue().toString());
				m.addChildNode(command);
			}
			mappings.addChildNode(m);
		}

		root.addChildNode(aliases);
		root.addChildNode(mappings);
		tree.setRoot(root);
		tree.write(out);
		out.flush();
		out.close();
		return null;
		
	}
}
