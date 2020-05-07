package br.ufc.insta;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import br.ufc.insta.models.Like;
import br.ufc.insta.models.Post;
import br.ufc.insta.service.GetDataService;
import br.ufc.insta.service.RetrofitClientInstance;
import br.ufc.insta.utils.GlideApp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostActivity extends AppCompatActivity {

    private TextView postTite, postDesc, postTimestamp, likeCount;
    private ImageView postImage, likeBtn;

    boolean likeStatus;
    boolean actionLike = true;

    private Post post;
    private List<Like> likes;

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

        String likesNumber = getIntent().getStringExtra("LIKES");

        if(likesNumber != null){
            likeCount.setText(likesNumber);
        }

        String longValue = post.getDatePost();
        long millisecond = Long.parseLong(longValue);
        String dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
        postTimestamp.setText(dateString);

        GlideApp.with(getApplicationContext())
                .load(post.getPhotoPost())
                .placeholder(R.drawable.usericon)
                .into(postImage);

        getAllLikes();

        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!actionLike) return;

                likeStatus =! likeStatus;
                if(likeStatus){
                    like();
                }
                else{
                    dislike();
                }

                updateLikeBtn(likeStatus);
            }
        });

    }

    private void like() {
        actionLike = false;

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Like> call = service.like(post.getId());
        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                Like like = response.body();
                likeCount.setText(like.getNrCurtidas());

                actionLike = true;
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                actionLike = true;
                Toast.makeText(PostActivity.this, "Algo deu errado... Tente novamente mais tarde!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void dislike() {
        actionLike = false;

        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<Like> call = service.dislike(post.getId());
        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                Like like = response.body();
                likeCount.setText(like.getNrCurtidas());

                actionLike = true;
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                actionLike = true;
                Toast.makeText(PostActivity.this, "Algo deu errado... Tente novamente mais tarde!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAllLikes() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Like>> call = service.getAllLikes();
        call.enqueue(new Callback<List<Like>>() {
            @Override
            public void onResponse(Call<List<Like>> call, Response<List<Like>> response) {
                likes = response.body();
                setLikesNumbers(likes);
            }

            @Override
            public void onFailure(Call<List<Like>> call, Throwable t) {
                Toast.makeText(PostActivity.this, "Algo deu errado... Tente novamente mais tarde!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLikesNumbers(List<Like> likes) {
        for(Like l : likes){
            if(l.getId().equalsIgnoreCase(post.getId())){
                likeCount.setText(l.getNrCurtidas());
                break;
            }
        }
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
