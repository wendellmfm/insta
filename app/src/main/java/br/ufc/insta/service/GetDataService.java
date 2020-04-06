package br.ufc.insta.service;

import java.util.List;

import br.ufc.insta.models.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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

    @GET("/api/contacts")
    Call<List<User>> getAllUsers();

    @GET("/api/authenticate")
    Call<User> userLogin(@Query("username") String username, @Query("password") String password);

    @Multipart
    @POST("/api/upload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file);
}
