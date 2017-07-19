package com.nastraneditor.osgi.services.genericnastraneditordata;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontData;

public class GenericNastranEditorData implements IGenericNastranEditorData {
	private int number = 0;
	@Override
	public String getNewDocumentNumber() {
		number++;
		return Integer.toString(number);
	}
	@Override
	public FontData getFontData() {
		
		String OS = System.getProperty("os.name");
		if(OS.startsWith("Win")){
			return new FontData("Monospac821 BT",10,SWT.NORMAL);
		}
		else{
			return new FontData("Monospac821 BT",10,SWT.NORMAL);
		}
	}
}
