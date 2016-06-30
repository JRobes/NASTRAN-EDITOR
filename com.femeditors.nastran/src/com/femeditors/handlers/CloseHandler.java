 
package com.femeditors.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;

public class CloseHandler {
	@Execute
	public void execute(IWorkbench workbench) {
		System.out.println("Close handler executed.");
		
            workbench.close();
	}

	
	
	
	
	
	
}