# Conversion SVG

ConversionSVG is a frontend wrapper around Inkscape's command line feature. It
provides users who want to perform batch conversions of their SVG files into
the other file formats Inkscape can export to.

## Installation

### Prerequisites

- [Inkscape][inkscape] is installed
- [Java 6][2] is installed

[Download Conversion SVG][1] and run it.

### Libraries Used

[ConversionSVG on SourceForge][0] -- I started with metalkev64's code from SourceForge, but eventually rewrote most of it. Without his contributions, I probably would not have even started this.

[Flamingo library][flamingo] -- I didn't want to use menu's; ever since I used Office 2007 with the Ribbon I refuse to use menus where I can and use Ribbons instead. So, upon doing some research I came across the flamingo swing component suite.

[JIDE Common Layer][jide-common] -- I wanted to improve the way I selected files to convert, and thought of the checkbox tree I've seen in Windows. After some searching, I came across the CheckBoxTree the jide library provides. 

[ColorChooser Widget][colorchooser] -- The background color selection in the original app was too slow for me, so I wondered if there was existing code that implemented a color picker. I came across the Color Picker at java.net.

[fstreem on SourceForge][fstreem] -- I didn't want to spend a lot of time on how to access the file system in a device independent way in Java, so I did some searching on how to do this in Java and came across a SF project which provided some code that happened to work for me.

[Tango Icons][tango] -- Most of the icons used are from the Tango Desktop Project.

[GoSquared Flag Icons][gosquared] -- The flags used for language icons are a set created by Go Squared Ltd.

[0]: http://sourceforge.net/projects/conversionsvg/ "The original application"
[1]: https://github.com/erichschroeter/conversionsvg/downloads "Downloads page"
[2]: http://java.com/en/download/ "Java download page"
[inkscape]: http://inkscape.org/ "Open source vector graphics editor"
[flamingo]: http://mvnrepository.com/artifact/com.github.insubstantial/flamingo/7.0 "A collection of ribbon components for Swing applications"
[jide-common]: https://jide-oss.dev.java.net/ "JIDE Common Layer (Professional Swing Components)"
[colorchooser]: https://colorchooser.dev.java.net/ "A Swing widget for selecting colors with single mouse gesture"
[fstreem]: http://sourceforge.net/projects/fstreem/files/ "SourceForge File System TreeModel"
[tango]: http://tango.freedesktop.org/Tango_Icon_Library "The Tango Desktop Project"
[gosquared]: http://www.gosquared.com/liquidicity/archives/1493 "2400 Flag Icon Set"
