/*******************************************************************************
 * Copyright (c) 2010 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 ******************************************************************************/
package com.femeditors.model;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.text.IDocument;

public interface IDocumentInput {
	//public IDocument getDocument();
	public IStatus save();
}
