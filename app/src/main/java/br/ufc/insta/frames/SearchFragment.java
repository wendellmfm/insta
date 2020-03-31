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

import java.util.ArrayList;
import java.util.List;

import br.ufc.insta.R;
import br.ufc.insta.adapters.SearchAdapter;
import br.ufc.insta.models.User;
import br.ufc.insta.utils.utility;

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

                    // do query when u click submit

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
