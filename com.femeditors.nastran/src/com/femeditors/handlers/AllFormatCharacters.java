 
package com.femeditors.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;

import es.robes.nastraneditor.events.NastranEditorEventConstants;



public class AllFormatCharacters {
	boolean status;
	@Execute
	public void execute(final MToolItem item, IEventBroker broker) {
		System.out.println("MToolItem element ID: "+ item.getElementId());
		if (item.isSelected()){
			System.out.println("Item is selected");
			status = true;
			//broker.post(NastranEditorEventConstants.WHITE_CHARACTERS_ENABLED, status);
		}
		  
		else{
			System.out.println("Item is not selected");
			status = false;
			//broker.post(NastranEditorEventConstants.WHITE_CHARACTERS_DISABLED, status);
		}
		broker.post(NastranEditorEventConstants.WHITE_CHARACTERS_STATUS, status);

	}
		
}