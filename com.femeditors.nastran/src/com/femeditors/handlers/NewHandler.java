 
package com.femeditors.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MBasicFactory;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

import java.util.List;



public class NewHandler {
	//private static int counter = 1; // counter for new files
	@Execute
	public void execute(EPartService partService, MApplication application,EModelService modelService, IEventBroker broker) {
	    // create new part
	    MPart part = MBasicFactory.INSTANCE.createPart();
	    //part.setLabel("New file " + counter++ );
	    part.setIconURI("platform:/plugin/com.femeditors.nastran/icons/1441374738_12File_NEW16x16.png");
	    //part.setContributionURI("platform:/plugin/TEST-BASE-PLUGIN/icons/1441374738_12File_NEW16x16.png");
	    part.setCloseable(true);
	    part.setContributionURI("bundleclass://com.femeditors.nastran/es.robes.editors.nastran.NastranEditor");
	    // get the part stack and show created part 
	    List<MPartStack> stacks = modelService.findElements(application, null, MPartStack.class, null);
	    stacks.get(1).getChildren().add(part);
	    partService.showPart(part, PartState.ACTIVATE);
	    
	   // broker.post(NastranEditorEventConstants.NEWFILE, new File(part.getLabel()) );
	}
}