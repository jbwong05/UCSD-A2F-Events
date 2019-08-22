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
}
