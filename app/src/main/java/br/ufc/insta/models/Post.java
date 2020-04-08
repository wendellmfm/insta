package br.ufc.insta.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Post implements Parcelable {

    @SerializedName("id")
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

    protected Post(Parcel in) {
        id = in.readString();
        text = in.readString();
        photoPost = in.readString();
        datePost = in.readString();
    }

    public static final Creator<Post> CREATOR = new Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel in) {
            return new Post(in);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(text);
        dest.writeString(photoPost);
        dest.writeString(datePost);
    }
}
