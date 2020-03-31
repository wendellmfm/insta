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

import br.ufc.insta.models.User;
import br.ufc.insta.utils.utility;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends AppCompatActivity {

    utility Utility;

    ProgressBar progressBar;

    EditText pass,username,name,desc;
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

        load();

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
                final String userName = username.getText().toString();
                String password=pass.getText().toString();
                final String Name = name.getText().toString();
                final String Desc = desc.getText().toString();

                if(TextUtils.isEmpty(Name) || TextUtils.isEmpty(password))
                {
                    Utility.makeToast(EditActivity.this,"Empty name or password fields..");
                    return;
                }


                if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(Name))
                {
                    // validate password

                }
                else{
                    Utility.makeToast(EditActivity.this,"Enter all fields..");
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

    void load(){
        pass=findViewById(R.id.edit_pass);
        username=findViewById(R.id.edit_username);
        name=findViewById(R.id.edit_name);
        desc=findViewById(R.id.edit_desc);
        imageView=findViewById(R.id.edit_imageview);
        saveBtn=findViewById(R.id.edit_savebtn);
        progressBar=findViewById(R.id.edit_progress);
        Utility=new utility();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!alreadyLoaded) {
            alreadyLoaded=true;


            }
    }
}
