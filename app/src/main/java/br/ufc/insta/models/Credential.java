package br.ufc.insta.models;

import com.google.gson.annotations.SerializedName;

public class Credential {

    @SerializedName("nickname")
    String nickName;

    @SerializedName("password")
    String password;

    public Credential(String nickName, String password){
        this.nickName = nickName;
        this.password = password;
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

}
