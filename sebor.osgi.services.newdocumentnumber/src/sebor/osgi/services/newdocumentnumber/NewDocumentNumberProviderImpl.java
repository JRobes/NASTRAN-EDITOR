package sebor.osgi.services.newdocumentnumber;

public class NewDocumentNumberProviderImpl implements INewDocumentNumberProvider {
	private static int number = 0;
	@Override
	public String getNewDocumentNumber() {
		number++;
		return Integer.toString(number);
	}

}
