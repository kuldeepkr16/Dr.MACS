package com.example.lenovo.dra.POJO;

public class ReadPojo {
    private String Image,desc,title, NameWhoPosted;
    public ReadPojo(){

    }

    public ReadPojo(String image, String desc, String title, String NameWhoPosted) {
        Image = image;
        this.desc = desc;
        this.title = title;
        this.NameWhoPosted = NameWhoPosted;
    }

    public String getImage() {
        return Image;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }
    public String getNameWhoPosted() {
        return NameWhoPosted;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setNameWhoPosted(String title) {
        this.title = title;
    }
}
