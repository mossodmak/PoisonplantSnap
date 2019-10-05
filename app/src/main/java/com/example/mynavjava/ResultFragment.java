package com.example.mynavjava;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment {
    String str = "";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_result, container, false);

        LinearLayout linear = new LinearLayout(v.getContext());
        LinearLayout line1 = new LinearLayout(linear.getContext());
        //str = getArguments().getString("test");
        MainActivity activity = (MainActivity)getActivity();
        str = activity.getTestString();

        TextView res = v.findViewById(R.id.title_result);
        res.setText(str);

        res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT ).show();
            }
        });
        return  v;
    }
}
