package com.femeditors.model;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.custom.StyledText;

public class TextEditorPart implements IDocumentInput_new {
	public StyledText st = null;
	public File file;
	private IDocument document;
	private static final int DEFAULT_FILE_SIZE = 15 * 1024;

	
	public IDocument getDocument() {
		if (file == null) {
			//System.out.println("Dentro de NewFileDoc.... en getDocument cuando es null");
			Document document = new Document();
			document.set("");
			this.document= document;
		}
		else{
			if (document == null) {
				Document document = new Document();
				Reader in = null;

				InputStream contentStream = null;
				try {
					contentStream = new FileInputStream(file);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if (contentStream == null) {
					return null;
				}

				try {
					String encoding = null;
					
					/*try {
						encoding = file.getCharset();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					*/

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

				} catch (IOException x) {
					x.printStackTrace();
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
		}

		return document;
	}
	
	
	
	
	public IStatus savePart() {
		System.err.println("Starting save");
		
		if (file == null){
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
		FileOutputStream fooStream = null;
		try {
			byteBuffer = encoder.encode(CharBuffer.wrap(document.get()));
			if (byteBuffer.hasArray())
				bytes= byteBuffer.array();
			else {
				bytes= new byte[byteBuffer.limit()];
				byteBuffer.get(bytes);
			}
			
			ByteArrayInputStream stream= new ByteArrayInputStream(bytes, 0, byteBuffer.limit());
			
			try {
			fooStream = new FileOutputStream(file, false);	// true to append
	         												// false to overwrite.
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
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
