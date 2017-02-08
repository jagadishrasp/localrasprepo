package org.eclipse.rasp.prudle.studio.plugin.handlers;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.rasp.prudle.studio.plugin.loginpopup.PasswordDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class EnterCredentialsHandler implements IViewActionDelegate {
	
	private Shell shell ; 

	@Override
	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		

	            PasswordDialog dialog = new PasswordDialog(shell);

	            // get the new values from the dialog
	            if (dialog.open() == Window.OK) {
	                /**    
	            	String user = dialog.getUser();
	                    String pw = dialog.getPassword();
	                    System.out.println(user);
	                    System.out.println(pw);*/
	            
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
