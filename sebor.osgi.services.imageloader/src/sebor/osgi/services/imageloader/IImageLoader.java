package sebor.osgi.services.imageloader;

import org.eclipse.swt.graphics.Image;

public interface IImageLoader {
	
		public Image loadImage(Class<?> clazz, String path) ;
	}

