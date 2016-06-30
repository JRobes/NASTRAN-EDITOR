package com.femeditors.handlers;


import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class SearchToolItem {
	
	
	@PostConstruct
	public void createControls(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
		comp.setLocation(0, 0);
		Text text = new Text(comp, SWT.SEARCH | SWT.ICON_SEARCH | SWT.CANCEL | SWT.BORDER);
		
		text.setLocation(0, 0);
		
		
		text.setText("Search");
		GridDataFactory.fillDefaults().hint(250, SWT.DEFAULT).applyTo(text);
	}
}