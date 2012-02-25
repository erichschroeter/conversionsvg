package usr.erichschroeter.conversionsvg;

import java.io.File;
import java.io.IOException;

import usr.erichschroeter.conversionsvg.util.SVGFilter;

public class SVGFilterTest extends AbstractFileSystemTestCase {

	private SVGFilter filter;
	
	public void testDirectory() {
		filter = new SVGFilter();
		assertTrue(filter.accept(getTestRoot().getAbsoluteFile()));
		File hiddenDir = createFolder(".hidden");
		assertFalse(filter.accept(hiddenDir));
		filter = new SVGFilter(true);
		assertTrue(filter.accept(hiddenDir));
	}
	
	public void testDirectoryWithExtension() {
		filter = new SVGFilter();
		File directory = createFolder("test.svg");
		assertTrue(filter.accept(directory));
	}
	
	public void testNonSVGFile() throws IOException {
		filter = new SVGFilter();
		File file = createFile("test.txt");
		assertFalse(filter.accept(file));
	}

	public void testLowerCaseExtension() throws IOException {
		filter = new SVGFilter();
		File file = createFile("test.svg");
		assertTrue(filter.accept(file));
	}

	public void testUpperCaseExtension() throws IOException {
		filter = new SVGFilter();
		File file = createFile("test.SVG");
		assertTrue(filter.accept(file));
	}
	
	public void testMixedCaseExtension() throws IOException {
		filter = new SVGFilter();
		File file = createFile("test.SvG");
		assertTrue(filter.accept(file));
	}

}
