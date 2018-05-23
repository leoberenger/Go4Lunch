package com.openclassrooms.go4lunch.controllers.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.openclassrooms.go4lunch.R;
import com.openclassrooms.go4lunch.controllers.activities.DetailActivity;
import com.openclassrooms.go4lunch.managers.WorkmatesMgr;
import com.openclassrooms.go4lunch.models.User;
import com.openclassrooms.go4lunch.utils.ItemClickSupport;
import com.openclassrooms.go4lunch.views.WorkmatesRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkmatesListFragment extends Fragment {

    // FOR DESIGN
    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    private WorkmatesRecyclerAdapter adapter;
    private List<User> workmates;

    public WorkmatesListFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        ButterKnife.bind(this, view);

        this.configureRecyclerView();
        this.configureOnClickRecyclerView();

        WorkmatesMgr workmatesMgr = WorkmatesMgr.getInstance();
        List<User> users = workmatesMgr.getWorkmates();

        updateUI(users);

        return view;
    }


    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureRecyclerView(){
        this.workmates = new ArrayList<>();
        this.adapter = new WorkmatesRecyclerAdapter(this.workmates, Glide.with(this), true);
        this.mRecyclerView.setAdapter(this.adapter);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.resto_recycler_view_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {

                        if(adapter.getUser(position).getSelectedRestoId() !=null){
                            String selectedRestoId = adapter.getUser(position).getSelectedRestoId();
                            Intent intent = new Intent(getActivity(), DetailActivity.class);
                            intent.putExtra("PLACE_ID", selectedRestoId);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getContext(), "No Restaurant Selected Yet", Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    // -----------------
    // UPDATE UI
    // -----------------

    private void updateUI(List<User> users){
        workmates.clear();
        workmates.addAll(users);
        adapter.notifyDataSetChanged();
    }
}