JAVAC = javac
JAVACFLAGS =

SRC =Main.java \
	com/brunt/Viewer/Window.java \
    com/brunt/ImageProcessing/Discs.java \
    com/brunt/ImageProcessing/HoughTransform2.java \
    com/brunt/ImageProcessing/ImageManager.java \
    com/brunt/ImageProcessing/Theta.java \
    com/brunt/ImageProcessing/Utils.java \
    com/brunt/ImageProcessing/Filters2/Filter.java \
    com/brunt/ImageProcessing/Filters2/CannyEdgeDetection.java \
    com/brunt/ImageProcessing/Filters2/GaussianFilter2.java \
    com/brunt/ImageProcessing/Filters2/SobelFilter2.java


CLS= $(SRC:.java=.class)

.SUFFIXES : .class .java
.java.class :
	$(JAVAC) $(JAVACFLAGS) $<

all:  $(CLS)

clean:
	find . -name "*.class" -type f -delete