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
		//text.setText("Search");
		GridDataFactory.fillDefaults().hint(250, SWT.DEFAULT).applyTo(text);
	}
	
	@Inject
	@Optional
	public void buttonListener(@UIEventTopic(NastranEditorEventConstants.FIND_TEXT_BUTTON_ALL_EVENTS) Event event) {
		//IRegion region;
		String topic = event.getTopic();
		//No requeridos
		//Object data = event.getProperty(IEventBroker.DATA);
		//File[] fileUpdated = (File[])data;
		boolean isEmpty = text.getText() == null || text.getText().trim().length() == 0;
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
		//System.out.println(editor.toString());
		
		
		if (!isEmpty) {
		    // handle the validation
			System.out.println("hay texto......" );
			switch(topic){
    			case NastranEditorEventConstants.FIND_TEXT_BUTTON_DOWN:
    				topic = NastranEditorEventConstants.FIND_TEXT_DOWN;
    				break;
    			case NastranEditorEventConstants.FIND_TEXT_BUTTON_UP:
    				topic = NastranEditorEventConstants.FIND_TEXT_UP;
    				break;
			}
			System.out.println("topic......\t" + topic);
			broker.post(topic, text.getText());
		}
	


	
	}
	
	
}