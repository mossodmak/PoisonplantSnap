package com.example.mynavjava;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    private TextView username;
    private Button logout;
    private ImageView imv_profile;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        username = view.findViewById(R.id.username);
        username.setText(currentUser.getDisplayName());
        imv_profile = view.findViewById(R.id.profile_image);
        imv_profile.setImageURI(currentUser.getPhotoUrl());
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signout();
            }
        });

        return view;
    }

    private void signout() {
        mAuth.signOut();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new MoreFragment()).commit();
    }
}
