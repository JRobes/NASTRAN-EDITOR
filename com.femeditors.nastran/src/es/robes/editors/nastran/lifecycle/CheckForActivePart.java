package es.robes.editors.nastran.lifecycle;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.UIEvents.EventTags;
import org.osgi.service.event.Event;

public class CheckForActivePart {
	
	@Inject
	@Optional
	public void subscribeTopicPartActivation(@UIEventTopic(UIEvents.UILifeCycle.ACTIVATE) Event event) {
	  
	  Object element = event.getProperty(EventTags.ELEMENT);
	  if (!(element instanceof MPart)) {
	    return;
	  }
	  
	  MPart part = (MPart) element;
	  
	  System.out.println("Part activated DESDE EL LIFECYCLE: " + part.getLabel());
	} 
	
}
