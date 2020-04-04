package br.ufc.insta.frames;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.ufc.insta.R;
import br.ufc.insta.RegisterActivity;
import br.ufc.insta.adapters.SearchAdapter;
import br.ufc.insta.models.Post;
import br.ufc.insta.models.User;
import br.ufc.insta.service.GetDataService;
import br.ufc.insta.service.RetrofitClientInstance;
import br.ufc.insta.utils.utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    SearchView searchView;

    View mView;
    View _rootView=null;

    utility Utility;

    RecyclerView recyclerView;
    private List<User> userList;
    private SearchAdapter adapter;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(_rootView==null) {
            mView = inflater.inflate(R.layout.fragment_search, container, false);
            _rootView=mView;
            searchView = mView.findViewById(R.id.searchView);
            recyclerView = mView.findViewById(R.id.search_recyclerView);

            userList = new ArrayList<>();
            adapter = new SearchAdapter(userList);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), ((LinearLayoutManager) layoutManager).getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);


            Utility = new utility();


            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(final String s) {
                    GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                    Call<User> call = service.getUserByNickname(s);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            //progressDoalog.dismiss();
                            User user = response.body();
                            userList.clear();
                            adapter.notifyItemRangeRemoved(0,userList.size()-1);

                            userList.add(user);

                            adapter.notifyDataSetChanged();
                            //Toast.makeText(MainActivity.this, "Size: " + retroPhotoList.size(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            //progressDoalog.dismiss();
                            //Toast.makeText(RegisterActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                            String message = t.getMessage();
                        }
                    });

                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    return false;
                }
            });

        }
        else{

        }

        return _rootView;
    }

    @Override
    public void onDestroyView() {
        if(_rootView.getParent()!=null)
        {
            ((ViewGroup)_rootView.getParent()).removeView(_rootView);
        }
        super.onDestroyView();
    }
}
