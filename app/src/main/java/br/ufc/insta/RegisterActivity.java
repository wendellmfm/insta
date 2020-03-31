package br.ufc.insta;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Map;

import br.ufc.insta.utils.utility;

public class RegisterActivity extends AppCompatActivity {

    EditText usernameField,nameField,passField,cpassField,emailField;
    TextView loginField;
    Button regBtn;

    utility Utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameField=findViewById(R.id.register_username);
        nameField=findViewById(R.id.register_fname);
        passField=findViewById(R.id.register_pass);
        cpassField=findViewById(R.id.register_cpass);
        emailField=findViewById(R.id.regiter_email);
        loginField=findViewById(R.id.register_login);
        regBtn=findViewById(R.id.register_regBtn);

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

                final String username=usernameField.getText().toString();
                final String name=nameField.getText().toString();
                String pass=passField.getText().toString();
                String cpass=cpassField.getText().toString();
                final String email=emailField.getText().toString();

                if(!TextUtils.isEmpty(username) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(cpass) && !TextUtils.isEmpty(email))
                {

                    if(pass.equals(cpass))
                    {
                        if(pass.length()<6)
                            Utils.makeToast(RegisterActivity.this,"Password must be atleast length 6");
                        else if(!pass.matches(".*[a-z].*"))
                            Utils.makeToast(RegisterActivity.this,"Password is missing lowercase letter.");
                        else if(!pass.matches(".*[A-Z].*"))
                            Utils.makeToast(RegisterActivity.this,"Password is missing uppercase letter.");
                        else{
                            //success pass. now try to add to database
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

    void goToHome(){
        Intent home=new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(home);
        finish();
    }
}
