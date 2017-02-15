 
package com.femeditors.handlers;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;

import com.femeditors.model.IDocumentInput;

import es.robes.editors.nastran.ISaveTextEditorPart;
import es.robes.editors.nastran.NastranEditor;


public class CopyHandler {
	@Execute
	public void execute(MPart part){
		if (part==null) {
			return;
		}
		NastranEditor editor = (NastranEditor) part.getObject();	
		Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
        StringSelection stringSelection = new StringSelection (editor.st.getSelectionText());
        clpbrd.setContents (stringSelection, null);
        //System.out.println("Copy called");
	}	
	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) MPart activePart){
		//System.out.println(activePart.toString());
		//activePart.getObject()
		if (activePart.getObject() instanceof IDocumentInput){
			//System.out.println("Copy called true");
			return true;

		}
		//System.out.println("Copy called false");

		return false;
	}
}