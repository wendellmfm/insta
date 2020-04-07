package br.ufc.insta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import br.ufc.insta.models.User;
import br.ufc.insta.service.GetDataService;
import br.ufc.insta.service.RetrofitClientInstance;
import br.ufc.insta.utils.ImageUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {

    ProgressBar progressBar;

    EditText pass, nickname, fullname, desc;
    CircleImageView imageView;
    Button saveBtn;


    private User userObject;
    private Uri imageURI=null;
    private  int RESULT_LOAD_IMAGE = 5;

    private boolean alreadyLoaded=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        pass = findViewById(R.id.edit_pass);
        nickname = findViewById(R.id.edit_username);
        fullname = findViewById(R.id.edit_name);
        desc = findViewById(R.id.edit_desc);
        imageView = findViewById(R.id.edit_imageview);
        saveBtn = findViewById(R.id.edit_savebtn);
        progressBar = findViewById(R.id.edit_progress);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, RESULT_LOAD_IMAGE);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                final String userName = nickname.getText().toString();
                String password = pass.getText().toString();
                final String name = fullname.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(password))
                {
                    Toast.makeText(EditActivity.this,"Empty name or password fields..", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(name))
                {
                    GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

                    MainActivity.user.setFullName(name);
                    MainActivity.user.setNickName(userName);
                    MainActivity.user.setFullName(password);
                    MainActivity.user.setPhoto(ImageUtils.getImagePath(getApplicationContext(), imageURI));

                    Call<User> call = service.createUser(MainActivity.user);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            //progressDoalog.dismiss();
                            Toast.makeText(EditActivity.this, "Algo deu errado. Por favor, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    Toast.makeText(EditActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RESULT_LOAD_IMAGE &&  resultCode == RESULT_OK)
        {
            imageURI = data.getData();
            imageView.setImageURI(imageURI);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

        if(!alreadyLoaded) {
            alreadyLoaded=true;


            }
    }
}
