package com.example.a2fevents;

public class FilesToDownload {

    private static final int MAX_NUM_EVENTS = 3;
    private static final int FIRST_LINK_INDEX = 0;
    private static final int SECOND_LINK_INDEX = 1;
    private static final int THIRD_LINK_INDEX = 2;
    private String destination;
    private Image[] images;
    private int numImages;

    public FilesToDownload(String theDestination) {
        // Stores off instance variables
        destination = theDestination;
        images = new Image[MAX_NUM_EVENTS];
        numImages = 0;
    }

    public void addImage(Image newImage) {

        // Adds the provided link to the Image array
        int newIndex = images[FIRST_LINK_INDEX] == null ? FIRST_LINK_INDEX : (images[SECOND_LINK_INDEX] == null ? SECOND_LINK_INDEX : THIRD_LINK_INDEX);

        images[newIndex] = newImage;
        numImages++;
    }

    public String getDestination() {
        return destination;
    }

    public Image[] getImages() {
        return images;
    }

    public int getNumImages() {
        return numImages;
    }

    public boolean hasImagesToDownload() {
        // Returns if links have been added to the links array
        return numImages != 0;
    }

    public boolean hasImage(Image theImage) {

        // Determines if link has already been added
        for(int i = 0; i < numImages; i++) {

            if(images[i].equals(theImage)) {
                return true;
            }
        }

        return false;
    }
}
