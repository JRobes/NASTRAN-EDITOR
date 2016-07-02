 
package com.femeditors.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;

import es.robes.nastraneditor.events.NastranEditorEventConstants;

public class SearchDownHandler {
	@Inject
	IEventBroker broker;
	@Execute
	public void execute() {
		broker.post(NastranEditorEventConstants.FIND_TEXT_BUTTON_DOWN, null);
		
	}
		
}