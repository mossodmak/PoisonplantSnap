package com.example.mynavjava;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class SearchFragment extends Fragment {
    RecyclerView recyclerView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    SearchView searchView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        TextView search = view.findViewById(R.id.search_title);

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("data");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firebaseSearch(query);
                firebaseSearchsciname(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                firebaseSearch(newText);
                firebaseSearchsciname(newText);
                return false;
            }
        });


        return view;
    }
    private void firebaseSearch(String searchtext){
        Query firebaseSearchQuery = reference.orderByChild("title").startAt(searchtext.toLowerCase()).endAt(searchtext.toLowerCase()+"\uf8ff");

        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.search_item,
                        ViewHolder.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.setDetails(getContext(),model.getTitle(),model.getImage(),model.getSciname());

                    }
                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView title = view.findViewById(R.id.rTitleTv);

                                //ImageView imageButton = view.findViewById(R.id.rImageTv);
                                String mtitle = title.getText().toString();

                                String mimage = getItem(position).getImage();

                                Intent intent = new Intent(view.getContext(), ShowDetail.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                //bitmap.compress(Bitmap.CompressFormat.PNG, 100,stream);
                                // byte[] bytes = stream.toByteArray();
                                intent.putExtra("image", mimage );
                                intent.putExtra("title",mtitle);
                                // for direct destination of back button
                                intent.putExtra("destination", "search");

                                startActivity(intent);
                            }
                            @Override
                            public void onItemlongClick(View view, int position) {

                            }
                        });
                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
    private void firebaseSearchsciname(String searchtext){
        Query firebaseSearchQuery = reference.orderByChild("sciname").startAt(searchtext.toUpperCase()).endAt(searchtext.toLowerCase()+"\uf8ff");

        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.search_item,
                        ViewHolder.class,
                        firebaseSearchQuery
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.setDetails(getContext(),model.getTitle(),model.getImage(),model.getSciname());

                    }
                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView title = view.findViewById(R.id.rTitleTv);

                                //ImageView imageButton = view.findViewById(R.id.rImageTv);
                                String mtitle = title.getText().toString();

                                String mimage = getItem(position).getImage();

                                Intent intent = new Intent(view.getContext(), ShowDetail.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                //bitmap.compress(Bitmap.CompressFormat.PNG, 100,stream);
                                // byte[] bytes = stream.toByteArray();
                                intent.putExtra("image", mimage );
                                intent.putExtra("title",mtitle);

                                startActivity(intent);
                            }
                            @Override
                            public void onItemlongClick(View view, int position) {

                            }
                        });
                        return viewHolder;
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Model, ViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Model, ViewHolder>(
                        Model.class,
                        R.layout.search_item,
                        ViewHolder.class,
                        reference
                ) {
                    @Override
                    protected void populateViewHolder(ViewHolder viewHolder, Model model, int position) {
                        viewHolder.setDetails(getContext(),model.getTitle(),model.getImage(),model.getSciname());

                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        ViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                        viewHolder.setOnClickListener(new ViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                TextView title = view.findViewById(R.id.rTitleTv);

                                //ImageView imageButton = view.findViewById(R.id.rImageTv);
                                String mtitle = title.getText().toString();

                                String mimage = getItem(position).getImage();

                                Intent intent = new Intent(view.getContext(), ShowDetail.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                //bitmap.compress(Bitmap.CompressFormat.PNG, 100,stream);
                                // byte[] bytes = stream.toByteArray();
                                intent.putExtra("image", mimage );
                                intent.putExtra("title",mtitle);

                                startActivity(intent);
                            }
                            @Override
                            public void onItemlongClick(View view, int position) {

                            }
                        });
                        return viewHolder;
                    }
                };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}

