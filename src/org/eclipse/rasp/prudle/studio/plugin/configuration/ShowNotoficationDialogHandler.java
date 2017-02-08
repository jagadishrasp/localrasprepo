package org.eclipse.rasp.prudle.studio.plugin.configuration;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

public class ShowNotoficationDialogHandler {
	@Execute
	public void execute(final Shell shell, String message) {
		// customized MessageDialog with configured buttons
		MessageDialog dialog = new MessageDialog(shell, "Prudle Studio", null, message, MessageDialog.INFORMATION,
				new String[] { "OK" }, 0);
		int result = dialog.open();
		System.out.println(result);
	}

	/**
	 * private static MultiStatus createMultiStatus(String msg, Throwable t) {
	 * 
	 * List<Status> childStatuses = new ArrayList<>(); StackTraceElement[]
	 * stackTraces = Thread.currentThread().getStackTrace();
	 * 
	 * for (StackTraceElement stackTrace: stackTraces) { Status status = new
	 * Status(IStatus.ERROR, "com.example.e4.rcp.todo", 0,
	 * stackTrace.toString(), t); childStatuses.add(status); }
	 * 
	 * MultiStatus ms = new MultiStatus("com.example.e4.rcp.todo",
	 * IStatus.ERROR, childStatuses.toArray(new Status[] {}), t.toString(), t);
	 * return ms; }
	 */
}
