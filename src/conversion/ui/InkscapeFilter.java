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

package conversion.ui;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * This class is a filter that only allows the user to navigate through
 * directories to find the Inkscape executable.
 */
public class InkscapeFilter extends FileFilter{

    public boolean accept(File file)
    {
    	// allow user to navigate through directories in a filechooser
        if(file.isDirectory())
        {
            return true;
        }
        
        String fileName = file.getName();
        // we need to be able to execute Inkscape in order for this app to work
        if (file.canExecute())
        {
	        if (fileName.equals("inkscape.exe") ||
	        	fileName.equals("inkscape"))
            {
                return true;
            }
	    }
        return false;
    }

    public String getDescription()
    {
        return "Inkscape Executable";
    }

}
