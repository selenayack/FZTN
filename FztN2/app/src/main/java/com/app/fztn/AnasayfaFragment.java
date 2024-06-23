package com.app.fztn;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AnasayfaFragment extends Fragment {

    private TextView textViewMail;
    private TextView textViewİsim;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_anasayfa, container, false);

        // Find TextViews by their IDs
        textViewMail = view.findViewById(R.id.textViewMail);
        textViewİsim = view.findViewById(R.id.textViewİsim);

        // Automatically update TextViews with user information from Firebase
        updateTextViewsFromFirebase();

        Button buttonneyimVar = view.findViewById(R.id.neyimVarBtn);
        buttonneyimVar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NeyimVarActivity.class);
                startActivity(intent);



            }
        });

        return view;
    }

    private void updateTextViewsFromFirebase() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            // User is signed in, update TextViews with user information
            String userEmail = currentUser.getEmail();
            String userName = currentUser.getDisplayName();

            if (userEmail != null) {
                textViewMail.setText(userEmail);
            }

            if (userName != null) {
                textViewİsim.setText(userName);
            }
        }
    }
}
