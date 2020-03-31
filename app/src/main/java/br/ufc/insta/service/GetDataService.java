package br.ufc.insta.service;

import java.util.List;

import br.ufc.insta.models.Post;
import br.ufc.insta.utils.RetroPhoto;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GetDataService {
    @GET("/photos")
    Call<List<Post>> getAllPhotos();
}
