package br.ufc.insta;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import br.ufc.insta.frames.ProfileFragment;
import br.ufc.insta.models.User;
import br.ufc.insta.service.GetDataService;
import br.ufc.insta.service.RetrofitClientInstance;
import br.ufc.insta.utils.GlideApp;
import br.ufc.insta.utils.ImageUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class EditActivity extends AppCompatActivity {
    private  int RESULT_LOAD_IMAGE = 5;

    private Uri imageURI = null;
    private String password;
    private String passwordConf;
    private String name;

    private ProgressBar progressBar;

    private EditText pass, passconf, fullname;
    private CircleImageView imageView;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        pass = findViewById(R.id.edit_pass);
        passconf = findViewById(R.id.edit_passconf);
        fullname = findViewById(R.id.edit_name);
        imageView = findViewById(R.id.edit_imageview);
        saveBtn = findViewById(R.id.edit_savebtn);

        progressBar = findViewById(R.id.edit_progress);
        progressBar.setVisibility(View.INVISIBLE);

        fullname.setText(MainActivity.user.getFullName());

        GlideApp.with(MainActivity.mainContext)
                .load(MainActivity.user.getPhoto())
                .placeholder(R.drawable.usericon)
                .into(imageView);

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
                password = pass.getText().toString();
                passwordConf = passconf.getText().toString();
                name = fullname.getText().toString();

                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordConf))
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(EditActivity.this,"Preencha todos os campos.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordConf) && !TextUtils.isEmpty(name))
                {
                    if (password.equals(passwordConf)) {
                        if (imageURI != null) {
                            uploadToServer(ImageUtils.getImagePath(getApplicationContext(), imageURI));
                        } else {
                            updateUser(password, name, null);
                        }
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(EditActivity.this, "Senhas diferentes. Tente novamente.", Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(EditActivity.this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void uploadToServer(String filePath) {
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetDataService service = retrofit.create(GetDataService.class);

        File file = new File(filePath);

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);

        Call<User> call = service.uploadProfileImage(MainActivity.user.getNickName(), part, "Perfil");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                progressBar.setVisibility(View.INVISIBLE);

                User user = response.body();

                updateUser(password, name, user.getPhoto());
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                String message = t.getMessage();
            }
        });
    }

    private void updateUser(String password, String name, String photo) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);

        MainActivity.user.setFullName(name);
        MainActivity.user.setPassword(password);
        if(photo != null){
            MainActivity.user.setPhoto(photo);
        }

        Call<User> call = service.createUser(MainActivity.user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(EditActivity.this, "Usuário editado com sucesso.", Toast.LENGTH_SHORT).show();

                ProfileFragment.reloadFragment = true;

                finish();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(EditActivity.this, "Algo deu errado. Por favor, tente novamente mais tarde.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE &&  resultCode == RESULT_OK)
        {
            imageURI = data.getData();
            imageView.setImageURI(imageURI);
        }

    }

}
