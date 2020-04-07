package br.ufc.insta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.ufc.insta.models.Like;
import br.ufc.insta.models.Post;
import br.ufc.insta.utils.GlideApp;


public class PostActivity extends AppCompatActivity {

    TextView postTite, postDesc, postTimestamp, likeCount;
    ImageView postImage,likeBtn;

    boolean likeStatus = false;

    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postTite = findViewById(R.id.post_title);
        postDesc = findViewById(R.id.post_desc);
        postTimestamp = findViewById(R.id.post_timestamp);
        postImage = findViewById(R.id.post_imageView);
        likeBtn = findViewById(R.id.postLikeButton);
        likeCount = findViewById(R.id.postLikeCount);

        post = getIntent().getParcelableExtra("POST");

        String longV = post.getDatePost();
        long millisecond = Long.parseLong(longV);
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
        postTimestamp.setText(dateString);

        GlideApp.with(getApplicationContext())
                .load(post.getPhotoPost())
                .placeholder(R.drawable.usericon)
                .into(postImage);

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeStatus =! likeStatus;
                Like like = new Like(likeStatus);

                updateLikeBtn(likeStatus);
            }
        });

    }

    private void updateLikeBtn(boolean likeStatus) {
        if(likeStatus)
        {
            likeBtn.setImageDrawable(getDrawable(R.drawable.liked));
        }
        else{
            likeBtn.setImageDrawable(getDrawable(R.drawable.likesimple));
        }
    }
}
