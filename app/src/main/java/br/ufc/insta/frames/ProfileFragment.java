package br.ufc.insta.frames;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import br.ufc.insta.MainActivity;
import br.ufc.insta.PostActivity;
import br.ufc.insta.R;
import br.ufc.insta.adapters.GridAdapter;
import br.ufc.insta.models.Like;
import br.ufc.insta.models.Post;
import br.ufc.insta.models.User;
import br.ufc.insta.service.GetDataService;
import br.ufc.insta.service.RetrofitClientInstance;
import br.ufc.insta.utils.GlideApp;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    private TextView postCount;
    private ProgressBar progressBar;

    private GridView gridLayout;

    private User user;
    private List<Post> postList;
    private HashMap<String, String> likeHashmap = new HashMap<>();
    private List<Like> likes;

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_profile, container, false);

        user = new User();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user = bundle.getParcelable("user");
        }
        else{
            user = MainActivity.user;
        }

        CircleImageView profImageView = mView.findViewById(R.id.profileImageView);
        TextView name = mView.findViewById(R.id.profile_FullName);
        TextView username = mView.findViewById(R.id.profile_UserName);
        postCount = mView.findViewById(R.id.profile_postCount);
        progressBar = mView.findViewById(R.id.profile_progressbar);
        gridLayout = mView.findViewById(R.id.gridView);

        name.setText(user.getFullName());
        username.setText(user.getNickName());

        GlideApp.with(MainActivity.mainContext)
                .load(user.getPhoto())
                .placeholder(R.drawable.usericon)
                .into(profImageView);

        loadPosts();

        gridLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.mainContext, PostActivity.class);
                intent.putExtra("POST", postList.get(position));
                intent.putExtra("LIKES", likeHashmap.get(postList.get(position).getId()));
                startActivity(intent);

            }
        });

        return mView;

    }

    private void loadPosts(){
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Post>> call = service.getUserPosts(user.getId());
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                if(response.body() != null){
                    generatePostList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generatePostList(List<Post> postList) {
        this.postList = postList;
        GridAdapter adapter = new GridAdapter(this.getContext(), postList);
        gridLayout.setAdapter(adapter);

        String count = " " + postList.size() + " Posts";
        postCount.setText(count);

        getAllLikes();
    }

    private void getAllLikes() {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Like>> call = service.getAllLikes();
        call.enqueue(new Callback<List<Like>>() {
            @Override
            public void onResponse(Call<List<Like>> call, Response<List<Like>> response) {
                likes = response.body();
                setLikesNumbers();
            }

            @Override
            public void onFailure(Call<List<Like>> call, Throwable t) {
                Toast.makeText(getContext(), "Algo deu errado... Tente novamente mais tarde!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLikesNumbers() {
        for(Like l : likes){
            for(Post p : postList){
                if(l.getId().equalsIgnoreCase(p.getId())){
                    likeHashmap.put(p.getId(), l.getNrCurtidas());
                    break;
                }
            }
        }
    }

}
