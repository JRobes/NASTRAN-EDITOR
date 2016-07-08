 
package es.robes.editors.nastran;

import java.io.File;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.MElementContainer;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MToolItem;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IPainter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.WhitespaceCharacterPainter;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.OverviewRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.VerticalRuler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.osgi.service.prefs.Preferences;



import com.femeditors.model.FEMFileDocumentInput;
import com.femeditors.nastran.preferences.PreferenceConstants;

import es.robes.nastraneditor.events.BackgroundPixels;
import es.robes.nastraneditor.events.NastranEditorEventConstants;



public class NastranEditor implements ITextEditorPart {
	/** Indicates the status of the WhiteSpaceCharacterPainter button on the toolbar for this part. */
	private boolean wsToolBarButtonStatus = false;
	/** The SourceViewer control to create the Nastran editor. */
	public SourceViewer sv = null;
	/** StyledText wrapped by SourceViewer control. */
	public StyledText st = null;
	/** Object used to paint non-printable characters: tab, space & RC. */
	private WhitespaceCharacterPainter whitespaceCharacterPainter;
	/** hPixel stores the value of the horizontal pixel of the StyledText 
	 * control used to scroll the background image. */
	int hPixel = 0;
	PaletteData paletteData;
	/**SI SE DECLARAN ESTAS VARIABLES DE DEBAJO COMO STATIC SOLAMENTE PINTA EL ULTIMO PART
	 * TAL COMO PASABA CON PINTAR LOS WHITESPACES
	 */
	Image whiteImage = null;
	//Image leftMarginImage = null;
	Image backgroundImage = null;
	
	ImageData imageData;
	
	public final int MAX_PIXELS_SIZE = 5000;
	
	private boolean isNewFile = true;
	
	private File[] fileBroker = {null,null};
   

	Display display;
	@Inject	ESelectionService selectionService;
	@Inject	EModelService modelService;
	@Inject MApplication app;
	@Inject EPartService partService;
	@Inject MDirtyable dirty;
	@Inject MPart parte;
	@Inject IEventBroker broker;
	//@Inject MPartStack partStack;
	private File file;
	private FEMFileDocumentInput fileIn;
	
	

