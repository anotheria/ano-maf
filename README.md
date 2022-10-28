[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.anotheria/ano-maf/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.anotheria/ano-maf)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)


ano-maf
=======

MicroActionFramework

This MicroActionFramework is stripped to the max filter which some action mapping (something like a more lightweight struts 1). 
The idea is to have very lightweight webui for integration in other projects (embedded management console).
Used by MoSKito among others.


An action as defined by ano-maf is a simple interface:

```
public interface Action {
	/**
	 * Called by the framework prior to call to execute. Useful for action hierarchies to put common activities (authorisation checks etc) into classes higher in the class hierarchy.
	 * @param mapping action mapping
	 * @param req http request
	 * @param res http response
	 * @throws Exception any exception
	 */
	void preProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception;
	/**
	 * Called by the framework. This is the method where you implement controller-logic (mvc) in your action.  
	 * @param mapping action mapping
	 * @param req http request
	 * @param res http response
	 * @return a forward to another action or jsp for view rendering.
	 * @throws Exception any exception
	 */
	ActionCommand execute(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception;
	/**
	 * Called by the framework after call to the execute.
	 * @param mapping action mapping
	 * @param req http request
	 * @param res http response
	 * @throws Exception any exception
	 */
	void postProcess(ActionMapping mapping, HttpServletRequest req, HttpServletResponse res) throws Exception; 
```
