package com.example.mynavjava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class SearchFragment extends Fragment {
    String test = "test in class";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search, container, false);
        TextView search = view.findViewById(R.id.search_title);

        // Change Fragment by Fragment
        final ResultFragment result = new ResultFragment();
        final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                transaction.replace(R.id.fragment_container, result);
                transaction.commit();
            }
        });

        return view;
    }
}
