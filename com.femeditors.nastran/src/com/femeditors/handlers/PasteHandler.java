 
package com.femeditors.handlers;
/*
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
*/
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;

import com.femeditors.model.IDocumentInput;


public class PasteHandler {
	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) MPart activePart){
		//System.out.println(activePart.toString());
		//activePart.getObject()
		if (activePart.getObject() instanceof IDocumentInput)
			return true;
		return false;
	}
	
	@Execute
	public void execute(@Named(IServiceConstants.ACTIVE_PART) MPart activePart){
		
		IDocumentInput editor = (IDocumentInput) activePart.getObject();	
		editor.paste();
		
		//ESTA ES UNA ALTERNATIVA, PERO HABRÍA QUE METER LA FUNCIÓN 
		//getClipboardString dentro del NastranEditor
		//editor.st.insert(getClipboardString());
	}		
	/*
	// Get String from Clipboard
    private String getClipboardString() {
        Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
        String result = "";          
        Transferable contents = clpbrd.getContents(null);
        boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
        if ( hasTransferableText ) {
          try {
            result = (String)contents.getTransferData(DataFlavor.stringFlavor);
          }
          catch (UnsupportedFlavorException ex){
            System.out.println(ex);
            ex.printStackTrace();
          }
          catch (IOException ex) {
            System.out.println(ex);
            ex.printStackTrace();
          }        
        }
     return result;
    }
    */
    
}