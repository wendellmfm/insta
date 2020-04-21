package br.ufc.insta.frames;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import br.ufc.insta.MainActivity;
import br.ufc.insta.PostActivity;
import br.ufc.insta.R;
import br.ufc.insta.adapters.GridAdapter;
import br.ufc.insta.models.Post;
import br.ufc.insta.models.User;
import br.ufc.insta.service.GetDataService;
import br.ufc.insta.service.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchByDateFragment extends Fragment {

    private EditText beginDate;
    private EditText endDate;
    private TextView postCount;
    private DatePickerDialog datePickerDialog;

    private GridView gridLayout;
    private ProgressBar progressBar;

    private User user;
    private List<Post> postList;

    private String beginDateFormatted;
    private String endDateFormatted;

    public SearchByDateFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_home, container, false);

        beginDate = mView.findViewById(R.id.beginDate);
        endDate = mView.findViewById(R.id.endDate);
        Button searchButton = mView.findViewById(R.id.searchPostsButton);
        postCount = mView.findViewById(R.id.postsCount);
        gridLayout = mView.findViewById(R.id.searchPostsGrid);
        progressBar = mView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        beginDate.setInputType(InputType.TYPE_NULL);
        beginDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    beginDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    beginDateFormatted = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                            }, year, month, day);
                    datePickerDialog.show();
                }
            }
        });

        endDate.setInputType(InputType.TYPE_NULL);
        endDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    datePickerDialog = new DatePickerDialog(getActivity(),
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    endDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                    endDateFormatted = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                }
                            }, year, month, day);
                    datePickerDialog.show();
                }
            }
        });

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

                if(!TextUtils.isEmpty(beginDateFormatted) && !TextUtils.isEmpty(endDateFormatted)){
                    loadPosts(beginDateFormatted, endDateFormatted);
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
                Toast.makeText(getContext(), "Algo deu errado... Tente novamente mais tarde!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generatePostList(List<Post> postList) {
        this.postList = postList;
        GridAdapter adapter = new GridAdapter(getActivity(), postList);
        gridLayout.setAdapter(adapter);

        postCount.setVisibility(View.VISIBLE);
        String count = " " + postList.size() + " Posts encontrados.";
        postCount.setText(count);
    }

}
