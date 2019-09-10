package com.example.a2fevents;

import java.util.ArrayList;
import java.util.List;

public class FilesToDownload {

    private String destination;
    private List<Image> images;

    public FilesToDownload(String theDestination) {
        // Stores off instance variables
        destination = theDestination;
        images = new ArrayList<>();
    }

    public void addImage(Image newImage) {

        // Adds the provided link to the Image array
        images.add(newImage);
    }

    public String getDestination() {
        return destination;
    }

    public List<Image> getImages() {
        return images;
    }

    public int getNumImages() {
        return images.size();
    }

    public boolean hasImagesToDownload() {
        // Returns if links have been added to the links array
        return images.size() != 0;
    }

    public boolean hasImage(Image theImage) {

        // Determines if link has already been added
        for(int i = 0; i < images.size(); i++) {

            if(images.get(i).equals(theImage)) {
                return true;
            }
        }

        return false;
    }
}
