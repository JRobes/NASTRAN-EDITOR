package com.femeditors.handlers;


import java.io.File;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.event.Event;

import es.robes.editors.nastran.ITextEditorPart;
import es.robes.nastraneditor.events.NastranEditorEventConstants;

public class SearchToolItem {
	public Text text; 
	@Inject	EModelService modelService;
	@Inject	MApplication app;
	@Inject IEventBroker broker;
	
	@PostConstruct
	public void createControls(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
		comp.setLocation(0, 0);
		text = new Text(comp, SWT.SEARCH | SWT.ICON_SEARCH | SWT.CANCEL | SWT.BORDER);
		text.setLocation(0, 0);
		text.setMessage("Search");
		GridDataFactory.fillDefaults().hint(250, SWT.DEFAULT).applyTo(text);
	}
	
	@Inject
	@Optional
	public void buttonListener(@UIEventTopic(NastranEditorEventConstants.FIND_TEXT_BUTTON_ALL_EVENTS) Event event) {
		String topic = event.getTopic();
		boolean isEmpty = text.getText() == null || text.getText().trim().length() == 0;
		
		if (!isEmpty) {
			System.out.println("hay texto......" );
			switch(topic){
    			case NastranEditorEventConstants.FIND_TEXT_BUTTON_DOWN:
    				topic = NastranEditorEventConstants.FIND_TEXT_DOWN;
    				break;
    			case NastranEditorEventConstants.FIND_TEXT_BUTTON_UP:
    				topic = NastranEditorEventConstants.FIND_TEXT_UP;
    				break;
			}
			//System.out.println("topic......\t" + topic);
			broker.post(topic, text.getText());
		}
}
	
	
}