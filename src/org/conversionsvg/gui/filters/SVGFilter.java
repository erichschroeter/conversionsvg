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

package org.conversionsvg.gui.filters;

import java.io.File;
import java.io.FileFilter;

public class SVGFilter implements FileFilter {

	/** Include directories */
	private boolean includeDirectories = false;
	/** Include hidden directories */
	private boolean includeHidden = false;

	/**
	 * Creates a filter that filters based on SVG files. Does not include hidden
	 * directories.
	 * 
	 * @see #SVGFilter(boolean)
	 */
	public SVGFilter() {
		this(false);
	}

	/**
	 * Creates a filter that filters based on SVG files. Includes directories.
	 * 
	 * @see #SVGFilter(boolean, boolean)
	 * @param includeHidden
	 *            include hidden directories
	 */
	public SVGFilter(boolean includeHidden) {
		this(true, includeHidden);
	}

	/**
	 * Creates a filter that filters based on SVG files.
	 * 
	 * @param directory
	 *            include directories
	 * @param includeHidden
	 *            include hidden directories
	 */
	public SVGFilter(boolean directory, boolean includeHidden) {
		this.includeDirectories = directory;
		this.includeHidden = includeHidden;
	}

	public boolean accept(File file) {
		if (file.isDirectory()) {
			if (includeDirectories) {
				if (includeHidden && file.isHidden()) {
					return true;
				} else if (!includeHidden && file.isHidden()) {
					return false;
				}
				return true;
			} else {
				return false;
			}
		}

		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		if (i > 0 && i < fileName.length() - 1) {
			String extension = fileName.substring(i + 1);
			if (extension.equalsIgnoreCase("svg")) {
				return true;
			}
		}
		return false;
	}

	public String getDescription() {
		return "Scalable Vector Graphics (*.svg)";
	}

}
