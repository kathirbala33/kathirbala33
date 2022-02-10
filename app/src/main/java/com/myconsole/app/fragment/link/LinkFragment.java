package com.myconsole.app.fragment.link;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.myconsole.app.R;
import com.myconsole.app.databinding.LinkPreviewFrgmentBinding;
import com.myconsole.app.fragment.link.linkPreview.LinkModel;
import com.myconsole.app.fragment.link.linkPreview.LinkPreview;
import com.myconsole.app.fragment.link.linkPreview.MetaData;
import com.myconsole.app.fragment.link.linkPreview.ResponseListener;

import java.util.ArrayList;
import java.util.List;

public class LinkFragment extends Fragment implements View.OnClickListener {
    private LinkPreviewFrgmentBinding binding;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.link_preview_frgment, container, false);
        binding = LinkPreviewFrgmentBinding.inflate(getLayoutInflater());
        initializeUI();
        context = getActivity();
        return binding.getRoot();
    }

    private void initializeUI() {
        binding.linkPreviewTextView.setOnClickListener(this);
        binding.yourLinkEditText.setFocusable(true);
//        RecyclerView linkRecyclerView = view.findViewById(R.id.linkRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.linkRecyclerView.setLayoutManager(linearLayoutManager);
//        LinkPreview linkPreview = new LinkPreview(new ResponseListener() {
//            @Override
//            public void onData(MetaData metaData) {
//                linkModel.setTitle(metaData.getTitle());
//                linkModel.setDescretion(metaData.getDescription());
//                linkList.add(linkModel);
//                LinkAdapter linkAdapter = new LinkAdapter(getActivity(), linkList);
//                binding.linkRecyclerView.setAdapter(linkAdapter);
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });
//        linkPreview.getPreview("https://myhealth.mydayda.com/login");
//        linkList.add("https://developer.android.com/training/location/change-location-settings");
//        linkList.add("https://myhealth.mydayda.com/login");

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.linkPreviewTextView) {
            binding.progressBar.setVisibility(View.VISIBLE);
            LinkPreview linkPreview = new LinkPreview(new ResponseListener() {
                @SuppressLint("CheckResult")
                @Override
                public void onData(MetaData metaData) {
                    binding.linkDescriptionTextView.setText(metaData.getDescription());
                    Glide.with(context).load(metaData.getImageurl()).into(binding.linkImageView);
                    binding.progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onError(Exception e) {
                    binding.progressBar.setVisibility(View.GONE);
                }
            });
            linkPreview.getPreview(binding.yourLinkEditText.getText().toString());
        }

    }
}
