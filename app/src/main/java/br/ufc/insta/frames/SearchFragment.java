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
import br.ufc.insta.adapters.SearchAdapter;
import br.ufc.insta.models.User;
import br.ufc.insta.service.GetDataService;
import br.ufc.insta.service.RetrofitClientInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchFragment extends Fragment {

    private SearchView searchView;

    private View mView;
    private View _rootView = null;

    private RecyclerView recyclerView;
    private List<User> userList;
    private SearchAdapter adapter;

    public SearchFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(_rootView == null) {
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

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(final String s) {
                    GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
                    Call<User> call = service.getUserByNickname(s);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            User user = response.body();
                            if(user != null){
                                userList.clear();
                                adapter.notifyItemRangeRemoved(0,userList.size()-1);

                                userList.add(user);

                                adapter.notifyDataSetChanged();
                            }
                            else{
                                Toast.makeText(getContext(), "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
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
            //TODO: Handle this...
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
