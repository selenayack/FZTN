package com.app.fztn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.List;


public class ProgramFragment extends Fragment {
    private DbAdapter dbAdapter;
    private RecyclerView recyclerView;
    private VideoAdapter videoAdapter;
    private List<VideoItem> videoList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_program, container, false);

        dbAdapter = new DbAdapter(getActivity()) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }
        };
        dbAdapter.open();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        videoList = dbAdapter.getRecommendedVideos(userId);
        videoAdapter = new VideoAdapter(videoList, getActivity());
        recyclerView.setAdapter(videoAdapter);

        return view;
    }
}


