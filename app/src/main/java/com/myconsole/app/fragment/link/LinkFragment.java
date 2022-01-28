package com.myconsole.app.fragment.link;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.myconsole.app.R;

import java.util.ArrayList;
import java.util.List;

public class LinkFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.link_preview_frgment, container, false);
        initializeUI(view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void initializeUI(View view) {
        RecyclerView linkRecyclerView = view.findViewById(R.id.linkRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linkRecyclerView.setLayoutManager(linearLayoutManager);
        List<String> linkList = new ArrayList<>();
        linkList.add("https://developer.android.com/training/location/change-location-settings");
        linkList.add("https://myhealth.mydayda.com/login");
        LinkAdapter  linkAdapter = new LinkAdapter(getActivity(),linkList);
        linkRecyclerView.setAdapter(linkAdapter);
    }
}
