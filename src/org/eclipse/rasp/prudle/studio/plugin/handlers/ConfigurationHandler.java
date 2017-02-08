package org.eclipse.rasp.prudle.studio.plugin.handlers;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.rasp.prudle.studio.plugin.configuration.ConfigurationSettings;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class ConfigurationHandler implements IViewActionDelegate {

	private Shell shell;

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub

		ConfigurationSettings dialog = new ConfigurationSettings(shell);
		dialog.create();
		if (dialog.open() == Window.OK) {
			// System.out.println(dialog.getFirstName());
			// System.out.println(dialog.getLastName());
		}

	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IViewPart arg0) {
		// TODO Auto-generated method stub

	}

}
