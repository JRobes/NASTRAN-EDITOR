package com.femeditors.handlers;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import es.robes.nastraneditor.events.NastranEditorEventConstants;

public class StatusBarHandler {
	Label label;
	
	@Inject
	@Optional
	public void  getEvent(@UIEventTopic(NastranEditorEventConstants.STATUSBAR) String message) {
	    updateInterface(message); 
	}
	@PostConstruct
	public void createGui(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		//gridLayout.numColumns=5;
		//GridData gridData = new GridData();
		//gridData.horizontalAlignment = GridData.FILL;
		//gridData.horizontalSpan = 5;
		
		comp.setLayout(gridLayout);
		//comp.setLayoutData(gridData);
		comp.setLocation(0, 0);
		label= new Label(comp, SWT.VERTICAL);
		GridDataFactory.fillDefaults().hint(1200, SWT.DEFAULT).applyTo(label);
		label.setText("TTTT");
		 //System.out.println(label.);
	}
	public void updateInterface(final String message)
    {
        try{
            Display.getDefault().asyncExec(new Runnable() {
              @Override
              public void run() {
                 try{
                        label.setText(message);  
                    }
                    catch(Exception exc){
                        System.out.println(exc);
                    }               
              }
            });
        }
        catch(Exception exception){
            System.out.println(exception);
        }   
    }


}