package es.robes.editors.nastran.lifecycle;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ElementMatcher;
import org.eclipse.e4.ui.workbench.modeling.ISaveHandler;
import org.eclipse.e4.ui.workbench.modeling.ISaveHandler.Save;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MHandler;


public class AppStartupCompleteEventHandler implements EventHandler {
    private MWindow theWindow;
	private MApplication application2;
	private static ISaveHandler saveHandler;
	//private EPartService partService;
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
     wb = application2.getContext().get(IWorkbench.class);
     // myWorkBench = workbench;
    
     Shell shell = (Shell) window.getWidget();
    }

	@Override
	public void handleEvent(Event event) {
		
		theWindow.getContext().set(ISaveHandler.class, new ISaveHandler() {

			@Override
			public boolean save(MPart dirtyPart, boolean confirm) {
				System.out.println("PARTE PARA SALVAR..." + dirtyPart.getLabel());

					EPartService partService = dirtyPart.getContext().get(EPartService.class);
					//partService.hidePart(dirtyPart, true);
					partService.hidePart(dirtyPart,true);
					
					//return partService.savePart(dirtyPart, confirm);
				return true;
				
<<<<<<< HEAD

=======
				EPartService partService = dirtyPart.getContext().get(EPartService.class);
				System.out.println("PART-SERVICE...???????"+partService.toString());

				return partService.savePart(dirtyPart, confirm);

				//return true;
>>>>>>> branch 'master' of https://github.com/JRobes/NASTRAN-EDITOR.git
			}

			@Override
			public boolean saveParts(Collection<MPart> dirtyParts, boolean confirm) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Save promptToSave(MPart dirtyPart) {
				
				return promptToSaveDialog(dirtyPart);
			}

			@Override
			public Save[] promptToSave(Collection<MPart> dirtyParts) {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
		
		
	      saveHandler  = (ISaveHandler)theWindow.getContext().get(ISaveHandler.class);
	      
	      theWindow.getContext().set(IWindowCloseHandler.class, new IWindowCloseHandler() {

			@Override
			public boolean close(MWindow window) {
				System.out.println("SE CERRO CORRECTAMENTE...");
				List<MHandler> listHandlers = window.getHandlers();
				System.out.println(listHandlers.size());
				Shell shell = (Shell) window.getWidget();
				if (MessageDialog.openConfirm(shell, "Close Nastran Editor", "Do you really want to close the entire application?")) {
					Collection<EPartService> allPartServices = getAllPartServices(application2);
					if (containsDirtyParts(allPartServices)) {
						System.out.println("TIENE DIRTY PARTS...");
						
						return iterateOverDirtyParts( allPartServices);			
					}
					else {
						System.out.println("NO TIENE DIRTY PARTS...");
						return true;
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
		System.out.println("Numero de part services ...\t"+ partServices.size());
		return partServices;
	}
	private static boolean containsDirtyParts(Collection<EPartService> partServices) {
		for (EPartService partService : partServices) {
			if (!partService.getDirtyParts().isEmpty())
				return true;
		}

		return false;
	}
	private static boolean iterateOverDirtyParts(Collection<EPartService> allPartServices) {
		for (EPartService partService : allPartServices) {
			Collection<MPart> dirtyParts = partService.getDirtyParts();
			
			for(MPart dirtyPart : dirtyParts) {
				
				System.out.println("DIRTY PART...dirty part");
				switch(saveHandler.promptToSave(dirtyPart)) {
					case NO:
						System.out.println("NO salvar dialogo");
						break;
					case YES:
						saveHandler.save(dirtyPart, false);
						
						
						System.out.println("SI salvar dialogo");
						break;
					case CANCEL:
						System.out.println("CANCEL salvar dialogo");
						return false;
						//break;	
				}
				
			}
				
		}
		return true;
		
		

	}
	
	private Save promptToSaveDialog(MPart dirtyPart) {
		
		
	    MessageDialog dialog = new MessageDialog( (Shell)theWindow.getWidget(), "Save file", null,
			    "'"+dirtyPart.getLabel()+"' has been modified. Save changes?", MessageDialog.QUESTION, new String[] { "YES",
			    "NO", "CANCEL" }, 0);
			int result =dialog.open();
			System.out.println("pulsado\t"+result);

	    	switch (result){
			case 0:
				return Save.YES;
				
			case 1:
				return Save.NO;
				
			case 2:
				return Save.CANCEL;
				
				
			default:
				return Save.CANCEL;
			}
	 
		
	}

}
