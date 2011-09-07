package org.conversionsvg.gui;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.conversionsvg.InkscapeCommandBuilder;

public class InkscapeCommandBuilderTest {

	public void testCommand() {
		InkscapeCommandBuilder i = new InkscapeCommandBuilder();
		File test1 = new File("test1.svg");
		File test2 = new File("test2.svg");
		File test3 = new File("test3.svg");
		List<File> files = new Vector<File>(Arrays.asList(test1, test2, test3));
		i.setFiles(files);
		i.setBackgroundColor(Color.GREEN);
		i.setBackgroundOpacity(0.5);
		i.exportAsPdf();
	}
	
	public void testExportMethods() {
		InkscapeCommandBuilder i = new InkscapeCommandBuilder();
		i.exportAsPdf();
		i.exportAsEps();
		i.exportAsPng();
		i.exportAsPs();
		
		List<List<String>> commands = i.getCommands();
	}
	
}