	@Inject
	public NastranEditor(Composite parent) {
	}
	
		
	@PostConstruct
	public void postConstruct(Composite parent, EPartService partService, MApplication application) {
		MPartStack partStack = (MPartStack)modelService.find("test-base-plugin.partstack", app);
		//partStack = (MPartStack)modelService.find("test-base-plugin.partstack", app);
		display = parent.getDisplay();
		//List<MStackElement> stackElement = partStack.getChildren();
        paletteData = new PaletteData(new RGB[] {new RGB(255,255,255), new RGB(245,245,245)});

		ImageData whiteBackgroundImageData = new ImageData(1,1,1,paletteData);
		//ORIGINAL PERO DE FALLO
		imageData = new ImageData(MAX_PIXELS_SIZE,1,1,paletteData);
		List<MStackElement> stackElement = partStack.getChildren();
		System.out.println("Number of NastranEditor parts: " +stackElement.size());
		//for(MStackElement part : stackElement ){
			//System.out.println("stack element\t" +part.toString());
		//}
		
		
		parte.getTags().add(EPartService.REMOVE_ON_HIDE_TAG);
		
	    file = (File) parte.getTransientData().get("File Name");
	    if (file!=null)
	    	System.out.println("convertido transient data to file\t" + file.getAbsolutePath());
	    IVerticalRuler  verticalRuler = new VerticalRuler(10);
	    OverviewRuler overviewRuler = new OverviewRuler(null, 20, null);
	    sv = new SourceViewer(parent, verticalRuler, overviewRuler, true, SWT.MULTI | SWT.V_SCROLL |SWT.H_SCROLL);
	   // sv = new SourceViewer(parent, ruler, SWT.MULTI | SWT.V_SCROLL |SWT.H_SCROLL);
	    st = sv.getTextWidget();
	    
	    
		System.out.println("NUMERO DE PIXELS EN EL LADO IZDO:\t" +st.getLeftMargin());
		st.setLeftMargin(0);

	    Font fuente = new Font(parent.getDisplay(),new FontData("Monospac821 BT",10,SWT.NORMAL));
	    st.setFont(fuente);

    	whitespaceCharacterPainter = new  WhitespaceCharacterPainter(sv);

    	if(file == null){
    		parte.setLabel(("Document"+stackElement.size()+".bdf"));
    	}
    	else{
    		isNewFile = false;
    		parte.setLabel(file.getName());
    	}
    	
    	fileIn = new FEMFileDocumentInput(file);
    	sv.setDocument(fileIn.getDocument());
    	
    	
    	  System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
      	  
    	  File f = new File(parte.getLabel());
    	  
    	  System.out.println(f.getAbsolutePath());
    	  System.out.println(f.getName());

      	  System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");

      	fileBroker[0] =  new File(parte.getLabel()); 
	    broker.post(NastranEditorEventConstants.FILE_NEW, fileBroker );

	    sv.addPainter(whitespaceCharacterPainter);
	    whitespaceCharacterPainter.deactivate(true);
	    final ScrollBar hBar = st.getHorizontalBar();
	    
	    
		Preferences instanceScopePreferences = InstanceScope.INSTANCE.getNode("com.femeditors.nastran");
		Preferences nastranNode = instanceScopePreferences.node("nastran");
		
		Preferences defaultScopePrefs = DefaultScope.INSTANCE.getNode("com.femeditors.nastran");
		Preferences defaultScopePreferences = DefaultScope.INSTANCE.getNode("com.femeditors.nastran/nastran");
		Preferences sub1 = defaultScopePrefs.node("nastran");
		System.out.println("Recogiendo valor defaultScopePreferences\t"+sub1.get("nastranTabSize", "falsoooll en default cagont�"));

		
		//Preferences defaultNastranNode = defaultScopePreferences.node("nastran");
		System.out.println("Recogiendo valor defaultScopePreferences\t"+defaultScopePreferences.get("nastranTabSize", "falsoooll en default"));
		System.out.println("Recogiendo valor defaultScopePreferences\t"+defaultScopePrefs.get("nastranTabSize", "falsoooll en default"));
		System.out.println("Recogiendo valor defaultScopePreferences\t"+defaultScopePreferences.get("otravariable", "falsoooll en default"));
		System.out.println("Recogiendo valor defaultScopePreferences\t"+defaultScopePrefs.get("otravariable", "falsoooll en default"));
		
		//Preferences sub2 = instanceScopePreferences.node("");
		System.out.println("Recogiendo valor pseudoContinuousScrolling\t"+nastranNode.get("nastranTabSize", "falsoooll"));
		//System.out.println("Recogiendo valor pseudoContinuousScrolling\t"+sub2.get(PreferenceConstants.NASTRAN_TABS_SIZE, "falsoooll----2"));
		System.out.println("Default-Scope");
		//System.out.println("Recogiendo valor pseudoContinuousScrolling\t"+sub2.get(PreferenceConstants.PDF_RENDERER, "falsoooll----2"));
		Preferences defaultNode = defaultScopePreferences.node("");
		System.out.println("Recogiendo valor pseudoContinuousScrolling en Default\t"+defaultNode.get(PreferenceConstants.NASTRAN_TABS_SIZE, "Este no puede ser nunca falso"));
		System.out.println("Recogiendo valor pseudoContinuousScrolling en Default\t"+defaultNode.get(PreferenceConstants.OTRA_VARIABLE, "Ni este puede ser falso"));
		
		
		System.out.println("Recogiendo valor pseudoContinuousScrolling en Default sin node\t"+defaultScopePreferences.get(PreferenceConstants.NASTRAN_TABS_SIZE, "Este no puede ser nunca falso"));
		

	    /*
        ImageData leftMarginImageData = new ImageData(st.getLeftMargin(),1,1,paletteData);
     
        for (int i = 0; i < st.getLeftMargin(); i++ ){
        	leftMarginImageData.setPixel(i, 0, 0);
        }
        */
        //leftMarginImage = new Image(display,leftMarginImageData);
        whiteBackgroundImageData.setPixel(0,0,0);
        whiteImage= new Image(display, whiteBackgroundImageData);
        
        
			if(backgroundImage != null){
				backgroundImage.dispose();
			}
			//imageData.setPixels(0, 0, st.getLeftMargin(), BackgroundPixels.leftMargen, 0);
			//imageData.setPixels(st.getLeftMargin()-1, 0, 3000, BackgroundPixels.stripes640Bit,  0);
			imageData.setPixels(0, 0, 3000, BackgroundPixels.stripes640Bit,  0);
	    	backgroundImage = new Image(display, imageData);
			st.setBackgroundImage(backgroundImage);
			
		st.addMouseListener(new MouseAdapter(){
			public void mouseDown(MouseEvent e){
				System.out.println("mouse presed");
				paintBackground(imageData, display);
			}
			
			
		});	
        st.addKeyListener(new KeyAdapter(){
        	public void keyPressed(KeyEvent e){
        		if(e.keyCode == SWT.ARROW_LEFT || 
        		   e.keyCode == SWT.ARROW_DOWN || 
        		   e.keyCode == SWT.ARROW_UP || 
        		   e.keyCode == SWT.ARROW_RIGHT ||
        		   e.keyCode == SWT.END ||
        		   e.keyCode == SWT.H_SCROLL ||
                   e.keyCode == SWT.HOME ||
        		   e.keyCode == SWT.PAGE_DOWN ||
        		   e.keyCode == SWT.PAGE_UP){
				System.out.println("Arrows and buttonss");
				paintBackground(imageData, display);
        		}
        		if(e.keyCode == SWT.INSERT){
        			System.out.println("INSERTS PULSADA");
        			//System.out.println(e.stateMask);
        			
        			
        		}
        		
        	}
        });
        
       // st.setKeyBinding(SWT.INSERT, SWT.NULL);
       // st.invokeAction(ST.TOGGLE_OVERWRITE);
        
        st.addCaretListener(new CaretListener (){

			@Override
			public void caretMoved(CaretEvent event) {
				@SuppressWarnings("unused")
				int myHPixel;
				myHPixel = st.getHorizontalPixel();
    			System.out.println("Caret Moved...   ");
                System.out.println("Horizontal Pixel."+  st.getHorizontalPixel());
				paintBackground(imageData, display);

				
			}
        	
        });
  
        st.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event event) {
          	  System.out.println("Listener Modify detetct..");
				paintBackground(imageData, display);
	
             }
          });
       
        st.addListener(SWT.Resize, new Listener() {
          public void handleEvent(Event event) {
        	  System.out.println("Listener RESIZE..");
			  paintBackground(imageData, display);
         }
        });
    
      /*
        st.addListener(SWT.CLOSE, new Listener() {
            public void handleEvent(Event event) {
          	  System.out.println("La X de CLOSE..");
  			  paintBackground(imageData, display);
           }
          });
     */
        hBar.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event event) {
               
                System.out.println("In horizontal bar Listener...");
                System.out.println("Horizontal Pixel."+  st.getHorizontalPixel());
				paintBackground(imageData, display);
                
            }

        });
        
        st.addModifyListener(new ModifyListener(){

			@Override
			public void modifyText(ModifyEvent e) {
				dirty.setDirty(true);
				
			}
        	
        });
	}
	
	public void setDirtyStatus(boolean dirtyStatus){
		dirty.setDirty(dirtyStatus);
	}
	
	/**
	 * Update the part when the user push the WhiteSpace toolbar button.
	 * Sets the sv.addPainter(whitespaceCharacterPainter) 
	 * or sv.removePainter(whitespaceCharacterPainter);
	 * 
	 * @param newButtonStatus Receives the status of the button from the IEventBroker
	 */
	@Inject
	@Optional
	public void updatePartByWSButton(@UIEventTopic(NastranEditorEventConstants.WHITE_CHARACTERS_STATUS) boolean newButtonStatus) {
		final MElementContainer<MUIElement>container = parte.getParent();
		
		if (parte.equals((MPart)container.getSelectedElement())){
			
			System.out.println("EL PART ES EL SELECTED ELEMENT cuando se aprieta el boton\t" + parte.getLabel());
			//if(wsToolBarButtonStatus != newButtonStatus)
			//{
				
				//System.out.println("newButtonStatus\t"+newButtonStatus);
				wsToolBarButtonStatus = newButtonStatus;
				//System.out.println("wsToolBarButtonStatus\t"+wsToolBarButtonStatus);
				//MToolItem item = (MToolItem) modelService.find("es.robes.nastraneditor.toolbarbuttons.whitespacespainterbutton",app);
				//item.setSelected(wsToolBarButtonStatus);
				if(wsToolBarButtonStatus){
				    //sv.addPainter(whitespaceCharacterPainter);
					this.whitespaceCharacterPainter.paint(IPainter.CONFIGURATION);
					System.out.println("Painting the monkey......");
				}
				else{
					whitespaceCharacterPainter.deactivate(true);
					sv.removePainter(whitespaceCharacterPainter);

					System.out.println("Deactivating......");
				}
		}
	} 
	
	/**
	 * Update the WhiteSpace toolbar button when the part is active.
	 * Only the active part is updated 
	 * 
	 * @param activePart
	 */
	
	@Inject
	@Optional
	public void updateWSButtonByPart(@Named(IServiceConstants.ACTIVE_PART) MPart activePart) {
		//System.out.println("EL PART ACTIVO TIENE LABEL:\t" + parte.getLabel());
		//System.out.println("PARTE"+parte.getLabel()+"\t"+ wsToolBarButtonStatus);
		//System.out.println("PARTE"+activePart.getLabel());

		if (parte.equals(activePart)) {
			
			System.out.println("EL PART ACTIVO TIENE LABEL:\t" + parte.getLabel());
    		MWindow window = (MWindow) modelService.find("test-base-plugin.trimmedwindow", app);

			if(file == null){
	    		
	    		broker.post(NastranEditorEventConstants.STATUSBAR, parte.getLabel());
	    		window.setLabel(NastranEditorEventConstants.APPLICATION_TITLE +" - " + parte.getLabel());
	    	}
			else{
				broker.post(NastranEditorEventConstants.STATUSBAR, file.getAbsolutePath());
				window.setLabel(NastranEditorEventConstants.APPLICATION_TITLE +" - "  + file.getAbsolutePath());
			}
	
			MToolItem item = (MToolItem) modelService.find("es.robes.nastraneditor.toolbarbuttons.whitespacespainterbutton",app);
			item.setSelected(wsToolBarButtonStatus);
			if(wsToolBarButtonStatus){
			    //sv.addPainter(whitespaceCharacterPainter);
				whitespaceCharacterPainter.paint(IPainter.SELECTION);
				//System.out.println("Painting the monkey......");
			}
			else{
				whitespaceCharacterPainter.deactivate(true);
				sv.removePainter(whitespaceCharacterPainter);
				
				//System.out.println("Deactivating......");

			} 
		}

	} 
	
    private  void paintBackground(ImageData imageData, Display display) {
    	if(hPixel != st.getHorizontalPixel()){
    		hPixel = st.getHorizontalPixel();
    		if(hPixel<640){

    			if(backgroundImage != null){
    				backgroundImage.dispose();
    			}
    			int startIndex = st.getHorizontalPixel();
    			if (startIndex == 0){
    				startIndex = 1;
    			}
    			//imageData.setPixels(0, 0, st.getLeftMargin(), BackgroundPixels.leftMargen, 0);
    			//imageData.setPixels(st.getLeftMargin()-1, 0, 2000, stripes640Bit, startIndex);
    			//
    			//imageData.setPixels(st.getLeftMargin()-1, 0, 2000, BackgroundPixels.stripes640Bit, startIndex);
    			imageData.setPixels(0, 0, 2000, BackgroundPixels.stripes640Bit, startIndex);
    			backgroundImage = new Image(display, imageData);

    			st.setBackgroundImage(backgroundImage);
    		}
    		else{
    			st.setBackgroundImage(whiteImage);
    		}

			
		}
	}
    
	@Persist
	public void guardarDatos(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell){
		System.out.println("guardando los datos...");
		System.out.println("guardando los datos..."+ file.getAbsolutePath());
		if (!isNewFile){
			fileIn.save();
			dirty.setDirty(false);
		}
		else{
			FileDialog saveDialogv= new FileDialog(shell, SWT.SAVE);
			
			String temp = saveDialogv.open();
			System.out.println("guardando los datos...\t" + temp);
		}
	
	}
	public void save(){
		System.out.println("guardando los datos...");
		//System.out.println("guardando los datos..."+ file.getAbsolutePath());
		if (isNewFile){
			
			FileDialog saveDialogv= new FileDialog(display.getActiveShell(), SWT.SAVE);
			String temp = saveDialogv.open();
			if(temp!= null){
				
				System.out.println("guardando los datos...\t" + temp);
				File newFile = new File(temp);
				
				fileIn.setFile(newFile);
				System.out.println("guardando los datos...111111");
				fileIn.save();
				System.out.println("guardando los datos...222222");
				parte.setLabel(newFile.getName());
				dirty.setDirty(false);
				isNewFile = false;
				System.out.println("guardando los datos...333333");
				fileBroker[1] = newFile;
			    broker.post(NastranEditorEventConstants.FILE_RENAME, fileBroker);
			    broker.post(NastranEditorEventConstants.STATUSBAR, fileBroker[1].getAbsolutePath());
			}
	
			else{
				System.out.println("FileDialog cancelado ... No hay cambios");
			}
		}
		else{
			fileIn.save();
			dirty.setDirty(false);


		}
		
	
	}
	@Focus
	public void onFocus(MToolItem item) {
		//styledText.setFocus();
		item.setSelected(wsToolBarButtonStatus);	
		System.out.println("On Focus, el Mtoolitem es:\t" +item.getElementId());

	}
	
	@PreDestroy
	public void dispose(){
		System.out.println("DISPOSE PART");
				
		MPartStack editorStack = (MPartStack) modelService.find("test-base-plugin.partstack", app);
		editorStack.getChildren().remove(parte);
		
		if(backgroundImage != null) backgroundImage.dispose();
	    if(whiteImage != null) whiteImage.dispose();
	    if(st != null) st.dispose();
	    
	    //if(file != null)
	    
	    broker.post(NastranEditorEventConstants.FILE_CLOSE, fileBroker );

	}


	@Override
	public void cut() {
		st.cut();
		
	}


	@Override
	public void copy() {
		st.copy();
	}


	@Override
	public void paste() {
		st.paste();
	}


	@Override
	public IRegion find(String findString, boolean searchFordward) {
				//Document document = new Document(text);
			FindReplaceDocumentAdapter documentAdapter = new FindReplaceDocumentAdapter(sv.getDocument());
		
			try {
						return documentAdapter.find(st.getCaretOffset(), findString, searchFordward, true, true, false);
				} catch (BadLocationException argh) {
					System.out.println("HA Entrado en el badlocationexception" +argh);
					return null;
				}
			
			
	}
	
	
	@Inject
	@Optional
	public void searchTextListener(@UIEventTopic(NastranEditorEventConstants.FIND_TEXT_ALL_EVENTS) org.osgi.service.event.Event events) {
		System.out.println("RECIBE DATOS DEL CONTROL!!!!!11" );
		final MElementContainer<MUIElement>container = parte.getParent();
		String topic = events.getTopic();
		String text  = (String) events.getProperty(IEventBroker.DATA);
		int initialCaretOffset = st.getCaretOffset();
		System.out.println("TOPIC...." + topic);
		IRegion region = null;
		if (parte.equals((MPart)container.getSelectedElement())){
			System.out.println("ha entrado en el parte TEXT CONTROL.....");
			switch(topic){
    			case NastranEditorEventConstants.FIND_TEXT_DOWN:
    				System.out.println("BUTTON DOWN.....");
    				region=find(text, true);
    				st.setCaretOffset(region.getOffset());
    				st.setSelection(st.getCaretOffset(), st.getCaretOffset()+region.getLength());
    				break;
    			case NastranEditorEventConstants.FIND_TEXT_UP:
    				System.out.println("BUTTON UP.....");
    				region=find(text, false);
    				st.setCaretOffset(region.getOffset());
    				
    				//st.setSelection(st.getCaretOffset(), st.getCaretOffset()+region.getLength());
    				break;
    		
			}
			System.out.println("REGION....\t" + region.toString() );

			
			
			
		}
			
	}

	
	
}





















