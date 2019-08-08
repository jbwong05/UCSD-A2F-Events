package com.example.a2fevents;

public class FilesToDownload {

    private static final int MAX_NUM_EVENTS = 3;
    private static final int FIRST_LINK_INDEX = 0;
    private static final int SECOND_LINK_INDEX = 1;
    private static final int THIRD_LINK_INDEX = 2;
    private String destination;
    private String[] links;
    private int numLinks;

    public FilesToDownload(String theDestination) {
        // Stores off instance variables
        destination = theDestination;
        links = new String[MAX_NUM_EVENTS];
        numLinks = 0;
    }

    public void addFile(String link) {

        // Adds the provided link to the String array
        int newIndex = links[FIRST_LINK_INDEX] == null ? FIRST_LINK_INDEX : (links[SECOND_LINK_INDEX] == null ? SECOND_LINK_INDEX : THIRD_LINK_INDEX);

        links[newIndex] = link;
        numLinks++;
    }

    public String getDestination() {
        return destination;
    }

    public String[] getLinks() {
        return links;
    }

    public int getNumLinks() {
        return numLinks;
    }

    public boolean hasFilesToDownload() {
        // Returns if links have been added to the links array
        return numLinks != 0;
    }

    public boolean hasLink(String link) {

        // Determines if link has already been added
        for(int i = 0; i < numLinks; i++) {

            if(links[i].equals(link)) {
                return true;
            }
        }

        return false;
    }
}
