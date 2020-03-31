package br.ufc.insta.frames;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import br.ufc.insta.MainActivity;
import br.ufc.insta.R;
import br.ufc.insta.utils.utility;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {

    EditText title,desc;
    ImageView imageView;
    FloatingActionButton button;

    ProgressBar progressBar;


    private int RESULT_LOAD_IMAGE=5;

    utility Utility;

    private View mView;

    private Uri imageURI=null;


    public CameraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_camera, container, false);

        title = mView.findViewById(R.id.camera_title);
        desc = mView.findViewById(R.id.camera_desc);
        imageView = mView.findViewById(R.id.camera_imageView);
        button = mView.findViewById(R.id.floatingActionButton);
        progressBar = mView.findViewById(R.id.camera_progressBar);

        Utility=new utility();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                startActivityForResult(gallery, RESULT_LOAD_IMAGE);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                final String titleText = title.getText().toString();
                final String descText = desc.getText().toString();

                if(!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(descText) && imageURI!=null)
                {


                }
                else{
                    progressBar.setVisibility(View.INVISIBLE);
                    Utility.makeToast(MainActivity.mainContext,"Enter all fields.");
                }
            }
        });



        return mView;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RESULT_LOAD_IMAGE &&  resultCode == RESULT_OK)
        {
            imageURI = data.getData();
            imageView.setImageURI(imageURI);
        }

    }
}
