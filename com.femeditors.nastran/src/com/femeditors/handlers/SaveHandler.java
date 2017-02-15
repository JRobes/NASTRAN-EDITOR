 
package com.femeditors.handlers;


import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import es.robes.editors.nastran.ISaveTextEditorPart;

public class SaveHandler {
	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) MPart activePart){
		if (!activePart.isDirty())
			return false;
		return true;
	}
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_PART) MPart activePart ){
		if (activePart.getObject() instanceof ISaveTextEditorPart){
			ISaveTextEditorPart	editor = (ISaveTextEditorPart)activePart.getObject();		
			editor.save();
		}
		
	}
	
	
}