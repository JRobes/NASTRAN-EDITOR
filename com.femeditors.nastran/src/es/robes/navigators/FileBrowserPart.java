package es.robes.navigators;



import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import sebor.osgi.services.imageloader.IImageLoader;

public class FileBrowserPart {
	private TreeViewer viewer;
	private Image folderIcon;
	private Image pdfFileIcon;
	private Image nastranFileIcon;
	//static File[] fileArray = null;
	
	@Inject 
	ESelectionService service;
	@Inject 
	EPartService partService;
	
	
	
	@PostConstruct
	public void createControls(Composite parent, IImageLoader imageLoader) {
		System.out.println("FileBrowser createControls...");
		String sSistemaOperativo = System.getProperty("os.name");
		String homeUsuario = System.getProperty("user.home");
		File[] files = new File[2];
		createImage(imageLoader);
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		
		//files[0] = new File("C:\\BORRA");
		//files[1] = new File("C:\\BORRA-2");
		
		String operatingSystem = System.getProperty("os.name");
		if(operatingSystem.startsWith("W")){
			files[0] = new File("C:\\BORRA");
			files[1] = new File("C:\\BORRA-2");
		}
		else{
			files[0]= new File("/home/javier/NASTRANEDITOR");
			files[1]= new File("/home/javier/NASTRANEDITOR");
		}
		
		//viewer.setInput(File.listRoots());
		viewer.setInput(files);
		hookDoubleClickCommand();
		
	}

	private void createImage(IImageLoader imageLoader) {
		
		this.folderIcon = imageLoader.loadImage(this.getClass(), "icons/folder.png");
		this.pdfFileIcon = imageLoader.loadImage(this.getClass(), "icons/pdf.png");
		this.nastranFileIcon = imageLoader.loadImage(this.getClass(), "icons/1441374738_12File_NEW16x16.png");
		
		/*
		 * Esto funciona y parece que carga al aplic mucho antes que con el servicio osgi
		Bundle bundle = FrameworkUtil.getBundle(ViewLabelProvider.class);
		URL url = FileLocator.find(bundle, new Path("icons/folder.png"), null);
		ImageDescriptor imageDcr = ImageDescriptor.createFromURL(url);
		this.folderImage = imageDcr.createImage();
				
		url = FileLocator.find(bundle, new Path("icons/pdf.png"), null);
		imageDcr = ImageDescriptor.createFromURL(url);
		this.pdfFileImage = imageDcr.createImage();
		
		url = FileLocator.find(bundle, new Path("icons/1441374738_12File_NEW16x16.png"), null);
		imageDcr = ImageDescriptor.createFromURL(url);
		this.nastranFileImage = imageDcr.createImage();
		*/
	}

	class ViewContentProvider implements ITreeContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return (File[]) inputElement;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			File file = (File) parentElement;
			
			System.out.println("principio de getchildren");
			FileFilter filter = new FileFilter() {
		        public boolean accept(File file) {
		        	if(file.isDirectory()){System.out.println("es directtorio");return true;}
		        	else{
		        		String path = file.getAbsolutePath().toLowerCase();
		        		if(path.endsWith(".txt")){System.out.println("es txt");return true;}
		        	}
		        	return false;
		        }
		    };
		    
		    Comparator comp = new Comparator() 
			    { 
				    public int compare(Object o1, Object o2) {
					    File f1 = (File) o1; 
					    File f2 = (File) o2; 
					    if (f1.isDirectory() && !f2.isDirectory()) 
						    { 
							    // Directory before non-directory 
							    return -1; 
						    } 
					    else if (!f1.isDirectory() && f2.isDirectory()) 
						    { 
							    // Non-directory after directory 
							    return 1; 
						    } 
					    else 
					    { 
						    // Alphabetic order otherwise 
						    //return o1.compareTo(o2); 
						    return f1.compareTo(f2);
					    } 
				    }
			    };
		    
			File[] aux = file.listFiles();
		    
			Arrays.sort(aux,comp);
			return aux; 
					//file.listFiles();
		}

		@Override
		public Object getParent(Object element) {
			File file = (File) element;
			
			return file.getParentFile();
		}

		@Override
		public boolean hasChildren(Object element) {
			File file = (File) element;
			if (file.isDirectory()) {
				return true;
			}
			return false;
		}

	}

	class ViewLabelProvider extends StyledCellLabelProvider {
		@Override
		public void update(ViewerCell cell) {
			Object element = cell.getElement();
			StyledString text = new StyledString();
			File file = (File) element;
			if (file.isDirectory()) {
				text.append(getFileName(file));
				cell.setImage(folderIcon);
				String[] files = file.list();
				if (files != null) {
					text.append(" ( " + files.length + " ) ",
							StyledString.COUNTER_STYLER);
				}
			} else {
        		String path = file.getAbsolutePath().toLowerCase();
        		if(path.endsWith(".pdf")){
        			text.append(getFileName(file));
        			cell.setImage(pdfFileIcon);
        		}
        		
				else{
					text.append(getFileName(file));
				}
				
				
				
			}
			cell.setText(text.toString());
			cell.setStyleRanges(text.getStyleRanges());
			super.update(cell);

		}

		private String getFileName(File file) {
			String name = file.getName();
			return name.isEmpty() ? file.getPath() : name;
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
		        	  if (file.isDirectory())return;
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

}