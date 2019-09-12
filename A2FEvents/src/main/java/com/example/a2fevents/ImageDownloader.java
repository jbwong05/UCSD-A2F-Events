package com.example.a2fevents;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

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
        // Downloads images in a background thread
        int count;

        try {
            // Connect to the url
            URL url = new URL(image.getLink());
            URLConnection connection = url.openConnection();
            connection.connect();

            // Download the file
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            // Output stream
            OutputStream output = new FileOutputStream(Image.getFullImagePath(destination, image.getName()));

            byte[] data = new byte[1024];

            long total = 0;

            // While there are still bytes to write
            while ((count = input.read(data)) != -1) {
                total += count;

                // Writing data to file
                output.write(data, 0, count);
            }

            // Flushing output
            output.flush();

            // Closing streams
            output.close();
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
