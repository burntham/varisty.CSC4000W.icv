HoughDiskDetector
=================

Varsity: Interactive computer vision assignment
Detecting circles in an image in Java using the HoughTransform

To be done:
-Refactoring
-hysteresis thresholding in Canny Edge detection
-Reduce memory footprint by using in place transpose etc.
-Thresholding the image after Gauss blur
-A more robust graphical display (that does not depend on screen size)
-Testing on other grey scale images

a sample image, test.gif is included

NB! Image files that are to be processed MUST be in the root directory and they MUST be .gif.
The code can be compiled by running either compiling the Main.java file in the root directory, or by running make. (Both files should be in the root directory already).
Two copies of test.gif are supplied, making it easier to run in IntelliJ and via the make file

Once compiled, the application can be executed by typing ‘java Main’ or ‘make run’ into terminal.

The program will request the image name in terminal as well as the minimum and maximum radii you wish to detect. Once entered, it will confirm that it can read the file and process the image.

After the image is processed, a window will open up and display various stages of the image processing.
NOTE: A full HD display is recommended

From left to write it will display: Original, GaussFitlered, SobelOperated, Canny(sobel with non maximal supression), the Hough space and the original image with features highlighted.

