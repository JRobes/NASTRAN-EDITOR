 
package com.femeditors.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import es.robes.editors.nastran.ITextEditorPart;
import es.robes.nastraneditor.events.NastranEditorEventConstants;

public class SearchUpHandler {
	@Inject
	IEventBroker broker;
	@Execute
	public void execute(MApplication app, EModelService modelService) {
		final MElementContainer<MUIElement> container = (MElementContainer<MUIElement>) modelService.find("test-base-plugin.partstack", app);
		MPart selected = (MPart)container.getSelectedElement();
		ITextEditorPart	editor = (ITextEditorPart)selected.getObject();
		
		if(editor instanceof ITextEditorPart){
			
			System.out.println("Es text editor");
			
			//ESTO ES PARA EL CHECKEO DE LA LINEA
			//boolean isEmpty = str == null || str.trim().length() == 0;
			//if (isEmpty) {
			    // handle the validation
			//}
			

		}
		System.out.println(editor.toString());
		
		
		
		
		broker.post(NastranEditorEventConstants.FIND_TEXT_BUTTON_UP, null);

	}
		
}