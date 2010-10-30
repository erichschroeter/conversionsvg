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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;

/**
 * The Converter class handles calling Inkscapes command line feature.
 */
public class Converter extends Thread
{
    protected Map<String, String>                    options;
    protected String                                 command;
    protected Process                                process;
    protected List<InkscapeProcessCompletedListener> listeners;

    public Converter(File executable, Map<String, String> options)
    {
        command = executable.getAbsolutePath();
        listeners = new ArrayList<InkscapeProcessCompletedListener>();
        this.options = options;
    }

    public void addProcessCompletedListener(
            InkscapeProcessCompletedListener listener)
    {
        listeners.add(listener);
    }

    @Override
    public void run()
    {
        List<String> commandline = new ArrayList<String>();
        commandline.add(command);
        for (Map.Entry<String, String> pair : options.entrySet())
        {
            commandline.add(pair.getKey());
            commandline.add(pair.getValue());
        }

        try
        {
            // Execute the command
            process = new ProcessBuilder(commandline).start();
            process.waitFor();
        } catch (IOException e)
        {
            // TODO handle gracefully
        } catch (InterruptedException e)
        {
            // TODO handle gracefully
        }

        for (InkscapeProcessCompletedListener l : listeners)
        {
            InkscapeProcessInfo info = new InkscapeProcessInfo(options);
            l.processCompleted(info);
        }
    }
}