package es.robes.editors.nastran.lifecycle;

import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;


public class AppStartupCompleteEventHandler implements EventHandler {
    private MWindow theWindow;
	private MApplication application2;
    //private IWindowCloseHandler closeHandler;
   // @Inject private MApplication app;

    AppStartupCompleteEventHandler(MWindow window, EModelService modelService, MApplication application)
    {
      theWindow = window;
      //closeHandler = new NastranEditorWindowCloseHandler(app, modelService);
      System.out.println("MODEL SERVICE    :   "+ application.toString());
      MWindow mw = application.getChildren().get(0);
      application2 = application;
    }

	@Override
	public void handleEvent(Event event) {
	      theWindow.getContext().set(IWindowCloseHandler.class, new IWindowCloseHandler() {

			@Override
			public boolean close(MWindow window) {
				EModelService modelService2 = application2.getContext().get(EModelService.class);
				return true;
			}});        

	}
	/*
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
     */

}
