package br.ufc.insta.service;

import java.util.List;

import br.ufc.insta.models.Post;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GetDataService {
//    @GET("/photos")
//    Call<List<Post>> getAllPhotos();

    @FormUrlEncoded
    @POST("/api/contact")
    Call<String> getUserRegister(
            @Field("id") String id,
            @Field("fullName") String fullName,
            @Field("nickName") String nickName,
            @Field("password") String password,
            @Field("email") String email,
            @Field("photo") String photo,
            @Field("phone") String phone,
            @Field("birthday") String birthday,
            @Field("posts") List<Post> posts
    );
}
