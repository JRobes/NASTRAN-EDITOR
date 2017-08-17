package com.femeditors.model;

import org.eclipse.jface.text.IRegion;

public interface IDocumentInput {
	//public IDocument getDocument();
	//public IStatus save();
	public boolean save();
	public void copy();
	public void paste();
	public void cut();
	public IRegion find(String findString, boolean searchForward, int intialCaretOffset); 
	 
}
