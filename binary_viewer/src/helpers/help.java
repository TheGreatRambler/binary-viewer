package helpers;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class help {
	public static File getResource(String resource) {
		URL url = ClassLoader.getSystemResource(resource);
		File file = new File(resource);
		try {
			FileUtils.copyURLToFile(url, file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
}
