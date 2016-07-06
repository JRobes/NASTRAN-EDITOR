 
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
		broker.post(NastranEditorEventConstants.FIND_TEXT_BUTTON_UP, null);

	}
		
}