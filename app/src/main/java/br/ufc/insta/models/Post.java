package br.ufc.insta.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Post {
    @SerializedName("title")
    private String title;

    private String desc;

    @SerializedName("thumbnailUrl")
    private String photourl;

    private Date timestamp;

    public Post(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Post(String title, String desc, String photourl, Date timestamp) {
        this.title = title;
        this.desc = desc;
        this.photourl = photourl;
        this.timestamp = timestamp;
    }
}
