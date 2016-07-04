package es.robes.editors.nastran;

import org.eclipse.jface.text.IRegion;

public interface ITextEditorPart {
	public void save();
	public void cut();
	public void copy();
	public void paste();

	public IRegion find(String findString, boolean searchForward); 


}

