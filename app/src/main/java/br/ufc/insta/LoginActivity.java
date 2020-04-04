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


import br.ufc.insta.models.User;
import br.ufc.insta.service.GetDataService;
import br.ufc.insta.service.RetrofitClientInstance;
import br.ufc.insta.utils.utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailfield,passfield;
    TextView reg;
    Button logBtn;

    utility Utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailfield=findViewById(R.id.login_email);
        passfield=findViewById(R.id.login_password);
        logBtn=findViewById(R.id.login_loginbtn);
        reg=findViewById(R.id.login_register2);

        Utils=new utility();

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailfield.getText().toString();
                String pass = passfield.getText().toString();
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass))
                {
                    //checking validity of the password
//                    if(pass.length()<6)
//                        Utils.makeToast(LoginActivity.this,"Password must be atleast length 6");
//                    else if(!pass.matches(".*[a-z].*"))
//                        Utils.makeToast(LoginActivity.this,"Password is missing lowercase letter.");
//                    else if(!pass.matches(".*[A-Z].*"))
//                        Utils.makeToast(LoginActivity.this,"Password is missing uppercase letter.");
//                    else{
                        //success pass. now check with database
                        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                        //Call<String> call = service.getUserRegister("4", fullName, email, nickName, password, "", "", "", new ArrayList<Post>() {});

                        //User user = builUser(fullName, password, email);

                        Call<User> call = service.userLogin(email, pass);
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
                                Toast.makeText(LoginActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                String message = t.getMessage();
                            }
                        });

//                    }
                }
                else{
                    Toast.makeText(LoginActivity.this,"Enter field values...", Toast.LENGTH_LONG).show();
                }


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

    void goToHome(){
        Intent home=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(home);
        finish();
    }

}
