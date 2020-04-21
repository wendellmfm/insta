package br.ufc.insta.frames;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.ufc.insta.EditActivity;
import br.ufc.insta.MainActivity;
import br.ufc.insta.PostActivity;
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


public class CameraFragment extends Fragment {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private int RESULT_LOAD_IMAGE = 5;

    private ImageView imageView;
    private FloatingActionButton button;
    private Button postButton;

    private ProgressBar progressBar;

    private View mView;

    private Uri imageURI = null;
    private String currentPhotoPath;
    private File imageFile;

    boolean isCameraImage = false;


    public CameraFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_camera, container, false);
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

            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isCameraImage){
                    uploadToServer(imageFile.getPath());
                }else{
                    uploadToServer(ImageUtils.getImagePath(getContext(), imageURI));
                }
            }
        });

        return mView;

    }

    private void uploadToServer(String filePath) {
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        GetDataService service = retrofit.create(GetDataService.class);

        File file = new File(filePath);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), fileReqBody);
        Call<Post> call = service.uploadPostImage(MainActivity.user.getNickName(), part, "POST");
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post post = response.body();

                Intent intent = new Intent(MainActivity.mainContext, PostActivity.class);
                intent.putExtra("POST", post);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(getContext(),"Não foi possível publicar a postagem. Por favor, tente novamente mais tarde!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            isCameraImage = true;
            Glide.with(this).load(imageFile.getPath()).into(imageView);
        }

        if(requestCode == RESULT_LOAD_IMAGE &&  resultCode == RESULT_OK)
        {
            isCameraImage = false;
            imageURI = data.getData();
            imageView.setImageURI(imageURI);
        }
    }

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