package org.lychie.eclipse.plugin.orc.actions;

import java.io.File;
import org.eclipse.jface.action.IAction;
import org.lychie.eclipse.plugin.orc.res.model.Configuration;
import org.lychie.eclipse.plugin.orc.ui.Guidance;
import org.lychie.eclipse.plugin.orc.ui.Guidance.OnOKClick;
import org.lychie.eclipse.plugin.orc.util.Configurations;

/**
 * ConfigureAction
 * 
 * @author Lychie Fan
 */
public class ConfigureAction extends ActionSuper {

	@Override
	public void run(IAction action) {
		try {
			showGuidance();
		} catch (Throwable e) {
			openerror(e.getMessage());
		}
	}
	
	private void showGuidance() throws Throwable {
		Guidance.show(window.getShell(), getConfiguration(), new OnOKClick() {
			@Override
			public void okClick(Configuration config) {
				Configurations.createDuplicate(pathname, config);
			}
		});
	}

	private Configuration getConfiguration() {
		File configFile = getConfigFile();
		Configuration configuration = null;
		if (configFile.exists()) {
			configuration = Configurations.parseConfiguration(configFile);
		}
		return configuration;
	}

}