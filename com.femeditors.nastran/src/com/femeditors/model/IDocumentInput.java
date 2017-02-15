package com.femeditors.model;

import org.eclipse.core.runtime.IStatus;

public interface IDocumentInput {
	//public IDocument getDocument();
	//public IStatus save();
	public void copy();
	public void paste();
	public void cut();
	 
}
