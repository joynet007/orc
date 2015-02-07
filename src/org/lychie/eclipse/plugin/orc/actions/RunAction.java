package org.lychie.eclipse.plugin.orc.actions;

import org.eclipse.jface.action.IAction;
import org.lychie.eclipse.plugin.orc.util.Task;

/**
 * RunAction
 * 
 * @author Lychie Fan
 */
public class RunAction extends ActionSuper {

	@Override
	public void run(IAction action) {
		try {
			if (configFileExists()) {
				Task.startup();
				refreshProject();
			}
		} catch (Throwable e) {
			openerror(e.getMessage());
		}
	}

}