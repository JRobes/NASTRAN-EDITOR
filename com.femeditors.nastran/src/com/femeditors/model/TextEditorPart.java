package com.femeditors.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import es.robes.nastraneditor.events.NastranEditorEventConstants;

public abstract class TextEditorPart implements IDocumentInput {
	public StyledText st = null;
	//public File file;
	public static Path documentPath = null;
	private IDocument document;
	public boolean isNewFile = false;
	protected Path[] pathBroker = {null,null};
	protected Display display;
	//public Display display;
	private static final int DEFAULT_FILE_SIZE = 15 * 1024;

	
	public IDocument getDocument() {
		Document document = new Document();
		Reader in = null;
		InputStream contentStream = null;
		if(!isNewFile){
			try {
				 contentStream = Files.newInputStream(documentPath);
				if (contentStream == null) {
					return null;
				}

				String encoding = null;
				if (encoding == null)
					encoding = "ASCII";
				in = new BufferedReader(new InputStreamReader(contentStream,
						encoding), DEFAULT_FILE_SIZE);
				StringBuffer buffer = new StringBuffer(DEFAULT_FILE_SIZE);
				char[] readBuffer = new char[2048];
				int n = in.read(readBuffer);
				while (n > 0) {
					buffer.append(readBuffer, 0, n);
					n = in.read(readBuffer);
				}

				document.set(buffer.toString());
				this.document = document;		
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					if (in != null)
						in.close();
					else
						contentStream.close();
				} catch (IOException x) {
				}
			}
			
		}
		else{
			URL url = Platform.getInstanceLocation().getURL();
			
			
			//System.out.println("Absolute path\n"+url.getPath());
			document.set("Absolute path\n"+url.getPath());
			this.document= document;
		}
		return document;
	}
	
	
	
	public boolean AAsave(){
		System.out.println("guardando los datos...");
		//System.out.println("guardando los datos..."+ file.getAbsolutePath());
		if (isNewFile){
			
			FileDialog saveDialogv= new FileDialog(display.getActiveShell(), SWT.SAVE);
			String temp = saveDialogv.open();
			if(temp!= null){
				
				System.out.println("guardando los datos...\t" + temp);
				//Path pathToSave = Paths.get(temp);
				documentPath= Paths.get(temp);
				
				System.out.println("guardando los datos...111111");
				System.out.println("El path para salvar\t"+ documentPath.toString());
				//fileIn.save();
				savePart();
				System.out.println("guardando los datos...222222");
				//
				//parte.setLabel(documentPath.getFileName().toString());
				//dirty.setDirty(false);
				//
				isNewFile = false;
				System.out.println("guardando los datos...333333");
				pathBroker[1] = documentPath;
			    //
				//parte.getTransientData().put("File Name", documentPath.toString());
			    //broker.post(NastranEditorEventConstants.FILE_RENAME, pathBroker);
			    //broker.post(NastranEditorEventConstants.STATUSBAR, pathBroker[1].toString());
				//
			}
	
			else{
				System.out.println("FileDialog cancelado ... No hay cambios");
			}
		}
		else{
			
			savePart();
			//
			//dirty.setDirty(false);
			//
		}
		
		
		
		return false;
	}
	
	public IStatus savePart() {
		System.err.println("Starting save");
		System.out.println("Document path:\t"+ documentPath.toString());
		
		if (documentPath == null){
			System.err.println("NUNCA DEBE ENTRAR AQUI....");
		}
		else{
			saveDocument();

		}

		System.err.println("Saving done");
		

		
		return null;
	}
	
	
	
	
	
	private void saveDocument() {
		String encoding = "ASCII";
			
		Charset charset= Charset.forName(encoding);
		CharsetEncoder encoder = charset.newEncoder();
		
		byte[] bytes;
		ByteBuffer byteBuffer;
		OutputStream fooStream = null;
		try {
			byteBuffer = encoder.encode(CharBuffer.wrap(document.get()));
			if (byteBuffer.hasArray()){
				System.out.println("hasarray true");
				bytes= byteBuffer.array();
			}
				
			else {
				System.out.println("hasarray FALSE");
				bytes= new byte[byteBuffer.limit()];
				byteBuffer.get(bytes);
			}
			
			//ByteArrayInputStream stream= new ByteArrayInputStream(bytes, 0, byteBuffer.limit());
			
			try {
				fooStream = Files.newOutputStream(documentPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//.newOutputStream(documentPath, false);
					//new FileOutputStream(file, false);	// true to append
	         												// false to overwrite. 
			try {
				fooStream.write(bytes);
				fooStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
						
		} catch (CharacterCodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public void cut() {
		st.cut();		
	}

	

}
