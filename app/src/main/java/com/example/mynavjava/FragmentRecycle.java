package com.example.mynavjava;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRecycle extends Fragment {

    private RecyclerView recycleView;
    private RecycleAdapter adapter;
    private static String[] s;

    public FragmentRecycle() {
        // Required empty public constructor
    }

    public static FragmentRecycle newInstance(String[] ss) {

        Bundle args = new Bundle();
        s = ss;
        FragmentRecycle fragment = new FragmentRecycle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_fragment_recycle, container, false);
        init(root);
        return root;

    }

    private void init(View root) {
        recycleView = root.findViewById(R.id.recycle_view);
        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RecycleAdapter(s);
        recycleView.setAdapter(adapter);

    }

}
