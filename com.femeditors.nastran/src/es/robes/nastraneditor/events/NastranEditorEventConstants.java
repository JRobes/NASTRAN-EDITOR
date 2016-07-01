package es.robes.nastraneditor.events;

public interface NastranEditorEventConstants {
	String APPLICATION_TITLE = "Nastran Editor";

	String WHITE_CHARACTERS_ENABLED = "TRUE";
	String WHITE_CHARACTERS_DISABLED = "FALSE";
	String WHITE_CHARACTERS_STATUS = "WHITE-CHARACTERS-STATUS";
	
	String NEWFILE = "NEW";
	
	
	String FILE_ALL_EVENTS = "FILE/*";
	String FILE_NEW = "FILE/NEW";
	String FILE_CLOSE = "FILE/CLOSE";
	String FILE_RENAME ="FILE/RENAME";

	String STATUSBAR ="statusbar";
	
	//the buttons up/down of the search toolbar send events to be received by the 
	//Text Control. then, the control send message to the editor to indicate the 
	//word to search and the direcction to search
	String FIND_TEXT_BUTTON_ALL_EVENTS = "FIND_TEXT_BUTTON/*";
	String FIND_TEXT_BUTTON_UP = "FIND_TEXT_BUTTON/UP";
	String FIND_TEXT_BUTTON_DOWN = "FIND_TEXT_BUTTON/DOWN";
	String FIND_TEXT_ALL_EVENTS = "FIND_TEXT_BUTTON/*";
	String FIND_TEXT_UP = "FIND_TEXT_UP";
	String FIND_TEXT_DOWN = "FIND_TEXT_DOWN";

	
	
	
}
