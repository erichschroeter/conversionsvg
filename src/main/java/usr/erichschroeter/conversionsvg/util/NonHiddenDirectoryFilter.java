package usr.erichschroeter.conversionsvg.util;

import java.io.File;
import java.io.FileFilter;

public class NonHiddenDirectoryFilter implements FileFilter {

	@Override
	public boolean accept(File file) {
		if (!file.isDirectory()) return false;
		
		return !file.isHidden();
	}

}
