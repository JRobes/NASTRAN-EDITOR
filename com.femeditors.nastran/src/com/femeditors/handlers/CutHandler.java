 
package com.femeditors.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import es.robes.editors.nastran.ITextEditorPart;

public class CutHandler {
	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) MPart activePart){
		if (activePart.getObject() instanceof ITextEditorPart)
			return true;
		return false;
	}
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_PART) MPart activePart){
		ITextEditorPart editor = (ITextEditorPart) activePart.getObject();	
		editor.cut();
	}		
	
		
}