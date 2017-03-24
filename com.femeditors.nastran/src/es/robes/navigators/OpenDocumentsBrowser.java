package es.robes.navigators;



import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.event.Event;

import es.robes.nastraneditor.events.NastranEditorEventConstants;
import sebor.osgi.services.imageloader.IImageLoader;

public class OpenDocumentsBrowser {
	private TreeViewer viewer;
	private Image folderIcon;
	private Image pdfFileIcon;
	private Image nastranFileIcon;
	//static File[] fileArray = null;
	//private String[] fileNames = new String[3];
	//private File[] openFiles = new File[6];
	private List<Path> listOfFiles = new ArrayList<Path>();

	@Inject ESelectionService service;
	@Inject	EPartService partService;
	@Inject	MPart parte;
	@Inject IEventBroker broker;
	@Inject	EModelService modelService;
	@Inject MApplication app;
	
	public OpenDocumentsBrowser(){
		
		
	}
	
	@PostConstruct
	public void createControls(Composite parent, IImageLoader imageLoader) {

		System.out.println("FileBrowser createControls...");
		//File[] files = new File[2];
		createImage(imageLoader);
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());

		//viewer.setInput(File.listRoots());
		//viewer.setInput(fileNames);
		viewer.setInput(listOfFiles);
		//hookDoubleClickCommand();
		
	}

	private void createImage(IImageLoader imageLoader) {
		
		this.folderIcon = imageLoader.loadImage(this.getClass(), "icons/folder.png");
		this.pdfFileIcon = imageLoader.loadImage(this.getClass(), "icons/pdf.png");
		this.nastranFileIcon = imageLoader.loadImage(this.getClass(), "icons/1441374738_12File_NEW16x16.png");

	}

	class ViewContentProvider implements ITreeContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object[] getElements(Object inputElement) {
			
			return ((List<Path>)inputElement).toArray();
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			//String cadena = (String) parentElement;
			//INTENTO QUE DEVUELVA NULL
			return null; 
					
		}

		@Override
		public Object getParent(Object element) {
			//String cadena = (String) element;
			//INTENTO QUE DEVUELVA NULL

			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
	
			return false;
		}

	}
	
	class ViewLabelProvider extends StyledCellLabelProvider {
		@Override
		public void update(ViewerCell cell) {
			Object element = cell.getElement();
			StyledString text = new StyledString();
			Path filePath = (Path) element;
			text.append(getFileName(filePath));
			cell.setImage(nastranFileIcon);
			cell.setText(text.toString());
			cell.setStyleRanges(text.getStyleRanges());
			super.update(cell);
		}
		private String getFileName(Path filePath) {
			String name = filePath.getFileName().toString();
			
			//return name.isEmpty() ? filePath.getPath() : name;
			return name;
		}
	}

	@Focus
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@PreDestroy
	public void dispose() {
		folderIcon.dispose();
		pdfFileIcon.dispose();
		nastranFileIcon.dispose();
	}
	
	
	@Inject
	@Optional
	public void updateListOfFilesByNewPart(@UIEventTopic(NastranEditorEventConstants.FILE_ALL_EVENTS) Event event) {

		String topic = event.getTopic();

		Object data = event.getProperty(IEventBroker.DATA);
		Path[] fileUpdated = (Path[])data;
		
		switch(topic){
    		case NastranEditorEventConstants.FILE_NEW:
    			listOfFiles.add(fileUpdated[0]);
    			viewer.refresh();
    			break;
    		case NastranEditorEventConstants.FILE_CLOSE:
    			listOfFiles.remove(fileUpdated[0]);
    			viewer.refresh();
    			break;
    		case NastranEditorEventConstants.FILE_RENAME:
    		
    			listOfFiles.set(listOfFiles.indexOf(fileUpdated[0]), fileUpdated[1]);
    			//listOfFiles.remove(fileUpdated[0]);
    			viewer.refresh();
    			break;
		}
		
        //System.out.println("list of files:"+  listOfFiles.get(0).getName());

	
	}
/*
	private void hookDoubleClickCommand() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
		      @Override
		      public void doubleClick(DoubleClickEvent event) {
		    	 
		    	  final IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		          File file = null;
		          boolean partExists = false;
		          if (selection.isEmpty()) return;
		          System.out.println("ANTES DEL if");
		         
		          if (selection.getFirstElement() instanceof File ) {
		        	  System.out.println("DENTRO DEL if");
		        	  file = (File) selection.getFirstElement();
		        	  service.setSelection(selection.getFirstElement());
		        	  System.out.println("Archivo selecionado, getName():" + file.getName());
		        	  System.out.println("Archivo selecionado, getParent():" + file.getParent());
		              for (MPart part1 : partService.getParts()) {
		                  if (part1.getLabel().equals(file.getName())) {

		                      partService.showPart(part1, PartState.ACTIVATE);
		                      partExists = true;
		                      break;
		                  }
		              }
		              if (!partExists) {
		                  MPart part2 = partService.createPart("test-base-plugin.partdescriptor.nastran.editor");
		                  //part2.setLabel(file.getName());
		                  part2.setCloseable(true);
		                  part2.getTransientData().put("File Name", file);
		                  partService.showPart(part2, PartState.ACTIVATE);
		              }
		    	  
		          } 
          
		          
		      }
		    });
		
		
	}
*/
	
	
	@Inject
	@Optional
	public void updateWSButtonByPart(@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {
		//System.out.println("EL PART ACTIVO TIENE LABEL:\t" + parte.getLabel());
		//System.out.println("PARTE"+parte.getLabel()+"\t"+ wsToolBarButtonStatus);
		//System.out.println("PARTE"+activePart.getLabel());

		if (parte.equals(activePart)) {
			//broker.post(NastranEditorEventConstants.STATUSBAR, "Natran Editor");
			//MWindow window = (MWindow) modelService.find("test-base-plugin.trimmedwindow", app);
			//window.setLabel(NastranEditorEventConstants.APPLICATION_TITLE);
			} 
		

	} 

}