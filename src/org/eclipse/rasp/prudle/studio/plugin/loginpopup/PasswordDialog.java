package org.eclipse.rasp.prudle.studio.plugin.loginpopup;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PasswordDialog extends Dialog {

	private Text txtUser = null;
	private Text txtPassword = null;
	private String userID = null;
	private String password = null;
	private Shell shell;
	private Composite container;

	public PasswordDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);

		newShell.setText("Login");
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		container = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginRight = 5;
		layout.marginLeft = 10;
		container.setLayout(layout);

		Label lblUser = new Label(container, SWT.NONE);
		lblUser.setText("User Name:");

		txtUser = new Text(container, SWT.BORDER);
		txtUser.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtUser.setText("");
		/**
		 * txtUser.addModifyListener(new ModifyListener() {
		 * 
		 * @Override public void modifyText(ModifyEvent e) { Text textWidget =
		 *           (Text) e.getSource(); String userText =
		 *           textWidget.getText(); user = userText; } });
		 */

		Label lblPassword = new Label(container, SWT.NONE);
		GridData gd_lblNewLabel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewLabel.horizontalIndent = 1;
		lblPassword.setLayoutData(gd_lblNewLabel);
		lblPassword.setText("Password:");

		txtPassword = new Text(container, SWT.BORDER | SWT.PASSWORD);
		txtPassword.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		// txtPassword.setText(password);
		/**
		 * txtPassword.addModifyListener(new ModifyListener() {
		 * 
		 * @Override public void modifyText(ModifyEvent e) { Text textWidget =
		 *           (Text) e.getSource(); String passwordText =
		 *           textWidget.getText(); password = passwordText; } });
		 */
		return container;
	}

	// override method to use "Login" as label for the OK button
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, "Login", true);
		createButton(parent, IDialogConstants.CANCEL_ID, "Cancel", true).addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				parent.dispose();
				return;
			}
		});
	}

	@Override
	protected Point getInitialSize() {
		return new Point(300, 150);
	}

	@Override
	protected void okPressed() {

		String userIdResponse = null;
		userID = txtUser.getText();
		System.out.println(userID);
		// password = txtPassword.getText();
		if (userID.equals("")) {
			MessageDialog.openInformation(shell, "Login", "Please enter user id");
			return;
		}

		HttpURLLoginConnection req = new HttpURLLoginConnection();
		try {
			userIdResponse = req.sendGet(userID);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			MessageDialog.openInformation(shell, "Success", "Please enter valid user id");
			return;
		}

		if (userIdResponse.equals(userID)) {
			ReadWriteLoginCredentials obj = new ReadWriteLoginCredentials();
			super.okPressed();
			MessageDialog.openInformation(shell, "Success", "Welcome to Prudle Studio");
			obj.writeEncryptedText(userID.trim());
		}

	}

	/**
	 * public String getUser() { return user; }
	 * 
	 * public void setUser(String user) { this.user = user; }
	 * 
	 * public String getPassword() { return password; }
	 * 
	 * public void setPassword(String password) { this.password = password; }
	 */

}
