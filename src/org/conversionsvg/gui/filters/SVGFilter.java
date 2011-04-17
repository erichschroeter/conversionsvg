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

    public boolean accept(File file)
    {
    	if (file.isDirectory()) return false;
    	
    	String fileName = file.getName();
        int i = fileName.lastIndexOf('.');
        if (i > 0 && i < fileName.length() - 1)
        {
            String extension = fileName.substring(i+1);
            if(extension.equalsIgnoreCase("svg"))
            {
                return true;
            }
        }
        return false;
    }

    public String getDescription()
    {
        return "Scalable Vector Graphics (*.svg)";
    }

}
