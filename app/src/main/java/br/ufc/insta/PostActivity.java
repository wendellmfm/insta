package br.ufc.insta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.ufc.insta.models.Like;


public class PostActivity extends AppCompatActivity {

    TextView postTite, postDesc, postTimestamp, likeCount;
    ImageView postImage,likeBtn;

    String uid, docid;

    ProgressBar progressBar;

    boolean likeStatus=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postTite=findViewById(R.id.post_title);
        postDesc=findViewById(R.id.post_desc);
        postTimestamp=findViewById(R.id.post_timestamp);
        postImage=findViewById(R.id.post_imageView);
        progressBar=findViewById(R.id.post_progressBar);
        likeBtn=findViewById(R.id.postLikeButton);
        likeCount=findViewById(R.id.postLikeCount);

        uid = getIntent().getStringExtra("POST_UID");
        docid = getIntent().getStringExtra("POST_DocID");

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeStatus=!likeStatus;
                Like like=new Like(likeStatus);

            }
        });


    }

    private void updateLikeBtn(boolean likeStatus) {
//        if(likeStatus)
//        {
//            likeBtn.setImageDrawable(getDrawable(R.drawable.liked));
//        }
//        else{
//            likeBtn.setImageDrawable(getDrawable(R.drawable.likesimple));
//        }
    }
}
