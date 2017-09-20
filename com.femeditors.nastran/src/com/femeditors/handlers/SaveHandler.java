 
package com.femeditors.handlers;


import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.ISaveHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import com.femeditors.model.IDocumentInput;

public class SaveHandler {
	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) MPart activePart){
		if (!activePart.isDirty())
			return false;
		return true;
	}
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_PART) MPart activePart, MWindow window ){
		//activePart..getParent().ge
		if (activePart.getObject() instanceof IDocumentInput){
			ISaveHandler saveHandler = window.getContext().get(ISaveHandler.class);
			if(activePart.getTransientData().get("File Name")==null){
				if(promtToNewFile(activePart)) {
					saveHandler.save(activePart, false);
				}
			}
		}
	}
	private boolean promtToNewFile(MPart dirtyPart) {
		FileDialog saveDialogv= new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
		String temp = saveDialogv.open();
		if(temp != null){
			 dirtyPart.getTransientData().put("File Name",temp);
			return true;
		}
		return false;
	}

}