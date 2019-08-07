package com.example.a2fevents;

import java.io.File;

public class FilesToDownload {

    private File destination;
    private String[] links;

    public FilesToDownload(File theDestination, String theFirstLink) {
        destination = theDestination;
        links = new String[] {theFirstLink};
    }

    public FilesToDownload(File theDestination, String theFirstLink, String theSecondLink) {
        destination = theDestination;
        links = new String[] {theFirstLink, theSecondLink};
    }

    public FilesToDownload(File theDestination, String theFirstLink, String theSecondLink, String theThirdLink) {
        destination = theDestination;
        links = new String[] {theFirstLink, theSecondLink, theThirdLink};
    }

    public String getDestination() {
        return destination.getAbsolutePath();
    }

    public String[] getLinks() {
        return links;
    }
}
