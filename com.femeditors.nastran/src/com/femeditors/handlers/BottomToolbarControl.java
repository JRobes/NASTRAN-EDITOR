package com.femeditors.handlers;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

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
import org.eclipse.e4.ui.services.IServiceConstants;


import es.robes.nastraneditor.events.NastranEditorEventConstants;

public class BottomToolbarControl {
	
		
	@PostConstruct
	public void createGui(Composite parent) {
	
    }
	
	@Inject
	@Optional
	public void partIsActivePart(@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {
		if (activePart != null) {
			
			System.out.println("ActivePart distinto null:\t"+ activePart.getElementId());
		}
		
		
	}

}