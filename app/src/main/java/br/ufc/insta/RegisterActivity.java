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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText nickNameField, fullNameField, passwordField, passwordConfirmationField, emailField;
    TextView loginField;
    Button regBtn;

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

        loginField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
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

                if (!TextUtils.isEmpty(nickName) && !TextUtils.isEmpty(fullName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConfirmation) && !TextUtils.isEmpty(email)) {
                    if (password.equals(passwordConfirmation)) {
                            GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                            //Call<String> call = service.getUserRegister("4", fullName, email, nickName, password, "", "", "", new ArrayList<Post>() {});

                            User user = builUser(fullName, password, email, nickName);

                            Call<User> call = service.createUser(user);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    User user = response.body();

                                    goToHome(user);
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Toast.makeText(RegisterActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                                }
                            });
                    } else
                        Toast.makeText(RegisterActivity.this, "Senhas diferentes. Tente novamente.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(RegisterActivity.this, "Preencha todos os campos.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private User builUser(String fullName, String password, String email, String nickname) {
        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setNickName(nickname);
        user.setPosts(new ArrayList<Post>());
        return user;
    }

    void goToHome(User user) {
        Intent home = new Intent(RegisterActivity.this, MainActivity.class);
        home.putExtra("user", user);
        startActivity(home);
        finish();
    }
}
