 
package com.femeditors.handlers;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

public class OpenHandler {
	@Inject 
	EPartService partService;
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell){
	//System.out.println("DENTRO DEL EXECUTE DE OPEN HANDLER");
	FileDialog dlg = new FileDialog(shell);
    String temp = dlg.open();
    

	//System.out.println("EN EL OPEN HANDLER, el archivo para abrir es:\t"+file);

    if (temp != null) {
       // Path filePath = Paths.get(temp);
        MPart part = partService.createPart("test-base-plugin.partdescriptor.nastran.editor");
        part.setCloseable(true);
        part.getTransientData().put("File Name", temp);
        partService.showPart(part, PartState.ACTIVATE);
    }
	}

}