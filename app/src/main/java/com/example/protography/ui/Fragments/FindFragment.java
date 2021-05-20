package com.example.protography.ui.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.protography.R;
import com.example.protography.databinding.FragmentFindBinding;
import com.example.protography.ui.Adapters.RecyclerViewAdapterFind;
import com.example.protography.ui.Models.Image;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindFragment extends Fragment {

    private static final String TAG = "FindFragment";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference databaseRoot = database.getReference();
    private FragmentFindBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_find, container, false);

        return root;
    }

    /*@Override
    public boolean onCreateOptionsMenu() {
        final SearchView searchView = (SearchView) getView().findViewById(R.id.Find_SearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                RecycleViewAdapterFind.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return true;
    }*/

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        List<Image> imageList = new ArrayList<>();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_find);

        RecyclerViewAdapterFind recyclerViewAdapterFind = new RecyclerViewAdapterFind (imageList, getContext());

        Query userFilter = databaseRoot.child("Images");

        userFilter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                imageList.clear();
                if(snapshot.exists() && snapshot.getChildrenCount() > 0) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Image image = dataSnapshot.getValue(Image.class);
                        //if(image.getImageTime().equals("Nighttime")){     //modificare filtro per togliere le foto caricate dall'utente ?
                        imageList.add(image);
                        //}
                    }
                    recyclerViewAdapterFind.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        recyclerView.setAdapter(recyclerViewAdapterFind);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }


}