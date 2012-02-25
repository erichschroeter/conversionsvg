package usr.erichschroeter.conversionsvg;

import java.io.File;
import java.io.IOException;

import usr.erichschroeter.conversionsvg.util.NonHiddenDirectoryFilter;


public class NonHiddenDirectoryFilterTest extends AbstractFileSystemTestCase {

	public static final NonHiddenDirectoryFilter filter = new NonHiddenDirectoryFilter();
	
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

	public void testDirectoryWindowsHidden() throws IOException {
		File directory = createFolder("hidden");
		// only way to make a hidden file in Windows is via Win32 API call
		if (System.getProperty("os.name").startsWith("Windows")) {
			Runtime.getRuntime().exec("attrib +H " + directory.getAbsolutePath());
			assertFalse(filter.accept(directory));
		} else {
			// if not on windows then just pass
			assertTrue(filter.accept(directory));
		}
	}
	
	public void testDirectoryLinuxHidden() {
		File directory = createFolder(".hidden");
		assertFalse(filter.accept(directory));
	}
	
	public void testFile() throws IOException {
		File file = createFile("test.txt");
		assertFalse(filter.accept(file));
	}
}
