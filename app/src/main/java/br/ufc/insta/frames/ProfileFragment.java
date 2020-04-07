package br.ufc.insta.frames;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.List;

import br.ufc.insta.MainActivity;
import br.ufc.insta.PostActivity;
import br.ufc.insta.R;
import br.ufc.insta.adapters.GridAdapter;
import br.ufc.insta.models.Post;
import br.ufc.insta.models.User;
import br.ufc.insta.service.GetDataService;
import br.ufc.insta.service.RetrofitClientInstance;
import br.ufc.insta.utils.GlideApp;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private View mView;

    CircleImageView profImageView;
    TextView name,username,desc,postCount,folingCount,folwCount;
    Button mainBtn;
    ProgressBar progressBar;
    GridView gridLayout;

    GridAdapter adapter;

    User user;
    private List<Post> postList;

    //private ArrayList<DocumentReference> docList;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        //loadUser(MainActivity.user);
        user = new User();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user = bundle.getParcelable("user");
        }
        else{
            user = MainActivity.user;
        }

        //loading
        profImageView = mView.findViewById(R.id.profileImageView);
        name = mView.findViewById(R.id.profile_FullName);
        username = mView.findViewById(R.id.profile_UserName);
        desc = mView.findViewById(R.id.profile_Description);
        postCount = mView.findViewById(R.id.profile_postCount);
        folingCount = mView.findViewById(R.id.profileFollowingCount);
        folwCount = mView.findViewById(R.id.profileFollowersCount);
        mainBtn = mView.findViewById(R.id.profile_Btn);
        progressBar = mView.findViewById(R.id.profile_progressbar);
        gridLayout = mView.findViewById(R.id.gridView);

        //docList=new ArrayList<DocumentReference>();
        name.setText(user.getFullName());
        username.setText(user.getNickName());

        GlideApp.with(MainActivity.mainContext)
                .load(user.getPhoto())
                .placeholder(R.drawable.usericon)
                .into(profImageView);

        loadPosts();
//        postList = new ArrayList<Post>();
//
//        adapter = new GridAdapter(this.getContext(),postList);
//        gridLayout.setAdapter(adapter);

        gridLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO : TRANSFER TO POST
                Intent act = new Intent(MainActivity.mainContext, PostActivity.class);
                act.putExtra("POST_UID", MainActivity.user.getNickName());
                //act.putExtra("POST_DocID",docList.get(position).getId());
                startActivity(act);
            }
        });

        //update button based on id

        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                //progressDoalog.dismiss();
                generatePostList(response.body());
                //List<Post> retroPhotoList = response.body();
                Toast.makeText(getContext(), "Size: " + response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                //progressDoalog.dismiss();
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generatePostList(List<Post> postList) {
        adapter = new GridAdapter(this.getContext(), postList);
        gridLayout.setAdapter(adapter);
    }

}
