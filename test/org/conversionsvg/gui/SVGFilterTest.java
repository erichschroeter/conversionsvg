/**
 * 
 */
package org.conversionsvg.gui;

import java.io.File;
import java.io.IOException;

import org.conversionsvg.gui.filters.SVGFilter;

/**
 * @author erich
 *
 */
public class SVGFilterTest extends AbstractFileSystemTestCase {

	public static final SVGFilter filter = new SVGFilter();
	
	public void testDirectory() {
		assertFalse(filter.accept(getTestRoot().getAbsoluteFile()));
	}
	
	public void testDirectoryWithExtension() {
		File directory = createFolder("test.svg");
		assertFalse(filter.accept(directory));
	}
	
	public void testNonSVGFile() throws IOException {
		File file = createFile("test.txt");
		assertFalse(filter.accept(file));
	}

	public void testLowerCaseExtension() throws IOException {
		File file = createFile("test.svg");
		assertTrue(filter.accept(file));
	}

	public void testUpperCaseExtension() throws IOException {
		File file = createFile("test.SVG");
		assertTrue(filter.accept(file));
	}
	
	public void testMixedCaseExtension() throws IOException {
		File file = createFile("test.SvG");
		assertTrue(filter.accept(file));
	}

}