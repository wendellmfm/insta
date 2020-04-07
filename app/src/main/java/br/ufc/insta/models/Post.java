package br.ufc.insta.models;

import com.google.gson.annotations.SerializedName;

public class Post {

    @SerializedName("title")
    private String id;

    @SerializedName("text")
    private String text;

    @SerializedName("photoPost")
    private String photoPost;

    @SerializedName("datePost")
    private String datePost;

    public Post(){

    }

    public Post(String id, String text, String photoPost, String datePost) {
        this.id = id;
        this.text = text;
        this.photoPost = photoPost;
        this.datePost = datePost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPhotoPost() {
        return photoPost;
    }

    public void setPhotoPost(String photoPost) {
        this.photoPost = photoPost;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }
}
