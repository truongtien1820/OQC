package com.example.OQC.AdapterAndDetail.PageDetail;

import java.io.File;

public class ImagePODetail {
    private String title;
    private String imageUrl;
    private File tempImageFile;
    private String mvlnho;

    public ImagePODetail(String title, String imageUrl , String mvlnho) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.mvlnho = mvlnho;
    }
    public String getMvlnho() {
        return mvlnho;
    }
    public void setMvlnho(String mvlnho) {
        this.mvlnho = mvlnho;
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
