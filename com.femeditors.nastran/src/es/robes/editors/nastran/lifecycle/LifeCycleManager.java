package es.robes.editors.nastran.lifecycle;

import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessRemovals;

public class LifeCycleManager {
	 @ProcessRemovals
     void postContextCreate(){
		 System.out.println(">>>>>>>>>>>>>>>>>>>>><<<<T CONSTRUCT DENTRO DEL CHECKEADOR DE ACTIVE PARTS");
		 CheckForActivePart CAP = new CheckForActivePart();
		 
	 }
}
