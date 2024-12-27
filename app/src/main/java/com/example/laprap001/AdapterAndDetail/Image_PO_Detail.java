package com.example.laprap001.AdapterAndDetail;

import java.io.File;

public class Image_PO_Detail {
    private String title;
    private String imageUrl;
    private File tempImageFile;
    public Image_PO_Detail(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public File getTempImageFile() {
        return tempImageFile;
    }
    public void setTempImageFile(File tempImageFile) {
        this.tempImageFile = tempImageFile;
    }
}
