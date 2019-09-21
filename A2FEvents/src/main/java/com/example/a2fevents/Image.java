package com.example.a2fevents;

import androidx.annotation.Nullable;

public class Image {
    private String name;
    private String link;

    public Image(String theName, String theLink) {
        name = theName;
        link = theLink;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        // Determines equality using image file names
        return (obj instanceof Image) && this.name.equals(((Image) obj).getName());
    }

    public static String getFullImagePath(String path, String imageName) {
        // Retrieves the full image path
        return path + "/" + StringConstants.IMAGE_PREFIX + imageName.hashCode() + getExtension(imageName);
    }

    private static String getExtension(String imageName) {
        String extension = imageName;
        while(extension.matches(".*[.][a-z]{3}$")) {
            extension = extension.substring(extension.indexOf('.') + 1);
        }

        return "." + extension;
    }
}
