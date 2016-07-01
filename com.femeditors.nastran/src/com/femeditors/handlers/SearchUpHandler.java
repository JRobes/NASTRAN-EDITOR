 
package com.femeditors.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

public class SearchUpHandler {
	@Execute
	public void execute(MApplication app, EModelService modelService) {
		final MElementContainer<MUIElement> container = (MElementContainer<MUIElement>) modelService.find("test-base-plugin.partstack", app);
		MUIElement selected = container.getSelectedElement();
		System.out.println(selected.toString());
	
	}
		
}