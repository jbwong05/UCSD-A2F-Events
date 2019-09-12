package com.example.a2fevents;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class ImageDownloader implements Runnable {

    private Image image;
    private String destination;

    public ImageDownloader(Image imageToDownload, String theDestination) {
        image = imageToDownload;
        destination = theDestination;
    }

    @Override
    public void run() {
        downloadImages();
    }

    private void downloadImages() {

        try {
            // Opens channel to URL
            URL website = new URL(image.getLink());
            ReadableByteChannel readableByteChannel = Channels.newChannel(website.openStream());

            // Output stream
            FileOutputStream outputStream = new FileOutputStream(Image.getFullImagePath(destination, image.getName()));
            outputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
