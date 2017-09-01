package es.robes.editors.nastran.lifecycle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.di.InjectionException;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.commands.MHandler;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ElementMatcher;
import org.eclipse.e4.ui.workbench.modeling.ISaveHandler;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

@SuppressWarnings("restriction")
public class LifeCycleManager {
	@Inject	IEventBroker eventBroker;
	
	@ProcessAdditions
	public void processAdditions(MApplication application, EModelService modelService){
	     MWindow window =(MWindow)modelService.find("test-base-plugin.trimmedwindow", application);
	     eventBroker.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE,
	                          new AppStartupCompleteEventHandler(window, modelService, application));
	}
	public class AppStartupCompleteEventHandler implements EventHandler {
		private MWindow theWindow;
		private MApplication app;
		private ISaveHandler saveHandler;

		AppStartupCompleteEventHandler(MWindow window, EModelService modelService, MApplication application){
    		theWindow = window;
   		 	app = application;
    		System.out.println("MODEL SERVICE    :   "+ application.toString());
		}

		@Override
		public void handleEvent(Event event) {
			theWindow.getContext().set(ISaveHandler.class, new ISaveHandler() {
				@Override
				public boolean save(MPart dirtyPart, boolean confirm) {
					System.out.println("PRIMERO ENTRA EN SAVE");

					if (confirm){
						System.out.println("El workbench lanza save(...,true)");

					     switch (promptToSave(dirtyPart)) {
					       default:
					       case NO: return true;
					       case CANCEL: return false;
					       case YES:
					    	   if(dirtyPart.getTransientData().get("File Name")==null) {
					    		   if(!promtToNewFile(dirtyPart)) {
					    			   return false;
					    		   }
					    	   }
					     }
					   }
					  try {
					     ContextInjectionFactory.invoke(dirtyPart.getObject(), Persist.class, dirtyPart.getContext());
					   }
					  catch (final InjectionException ex)
					   {
					     // TODO ignore or log error
					   }
					  return true;
				}
	
				private boolean promtToNewFile(MPart dirtyPart) {
					FileDialog saveDialogv= new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
					String temp = saveDialogv.open();
					if(temp != null){
						 dirtyPart.getTransientData().put("File Name",temp);
						return true;
					}
					return false;
				}

				@Override
				public boolean saveParts(Collection<MPart> dirtyParts, boolean confirm) {
					return false;
				}
				@Override
				public Save promptToSave(MPart dirtyPart) {
					if(dirtyPart.getTransientData().get("File Name")==null) {
						System.out.println("PART NAME NULLLLLLL");
					}
					MessageDialog dialog = new MessageDialog( (Shell)theWindow.getWidget(), "Save filellllll", null,
						    "'"+dirtyPart.getLabel()+"' has been modified. Save changes?", MessageDialog.QUESTION, new String[] { "YES", "NO", "CANCEL" }, 0);
				    	switch (dialog.open()){
							case 0:	
								System.out.println("sAve YES");
								return Save.YES;
							case 1:	
								System.out.println("sAve NO");
								return Save.NO;


							case 2:	
								System.out.println("sAve CANCEL");

								return Save.CANCEL;
							default:return Save.CANCEL;
						}
				}
				@Override
				public Save[] promptToSave(Collection<MPart> dirtyParts) {
					return null;
				}
			});
			saveHandler  = (ISaveHandler)theWindow.getContext().get(ISaveHandler.class);
			theWindow.getContext().set(IWindowCloseHandler.class, new IWindowCloseHandler() {
				@Override
				public boolean close(MWindow window) {
					List<MHandler> listHandlers = window.getHandlers();
					System.out.println(listHandlers.size());
					Shell shell = (Shell) window.getWidget();
					if (MessageDialog.openConfirm(shell, "Close Nastran Editor", "Do you really want to close the entire application?")) {
						Collection<EPartService> allPartServices = getAllPartServices(app);
						if (containsDirtyParts(allPartServices)) {
							return iterateOverDirtyParts( allPartServices);
						}
					else {
						return true;
					}
				}
				return false;
			}});
	}
	private Collection<EPartService> getAllPartServices(MApplication application) {
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
	private boolean containsDirtyParts(Collection<EPartService> partServices) {
		for (EPartService partService : partServices) {
			if (!partService.getDirtyParts().isEmpty())	return true;
		}
		return false;
	}
	private  boolean iterateOverDirtyParts(Collection<EPartService> allPartServices) {
		for (EPartService partService : allPartServices) {
			Collection<MPart> dirtyParts = partService.getDirtyParts();
			for(MPart dirtyPart : dirtyParts) {
				System.out.println("DIRTY PART...dirty part");
				if(!saveHandler.save(dirtyPart, false)) {
					return false;
				}
				/*
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
				}
				*/
			}
		}
		return true;
	}
}
}///END of LifeCycleManager

