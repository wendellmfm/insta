package br.ufc.insta.models;

import com.google.gson.annotations.SerializedName;

public class Like {

    @SerializedName("id")
    private String id;

    @SerializedName("nrCurtidas")
    private String nrCurtidas;

    boolean likestatus;

    public Like(boolean likestatus) {
        this.likestatus = likestatus;
    }

    public boolean isLikestatus() {
        return likestatus;
    }

    public void setLikestatus(boolean likestatus) {
        this.likestatus = likestatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNrCurtidas() {
        return nrCurtidas;
    }

    public void setNrCurtidas(String nrCurtidas) {
        this.nrCurtidas = nrCurtidas;
    }


}
