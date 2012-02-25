package org.conversionsvg.gui;

import static org.junit.Assert.*;

import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import usr.erichschroeter.conversionsvg.InkscapeCommand;
import usr.erichschroeter.conversionsvg.InkscapeCommandBuilder;

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
		i.exportAsPdf(new File("test1.pdf"));
	}
	
	public void testExportMethods() {
		InkscapeCommandBuilder i = new InkscapeCommandBuilder();
		i.setFiles(new File("test.svg"));
		i.exportAsPdf(new File("test1.pdf"));
		i.exportAsEps(new File("test1.eps"));
		i.exportAsPng(new File("test1.png"));
		i.exportAsPs(new File("test1.ps"));
		
		List<InkscapeCommand> commands = i.getCommands();
		assertNotNull(commands);
		assertEquals(4, commands.size());
	}
	
}
