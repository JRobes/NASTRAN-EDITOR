package es.robes.editors.nastran.lifecycle;

import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ElementMatcher;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;


public class AppStartupCompleteEventHandler implements EventHandler {
    private MWindow theWindow;
	private MApplication application2;
	@Inject
	IWorkbench wb;
	//private IWorkbench myWorkBench;
	
    //private IWindowCloseHandler closeHandler;
   // @Inject private MApplication app;

    AppStartupCompleteEventHandler(MWindow window, EModelService modelService, MApplication application)
    {
      theWindow = window;
      
      //closeHandler = new NastranEditorWindowCloseHandler(app, modelService);
      System.out.println("MODEL SERVICE    :   "+ application.toString());
      MWindow mw = application.getChildren().get(0);
      application2 = application;
     // myWorkBench = workbench;
    }

	@Override
	public void handleEvent(Event event) {
	      theWindow.getContext().set(IWindowCloseHandler.class, new IWindowCloseHandler() {

			@Override
			public boolean close(MWindow window) {
				System.out.println("SE CERRÓ CORRECTAMENTE...");
				EModelService modelService2 = application2.getContext().get(EModelService.class);
				Collection<EPartService> allPartServices = getAllPartServices(application2);
				
				if (containsDirtyParts(allPartServices)) {
					System.out.println("TIENE DIRTY PARTS...");
					return true;
				}
				else {
					Shell shell = (Shell) window.getWidget();
					if (MessageDialog.openConfirm(shell, "Close Application", "Do you really want to close the entire application?")) {
						return wb.close();
						//return true;
					}
				}
				return false;
			}});        

	}

	private static Collection<EPartService> getAllPartServices(MApplication application) {
		List<EPartService> partServices = new ArrayList<EPartService>();

		EModelService modelService = application.getContext().get(EModelService.class);
		List<MWindow> elements = modelService.findElements(application, MWindow.class, EModelService.IN_ACTIVE_PERSPECTIVE,
				new ElementMatcher(null, MWindow.class, (List<String>) null));
		for (MWindow w : elements) {
			if (w.isVisible() && w.isToBeRendered()) {
				EPartService partService = w.getContext().get(EPartService.class);
				if (partService != null) {
					partServices.add(partService);
				}
			}
		}

		return partServices;
	}
	private static boolean containsDirtyParts(Collection<EPartService> partServices) {
		for (EPartService partService : partServices) {
			if (!partService.getDirtyParts().isEmpty())
				return true;
		}

		return false;
	}

}
