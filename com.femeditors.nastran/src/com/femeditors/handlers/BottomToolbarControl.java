package com.femeditors.handlers;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.osgi.service.event.Event;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.UIEvents.EventTags;

import es.robes.nastraneditor.events.NastranEditorEventConstants;

public class BottomToolbarControl {
	
		
	@PostConstruct
	public void createGui(Composite parent) {
	
    }
	
		
	@Inject
	@Optional
	public void subscribeTopicPartActivation(@UIEventTopic(UIEvents.UILifeCycle.ACTIVATE) Event event) {
	  
	  Object element = event.getProperty(EventTags.ELEMENT);
	  if (!(element instanceof MPart)) {
	    return;
	  }
	  
	  MPart part = (MPart) element;
	  
	  System.out.println("Part activated: " + part.getLabel());
	} 

}