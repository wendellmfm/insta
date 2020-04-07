package br.ufc.insta.frames;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.ufc.insta.MainActivity;
import br.ufc.insta.R;
import br.ufc.insta.models.Post;
import br.ufc.insta.models.User;
import br.ufc.insta.service.GetDataService;
import br.ufc.insta.service.RetrofitClientInstance;
import br.ufc.insta.utils.ImageUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int RESULT_LOAD_IMAGE=5;

    EditText title,desc;
    ImageView imageView;
    FloatingActionButton button;
    Button postButton;

    ProgressBar progressBar;

    private View mView;

    private Uri imageURI = null;
    String currentPhotoPath;
    private File imageFile;


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
        postButton = mView.findViewById(R.id.button);
        progressBar = mView.findViewById(R.id.camera_progressBar);

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

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    // Create the File where the photo should go
                    //File photoFile = null;
                    try {
                        imageFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (imageFile != null) {
                        imageURI = FileProvider.getUriForFile(getContext(),
                                "br.ufc.insta.fileprovider",
                                imageFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
//                progressBar.setVisibility(View.VISIBLE);
//
//                final String titleText = title.getText().toString();
//                final String descText = desc.getText().toString();
//
//                if(!TextUtils.isEmpty(titleText) && !TextUtils.isEmpty(descText) && imageURI!=null)
//                {
//
//
//                }
//                else{
//                    progressBar.setVisibility(View.INVISIBLE);
//                    Utility.makeToast(MainActivity.mainContext,"Enter all fields.");
//                }
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    uploadToServer(ImageUtils.getImagePath(getContext(), imageURI));
            }
        });

        return mView;

    }

    private void uploadToServer(String filePath) {
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetDataService service = retrofit.create(GetDataService.class);

        //Create a file object using file path
        File file = new File(filePath);

        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);

        //Create request body with text description and text media type
        //RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");

        Call<Post> call = service.uploadPostImage(MainActivity.user.getNickName(), part, "POST");
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post post = response.body();
                MainActivity.user.getPosts().add(post);
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {

                String message = t.getMessage();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Glide.with(this).load(imageFile.getPath()).into(imageView);
        }

        if(requestCode == RESULT_LOAD_IMAGE &&  resultCode == RESULT_OK)
        {
            imageURI = data.getData();
            imageView.setImageURI(imageURI);
        }
    }

//    public String getImagePath(Uri uri){
//        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
//        cursor.moveToFirst();
//        String document_id = cursor.getString(0);
//        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
//        cursor.close();
//
//        cursor = getContext().getContentResolver().query(
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
//        cursor.moveToFirst();
//        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//        cursor.close();
//
//        return path;
//    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}