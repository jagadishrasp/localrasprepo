package org.eclipse.rasp.prudle.studio.plugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class ConvertHandler extends AbstractHandler {
        

        @Override
        public Object execute(ExecutionEvent event) throws ExecutionException {
    		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
    		MessageDialog.openInformation(
    				window.getShell(),
    				"Plugin",
    				"Hello, Eclipse Prudle Studio Action");
    		return null ;
        }

}
