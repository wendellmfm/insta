package br.ufc.insta.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("id")
    String id;

    @SerializedName("fullName")
    String fullName;

    @SerializedName("nickname")
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
        return photo;
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
}
