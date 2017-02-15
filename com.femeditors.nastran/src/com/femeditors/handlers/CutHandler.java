 
package com.femeditors.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;

import com.femeditors.model.IDocumentInput_new;

import es.robes.editors.nastran.ISaveTextEditorPart;

public class CutHandler {
	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) MPart activePart){
		if (activePart.getObject() instanceof IDocumentInput_new)
			return true;
		return false;
	}
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_PART) MPart activePart){
		IDocumentInput_new editor = (IDocumentInput_new) activePart.getObject();	
		editor.cut();
	}		
	
		
}