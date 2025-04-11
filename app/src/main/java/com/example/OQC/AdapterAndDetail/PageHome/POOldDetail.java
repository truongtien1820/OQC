package com.example.OQC.AdapterAndDetail.PageHome;

import java.io.File;

public class POOldDetail {
    private String POID,PONO;
    private String imageUrlPOOld,imageUrlCheck;
    private File tempImageFile;

    public String getPOID() {
        return POID;
    }

    public String getPONO() {
        return PONO;
    }

    public void setPOID(String POID) {
        this.POID = POID;
    }

    public void setPONO(String PONO) {
        this.PONO = PONO;
    }

    public String getImageUrlPOOld() {
        return imageUrlPOOld;
    }

    public String getImageUrlCheck() {
        return imageUrlCheck;
    }



    public void setImageUrlPOOld(String imageUrlPOOld) {
        this.imageUrlPOOld = imageUrlPOOld;
    }

    public void setImageUrlCheck(String imageUrlCheck) {
        this.imageUrlCheck = imageUrlCheck;
    }

    public File getTempImageFile() {
        return tempImageFile;
    }
    public void setTempImageFile(File tempImageFile) {
        this.tempImageFile = tempImageFile;
    }



    public POOldDetail (String POID,String PONO,  String imageUrlPOOld, String imageUrlCheck) {
        this.PONO = PONO;
        this.POID = POID;
        this.imageUrlPOOld = imageUrlPOOld;
        this.imageUrlCheck = imageUrlCheck;
    }
}
