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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailfield, passfield;
    private TextView reg;
    private Button logBtn;

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

                String nickName = emailfield.getText().toString();
                String password = passfield.getText().toString();
                if(!TextUtils.isEmpty(nickName) && !TextUtils.isEmpty(password))
                {
                        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                        Call<User> call = service.userLogin(nickName, password);
                        call.enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                User user = response.body();

                                Toast.makeText(LoginActivity.this, "Login efetuado com sucesso.", Toast.LENGTH_SHORT).show();
                                goToHome(user);
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(LoginActivity.this, "Algo deu errado... Tente novamente mais tarde!", Toast.LENGTH_SHORT).show();
                                String message = t.getMessage();
                            }
                        });
                    } else{
                        Toast.makeText(LoginActivity.this,"Preencha todos os campos.", Toast.LENGTH_LONG).show();
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

    void goToHome(User user){
        Intent home = new Intent(LoginActivity.this, MainActivity.class);
        home.putExtra("user", user);
        startActivity(home);
        finish();
    }

}
