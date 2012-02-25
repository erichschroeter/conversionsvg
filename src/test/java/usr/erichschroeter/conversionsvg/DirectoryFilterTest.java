package usr.erichschroeter.conversionsvg;

import java.io.File;
import java.io.IOException;

import usr.erichschroeter.conversionsvg.util.DirectoryFilter;

public class DirectoryFilterTest extends AbstractFileSystemTestCase {

	public static final DirectoryFilter filter = new DirectoryFilter();
	
	public void testDirectory() {
		assertTrue(filter.accept(getTestRoot().getAbsoluteFile()));
	}

	public void testDirectoryWithExtension() {
		File directory = createFolder("test.extension");
		assertTrue(filter.accept(directory));
	}

	public void testDirectoryWithNoExtension() {
		File directory = createFolder("test.");
		assertTrue(filter.accept(directory));
	}

	public void testDirectoryLinuxHidden() {
		File directory = createFolder(".hidden");
		assertTrue(filter.accept(directory));
	}
	
	public void testFile() throws IOException {
		File file = createFile("test.txt");
		assertFalse(filter.accept(file));
	}
}
