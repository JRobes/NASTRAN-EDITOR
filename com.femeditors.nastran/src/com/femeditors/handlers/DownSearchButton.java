package com.femeditors.handlers;

import javax.annotation.PostConstruct;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class DownSearchButton {
	
	@PostConstruct
	public void createGui(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout());
		comp.setLocation(0, 0);
		Button boton = new Button(comp, SWT.ARROW | SWT.DOWN);
	}
}