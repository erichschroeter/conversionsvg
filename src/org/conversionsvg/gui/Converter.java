/*
    ConversionSVG 1.0
    Programm� par Kevin Albert
    Copyright (C) 2007-2008  Soci�t� Grics

    This file is part of ConversionSVG 1.0

    ConversionSVG 1.0 is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ConversionSVG 1.0 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ConversionSVG 1.0.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.conversionsvg.gui;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * The Converter class handles calling Inkscape's command line feature.
 */
public class Converter extends Thread implements Comparable<Converter> {

	static final Logger logger = Logger.getLogger(Converter.class);

	/**
	 * The Inkscape executable file.
	 */
	protected File exe;

	/**
	 * The command containing all the command options and arguments to be passed
	 * to Inkscape command line.
	 * <p>
	 * This <code>List</code> object is passed to a <code>ProcessBuilder</code>
	 * </p>
	 */
	protected List<String> command;

	/**
	 * A list of listeners that need to be notified when an Inkscape process has
	 * completed.
	 */
	protected List<InkscapeProcessCompletedListener> listeners;

	public Converter(File inkscape, List<String> command) {
		exe = inkscape;
		listeners = new ArrayList<InkscapeProcessCompletedListener>();
		this.command = command;
	}

	public void addProcessCompletedListener(
			InkscapeProcessCompletedListener listener) {
		listeners.add(listener);
	}

	@Override
	public void run() {
		
		Process process;
		try {
			// Execute the command
			process = new ProcessBuilder(command).start();
			process.waitFor();
		} catch (IOException e) {
			logger.error(e.getMessage());
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}

		for (InkscapeProcessCompletedListener l : listeners) {
			InkscapeProcessInfo info = new InkscapeProcessInfo(command);
			l.processCompleted(info);
		}
	}

	@Override
	public int compareTo(Converter o) {
		return 0;
	}
}