package es.robes.editors.nastran.lifecycle;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

@SuppressWarnings("restriction")
public class LifeCycleManager {
	 @ProcessRemovals
     void postContextCreate(){
		 System.out.println(">>>>>>>>>>>>>>>>>>>>><<<<T CONSTRUCT DENTRO DEL CHECKEADOR DE ACTIVE PARTS");
		 //CheckForActivePart CAP = new CheckForActivePart();
		 
	 }
	 
	  @ProcessAdditions
	  public void processAdditions(IEventBroker eventBroker, MApplication app, EModelService modelService)
	  {
	     MWindow window =(MWindow)modelService.find("test-base-plugin.trimmedwindow", app);

	     eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE, 
	                          new AppStartupCompleteEventHandler(window, modelService, app));
	     
	  }


	 
}
