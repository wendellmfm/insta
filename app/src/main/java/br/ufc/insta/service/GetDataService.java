package br.ufc.insta.service;

import java.util.List;

import br.ufc.insta.models.Post;
import br.ufc.insta.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @POST("/api/contact")
    Call<User> createUser(@Body User user);

    @GET("/api/contact/nickname/{nickName}")
    Call<User> getUserByNickname(@Path("nickName") String nickName);

    @GET("/api/contacts")
    Call<List<User>> getAllUsers();

    @GET("api/authenticate")
    Call<User> userLogin(@Query("username") String username, @Query("password") String password);
}
