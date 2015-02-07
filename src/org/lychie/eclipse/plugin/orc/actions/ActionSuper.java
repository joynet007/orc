package org.lychie.eclipse.plugin.orc.actions;

import java.io.File;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.lychie.eclipse.plugin.orc.util.Configurations;
import org.lychie.eclipse.plugin.orc.util.Project;
import org.lychie.jutil.FileUtil;

/**
 * ActionSuper
 * 
 * @author Lychie Fan
 */
public abstract class ActionSuper implements IWorkbenchWindowActionDelegate {

	protected String basepath;
	protected String pathname;
	protected IProject iproject;
	protected IWorkbenchWindow window;

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot workspaceRoot = workspace.getRoot();
		IPath path = workspaceRoot.getLocation();
		basepath = FileUtil.getUniformPath(path.toOSString()) + File.separator;
	}
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		try {
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object firstElement = structuredSelection.getFirstElement();
			if (firstElement instanceof IAdaptable) {
				IAdaptable adaptable = (IAdaptable) firstElement;
				IProject project = (IProject) adaptable.getAdapter(IProject.class);
				IPath path = project.getFullPath();
				pathname = basepath + path.toOSString().substring(1);
				iproject = project;
				Project.setPath(pathname);
			}
		} catch (Throwable e) {}
	}

	@Override
	public void dispose() {
		
	}
	
	protected void refreshProject() {
		try {
			iproject.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	protected File getConfigFile() {
		return new File(pathname, Configurations.CONFIG_FILE_NAME);
	}
	
	protected boolean configFileExists() {
		return getConfigFile().exists();
	}

	protected void openerror(String message) {
		MessageDialog.openError(window.getShell(), "Error", message);
	}

}