package br.ufc.insta.frames;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import br.ufc.insta.MainActivity;
import br.ufc.insta.R;
import br.ufc.insta.utils.utility;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraFragment extends Fragment {
    private static final String KEY = "AKIA2OJ5A2QLWSF4XTNJ";
    private static final String SECRET = "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";
    public static final String BUCKET_NAME = "redesocialtrinta";

    EditText title,desc;
    ImageView imageView;
    FloatingActionButton button;
    Button postButton;

    ProgressBar progressBar;


    private int RESULT_LOAD_IMAGE=5;

    utility Utility;

    private View mView;

    private Uri imageURI = null;
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

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });


        return mView;

    }

    public void upload(){
        // KEY and SECRET are gotten when we create an IAM user above
        BasicAWSCredentials credentials = new BasicAWSCredentials(KEY, SECRET);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(MainActivity.mainContext)
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();

        //--------------------------------------
        //Criação de arquivo.
        final String fileName = "nome_da_foto";

        if (!validateInputFileName(fileName)) {
            return;
        }

        final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "/" + fileName);

        createFile(getActivity().getApplicationContext(), imageURI, file);

        //File imageFile = new File(imageURI.getPath());
        // "jsaS3" will be the folder that contains the file
        //-------------------------------------------
        TransferObserver uploadObserver =
                transferUtility.upload(BUCKET_NAME, "testeS3/" + fileName + "." + getFileExtension(imageURI), file);

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed download.
                    Log.d("YourActivity", "Funfou!?");
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float)bytesCurrent/(float)bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });

// If your upload does not trigger the onStateChanged method inside your
// TransferListener, you can directly check the transfer state as shown here.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private boolean validateInputFileName(String fileName) {

        if (TextUtils.isEmpty(fileName)) {
            Toast.makeText(getActivity(), "Enter file name!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void createFile(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE &&  resultCode == RESULT_OK)
        {
            imageURI = data.getData();
            imageView.setImageURI(imageURI);
        }

    }

}
