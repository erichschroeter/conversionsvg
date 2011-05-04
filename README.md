# conversion-svg

Contributors: Erich Schroeter, _see **Credit** section_

ConversionSVG is a frontend wrapper around Inkscape's command line feature. It
provides users who want to perform batch conversions of their SVG files into
the other file formats Inkscape can export to.

## Description

**Why does ConversionSVG exist?**

I needed an easy way for me to perform batch conversions of SVG files for work,
because it would take too much time to open each file and export individually.
I did some searching around for batch image converters for SVG and came across
ConversionSVG on SourceForge, and it worked in the short run, but eventually I
got tired of the user interface. I didn't want to have to select whether I was
converting files in a single directory or a single file; I wanted to select the
files I wanted to convert faster than the user interface allowed me to.
[SourceForge ConversionSVG][sfConversionSVG]

It had been about a year since I had done any programming in Java, and this
seemed like a small enough project to get back in. When I started getting into
the code, it difficult to read since it was written in French. So I had to go
around refactoring the code so I could read it. As I did that ideas began
popping up in my head for improvements.

Part of the requirements I had personally was to not spend a ton of time on this;
basically quickly improve this application for personal use, but allow others
to use it if they come across it in their pursuit to improve their efficiency.
With that said, it is the main reason I reuse code I've found through searching
the internet for the implementing the features in ConversionSVG.

## Installation

### Prerequisites

The following are what I used.

- [Inkscape][inkscape]
- Java 6
- JUnit 4

### Building

 I used Eclipse to develop ConversionSVG, and I setup each group of source code
 as individual projects. So, to replicate what I did, you would need to add a
 new Java Project using existing source (selecting the top directory). Then for
 the projects that use JUnit you would have to add the JUnit library to their
 project build-path settings.  

## Credit

_**Recognized Code Usage in ConversionSVG**_

I started with metalkev64's code from SourceForge, but eventually rewrote most
of it. Without his contributions, I probably would not have even started this.
[ConversionSVG on SourceForge][sfConversionSVG]

I didn't want to use menu's; ever since I used Office 2007 with the Ribbon I
refuse to use menus where I can and use Ribbons instead. So, upon doing some
research I came across the flamingo swing component suite. [Flamingo library][flamingo]

I wanted to improve the way I selected files to convert, and thought of the
checkbox tree I've seen in Windows. After some searching, I came across the
CheckBoxTree the jide library provides. [Powered by JIDE (http://www.jidesoft.com)][jide] and [JIDE Common Layer][jide-common]

The background color selection in the original app was too slow for me, so I
wondered if there was existing code that implemented a color picker. I came
across the Color Picker at java.net. [ColorChooser Widget][colorchooser]

I didn't want to spend a lot of time on how to access the file system in a
device independent way in Java, so I did some searching on how to do this in
Java and came across a SF project which provided some code that happened to
work for me. [fstreem on SourceForge][fstreem]

Most of the icons used are from the Tango Desktop Project. [Tango Icons][tango]

The flags used for language icons are a set created by Go Squared Ltd. [GoSquared Flag Icons][gosquared]

[inkscape]: http://inkscape.org/ "Open source vector graphics editor"
[sfConversionSVG]: http://sourceforge.net/projects/conversionsvg/ "The original application"
[flamingo]: https://flamingo.dev.java.net/ "A collection of components for Swing applications"
[jide-common]: https://jide-oss.dev.java.net/ "JIDE Common Layer (Professional Swing Components)"
[jide]: http://www.jidesoft.com "Powered by JIDE (http://www.jidesoft.com)"
[colorchooser]: https://colorchooser.dev.java.net/ "A Swing widget for selecting colors with single mouse gesture"
[fstreem]: http://sourceforge.net/projects/fstreem/files/ "SourceForge File System TreeModel"
[tango]: http://tango.freedesktop.org/Tango_Icon_Library "The Tango Desktop Project"
[gosquared]: http://www.gosquared.com/liquidicity/archives/1493 "2400 Flag Icon Set"
