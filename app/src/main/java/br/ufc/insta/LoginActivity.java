package br.ufc.insta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

import br.ufc.insta.models.Post;
import br.ufc.insta.models.User;
import br.ufc.insta.service.GetDataService;
import br.ufc.insta.service.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailfield,passfield;
    TextView reg;
    Button logBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailfield = findViewById(R.id.login_email);
        passfield = findViewById(R.id.login_password);
        logBtn = findViewById(R.id.login_loginbtn);
        reg = findViewById(R.id.login_register2);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User user = new User();
                user.setId("10");
                user.setFullName("Wendell Mendes");
                user.setNickName("wendell");
                user.setPassword("teste");
                user.setEmail("wendell@emai.com");
                user.setBirthday("");
                user.setPhoto("https://redesocialtrinta.s3.amazonaws.com/thiagoqueiroz/DSC_1000e.jpg");
                user.setPosts(new ArrayList<Post>());

                goToHome(user);


//                String nickName = emailfield.getText().toString();
//                String password = passfield.getText().toString();
//                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass))
//                {
                    //checking validity of the password
//                    if(pass.length()<6)
//                        Utils.makeToast(LoginActivity.this,"Password must be atleast length 6");
//                    else if(!pass.matches(".*[a-z].*"))
//                        Utils.makeToast(LoginActivity.this,"Password is missing lowercase letter.");
//                    else if(!pass.matches(".*[A-Z].*"))
//                        Utils.makeToast(LoginActivity.this,"Password is missing uppercase letter.");
//                    else{
                        //success pass. now check with database
//                        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
//                        //Call<String> call = service.getUserRegister("4", fullName, email, nickName, password, "", "", "", new ArrayList<Post>() {});
//
//                        //User user = builUser(fullName, password, email);
//                        //Credential credentials = new Credential(nickName, password);
//                        Call<User> call = service.userLogin(nickName, password);
//                        call.enqueue(new Callback<User>() {
//                            @Override
//                            public void onResponse(Call<User> call, Response<User> response) {
//                                //progressDoalog.dismiss();
//                                User user = response.body();
//                                goToHome(user);
//                                //generateDataList(response.body());
//                                //List<RetroPhoto> retroPhotoList = response.body();
//                                //Toast.makeText(MainActivity.this, "Size: " + retroPhotoList.size(), Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onFailure(Call<User> call, Throwable t) {
//                                //progressDoalog.dismiss();
//                                Toast.makeText(LoginActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
//                                String message = t.getMessage();
//                            }
//                        });

//                    }
//                }
//                else{
//                    Toast.makeText(LoginActivity.this,"Enter field values...", Toast.LENGTH_LONG).show();
//                }

            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regAct=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(regAct);
                finish();
            }
        });


    }

    void goToHome(User user){
        Intent home = new Intent(LoginActivity.this, MainActivity.class);
        home.putExtra("user", user);
        startActivity(home);
        finish();
    }

}
