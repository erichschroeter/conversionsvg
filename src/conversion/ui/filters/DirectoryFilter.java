package conversion.ui.filters;

import java.io.File;
import java.io.FileFilter;

public class DirectoryFilter implements FileFilter {

	@Override
	public boolean accept(File file) {
        return file.isDirectory();
	}

}
