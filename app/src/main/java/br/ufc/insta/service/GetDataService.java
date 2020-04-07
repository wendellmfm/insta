package br.ufc.insta.service;

import java.util.List;

import br.ufc.insta.models.Credential;
import br.ufc.insta.models.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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

    @GET("/api/contacts")
    Call<List<User>> getAllUsers();

    //@Headers("Content-Type: application/json")
//    @POST("/api/authenticate")
//    Call<User> userLogin(@Query("nickName") String nickName, @Query("password") String password);

    @POST("/api/authenticate")
    Call<User> userLogin(@Body Credential credentials);

    @Multipart
    @POST("/api/upload")
    Call<ResponseBody> uploadProfileImage(@Query("nickName") String nickName, @Part MultipartBody.Part file);

    @Multipart
    @POST("/api/upload")
    Call<ResponseBody> uploadPostImage(@Query("nickName") String nickName, @Part MultipartBody.Part file);
}
