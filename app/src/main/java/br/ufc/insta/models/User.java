package br.ufc.insta.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User implements Parcelable {
    @SerializedName("id")
    String id;

    @SerializedName("fullName")
    String fullName;

    @SerializedName("nickName")
    String nickName;

    @SerializedName("password")
    String password;

    @SerializedName("email")
    String email;

    @SerializedName("photo")
    String photo;

    @SerializedName("phone")
    String phone;

    @SerializedName("birthday")
    String birthday;

    @SerializedName("posts")
    public List<Post> posts = null;

    public User(){

    }

    public User(String fullName, String email, String nickName, String password, String photo) {
        this.fullName = fullName;
        this.email = email;
        this.nickName = nickName;
        this.password = password;
        this.photo = photo;
    }

    protected User(Parcel in) {
        id = in.readString();
        fullName = in.readString();
        nickName = in.readString();
        password = in.readString();
        email = in.readString();
        photo = in.readString();
        phone = in.readString();
        birthday = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        if(photo != null){
            return photo.replace("cloud.google", "googleapis");
        }
        return null;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(fullName);
        dest.writeString(nickName);
        dest.writeString(password);
        dest.writeString(email);
        dest.writeString(photo);
        dest.writeString(phone);
        dest.writeString(birthday);
    }
}
