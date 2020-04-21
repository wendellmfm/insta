package br.ufc.insta.frames;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.ufc.insta.MainActivity;
import br.ufc.insta.PostActivity;
import br.ufc.insta.R;
import br.ufc.insta.adapters.GridAdapter;
import br.ufc.insta.models.Post;
import br.ufc.insta.models.User;
import br.ufc.insta.service.GetDataService;
import br.ufc.insta.service.RetrofitClientInstance;
import br.ufc.insta.utils.MaskEditUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchByDateFragment extends Fragment {

    private View mView;

    EditText beginDate;
    EditText endDate;
    TextView postCount;
    Button searchButton;

    GridView gridLayout;
    GridAdapter adapter;
    ProgressBar progressBar;

    User user;
    private List<Post> postList;

    public SearchByDateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        beginDate = mView.findViewById(R.id.beginDate);
        endDate = mView.findViewById(R.id.endDate);
        searchButton = mView.findViewById(R.id.searchPostsButton);
        postCount = mView.findViewById(R.id.postsCount);
        gridLayout = mView.findViewById(R.id.searchPostsGrid);
        progressBar = mView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        beginDate.addTextChangedListener(MaskEditUtil.mask(beginDate, MaskEditUtil.FORMAT_DATE));
        endDate.addTextChangedListener(MaskEditUtil.mask(endDate, MaskEditUtil.FORMAT_DATE));
        postCount.setVisibility(View.INVISIBLE);

        user = new User();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user = bundle.getParcelable("user");
        }
        else{
            user = MainActivity.user;
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);

                String begin = beginDate.getText().toString();
                String end = endDate.getText().toString();

                if(!TextUtils.isEmpty(begin) && !TextUtils.isEmpty(end)){
                    loadPosts(beginDate.getText().toString(), endDate.getText().toString());
                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

        gridLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.mainContext, PostActivity.class);
                intent.putExtra("POST", postList.get(position));
                startActivity(intent);

            }
        });


        return mView;
    }

    private void loadPosts(String dataInicio, String dataFim){
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Post>> call = service.getUserPostsByData(dataInicio, dataFim);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                generatePostList(response.body());
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
        adapter = new GridAdapter(this.getContext(), postList);
        gridLayout.setAdapter(adapter);

        postCount.setVisibility(View.VISIBLE);
        String count = " " + postList.size() + " Posts encontrados.";
        postCount.setText(count);
    }

}
