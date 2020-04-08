package br.ufc.insta.service;

import java.util.List;

import br.ufc.insta.models.Like;
import br.ufc.insta.models.Post;
import br.ufc.insta.models.User;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {

    @POST("/api/contact")
    Call<User> createUser(@Body User user);

    @GET("/api/contact/nickname/{nickName}")
    Call<User> getUserByNickname(@Path("nickName") String nickName);

    @GET("/api/post/contactid/{id}")
    Call<List<Post>> getUserPosts(@Path("id") String id);

    @GET("/api/post/{datainicial}/{datafinal}")
    Call<List<Post>> getUserPostsByData(@Path("datainicial") String datainicial, @Path("datafinal") String datafinal);

    @GET("/api/authenticate/{nickname}/{password}")
    Call<User> userLogin(@Path("nickname") String nickName, @Path("password") String password);

    @Multipart
    @POST("/api/upload")
    Call<User> uploadProfileImage(@Query("nickName") String nickName, @Part MultipartBody.Part file, @Query("tipoFoto") String tipoFoto);

    @Multipart
    @POST("/api/upload")
    Call<Post> uploadPostImage(@Query("nickName") String nickName, @Part MultipartBody.Part file, @Query("tipoFoto") String tipoFoto);

    @GET("/")
    Call<List<Like>> getAllLikes();

    @GET("/post/curtir/{id}")
    Call<Like> like(@Path("id") String id);

    @GET("/post/descurtir/{id}")
    Call<Like> dislike(@Path("id") String id);
}
