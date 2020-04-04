package br.ufc.insta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import br.ufc.insta.utils.utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText nickNameField, fullNameField, passwordField, passwordConfirmationField, emailField;
    TextView loginField;
    Button regBtn;

    utility Utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nickNameField = findViewById(R.id.register_username);
        fullNameField = findViewById(R.id.register_fname);
        passwordField = findViewById(R.id.register_pass);
        passwordConfirmationField = findViewById(R.id.register_cpass);
        emailField = findViewById(R.id.regiter_email);
        loginField = findViewById(R.id.register_login);
        regBtn = findViewById(R.id.register_regBtn);

        Utils=new utility();


        loginField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(login);
                finish();
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nickName = nickNameField.getText().toString();
                final String fullName = fullNameField.getText().toString();
                String password = passwordField.getText().toString();
                String passwordConfirmation = passwordConfirmationField.getText().toString();
                final String email = emailField.getText().toString();

                if(!TextUtils.isEmpty(nickName) && !TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirmation) && !TextUtils.isEmpty(email))
                {

                    if(password.equals(passwordConfirmation))
                    {
                        if(password.length()<6)
                            Utils.makeToast(RegisterActivity.this,"Password must be atleast length 6");
                        else if(!password.matches(".*[a-z].*"))
                            Utils.makeToast(RegisterActivity.this,"Password is missing lowercase letter.");
                        else if(!password.matches(".*[A-Z].*"))
                            Utils.makeToast(RegisterActivity.this,"Password is missing uppercase letter.");
                        else{
                            //success pass. now try to add to database
                            /*Create handle for the RetrofitInstance interface*/
                            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                            //Call<String> call = service.getUserRegister("4", fullName, email, nickName, password, "", "", "", new ArrayList<Post>() {});

                        User user = builUser(fullName, password, email);

                        Call<User> call = service.createUser(user);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    //progressDoalog.dismiss();
                                    User jsonresponse = response.body();
                                    //generateDataList(response.body());
                                    //List<RetroPhoto> retroPhotoList = response.body();
                                    //Toast.makeText(MainActivity.this, "Size: " + retroPhotoList.size(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    //progressDoalog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    else
                        Utils.makeToast(RegisterActivity.this,"Password mismatch...");

                }
                else{
                    Utils.makeToast(RegisterActivity.this,"Enter all fields...");
                }
            }
        });

    }

    private User builUser(String fullName, String password, String email) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPosts(new ArrayList<Post>());
        return user;
    }

    void goToHome(){
        Intent home=new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(home);
        finish();
    }
}
