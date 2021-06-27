package com.system.dms.entity;

import java.io.File;

public class HeadImage {
    private File headImg;
    private String headImgContantType;
    private String headImgFileName;

    public File getHeadImg() {
        return headImg;
    }

    public void setHeadImg(File headImg) {
        this.headImg = headImg;
    }

    public String getHeadImgContantType() {
        return headImgContantType;
    }

    public void setHeadImgContantType(String headImgContantType) {
        this.headImgContantType = headImgContantType;
    }

    public String getHeadImgFileName() {
        return headImgFileName;
    }

    public void setHeadImgFileName(String headImgFileName) {
        this.headImgFileName = headImgFileName;
    }

    @Override
    public String toString() {
        return "HeadImage{" +
                "headImg=" + headImg +
                ", headImgContantType='" + headImgContantType + '\'' +
                ", headImgFileName='" + headImgFileName + '\'' +
                '}';
    }
}
