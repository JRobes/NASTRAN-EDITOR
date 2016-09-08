package com.femeditors.nastran.sourceviewerconf;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.PatternRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class NastranSourceViewerConf extends SourceViewerConfiguration {
	public ITokenScanner tokenScanner;
	public IRule patternRule;
	public IRule endOfLineRule;
	public IRule multiLineaRegla;
	
	public NastranSourceViewerConf(){
		tokenScanner = createTokenScanner();
	}
	
	//@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer viewer) {
		 System.out.println("getPresentationReconciler....l");

		 PresentationReconciler reconciler= new PresentationReconciler();
		 DefaultDamagerRepairer dflt= new DefaultDamagerRepairer(tokenScanner);
		 reconciler.setDamager(dflt, IDocument.DEFAULT_CONTENT_TYPE);
		 reconciler.setRepairer(dflt, IDocument.DEFAULT_CONTENT_TYPE);
		 return reconciler;
	}
	private ITokenScanner createTokenScanner() {
		 RuleBasedScanner scanner= new RuleBasedScanner();
		 scanner.setRules(createRules());
		 return scanner;
	}
	private IRule[] createRules() {
		 Display display = Display.getCurrent();
		 if(display!=null){
			 System.out.println("display != null");
		 }
		 Color blue = display.getSystemColor(SWT.COLOR_BLUE);
		 Color green = display.getSystemColor(SWT.COLOR_GREEN);
		 Color magenta = display.getSystemColor(SWT.COLOR_DARK_MAGENTA);

		 IToken tokenA= new Token(new TextAttribute(blue));
		 IToken tokenB= new Token(new TextAttribute(green));
		 IToken tokenC= new Token(new TextAttribute(magenta, null, SWT.BOLD));

		 URL url = Platform.getInstanceLocation().getURL();
		 String instancePath = url.getPath();
		 String osAppropriatePath = System.getProperty("os.name").contains("indow") ? instancePath.substring(1) : instancePath;		 
		 Path path = Paths.get(osAppropriatePath, "syntax", "BULK-DATA-ENTRIES.syn");
		 System.out.println("TestSourceViewerConfffffff ñÑÑÑÑÑÑÑÑÑÑÑ");
		 IRule[] iRules; 
		 patternRule= new PatternRule(">", "<", tokenA, 'R', false);
		 endOfLineRule = new EndOfLineRule("$", tokenB);
		 multiLineaRegla = new MultiLineRule("'","'",tokenC);
		 WordRule keywords = new WordRule(new NASTRANWordDetector());

		 if (Files.exists(path)){
			 System.out.println("En TestSourceViewerConf......");
				 
			BufferedReader linea;
			try {
				linea = new BufferedReader(new FileReader(path.toString()));
				try {
					String aux = linea.readLine(); 
					String[] words = aux.trim().split("\\s++");
					System.out.println("El numero de palabras es:\t" + words.length);
					iRules = new IRule[words.length+1];
					linea.close();
					iRules[0] = endOfLineRule;
					
				        for (int i = 0; i < words.length; i++) {
				            String keyword = words[i];
				            keywords.addWord(keyword, tokenA);
				        }
				        
					
										
					
				} catch (IOException e) {
					e.printStackTrace();
				}	
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			
			
		 }
		 else{
		    	
		 }
		 
		
		// begginOfLineRule = new BegginOfLineRule();
		
		 
		 
		 //System.out.println("EL IRULESTIENE LONGITUD :" +iRules.length);
		 
		 return new IRule[] {endOfLineRule, keywords, multiLineaRegla};
	}
	
	class NASTRANWordDetector implements IWordDetector {

        /* (non-Javadoc)
         * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
         */
        public boolean isWordStart(char c) {
            return Character.isLetter(c);
        }

        /* (non-Javadoc)
         * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
         */
        public boolean isWordPart(char c) {
            return Character.isLetter(c) || Character.isDigit(c);
        }
    }
	private void jander(){
			 System.out.println("jander....");
		}
}
