package es.robes.editors.nastran.lifecycle;

import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

public class AppStartupCompleteEventHandler implements EventHandler {
    private MWindow theWindow;
    private WindowCloseHandler closeHandler;

    AppStartupCompleteEventHandler(MWindow window)
    {
      theWindow = window;
      closeHandler=new WindowCloseHandler();

    }

	@Override
	public void handleEvent(Event event) {
	      theWindow.getContext().set(IWindowCloseHandler.class, closeHandler);        

	}
	
    private static class WindowCloseHandler implements IWindowCloseHandler{

  	    @Override
  	    public boolean close(MWindow window) {
  	        // TODO Auto-generated method stub
  	        Shell shell = new Shell();

  	        if (MessageDialog.openConfirm(shell, "Confirmation",
  	                "Do you want to exit?")) {
  	            return true;
  	        }
  	        return false;
  	    } 
     }

}
